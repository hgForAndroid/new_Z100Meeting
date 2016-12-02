package com.gzz100.Z100_HuiYi.tcpController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.data.eventBean.PeopleIn;
import com.gzz100.Z100_HuiYi.utils.Constant;

import org.greenrobot.eventbus.EventBus;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Server extends Service implements IControllerListener {
    public static final String TAG = "Server";
    private PrintWriter mOutput;
    private PrintWriter mOut;
    private ServerSocket mServerSocket;
    private Map<String, String> mHashMap;

    public Server() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHashMap = new HashMap<>();
        new Thread(mRunnable).start();


    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Boolean endFlag = false;
                mServerSocket = new ServerSocket(Constant.TCP_PORT);
                while (!endFlag) {
                    // 等待客户端连接
                    Socket s = mServerSocket.accept();
                    //读取客户端发来的消息
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String message = input.readLine();
                    Log.e("来自客户端的消息 ： ", message);

                    if (message.equals("haha")) {
                        int meetingIsProgress = MyAPP.getInstance().isMeetingIsProgress();
                        if (meetingIsProgress == 2 || meetingIsProgress == 4 || meetingIsProgress == 8 ) {//会议进行中
                            Log.e(TAG, "run: 会议进行中，发送消息给客户端");
                            EventBus.getDefault().post(new PeopleIn(true, s.getInetAddress().getHostAddress().toString()));
                        }
                    }
                    else if (message.equals("exit")) {
                        removeFromMap(s);
                    }
                    else {
                        if (message.contains("192")) {
                            splitMessage(message);
                        }else {
                            addToMap(s);
                            mOutput = new PrintWriter(s.getOutputStream(), true);
                            mOutput.println("已连接到服务器！！！");
                        }
                    }
                    //发送消息需要先调用该方法
                    ControllerUtil.getInstance().setIControllerListener(Server.this);
                }
                mServerSocket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    private void splitMessage(String message) {
        String[] split = message.split(",");
        if (split.length > 0) {
            mHashMap.put(split[1], split[0]);
        }
    }

    private Map<String, Socket> hashMap = new HashMap<>();

    private void addToMap(Socket socket) {
        String ip = socket.getInetAddress().getHostAddress().toString();
//        if (hashMap.get(ip) == null) {
//            hashMap.put(ip, socket);
//        }else {
//            hashMap.remove(ip);
//            hashMap.put(ip,socket);
//        }
//        else {//有没有保存，put进来，有保存，也put进来，hashMap会覆盖，不会增加Socket
//            hashMap.remove(ip);
            hashMap.put(ip,socket);
//        }
    }

    /**
     * 当客户端发出消息请求断开，将该客户端的socket移除集合
     *
     * @param socket
     */
    private void removeFromMap(Socket socket) {
        String ip = socket.getInetAddress().getHostAddress().toString();
        if (hashMap.get(ip) != null) {
            hashMap.remove(ip);
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mHashMap.get(ip) != null) {//mHashMap保存ip与人名
                mHashMap.remove(ip);//移除该人名
            }
        }
    }

    @Override
    public void sendMessage(String message) {
        if (hashMap.size() > 0) {
            try {
                if (Constant.DEBUG)
                    Log.e(TAG, "sendMessage: hashMap有值：" + hashMap.size());
                Set<String> keySet = hashMap.keySet();
                for (String key : keySet) {
                    Socket socket = hashMap.get(key);
                    if (Constant.DEBUG){
                        Log.e(TAG, "sendMessage: Socket是否isClosed："+socket.isClosed());
                        Log.e(TAG, "sendMessage: socket是否还isConnected：" + socket.isConnected());
                        Log.e(TAG, "sendMessage: socket是否还isBound ：" + socket.isBound());
                        Log.e(TAG, "sendMessage: socket是否还isInputShutdown ：" + socket.isInputShutdown());
                        Log.e(TAG, "sendMessage: socket是否还isOutputShutdown ：" + socket.isOutputShutdown());
                        Log.e(TAG, "sendMessage: socket是否还getOOBInline ：" + socket.getOOBInline());

                    }

                    mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
//                    mOut = new PrintWriter(next.getOutputStream(),true);
                    mOut.println(message);
                    if (Constant.DEBUG)
                        Log.e(TAG, "ip  ====== " + socket.getInetAddress().getHostAddress().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Constant.DEBUG)
                Log.e(TAG, "sendMessage: hashMap没有值");
        }
    }

    @Override
    public void sendLastSocketMessage(String deviceIp, String message) {
        if (hashMap.get(deviceIp) != null) {
            try {
                Socket socket = hashMap.get(deviceIp);
                mOut = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
                mOut.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
