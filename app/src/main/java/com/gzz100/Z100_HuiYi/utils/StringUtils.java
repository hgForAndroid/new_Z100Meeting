package com.gzz100.Z100_HuiYi.utils;

import com.gzz100.Z100_HuiYi.R;

import java.text.DecimalFormat;
/**
* @author XieQXiong
* create at 2016/9/27 16:13
*/

public class StringUtils {
    /**
     * 获取下载地址前面部分
     * @param urlString   全部地址
     * @return
     */
    public static String getHostName(String urlString) {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1) {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
    }

    /**
     * 获取下载的文件大小
     * @param var0
     * @return
     */
    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }

    /**
     * 根据 符号   ：    将时间分割成分秒
     *
     * @param duration 带 : 的时间字符串
     * @return 时分集合
     */
    public static String[] splitDuration(String duration) {
        String[] strings = null;
        if (duration.contains(":")) {
            strings = duration.split(":");
        }else{
            ToastUtil.showMessage(R.string.string_time_duration_str);
            return null;
        }
        return strings;
    }

    /**
     * 重新设置数字，如果是1-9，则在前面加上“0”，否则返回原值（字符串格式）
     * @param num   需设置的值
     * @return      重新设置的值
     */
    public static String resetNum(int num) {
        return num > 9 ? num + "" : "0" + num;
    }

    // 获取手机系统版本
    public static String getLocalSystemVersion() {
        String version = android.os.Build.VERSION.RELEASE;
        if (version == null) {
            version = "";
        }
        return version;

    }
}
