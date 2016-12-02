package com.gzz100.Z100_HuiYi.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.gzz100.Z100_HuiYi.multicast.SendMulticastService;
import com.gzz100.Z100_HuiYi.tcpController.Client;
import com.gzz100.Z100_HuiYi.tcpController.Server;

import java.util.Map;
import java.util.Set;
/**
* SharedPreferences操作工具类。
* @author XieQXiong
* create at 2016/9/22 10:15
*/

public class SharedPreferencesUtil {
    /**
     * 参数配置工具
     */
    public static final String PREFERENCES_NAME = "APP_SETTINGS";
    private static SharedPreferencesUtil mInstance;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private Context mContext;

    private SharedPreferencesUtil(Context context) {
        mContext = context;
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new SharedPreferencesUtil(context.getApplicationContext());
            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            editor = sp.edit();
        }
        return mInstance;
    }

    /**
     * 获取全部SharedPreferences参数
     *
     * @return 返回包含所有SharedPreferences键值对参数的Map
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * 获取指定String，若查无结果，则返回默认值
     *
     * @param key      关键字
     * @param defValue 默认值
     * @return String
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 获取指定StringSet，若查无结果，则返回默认值。
     * 调用此方法，需API>=11
     *
     * @param key       关键字
     * @param defValues 默认值
     * @return Set<String>
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return sp.getStringSet(key, defValues);
    }

    /**
     * 获取指定int，若查无结果，则返回默认值
     *
     * @param key      关键字
     * @param defValue 默认值
     * @return int
     */
    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    /**
     * 获取指定int，若查无结果，则返回默认值
     *
     * @param key      关键字
     * @param defValue 默认值
     * @return int
     */
    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    /**
     * 获取指定float，若查无结果，则返回默认值
     *
     * @param key      关键字
     * @param defValue 默认值
     * @return float
     */
    public float getFloat(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    /**
     * 获取指定boolean，若查无结果，则返回默认值
     *
     * @param key      关键字
     * @param defValue 默认值
     * @return boolean
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 检查是否存在指定关键字。存在返回true；不存在返回false
     *
     * @param key 关键字
     * @return boolean
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }


    /**
     * 向SharedPreferences写入String类型参数
     *
     * @param key   关键字
     * @param value 值
     */
    public boolean putString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 向SharedPreferences写入Set<String>类型参数，
     * 调用此方法，需API>=11
     *
     * @param key    关键字
     * @param values 值
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean putStringSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
        return editor.commit();
    }

    /**
     * 向SharedPreferences写入int类型参数
     *
     * @param key   关键字
     * @param value 值
     */
    public boolean putInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 向SharedPreferences写入long类型参数
     *
     * @param key   关键字
     * @param value 值
     */
    public boolean putLong(String key, long value) {
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 向SharedPreferences写入float类型参数
     *
     * @param key   关键字
     * @param value 值
     */
    public boolean putFloat(String key, float value) {
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 向SharedPreferences写入boolean类型参数
     *
     * @param key   关键字
     * @param value 值
     */
    public boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 删除SharedPreferences中指定键值对
     *
     * @param key 关键字
     */
    public boolean remove(String key) {
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 清空SharedPreferences
     */
    public boolean clear() {
        editor.clear();
        return editor.commit();
    }

    /**
     * 清除sharedPreference中，对显示有影响的缓存
     */
    public void clearKeyInfo(){
        remove(Constant.IS_MEETING_END);
        remove(Constant.COUNTING_MIN);
        remove(Constant.COUNTING_SEC);
        remove(Constant.PAUSE_AGENDA_INDEX);
        remove(Constant.PAUSE_DOCUMENT_INDEX);
        remove(Constant.ENDING_CURRENT_TIME);
        remove(Constant.IS_VOTE_BEGIN);
        remove(Constant.IS_VOTE_COMMIT);
        remove(Constant.MEETING_STATE);
    }

    /**
     * kill掉正在运行的服务
     */
    public void killAllRunningService(){
        if (AppUtil.isServiceRun(mContext, "com.gzz100.Z100_HuiYi.tcpController.Server")) {
            mContext.stopService(new Intent(mContext, Server.class));
        }
        if (AppUtil.isServiceRun(mContext, "com.gzz100.Z100_HuiYi.tcpController.Client")) {
            mContext.stopService(new Intent(mContext, Client.class));
        }
        if (AppUtil.isServiceRun(mContext, "com.gzz100.Z100_HuiYi.multicast.SendMulticastService")) {
            mContext.stopService(new Intent(mContext, SendMulticastService.class));
        }
    }

    public void killSendMulticastService(){
        if (AppUtil.isServiceRun(mContext, "com.gzz100.Z100_HuiYi.multicast.SendMulticastService")) {
            mContext.stopService(new Intent(mContext, SendMulticastService.class));
        }
    }

}
