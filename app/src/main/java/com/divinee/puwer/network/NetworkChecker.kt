package com.divinee.puwer.network

import android.content.Context
import android.net.ConnectivityManager

object NetworkChecker {

    fun checkEthernetStatus(context: Context): Boolean =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo?.isConnectedOrConnecting == true
}