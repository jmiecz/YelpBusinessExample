package net.mieczkowski.yelpbusinessexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("MissingPermission")
public class ServiceChecker {

    public static final String WIFI = "WIFI";
    public static final String MOBILE = "MOBILE";

    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;
    private WifiManager wifiManager;

    public ServiceChecker(Context context) {
        if (context != null) {
            this.connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            this.telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
    }

    public boolean isConnected() {
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return isConnected(activeNetworkInfo);
    }

    public boolean isConnected(NetworkInfo activeNetwork) {
        return activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.isConnected();
    }

    private String getConnectionMethod() {
        if (connectivityManager == null) {
            return null;
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (isConnected(activeNetwork)) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return WIFI;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return MOBILE;
            }
        }

        return null;
    }

    public boolean isWifiActiveNetwork() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public boolean isMobileActiveNetwork() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public String getSSID() {
        if (connectedToWifi()) {
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !connectionInfo.getSSID().isEmpty()) {
                return connectionInfo.getSSID().replace("\"", "");
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public void enableWifi() {
        wifiManager.setWifiEnabled(true);
    }

    public void disableWifi() {
        wifiManager.setWifiEnabled(false);
    }

    public boolean connectedToWifi() {
        return WIFI.equalsIgnoreCase(getConnectionMethod());
    }

    public boolean isPhone() {
        return telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public int getIpAddress() {
        return wifiManager.getConnectionInfo().getIpAddress();
    }

    @SuppressLint("HardwareIds")
    public String getMacAddress() {
        return wifiManager.getConnectionInfo().getMacAddress();
    }

    public List<WifiConfiguration> getKnownWifiDevices() {
        return wifiManager.getConfiguredNetworks();
    }

    public List<String> getListOfWifiSSIDs() {
        List<String> listOfSSIDs = new ArrayList<>();

        List<WifiConfiguration> configuredNetworks = getKnownWifiDevices();
        if (configuredNetworks == null) {
            return listOfSSIDs;
        }

        Collections.sort(configuredNetworks, new Comparator<WifiConfiguration>() {

            @Override
            public int compare(WifiConfiguration o1, WifiConfiguration o2) {
                if (o1 == null && o2 == null) return 0;
                if (o1 == null) return 1;
                if (o2 == null) return -1;

                if (o1.status == WifiConfiguration.Status.CURRENT) return -1;
                if (o2.status == WifiConfiguration.Status.CURRENT) return 1;
                if (o1.SSID == null && o2.SSID == null) return 0;
                if (o1.SSID == null) return 1;
                if (o2.SSID == null) return -1;
                return o1.SSID.compareToIgnoreCase(o2.SSID);
            }
        });

        for (WifiConfiguration c : configuredNetworks) {
            if (c != null && c.SSID != null) {
                listOfSSIDs.add(c.SSID.replace("\"", ""));
            }
        }

        return listOfSSIDs;
    }

    public int getChargingType(Context context) {
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        return status == -1 ? BatteryManager.BATTERY_STATUS_UNKNOWN : status;
    }

    public String getServerChargingType(Context context) {
        int status = getChargingType(context);

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return "charging";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return "unplugged";
            case BatteryManager.BATTERY_STATUS_FULL:
                return "full";
            default:
                return "unknown";
        }
    }

    public boolean isCharging(Context context) {
        int status = getChargingType(context);

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }

    public float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return level / (float) scale;
    }
}