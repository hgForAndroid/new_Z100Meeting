package com.gzz100.Z100_HuiYi.tcpController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.data.eventBean.KillService;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;

/**
 * 描述：这是一个其他参会人员客户端。用于接收主持人端（通过tcp连接的方式）发送过来的数据的类
 * 类名：Client
 *
 * @author XieQXiong
 *         create at 2017/2/27 14:45
 */

public class Client extends Service implements IControllerListener {
    private static final String TAG = "Client";
    private BufferedReader mInput;
    private PrintWriter mOutput;
    private Socket mS;
    private String ip;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        ip = SharedPreferencesUtil.getInstance(this.getApplicationContext()).getString(Constant.TCP_SERVER_IP, "");
        if (Constant.DEBUG)
            Log.e(TAG, "存本地的平板ip是 ：" + ip);
        new Thread(mRunnable).start();

    }

    /**
     * 接收主持人端的消息。
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mS = new Socket(ip, Constant.TCP_PORT);
                OutputStream out = mS.getOutputStream();
                // 注意第二个参数据为true将会自动flush，否则需要需要手动操作out.flush()
                mOutput = new PrintWriter(out, true);
                if (Constant.DEBUG)
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
                            ControllerInfoBean bean = gson.fromJson(decode, ControllerInfoBean.class);
                            //发送到MAinActivity或FileDetailActivity处理
                            EventBus.getDefault().post(bean);
                        }
                        if (Constant.DEBUG)
                            Log.e("客户端的服务接收到  === ", decode);
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 发送消息给主持人端。
     *
     * @param message 发送的实体类json字符串
     */
    @Override
    public void sendMessage(String message) {
        OutputStream out = null;
        try {
            Socket socket = new Socket(ip, Constant.TCP_PORT);
            out = socket.getOutputStream();
            // 注意第二个参数据为true将会自动flush，否则需要需要手动操作out.flush()
            mOutput = new PrintWriter(out, true);
            mOutput.println(message);
            if (Constant.DEBUG)
                Log.e("=========", "客户端发消息给服务器端，" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 在{@link Server#sendLastSocketMessage(String, String)}中才有实现，
     * 该方法是会议过程中临时有人进入会议，通知其平板进入受控状态，是在主持人端的Server中发送消息。
     * 本类属于其他参会人员客户端，不需要实现该方法。
     *
     * @param deviceIp 设备ip
     * @param message  信息
     */
    @Override
    public void sendLastSocketMessage(String deviceIp, String message) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kill(KillService k) {
        if (k.getName().equals("Client"))
            stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
