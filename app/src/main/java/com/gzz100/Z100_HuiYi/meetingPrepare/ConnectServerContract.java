package com.gzz100.Z100_HuiYi.meetingPrepare;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.multicast.KeyInfoBean;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public interface ConnectServerContract {
    interface View extends BaseView<Presenter>{
        /**
         * 本地有保存的IP地址，将其显示出来
         * @param ips
         */
        void showHistory(List<String> ips);

        /**
         * 本地没有历史记录
         */
        void showNoHistory();
        /**
         * 跳转到选择会议界面
         */
        void showSelectMeeting();

        /**
         * 点击历史记录的IP，将IP填充到输入框
         * @param ip
         */
        void setIPFromHistory(String ip);

        /**
         * 跳转到签到界面
         * @param keyInfoBean  关键信息的实体类
         */
        void showSignInActivity(KeyInfoBean keyInfoBean);

    }

    interface Presenter extends BasePresenter{
        /**
         * 保存输入的IP
         * @param ip IP地址
         */
        void saveIp(String ip);

        /**
         * 点击 IP 历史记录，取得 IP
         * @param position
         */
        void getIPFromHistory(int position);

        /**
         * 获取本地保存的IP
         */
        void getIPHistory();

        /**
         * 删除IP
         * @param position IP序号
         */
        void deleteIP(int position);

        /**
         * 确认后 存储输入框内的IP地址到本地
         * @param ip   当前确认的IP地址
         */
        void saveCurrentIP(String ip);

        /**
         * 接收主持人端发送过来的关键数据，
         * 包括  会议id，服务器ip，主持人端的平板设备在局域网内的ip
         */
        void receivedKeyInfoFromHost();
    }
}
