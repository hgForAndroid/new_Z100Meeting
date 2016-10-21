package com.gzz100.Z100_HuiYi.multicast;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendMulticastService extends Service {

    private String mKeyInfoJson;
    private Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String currentIP = intent.getStringExtra(Constant.CURRENT_IP);
        String meetingId = intent.getStringExtra(Constant.MEETING_ID);
        String tcpServerIP = intent.getStringExtra(Constant.TCP_SERVER_IP);
        KeyInfoBean keyInfoBean = new KeyInfoBean(currentIP,meetingId,tcpServerIP);
        Gson gson = new Gson();
        mKeyInfoJson = gson.toJson(keyInfoBean);

        // 通过Handler启动线程
        HandlerThread handlerThread = new HandlerThread("SendMulticastService");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(mRunnable);

//        new Thread(mRunnable).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            while (true){
//                try {
//                    SystemClock.sleep(2000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                //因为是udp组播发送，循环发送这些值到客户端，确保接收到
                MulticastController.getDefault().sendMessage(mKeyInfoJson);
//                Log.e("发送组播 ====== ",mKeyInfoJson);
            }
        }
    };

}
