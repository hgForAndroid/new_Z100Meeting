package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.UserBean;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/23.
 */
public interface SignInContract {
    interface View extends BaseView<Presenter>{
        /**
         * 显示参会人员信息
         * @param userBean  参会人员实体类
         */
        void showDelegate(UserBean userBean);
        void showNoDelegate();
        /**
         * 前往主界面，这里还需要参数，未定
         */
        void showMainActivity();

        /**
         * 开始下载
         * @param fileIDs   文件id集合
         */
        void startDownLoad(List<String> fileIDs);

        /**
         * 获取铭牌的WiFi成功，开始连接到这个WiFi
         * @param deviceSSID   铭牌的WiFi名称
         */
      //  void connectToLedDevice(String deviceSSID);

        /**
         * 获取铭牌的WiFi失败，提示用户。
         * @param errorMsg   失败信息。
         */
       // void findLedDeviceFailed(String errorMsg);

        /**
         * 提示用户。
         * 连接不上WiFi 为 deviceSSID  的铭牌。
         * @param deviceSSID
         */
        //void connectLedDeviceFailed(String deviceSSID);

        /**
         * 成功连接回原来的WiFi，这个方法需要调用{@link SignInActivity#sendKeyMessageToClients()}
         */
       // void originalWifiConnectedSuccessful();

    }

    interface Presenter extends BasePresenter{
        /**
         * 获取当前设备对应的当前参会人员
         * @param forceUpdate   主动获取
         * @param IMEI        设备id
         * @param meetingID   会议id
         */
        void fetchCurrentUserBean(boolean forceUpdate, String IMEI, int meetingID);

        /**
         * 调用   SignInContract.View  的  showMainActivity
         * 跳转到主界面，这里需要取得会议全部信息后再跳转
         * @param IMEI        设备id
         * @param meetingID   会议id
         */
        void signIn(String IMEI,int meetingID);

        /**
         * 主持人端开启TCP服务器端的服务，等待客户端的连接请求
         */
        void startTCPService();

        /**
         * 获取设备IMEI 对应的铭牌的WiFi名称。
         * @param IMEI   当前设备IMEI。
         */
       // void fetchLedDeviceWifiSSID(String IMEI);
        /**
         * 连接名片WiFi，连接成功，马上将数据发送到铭牌。
         * 失败则提示用户。
         * @param deviceSSID  铭牌WiFi名称。
         */
       // void connectLedWifi(String deviceSSID);

        /**
         * 重新连接到原始的WiFi
         */
       // void reConnectToOriginalWifi();

    }
}
