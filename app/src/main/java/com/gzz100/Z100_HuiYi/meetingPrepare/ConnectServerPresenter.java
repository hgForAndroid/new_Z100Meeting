package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.multicast.KeyInfoBean;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

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

    /**
     * 跳转到会议列表界面前，保存当前的服务器ip地址，并存入历史记录中
     * @param ip IP地址
     */
    @Override
    public void saveIpThenShowSelectMeeting(String ip) {
        saveCurrentIP(ip);
        saveHistory(ip);
        mView.showSelectMeeting();
    }

    /**
     * 保存IP地址
     *
     * @param ip
     */
    private void saveHistory(String ip) {
        String oldText = SharedPreferencesUtil.getInstance(mContext).getString(Constant.IP_HISTORY, "");
        if (!TextUtils.isEmpty(ip) && !oldText.contains(ip + ",")) {//ip不为空且没有保存过
            StringBuilder builder = new StringBuilder(ip);
            builder.append("," + oldText);
            SharedPreferencesUtil.getInstance(mContext).putString(Constant.IP_HISTORY, builder.toString());
        }
    }

    /**
     * 从历史记录中获取选中的ip
     * @param position
     */
    @Override
    public void getIPFromHistory(int position) {
        mView.setIPFromHistory(mIPs.get(position));
    }

    /**
     * 获取ip历史记录
     */
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
            if (mIPs.size() > 0)
                mView.showHistory(mIPs);
            else
                mView.showNoHistory();
        } else {
            mView.showNoHistory();
        }

    }

    /**
     * 从ip历史记录中删除选中的ip
     * @param position IP序号
     */
    @Override
    public void deleteIP(int position) {
        mIPs.remove(position);
        StringBuilder builder = new StringBuilder("");
        for (String ip : mIPs) {
            builder.append(ip + ",");
        }
        SharedPreferencesUtil.getInstance(mContext).putString(Constant.IP_HISTORY, builder.toString());
        getIPHistory();
    }

    /**
     * 保存输入的服务器地址ip
     * @param ip   当前确认的IP地址
     */
    @Override
    public void saveCurrentIP(String ip) {
        SharedPreferencesUtil.getInstance(mContext).putString(Constant.CURRENT_IP, ip);
    }

    @Override
    public void receivedKeyInfoFromHost() {
        if (Constant.DEBUG)
            Log.e("开始接收组播  ===", "");
        new Thread(mRunnable).start();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            startReceivedData();
        }
    };

    /**
     * 接收组播信息
     */
    private void startReceivedData() {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(Constant.MULTI_PORT);
            InetAddress inetAddress = InetAddress.getByName(Constant.MULTI_IP);
            multicastSocket.joinGroup(inetAddress);
            // 接收数据
            byte[] rev = new byte[512];
            DatagramPacket packet = new DatagramPacket(rev, rev.length);
            while (true) {
                multicastSocket.receive(packet);
                if (MyAPP.getInstance().getUserRole() == 1) {//主持人不做接收处理
                    return;
                }
                try {
                    byte[] data = packet.getData();
                    String string = new String(data).trim();
                    if (!TextUtils.isEmpty(string)) {
                        if (Constant.DEBUG)
                            Log.e("组播接收的字符串信息  == ", string);
                        if (string.contains("{") && string.contains("}")) {
                            Gson gson = new Gson();
                            KeyInfoBean keyInfoBean = gson.fromJson(string, KeyInfoBean.class);
                            //保存服务器地址，主持人端ip地址，会议id
                            SharedPreferencesUtil.getInstance(mContext).putString(Constant.CURRENT_IP, keyInfoBean.getServerIP());
                            SharedPreferencesUtil.getInstance(mContext).putString(Constant.TCP_SERVER_IP, keyInfoBean.getTcpServerIP());
                            SharedPreferencesUtil.getInstance(mContext).putInt(Constant.MEETING_ID, keyInfoBean.getMeetingId());
                            /**
                             * 通知界面，去跳转到签到界面
                             * 调用下面的方法会跳转到{@link ConnectServerActivity#showSignInActivity(KeyInfoBean)}
                             */
                            mView.showSignInActivity(keyInfoBean);
                        }
                        break;
                    } else {
                        if (Constant.DEBUG)
                            Log.e("接收的组播信息是空的", "======================");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //接收完组播后，离开组播组
            multicastSocket.leaveGroup(inetAddress);
            multicastSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        //先获取本地存储的IP历史记录，有显示出来
        getIPHistory();
    }
}
