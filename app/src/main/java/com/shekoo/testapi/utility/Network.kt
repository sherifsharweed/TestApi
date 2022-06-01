package com.shekoo.testapi.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

object Network {
                fun hasInternet(context: Context): Boolean {
                    try {
                        val connectivityManager = context.getSystemService(
                            Context.CONNECTIVITY_SERVICE
                        ) as ConnectivityManager
                        val activeNetwork = connectivityManager.activeNetwork ?: return false
                        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                        return when {
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    } catch (t: Throwable) {
                        return false
                    }
                }

}