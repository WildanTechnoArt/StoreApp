package com.wildan.storeapp.network

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityStatus {
    companion object {
        fun isConnected(context: Context): Boolean {
            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val connection =
                manager.activeNetwork
            return connection != null
        }
    }
}