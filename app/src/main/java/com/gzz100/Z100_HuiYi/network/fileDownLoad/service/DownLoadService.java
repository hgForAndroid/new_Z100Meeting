package com.gzz100.Z100_HuiYi.network.fileDownLoad.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Download;
import com.gzz100.Z100_HuiYi.network.fileDownLoad.DownloadAPI;
import com.gzz100.Z100_HuiYi.network.fileDownLoad.downLoad.DownloadProgressListener;
import com.gzz100.Z100_HuiYi.utils.AppUtil;
import com.gzz100.Z100_HuiYi.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import rx.Subscriber;

/**
 * 下载文件的服务，IntentService新开线程下载，下载完在一定的时间会回收
 * Created by XieQXiong on 2016/9/27.
 */
public class DownLoadService extends IntentService {
    //id,消息的唯一标识
    private int id;
    //文件名
    private String name;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    //下载总大小
    private int downloadCount = 0;
    //文件下载地址
    private String apkUrl;

    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        id = intent.getIntExtra("id",0);
        name = intent.getStringExtra("name");
        apkUrl = intent.getStringExtra("url");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_download)
                .setContentTitle("文件下载")
                .setContentText(name+"等待下载")
        .setAutoCancel(true);

        mNotificationManager.notify(id,mNotificationBuilder.build());
        download();


    }

    private void download() {
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                //不频繁发送通知，防止通知栏下拉卡顿
                int progress = (int) ((bytesRead * 100) / contentLength);
                if ((downloadCount == 0) || progress > downloadCount) {
                    Download download = new Download();
                    download.setTotalFileSize(contentLength);
                    download.setCurrentFileSize(bytesRead);
                    download.setProgress(progress);

                    sendNotification(download);
                }
            }
        };
//        File outputFile = new File(Environment.getExternalStoragePublicDirectory
//                (Environment.DIRECTORY_DOWNLOADS), name+".apk");
        if (name.contains("/")){
            //如果路劲包含“/”，取最后的名字
            name = name.substring(name.lastIndexOf("/"),name.length());
        }
        File outputFile = new File(AppUtil.getCacheDir(this.getApplicationContext()), name);
        String baseUrl = StringUtils.getHostName(apkUrl);

        new DownloadAPI(baseUrl, listener).downloadFile(apkUrl, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
                downloadCompleted();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                downloadCompleted();
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private void downloadCompleted() {
        Download download = new Download();
        download.setProgress(100);
        mNotificationManager.cancel(id);
        mNotificationBuilder.setProgress(0, 0, false);
        mNotificationBuilder.setContentText(name+" 下载完成");
        mNotificationManager.notify(id, mNotificationBuilder.build());

        EventBus.getDefault().post(id+1);
    }

    private void sendNotification(Download download) {

        mNotificationBuilder.setProgress(100, download.getProgress(), false);
        mNotificationBuilder.setContentText(
                StringUtils.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtils.getDataSize(download.getTotalFileSize()));
        mNotificationManager.notify(id, mNotificationBuilder.build());
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mNotificationManager.cancel(id);
    }
    
}
