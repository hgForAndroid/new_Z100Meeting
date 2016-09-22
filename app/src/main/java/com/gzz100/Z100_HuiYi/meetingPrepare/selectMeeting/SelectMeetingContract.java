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
         */
        void showNoMeetingList();
        /**
         * 跳转到签到界面
         */
        void showSignIn();

    }

    interface Presenter extends BasePresenter{
        /**
         * 获取会议列表
         * @param forceLoad   是否主动获取
         */
        void fetchMeetingList(boolean forceLoad);
        /**
         * 选择的会议
         * @param IMEI     设备标识码
         * @param  meetingID  会议id
         */
        void selectMeeting(String IMEI,String meetingID);

        /**
         * 保存设备标志码，之后都在本地取
         * @param context     上下文，在存储时需要
         * @param deviceIMEI  舌标标识码
         */
        void saveIMEI(Context context, String deviceIMEI);


    }
}
