package com.gzz100.Z100_HuiYi.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * 手机和APP相关信息
 */
public class MPhone {

	private MPhone() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static String imei;

	/**
	 * 获取设备IMEI码
	 */
	public static String getDeviceIMEI(Context ctx) {
		if (imei == null) {
			TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(
					Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		}

		return imei;
	}

	/**
	 * APK信息
	 */
	private static PackageInfo getAppVersione(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(ctx.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * APK版本code
	 */
	public static int getAppVersionCode(Context ctx) {
		PackageInfo pi = getAppVersione(ctx);
		if (pi != null) {
			return pi.versionCode;
		} else {
			return 1;
		}
	}

	/**
	 * APK版本名称
	 */
	public static String getAppVersionName(Context ctx) {
		PackageInfo pi = getAppVersione(ctx);
		if (pi != null) {
			return pi.versionName;
		} else {
			return "未知版本";
		}
	}
}
