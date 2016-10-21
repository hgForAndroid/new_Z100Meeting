package com.gzz100.Z100_HuiYi.tcpController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gzz100.Z100_HuiYi.utils.Constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Service implements IControllerListener{
    public static final String TAG = "Server";
    private PrintWriter mOutput;
    private PrintWriter mOut;

    public Server() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(mRunnable).start();

    }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Boolean endFlag = false;
                ServerSocket ss = new ServerSocket(Constant.TCP_PORT);
                while (!endFlag) {
                    // 等待客户端连接
                    Log.e(TAG,"等待请求发来");
                    Socket s = ss.accept();
                    Log.e(TAG,"请求已到，连接客户端");
                    addIPToList(s);
                    //读取客户端发来的消息
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String message = input.readLine();
                    Log.e("来自客户端的消息 ： ", message);
                    //向客户端发送消息
                    //注意第二个参数据为true将会自动flush，否则需要需要手动操作output.flush()
                    mOutput = new PrintWriter(s.getOutputStream(), true);
                    mOutput.println("已连接到服务器！！！");

                    ControllerUtil.getInstance().setIControllerListener(Server.this);
                }
                ss.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    private List<Socket> ips = new ArrayList<>();

    private void addIPToList(Socket socket) {
        ips.add(socket);
    }

    @Override
    public void sendMessage(String message) {
        if (ips != null && ips.size() > 0) {
            Log.e(TAG,"连接数  ====== "+ips.size());
            for (int i = 0; i < ips.size(); i++) {
                try {
                    Socket next = ips.get(i);
                    mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(next.getOutputStream(),"UTF-8")),true);
//                    mOut = new PrintWriter(next.getOutputStream(),true);
                    mOut.println(message);
//                    mOut.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
