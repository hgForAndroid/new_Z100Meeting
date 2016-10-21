package com.gzz100.Z100_HuiYi.tcpController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

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

public class TcpService extends Service implements IControllerListener {

    private final String TAG = "TcpService服务器端服务";

    private PrintWriter mOutput;
    private PrintWriter mOut;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        ControllerUtil.getInstance().setIControllerListener(this);
        Log.e(TAG,"开启tcp服务器服务");
        new Thread(mRunnable).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            initSocket();
        }
    };

    private void initSocket() {
        try {
            Boolean endFlag = false;
            ServerSocket ss = new ServerSocket(Constant.TCP_PORT);
            while (!endFlag) {
                // 等待客户端连接
                Socket s = ss.accept();
                addIPToList(s);
                //读取客户端发来的消息
                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String message = input.readLine();
                Log.e("来自客户端的消息 ： ", message);
                ToastUtil.showMessage(message);
                ControllerUtil.getInstance().setIControllerListener(TcpService.this);

                //向客户端发送消息
                //注意第二个参数据为true将会自动flush，否则需要需要手动操作output.flush()
                mOutput = new PrintWriter(s.getOutputStream(), true);
                mOutput.println("成功连接到服务器端");
            }
            ss.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
//                    mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(next.getOutputStream(),"UTF-8")),true);
                    mOut = new PrintWriter(next.getOutputStream(),true);
                    mOut.print(message);
                    mOut.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
