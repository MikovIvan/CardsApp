package ru.mikov.test.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData

object NetworkMonitor {
    var isConnected: Boolean = false
    val isConnectedLive = MutableLiveData(false)
    val networkTypeLive = MutableLiveData(NetworkType.NONE)

    private lateinit var cm: ConnectivityManager

    fun registerNetworkMonitor(ctx: Context) {
        cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        obtainNEtworkType(cm.activeNetwork?.let { cm.getNetworkCapabilities(it) })
            .also { networkTypeLive.postValue(it) }

        cm.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    networkTypeLive.postValue(obtainNEtworkType(networkCapabilities))
                }

                override fun onLost(network: Network) {
                    isConnected = false
                    isConnectedLive.postValue(false)
                    networkTypeLive.postValue(NetworkType.NONE)
                }

                override fun onAvailable(network: Network) {
                    isConnected = true
                    isConnectedLive.postValue(true)
                }
            }
        )
    }

    fun obtainNEtworkType(networkCapabilities: NetworkCapabilities?): NetworkType = when {
        networkCapabilities == null -> NetworkType.NONE
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        else -> NetworkType.UNKNOWN
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setNetworkIsConnected(isConnected: Boolean = true) {
        this.isConnected = isConnected
    }
}

enum class NetworkType {
    NONE, UNKNOWN, WIFI, CELLULAR
}
