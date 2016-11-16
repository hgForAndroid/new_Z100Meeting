package com.gzz100.Z100_HuiYi.tcpController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
import java.util.List;

public class Server extends Service implements IControllerListener{
    public static final String TAG = "Server";
    private PrintWriter mOutput;
    private PrintWriter mOut;

    public Server() {
    }

    @Override
    public IBinder onBind(Intent intent) {
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
                    Socket s = ss.accept();
                    //读取客户端发来的消息
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String message = input.readLine();
                    Log.e("来自客户端的消息 ： ", message);
                    if (message.equals("exit")){
                        removeClientSocket(s);
                    }else {
                        addClientSocketToList(s);
                    }

                    //向客户端发送消息
                    //注意第二个参数据为true将会自动flush，否则需要需要手动操作output.flush()
                    mOutput = new PrintWriter(s.getOutputStream(), true);
                    mOutput.println("已连接到服务器！！！");
                    //发送消息需要先调用该方法
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

    /**
     * 当客户端发出消息请求断开，将该客户端的socket移除集合
     * @param socket
     */
    private void removeClientSocket(Socket socket) {
        Log.e("准备移除","==========================");
        if (clientSockets != null && clientSockets.size() > 0){
            boolean hasSocket = false;
            for (int i = 0; i < clientSockets.size(); i++) {
                String address = clientSockets.get(i).getInetAddress().getHostAddress();
                if (socket.getInetAddress().getHostAddress().equals(address)){
                    //设备ip与存储的Socket有相同的ip
                    clientSockets.remove(socket);
                    Log.e("已经移除","==========================");
                }
            }
        }
    }

    private List<Socket> clientSockets = new ArrayList<>();

    private void addClientSocketToList(Socket socket) {
        if (clientSockets != null && clientSockets.size() > 0){
            boolean isInside = false;
            for (int i = 0; i < clientSockets.size(); i++) {
                String address = clientSockets.get(i).getInetAddress().getHostAddress();
                if (socket.getInetAddress().getHostAddress().equals(address)){
                    //设备ip与存储的Socket有相同的ip
                    isInside = true;
                }
            }
            if (!isInside){//没有保存过
                clientSockets.add(socket);
            }
        }else {
            clientSockets.add(socket);
        }
        //更新连接数
//        EventBus.getDefault().post(clientSockets.size()+"");

    }

    @Override
    public void sendMessage(String message) {
        if (clientSockets != null && clientSockets.size() > 0) {
            Log.e(TAG,"连接数  ====== "+ clientSockets.size());
            for (int i = 0; i < clientSockets.size(); i++) {
                try {
                    Socket next = clientSockets.get(i);
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
