package com.gzz100.Z100_HuiYi.tcpController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;

public class TcpClient extends Service {

    private final String TAG = "TcpClient客户端服务";
    private Socket mSocket;
    private PrintWriter mOutput;
    private BufferedReader mInput;
    private String mTcpServerIP ;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTcpServerIP = SharedPreferencesUtil.getInstance(this.getApplicationContext()).getString(Constant.TCP_SERVER_IP,"");
        Log.e(TAG,"平板ip  === "+mTcpServerIP);
        connectTCPService();
//        receivedMessage();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        mTcpServerIP = SharedPreferencesUtil.getInstance(this.getApplicationContext()).getString(Constant.TCP_SERVER_IP,"");
//        Log.e("平板ip  === ",mTcpServerIP);
//        connectTCPService();
//        receivedMessage();
//        return super.onStartCommand(intent, flags, startId);
//    }

    private void receivedMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mMessage;
                while (true) {
                    try {
                        if ((mMessage = mInput.readLine()) != null) {
                            String decode = URLDecoder.decode(mMessage, "UTF-8");
                            //这里加“-”是为了避免得到的字符串前面有多了奇怪的字符，测试中有遇到过，
                            // 加“-”完再截取让得到的字符串准确
                            String substring = decode;
                            if (decode.contains("{") && decode.contains("}")) {
                                decode = "-" + decode + "-";
                                int i = decode.indexOf("{");
                                int j = decode.lastIndexOf("}");
                                substring = decode.substring(i, j + 1);
                            }
                            ToastUtil.showMessage(substring);
                            Log.e("TCP客户端接收到的数据  == ",substring);
                            //发送数据
//                            ControllerUtil.getInstance().sendMessage(substring);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void connectTCPService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    mSocket = new Socket(mTcpServerIP, Constant.TCP_PORT);
                    // outgoing stream redirect to socket
                    OutputStream out = mSocket.getOutputStream();
                    // 注意第二个参数据为true将会自动flush，否则需要需要手动操作out.flush()
                    mOutput = new PrintWriter(out, true);
                    mOutput.println("客户端请求连接服务器端！");
                    mInput = new BufferedReader(new InputStreamReader(mSocket
                            .getInputStream(), "UTF-8"));

                    String mMessage;
                    while (true) {
                        try {
                            if ((mMessage = mInput.readLine()) != null) {
                                String decode = URLDecoder.decode(mMessage, "UTF-8");
                                //这里加“-”是为了避免得到的字符串前面有多了奇怪的字符，测试中有遇到过，
                                // 加“-”完再截取让得到的字符串准确
                                String substring = decode;
                                if (decode.contains("{") && decode.contains("}")) {
                                    decode = "-" + decode + "-";
                                    int i = decode.indexOf("{");
                                    int j = decode.lastIndexOf("}");
                                    substring = decode.substring(i, j + 1);
                                }
                                ToastUtil.showMessage(substring);
                                Log.e("TCP客户端接收到的数据  == ",substring);
                                //发送数据
//                            ControllerUtil.getInstance().sendMessage(substring);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
