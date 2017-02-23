package com.ltf.mytoolslibrary.viewbase.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author 李堂飞 20160614 网络参与相关
 */
public class NetworkUtils {
	public static final String DEFAULT_WIFI_ADDRESS = "00-00-00-00-00-00";
	public static final String WIFI = "Wi-Fi";
	public static final String TWO_OR_THREE_G = "2G/3G";
	public static final String UNKNOWN = "Unknown";

	private static String convertIntToIp(int paramInt) {
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
				+ (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
	}

	public static boolean isNetworkAvailable(Context c) {
		if (c != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) c
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (isNetworkConnected(c)) {
				if (getConnectedType(c) != -1) {
					State state = mConnectivityManager.getNetworkInfo(
							ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
					if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络
						if(isWifiConnected(c)){
							return true;
						}else{
							return false;
						}
					}
					state = mConnectivityManager.getNetworkInfo(
							ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
					if (State.CONNECTED == state) { // 判断是否正在使用GPRS网络
						if(isMobileConnected(c)){
							return true;
						}else{
							return false;
						}	
					}
				} else {
					return false;
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/***
	 * 获取当前网络类型
	 * 
	 * @return type[0] WIFI , TWO_OR_THREE_G , UNKNOWN type[0] SubtypeName
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/***
	 * 获取wifi 地址
	 * 
	 * @param pContext
	 * @return
	 */

	public static String getWifiAddress(Context pContext) {
		String address = DEFAULT_WIFI_ADDRESS;
		if (pContext != null) {
			WifiInfo localWifiInfo = ((WifiManager) pContext
					.getSystemService("wifi")).getConnectionInfo();
			if (localWifiInfo != null) {
				address = localWifiInfo.getMacAddress();
				if (address == null || address.trim().equals(""))
					address = DEFAULT_WIFI_ADDRESS;
				return address;
			}

		}
		return DEFAULT_WIFI_ADDRESS;
	}

	/***
	 * 获取wifi ip地址
	 * 
	 * @param pContext
	 * @return
	 */
	public static String getWifiIpAddress(Context pContext) {
		WifiInfo localWifiInfo = null;
		if (pContext != null) {
			localWifiInfo = ((WifiManager) pContext.getSystemService("wifi"))
					.getConnectionInfo();
			if (localWifiInfo != null) {
				String str = convertIntToIp(localWifiInfo.getIpAddress());
				return str;
			}
		}
		return "";
	}

	/**
	 * 获取WifiManager
	 * 
	 * @param pContext
	 * @return
	 */
	public static WifiManager getWifiManager(Context pContext) {
		return (WifiManager) pContext.getSystemService("wifi");
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}

}