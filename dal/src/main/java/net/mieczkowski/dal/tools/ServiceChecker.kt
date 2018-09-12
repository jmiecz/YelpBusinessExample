package net.mieczkowski.dal.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
class ServiceChecker(context: Context) {

    enum class ConnectionType(val type: String){
        Wifi("WIFI"),
        Mobile("MOBILE")
    }

    private val connectivityManager: ConnectivityManager? = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    private val telephonyManager: TelephonyManager? = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
    private val wifiManager: WifiManager? = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?

    fun hasInternetAccess(): Boolean{
        return connectivityManager?.let {
            val activeNetworkInfo = it.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isAvailable && activeNetworkInfo.isConnected
        } ?: false
    }

    fun isWifiActiveNetwork(): Boolean = hasInternetAccess() && getConnectionType() == ConnectionType.Wifi

    fun isMobileActivieNetwork(): Boolean = hasInternetAccess() && getConnectionType() == ConnectionType.Mobile

    private fun getConnectionType(): ConnectionType?{
        if(hasInternetAccess()){
            connectivityManager?.activeNetworkInfo?.let {
                return when(it.type){
                    ConnectivityManager.TYPE_WIFI -> ConnectionType.Wifi
                    ConnectivityManager.TYPE_MOBILE -> ConnectionType.Mobile
                    else -> null
                }
            }
        }

        return null
    }

}