package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.content.Context;
import android.nfc.FormatException;
import android.text.TextUtils;
import android.view.TextureView;

import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

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
    public void start() {
        getIPHistory();
    }
}
