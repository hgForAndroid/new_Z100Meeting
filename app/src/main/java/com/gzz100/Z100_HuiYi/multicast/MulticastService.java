package com.gzz100.Z100_HuiYi.multicast;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastService extends Service {
    private MulticastSocket socket;
    private InetAddress address;

    private int portNumber = 30001;
    private String ipAddress = "239.0.0.1";

    private Handler mHandler;

    private Runnable mRunnable = new Runnable() {
        public void run() {
            try {
                initSocket(portNumber, ipAddress);
                startReceivingMulticast();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    public MulticastService() {
    }

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
        try {
            closeSocket();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initSocket(int portNumber, String ipAddress) throws IOException{
        socket = new MulticastSocket(portNumber);
        address = InetAddress.getByName(ipAddress);
        socket.joinGroup(address);
    }

    private void startReceivingMulticast() throws IOException{
        DatagramPacket packet;

        // 接收数据
        byte[] rev = new byte[512];
        packet = new DatagramPacket(rev, rev.length);

        while (true){
            socket.receive(packet);
            String receiver = new String(packet.getData()).trim();  //不加trim，则会打印出512个byte，后面是乱码
            Toast.makeText(this.getApplicationContext(), receiver, Toast.LENGTH_LONG).show();
            //根据收到的不同信息进行相应的处理\
        }
    }

    private void closeSocket() throws IOException{
        //退出组播
        socket.leaveGroup(address);
        socket.close();
    }
}
