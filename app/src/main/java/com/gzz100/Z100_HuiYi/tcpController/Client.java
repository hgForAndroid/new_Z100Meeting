package com.gzz100.Z100_HuiYi.tcpController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;

public class Client extends Service implements IControllerListener{
    private static final String TAG = "Client";
    private BufferedReader mInput;
    private PrintWriter mOutput;
    private Socket mS;
//    private String ip = "192.168.1.37" ;
    private String ip;

    public Client() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ip = SharedPreferencesUtil.getInstance(this.getApplicationContext()).getString(Constant.TCP_SERVER_IP, "");
        Log.e(TAG,"存本地的平板ip是 ："+ip);
        new Thread(mRunnable).start();

//        new Thread(received).start();

    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mS = new Socket(ip, Constant.TCP_PORT);
                // outgoing stream redirect to socket
                OutputStream out = mS.getOutputStream();
                // 注意第二个参数据为true将会自动flush，否则需要需要手动操作out.flush()
                mOutput = new PrintWriter(out, true);
                mOutput.println("我是客户端，已接收到！");
                ClientSendMessageUtil.getInstance().setIControllerListener(Client.this);
                mInput = new BufferedReader(new InputStreamReader(mS
                        .getInputStream(), "UTF-8"));
                String mMessage;
                while (true) {
                    if ((mMessage = mInput.readLine()) != null) {
                        String decode = URLDecoder.decode(mMessage, "UTF-8");
                        //这里加“-”是为了避免得到的字符串前面有多了奇怪的字符，测试中有遇到过，加“-”完再截取让得到的字符串准确
                        if (decode.contains("{") && decode.contains("}")) {
                            decode = "-" + decode + "-";
                            int i = decode.indexOf("{");
                            int j = decode.lastIndexOf("}");
                            //截取整个对象的json字符串
                            decode = decode.substring(i, j + 1);
                            Gson gson = new Gson();
                            ControllerInfoBean bean = gson.fromJson(decode,ControllerInfoBean.class);
                            EventBus.getDefault().post(bean);
                        }
                        Log.e("客户端的服务接收到  === ", decode);

//                        break;
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    private Runnable received = new Runnable() {
        @Override
        public void run() {
            try {

                String mMessage;
                while (true) {
                    if ((mMessage = mInput.readLine()) != null) {
                        String decode = URLDecoder.decode(mMessage, "UTF-8");
                        //这里加“-”是为了避免得到的字符串前面有多了奇怪的字符，测试中有遇到过，加“-”完再截取让得到的字符串准确
                        if (decode.contains("{") && decode.contains("}")) {
                            decode = "-" + decode + "-";
                            int i = decode.indexOf("{");
                            int j = decode.lastIndexOf("}");
                            //截取整个对象的json字符串
                            decode = decode.substring(i, j + 1);
                        }
                        Log.e("客户端的服务接收到  === ", decode);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    @Override
    public void sendMessage(String message){
        // outgoing stream redirect to socket
        OutputStream out = null;
        try {
            out = mS.getOutputStream();
            // 注意第二个参数据为true将会自动flush，否则需要需要手动操作out.flush()
            mOutput = new PrintWriter(out, true);
            mOutput.println("exit");
            Log.e("=========","客户端发消息给服务器端，exit");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
