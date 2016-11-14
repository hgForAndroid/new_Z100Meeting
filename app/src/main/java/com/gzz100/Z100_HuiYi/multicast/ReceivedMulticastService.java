package com.gzz100.Z100_HuiYi.multicast;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
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
/**
* 接收组播的服务类，目前接收组播的服务，在ConnectServerPresenter中使用线程来接收
* 所以这个服务类没有使用到，之后如要使用这种方式，则不删除该类，如不使用，可以考虑删除
* @author XieQXiong
* create at 2016/11/9 15:29
*/
public class ReceivedMultiCastService extends Service {
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            try {
                startReceivedData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        // 通过Handler启动线程
        HandlerThread handlerThread = new HandlerThread("MultiSocketB");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(mRunnable);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopSelf();
    }

    private void startReceivedData() {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(Constant.MULTI_PORT);
            InetAddress inetAddress = InetAddress.getByName(Constant.MULTI_IP);
            multicastSocket.joinGroup(inetAddress);
            DatagramPacket packet;
            // 接收数据
            byte[] rev = new byte[512];
            packet = new DatagramPacket(rev, rev.length);
            while (true){
                if (MyAPP.getInstance().getUserRole() == 1){
                    return;
                }
                multicastSocket.receive(packet);
                try {
                    byte[] data = packet.getData();
                    String string = new String(data).trim();

                    if (!TextUtils.isEmpty(string)){
                        Log.e("组播接收的字符串信息  == ",string);
                        if (string.contains("{") && string.contains("}")){
                            Gson gson = new Gson();
                            KeyInfoBean keyInfoBean = gson.fromJson(string, KeyInfoBean.class);
                            //保存服务器地址
                            SharedPreferencesUtil.getInstance(this).putString(Constant.CURRENT_IP,keyInfoBean.getServerIP());
                        }else {
                            //保存服务器地址
                            SharedPreferencesUtil.getInstance(this).putString(Constant.CURRENT_IP,string);
                        }
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            multicastSocket.leaveGroup(inetAddress);
            multicastSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
