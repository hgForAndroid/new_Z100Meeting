package com.gzz100.Z100_HuiYi.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.tcpController.ClientSendMessageUtil;
import com.gzz100.Z100_HuiYi.tcpController.ControllerUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * APP未知异常捕获工具类
 * Created by XieQXiong on 2016/11/15.
 */

public class AppCrashCaughtUtil implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "AppCrashCaughtUtil";
    private static final int TYPE_SAVE_SDCARD = 1; //崩溃日志保存本地SDCard  --建议开发模式使用
    private static final int TYPE_SAVE_REMOTE = 2; //崩溃日志保存远端服务器 --建议生产模式使用

    private int type_save = 1;  //崩溃保存日志模式 默认为2，采用保存Web服务器
    private static final String CRASH_SAVE_SDPATH = "sdcard/fda_cache/"; //崩溃日志SD卡保存路径
    private static final String CARSH_LOG_DELIVER = "http://img2.xxh.cc:8080/SalesWebTest/CrashDeliver";
    private static AppCrashCaughtUtil instance = new AppCrashCaughtUtil();
    private Context mContext;

    private AppCrashCaughtUtil() {
    }

    /**
     * @return
     */
    public static AppCrashCaughtUtil getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc) 进行重写捕捉异常
     *
     * @see
     * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
     * .Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (type_save == TYPE_SAVE_SDCARD) {
            // 1,保存信息到sdcard中
            saveToSdcard(mContext, ex);
        } else if (type_save == TYPE_SAVE_REMOTE) {
            // 2,异常崩溃信息投递到服务器,如果需要
            saveToServer(mContext, ex);
        }
        if (MyAPP.getInstance().getUserRole() != 1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ClientSendMessageUtil.getInstance().sendMessage("exit");
                }
            }).start();
        }
        SharedPreferencesUtil.getInstance(mContext).clearKeyInfo();//清缓存
        SharedPreferencesUtil.getInstance(mContext).killAllRunningService();//杀服务
        //删除会议前预下载的所有文件
        AppUtil.DeleteFolder(AppUtil.getCacheDir(mContext));
        mContext.deleteDatabase(DBHelper.DB_NAME);//删除数据库
        ActivityStackManager.clear();//清Activity
        android.os.Process.killProcess(android.os.Process.myPid());
        // 3,应用准备退出
        showToast(mContext, "很抱歉,程序发生异常,即将推出.");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置自定异常处理类
     *
     * @param pContext
     */
    public void setAppCrashCaughtUtilInfo(Context pContext) {
        this.mContext = pContext;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 保存异常信息到sdcard中
     *
     * @param pContext
     * @param ex       异常信息对象
     */
    private void saveToSdcard(Context pContext, Throwable ex) {
        String fileName = null;
        StringBuffer sBuffer = new StringBuffer();
        // 添加异常信息
        sBuffer.append(getExceptionInfo(ex));
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file1 = new File(CRASH_SAVE_SDPATH);
            if (!file1.exists()) {
                file1.mkdir();
            }
            fileName = file1.toString() + File.separator + paserTime(System.currentTimeMillis()) + "  APP崩溃异常.txt";
            File file2 = new File(fileName);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file2);
                fos.write(sBuffer.toString().getBytes());
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进行把数据投递至服务器
     *
     * @param pContext
     * @param ex       崩溃异常
     */
    private void saveToServer(Context pContext, Throwable ex) {

    }

    /**
     * 获取并且转化异常信息
     * 同时可以进行投递相关的设备，用户信息
     *
     * @param ex
     * @return 异常信息的字符串形式
     */
    private String getExceptionInfo(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("---------Crash Log Begin---------\n");
        //在这边可以进行相关设备信息投递--这边就稍微设置几个吧
        //其他设备和用户信息大家可以自己去扩展收集上传投递
        stringBuffer.append("SystemVersion:" + StringUtils.getLocalSystemVersion() + "\n");
        stringBuffer.append(sw.toString() + "\n");
        stringBuffer.append("---------Crash Log End---------\n");
        return stringBuffer.toString();
    }

    /**
     * 进行弹出框提示
     *
     * @param pContext
     * @param msg
     */
    private void showToast(final Context pContext, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(pContext, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     *
     * @param milliseconds
     * @return
     */
    private String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String times = format.format(new Date(milliseconds));

        return times;
    }
}
