package com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting;

import android.content.Context;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.MeetingBean;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public interface SelectMeetingContract {
    interface View extends BaseView<Presenter>{
        /**
         * 显示服务器返回的所有会议
         * @param meetings
         */
        void showMeetingList(List<MeetingBean> meetings);

        /**
         * 没有会议
         * @param errorMsg  当该值不为空，则代表取数据失败，该值为失败信息
         */
        void showNoMeetingList(String errorMsg);
        /**
         * 跳转到签到界面
         * @param IMEI    设备id
         * @param meetingID   会议id
         */
        void showSignIn(String IMEI, int meetingID);

    }

    interface Presenter extends BasePresenter{
        /**
         * 获取会议列表
         * @param forceLoad   是否主动获取
         * @param IMEI        设备id
         */
        void fetchMeetingList(boolean forceLoad,String IMEI);
        /**
         * 选择的会议
         * @param IMEI     设备标识码
         * @param  meetingID  会议id
         */
        void selectMeeting(String IMEI,int meetingID);

        /**
         * 保存设备标志码，之后都在本地取
         * @param context     上下文，在存储时需要
         * @param deviceIMEI  设备标识码
         */
        void saveIMEI(Context context, String deviceIMEI);


    }
}
