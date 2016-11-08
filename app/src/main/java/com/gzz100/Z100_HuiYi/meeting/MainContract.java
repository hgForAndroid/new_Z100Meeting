package com.gzz100.Z100_HuiYi.meeting;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;

/**
 * Created by XieQXiong on 2016/11/7.
 */

public interface MainContract {
    interface View extends BaseView<Presenter>{
        /**
         * 客户端响应开始
         * @param agendaIndex
         * @param documentIndex
         * @param upLevelTitle
         */
        void clientResponseMeetingBegin(int agendaIndex, int documentIndex, String upLevelTitle);

        /**
         * 客户端响应投票
         * @param VoteId
         */
        void clientResponseMeetingVote(int VoteId);

        /**
         * 客户端响应继续
         * @param agendaIndex
         * @param documentIndex
         * @param upLevelTitle
         * @param countingMin
         * @param countingSec
         */
        void clientResponseMeetingContinue(int agendaIndex, int documentIndex, String upLevelTitle,
                                           String countingMin, String countingSec);

        /**
         * 客户端响应暂停
         */
        void clientResponseMeetingPause();

        /**
         * 客户端响应结束
         */
        void clientResponseMeetingEnd();


        /**
         * 主持人端响应开始
         * @param agendaIndex
         * @param documentIndex
         * @param upLevelTitle
         */
        void hostResponseMeetingBegin(int agendaIndex,int documentIndex,String upLevelTitle);

        /**
         * 主持人端响应继续
         * @param agendaIndex
         * @param documentIndex
         * @param upLevelTitle
         * @param countingMin
         * @param countingSec
         */
        void hostResponseMeetingContinue(int agendaIndex,int documentIndex,String upLevelTitle,
                                     String countingMin,String countingSec);

        /**
         * 主持人端响应暂停
         */
        void hostResponseMeetingPause();

        /**
         * 主持人端响应结束
         */
        void hostResponseMeetingEnd();

        /**
         * 启动后没有先开启过议程，则没有倒计时的时间，结束投票需要从第一个议程开始
         * @param agendaIndex    值需要为1
         * @param documentId      值需要为0
         * @param upTitleText
         */
        void hostResponseEndVoteWithoutStart(int agendaIndex,int documentId,String upTitleText);

        /**
         * 已经开始过会议，从议程中来到投票界面，此时结束投票，需要返回原来的议程
         * @param agendaIndex    原来的议程序号
         * @param documentId     原来的议程文件序号
         * @param upTitleText    上一级标题
         * @param countingMin    议程倒计时分钟
         * @param countingSec     议程倒计时秒数
         */
        void hostResponseEndVoteAlreadyStart(int agendaIndex,int documentId,String upTitleText,
                                     String countingMin,String countingSec);

        /**
         * 投票结束之后，对界面的一些参数设置
         */
        void hostResponseVoteEnd();

        /**
         * 主持人开启投票成功
         */
        void hostResponseLaunchVoteSuccess();

        /**
         * 主持人关闭投票成功
         */
        void hostResponseCloseVoteSuccess();

        /**
         * 主持人开启（关闭）投票失败
         * @param failFlag   0：代表开启失败   -1：代表关闭失败
         */
        void hostResponseLaunchOrCloseVoteFail(int failFlag);

    }

    interface Presenter extends BasePresenter{
        /**
         * 客戶端接收到主持人端發送消息，進行處理
         * @param controllerInfoBean
         */
        void handleControllerInfoBean(ControllerInfoBean controllerInfoBean);

        /**
         * 主持人点击开始会议
         */
        void hostStartMeeting(ControllerInfoBean controllerInfoBean,int meetingState);
        /**
         * 主持人点击结束会议
         */
        void hostEndMeeting(ControllerInfoBean controllerInfoBean,int meetingState);
        /**
         * 主持人点击暂停会议
         */
        void hostPauseMeeting(ControllerInfoBean controllerInfoBean,int meetingState);
        /**
         * 主持人点击继续会议
         */
        void hostContinueMeeting(ControllerInfoBean controllerInfoBean,int meetingState);

        /**
         * 主持人点击结束投票
         */
        void hostEndVote(ControllerInfoBean controllerInfoBean,int meetingState);

        /**
         * 主持人点击开始投票(结束)之前，需要先启动（关闭）投票
         * @param IMEI
         * @param meetingId
         * @param voteId
         * @param startOrEnd    0:表示开启   -1：表示关闭
         * @param mControllerInfoBean      消息实体，成功开启投票后，发送消息给所有客户端
         * @param meetingState     会议状态
         */
        void hostLaunchOrCloseVote(String IMEI,String meetingId,int voteId,int startOrEnd,
                                   ControllerInfoBean mControllerInfoBean,int meetingState);

    }
}
