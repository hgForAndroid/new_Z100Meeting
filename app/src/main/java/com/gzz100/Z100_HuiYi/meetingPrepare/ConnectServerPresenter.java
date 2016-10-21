package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.meetingPrepare.signIn.SignInActivity;
import com.gzz100.Z100_HuiYi.multicast.KeyInfoBean;
import com.gzz100.Z100_HuiYi.multicast.ReceivedMulticastService;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public class ConnectServerPresenter implements ConnectServerContract.Presenter {
    private Context mContext;
    private ConnectServerContract.View mView;

    private List<String> mIPs;

    public ConnectServerPresenter(Context context, ConnectServerContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void saveIp(String ip) {
        saveHistory(ip);
        mView.showSelectMeeting();
    }
    /**
     * 保存IP地址
     * @param ip
     */
    private void saveHistory(String ip) {
        String oldText = SharedPreferencesUtil.getInstance(mContext).getString(Constant.IP_HISTORY, "");
        StringBuilder builder = new StringBuilder(ip);
        builder.append("," + oldText);
        if (!TextUtils.isEmpty(ip) && !oldText.contains(ip + ",")) {
            SharedPreferencesUtil.getInstance(mContext).putString(Constant.IP_HISTORY, builder.toString());
        }
    }

    @Override
    public void getIPFromHistory(int position) {
        mView.setIPFromHistory( mIPs.get(position));
    }

    @Override
    public void getIPHistory() {
        String history = SharedPreferencesUtil.getInstance(mContext).getString(Constant.IP_HISTORY, "");
        if (!TextUtils.isEmpty(history)) {
            mIPs = new ArrayList<>();
            String[] split = history.split(",");
            for (String ip : split) {
                if (!TextUtils.isEmpty(ip))
                     mIPs.add(ip);
            }
            mView.showHistory(mIPs);
        }else {
            mView.showNoHistory();
        }

    }

    @Override
    public void deleteIP(int position) {
        mIPs.remove(position);
        StringBuilder builder = new StringBuilder("");
        for (String ip:mIPs){
            builder.append(ip+",");
        }
        SharedPreferencesUtil.getInstance(mContext).putString(Constant.IP_HISTORY, builder.toString());
        getIPHistory();
    }

    @Override
    public void saveCurrentIP(String ip) {
        SharedPreferencesUtil.getInstance(mContext).putString(Constant.CURRENT_IP, ip);
    }

    @Override
    public void receivedKeyInfoFromHost() {
//        Log.e("开始接收组播  ===","");
        new Thread(mRunnable).start();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            startReceivedData();
        }
    };

    private void startReceivedData() {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(Constant.MULTI_PORT);
            InetAddress inetAddress = InetAddress.getByName(Constant.MULTI_IP);
            multicastSocket.joinGroup(inetAddress);
            // 接收数据
            byte[] rev = new byte[512];
            DatagramPacket packet = new DatagramPacket(rev, rev.length);

            while (true){
                Log.e("等待组播 ====","=======================================");
                multicastSocket.receive(packet);
                if (MyAPP.getInstance().getUserRole() == 1){//主持人不做接收处理
                    return;
                }
                try {
                    byte[] data = packet.getData();
                    String string = new String(data).trim();

                    if (!TextUtils.isEmpty(string)){
                        Log.e("组播接收的字符串信息  == ",string);
                        if (string.contains("{")&&string.contains("}")){
                            Gson gson = new Gson();
                            KeyInfoBean keyInfoBean = gson.fromJson(string, KeyInfoBean.class);
                            //保存服务器地址
                            SharedPreferencesUtil.getInstance(mContext).putString(Constant.CURRENT_IP,keyInfoBean.getServerIP());
                            SharedPreferencesUtil.getInstance(mContext).putString(Constant.TCP_SERVER_IP,keyInfoBean.getTcpServerIP());
                            mView.showSignInActivity(keyInfoBean);
                        }

                        break;
                    }else {
                        Log.e("接收的组播信息是空的","======================");
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

    @Override
    public void start() {
        getIPHistory();
    }
}
