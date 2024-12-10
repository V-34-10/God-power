package com.divinee.puwer.app

import android.app.Application
import android.content.Context
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.BuildConfig
import com.adjust.sdk.LogLevel
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.FirebaseApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import io.appmetrica.analytics.StartupParamsCallback
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class App : Application() {
    private val apiMetrica = "9e676000-d8b0-4002-a720-0b5c777cb6c1"
    private val appToken = "m7x5baqm4w74"

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var referrerClient: InstallReferrerClient

    private val advertisingInfoDeferred = CompletableDeferred<String?>()
    private val installReferrerDeferred = CompletableDeferred<String?>()
    private val yandexDeviceIdDeferred = CompletableDeferred<String?>()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // Adjust Config
        val environment =
            if (!BuildConfig.DEBUG) AdjustConfig.ENVIRONMENT_SANDBOX else AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(this, appToken, environment)
        config.setLogLevel(LogLevel.VERBOSE)
        Adjust.onCreate(config)

        // AppMetrica Config
        val configMetrica = AppMetricaConfig.newConfigBuilder(apiMetrica).build()
        AppMetrica.activate(this, configMetrica)
        getAppMetricaDeviceId()

        // Advertiser Config
        initAdvertisingId()

        // InstallReferrer Config
        initInstallReferrerId()

    }

    private fun getAppMetricaDeviceId() {
        applicationScope.launch {
            try {
                val startupParams = suspendCancellableCoroutine { continuation ->
                    AppMetrica.requestStartupParams(
                        this@App, object : StartupParamsCallback {
                            override fun onReceive(result: StartupParamsCallback.Result?) {
                                continuation.resume(result)
                            }

                            override fun onRequestError(
                                reason: StartupParamsCallback.Reason,
                                result: StartupParamsCallback.Result?
                            ) {
                                continuation.resumeWithException(Exception("AppMetrica startup params error: $reason"))
                            }
                        },
                        listOf(StartupParamsCallback.APPMETRICA_DEVICE_ID_HASH)
                    )
                }
                yandexDeviceIdDeferred.complete(startupParams?.deviceIdHash)
            } catch (e: Exception) {
                Log.e("AppMetrica", "Failed to fetch device ID hash: ${e.message}")
                yandexDeviceIdDeferred.completeExceptionally(e)
            }
        }
    }

    private fun initAdvertisingId() {
        applicationScope.launch {
            try {
                val advertisingId = getAdvertisingInfo(this@App)
                advertisingInfoDeferred.complete(advertisingId)
            } catch (e: Exception) {
                Log.e("AdvertisingID", "Failed to get Advertising ID: ${e.message}")
                advertisingInfoDeferred.completeExceptionally(e)
            }
        }
    }

    private fun initInstallReferrerId() {
        applicationScope.launch {
            try {
                installReferrerDeferred.complete(getInstallReferrer())
            } catch (e: Exception) {
                Log.e("InstallReferrer", "Failed to get Install Referrer: ${e.message}")
                installReferrerDeferred.completeExceptionally(e)
            }
        }
    }

    suspend fun getData(): Map<String, String?> = applicationScope.async {
        val advertisingInfo = advertisingInfoDeferred.await()
        val referrer = installReferrerDeferred.await()
        val adjustDeviceId = Adjust.getAdid()
        val yandexDeviceId = yandexDeviceIdDeferred.await()
        Log.d("headers", "Yandex-UUID: $yandexDeviceId")
        Log.d("headers", "Adjust-Device-ID: $adjustDeviceId")
        Log.d("headers", "Advertising-ID: $advertisingInfo")
        Log.d("headers", "Install-Referrer: $referrer")
        mapOf(
            "Yandex-UUID" to yandexDeviceId,
            "Adjust-Device-ID" to adjustDeviceId,
            "Advertising-ID" to advertisingInfo,
            "Install-Referrer" to referrer
        )
    }.await()

    private suspend fun getInstallReferrer(): String? {
        return suspendCancellableCoroutine { continuation ->
            referrerClient = InstallReferrerClient.newBuilder(this).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                            val referrer = referrerClient.installReferrer.installReferrer
                            continuation.resume(referrer)
                            Log.d("InstallReferrer", "Referrer: $referrer")
                        } else {
                            val errorMessage = when (responseCode) {
                                InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> "API not available"
                                InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> "Service unavailable"
                                else -> "Unknown error code: $responseCode"
                            }
                            Log.e("InstallReferrer", errorMessage)
                            if (responseCode == InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED) {
                                continuation.resume(null)
                            } else {
                                continuation.resumeWithException(Exception(errorMessage))
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("InstallReferrer", "Error getting referrer", e)
                        continuation.resumeWithException(e)
                    } finally {
                        referrerClient.endConnection()
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    Log.w("InstallReferrer", "Service disconnected")
                    continuation.resumeWithException(Exception("Service disconnected"))
                }
            })
        }
    }

    private suspend fun getAdvertisingInfo(context: Context): String? =
        withContext(Dispatchers.IO) {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
                val advertisingId = adInfo.id
                Log.d(
                    "AdvertisingID",
                    "Advertising ID: $advertisingId, Limit Ad Tracking: ${adInfo.isLimitAdTrackingEnabled}"
                )
                return@withContext advertisingId
            } catch (e: Exception) {
                Log.e("AdvertisingID", "Failed to get Advertising ID: ${e.message}")
                null
            }
        }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }
}