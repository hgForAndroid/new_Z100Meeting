package com.gzz100.Z100_HuiYi.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

/**
* 获取手机存储路径，是否联网
* @author XieQXiong
* create at 2016/9/10 15:04
*/

public class AppUtil {
    /**
     * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
     */
    public static String getCacheDir(Context context) {
        String cacheDir;
        if (context.getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = context.getExternalCacheDir().toString();
        } else {
            cacheDir = context.getCacheDir().toString();
        }
        return cacheDir;
    }

    public static boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 只关注是否联网
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断服务是否后台运行
     *
     * @param context
     *            Context
     * @param className
     *            判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRun(Context context, String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(40);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }
}
