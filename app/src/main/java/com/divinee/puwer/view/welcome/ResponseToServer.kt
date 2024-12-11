package com.divinee.puwer.view.welcome

import android.content.Context
import android.util.Log
import com.divinee.puwer.R
import com.divinee.puwer.app.App
import com.divinee.puwer.models.GameOfferWall
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

class ResponseToServer(private val context: Context) {
    private var link: String = context.getString(R.string.link_response_server)
    private val client = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private suspend fun makeRequest(): Response? {
        return withContext(Dispatchers.IO) {
            try {
                val headers = withTimeout(5000L) {
                    (context.applicationContext as App).getData()
                }
                val request = Request.Builder()
                    .url(link)
                    .apply {
                        headers.forEach { (key, value) ->
                            if (value != null) {
                                addHeader(key, value)
                                Log.d("ApiRequest", "Headers: $key : $value")
                            }
                        }
                    }
                    .build()

                client.newCall(request).execute()
            } catch (e: TimeoutCancellationException) {
                Log.e("ApiRequest", "Timeout getting headers: ${e.message}")
                null
            } catch (e: Exception) {
                Log.e("ApiRequest", "Error making request: ${e.message}")
                null
            }
        }
    }

    suspend fun getGameOffers(): List<GameOfferWall> {
        val response = makeRequest()
        return if (response != null && response.isSuccessful) {
            val responseBody = response.body?.string()
            Log.d("ApiRequest", "Response: $responseBody")
            parseJsonResponse(responseBody)
        } else {
            Log.e(
                "ApiRequest",
                "Error getting game offers: ${response?.code} - ${response?.message}"
            )
            emptyList()
        }
    }

    private fun parseJsonResponse(json: String?): List<GameOfferWall> {
        return try {
            if (json.isNullOrEmpty()) {
                Log.w("ApiRequest", "JSON is null or empty. Returning empty list.")
                emptyList()
            } else {
                Gson().fromJson(json, Array<GameOfferWall>::class.java).toList()
            }
        } catch (e: JsonSyntaxException) {
            Log.e("ApiRequest", "Error parsing JSON: ${e.message}", e)
            emptyList()
        }
    }
}