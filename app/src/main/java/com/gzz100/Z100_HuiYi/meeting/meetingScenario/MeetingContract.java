package com.gzz100.Z100_HuiYi.meeting.meetingScenario;

import android.app.Dialog;
import android.content.Context;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;

import java.util.List;

/**
 * Created by XieQXiong on 2016/9/5.
 */
public interface MeetingContract {
    interface View extends BaseView<Presenter>{
        /**
         * 显示会议概况。该方法中应该有参数，该参数为会议概况的实体类
         * @param dialog 会议概况弹窗
         */
        void showMeetingInfo(Dialog dialog, android.view.View contentView,MeetingInfo meetingInfo);

        /**
         * 关闭弹窗
         * @param dialog  弹窗
         */
        void dismissDialog(Dialog dialog);

        /**
         * 跳转到用户详情界面
         */
        void showUserInfo(DelegateModel delegateBean);

        /**
         * 跳转到个人详情时，查询没有这个人
         */
        void showNoUser();

        /**
         * 显示会议桌
         * @param users 用户列表
         */
        void showMeetingRoom(List<DelegateModel> users);

        /**
         * 设置其他人员个数
         * @param isShow       是否显示其他人员标签
         * @param othersNum    显示个数
         */
        void setOthersNum(boolean isShow,int othersNum);
        /**
         * 显示人员界面中的其他参会人员
         * @param isShowDelegate  是否显示人员界面
         */
        void showDelegate(Boolean isShowDelegate);

        /**
         * 设置会议结束后的   会议开始于
         * @param beginTime  开始时间
         */
        void setEndBegin(String beginTime);
        /**
         * 设置会议结束后的   会议结束于
         * @param endingTime  结束时间
         */
        void setEndEnding(String endingTime);
        /**
         * 设置会议结束后的   会议时长
         * @param duration  会议时长
         */
        void setEndDuration(String duration);
        /**
         * 设置会议结束后的   会议纪要
         * @param record  会议纪要
         */
        void setEndRecord(String record);

        /**
         * 设置会议结束的布局显示
         */
        void setEndingLayoutShow();

    }

    interface Presenter extends BasePresenter{
        /**
         * 获取会议概况，封装成一个会议概况实体类，再调用 MeetingContract.View 的 showMeetingInfo
         * @param context 上下文，用于创建弹窗
         */
        void getMeetingInfo(Context context);

        /**
         * 根据用户id获取用户信息，再调用 MeetingContract.View 的 showUserInfo
         * @param userId 用户id
         */
        void fetchUserInfo(int userId);

        /**
         * 获取显示在会议桌的参会人员列表，再调用 MeetingContract.View 的 showMeetingRoom
         */
        void fetchMainUsers();

        /**
         * 重新设置为第一次加载，避免界面销毁后再回来时，数据无法重新获取到
         */
        void resetFirstLoad();

        /**
         * 调用 MeetingContract.View 的 showDelegate
         */
        void showDelegate();

        /**
         * 取得其他参会人数后调用 MeetingContract.View 的  setOthersNum
         * 人数为0 的话，设置setOtherNum中的第一个参数为false
         * @param otherNum
         */
        void setOtherNum(int otherNum);

    }
}
