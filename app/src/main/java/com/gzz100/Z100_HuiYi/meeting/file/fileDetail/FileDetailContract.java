package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.view.View;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.Update;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;

import java.io.File;

/**
* 文件详情的契约接口
* @author XieQXiong
* create at 2016/8/30 10:43
*/

public interface FileDetailContract {
    interface DetailView extends BaseView<Presenter>{
        /**
         * 返回
         */
        void fallback();

        /**
         * 侧滑菜单向左滑
         * @param distanceX
         */
        void slideLeft(int distanceX);
        /**
         * 侧滑菜单向右滑
         * @param distanceX
         */
        void slideRight(int distanceX);

        /**
         * 加载文件
         * @param file
         */
        void loadFile(File file);

        /**
         * 提示文件正在下载中
         * @param tip
         */
        void showFileIsDownLoading(String tip);

        /**
         * 设置会议当前状态,该方法是在继续状态下，支持人点击切换文件操作时调用
         * @param meetingState   会议状态
         */
        void setMeetingState(int meetingState);

        /**
         * 开始
         */
        void meetingBegin();

        /**
         * 结束
         */
        void meetingEnding();

        /**
         * 结束会议成功，调用该方法
         */
        void endMeetingSuccess();

        /**
         * 暂停
         */
        void meetingPause();
        /**
         * 继续
         */
        void meetingContinue();

        /**
         * 显示投票
         */
        void showVote();

        /**
         * 显示投票结果
         */
        void showVoteResult();

        /**
         * 启动投票成功，需要去投票界面进行投票，并且通知所有客户端
         */
        void launchVoteSuccess();

        /**
         * 重设议程时间，
         * 如果是主持人端的，controllerInfoBean应该设为null,只传入agendaIndex，
         * 如果是客户端的，则需要处理controllerInfoBean，而不处理agendaIndex。
         * @param controllerInfoBean   消息实体
         * @param agendaIndex          议程序号
         */
        void resetAgendaTimeCounting(ControllerInfoBean controllerInfoBean,int agendaIndex);

        /**
         * 重设议程所有显示的信息
         * @param agendaIndex    议程序号
         */
        void resetAgendaContent(int agendaIndex);

        /**
         * 该方法是客户端在会议继续时，并且主持人端的议程已经变化时调用，
         * 主持人发送过来的消息中，议程已经变化，进行响应。
         * @param controllerInfoBean    消息实体
         */
        void respondAgendaHasChange(ControllerInfoBean controllerInfoBean);

        /**
         * 该方法是客户端在会议继续时，并且主持人端的议程无变化时调用，
         * 主持人发送过来的消息中，议程无变化，进行响应。
         * @param controllerInfoBean    消息实体
         */
        void respondAgendaNotChange(ControllerInfoBean controllerInfoBean);

        /**
         * 会议开启过，并且议程倒计时了 ，该方法是在客户端接收到继续时，通知界面，
         * 为了将客户端的时间倒计时与主持人端的一致，而使用的此标识。
         * @param isAgendaTimeCountDown
         */
        void respondAgendaTimeIsCounting(boolean isAgendaTimeCountDown);
        /**
         * 主持人发送过来的消息中，议程序号变化了，进行响应
         * @param controllerInfoBean  消息实体
         */
        void respondAgendaIndexChange(ControllerInfoBean controllerInfoBean);
        /**
         * 主持人发送过来的消息中，文件序号变化了，进行响应
         * @param documentIndex    变化的文件序号
         */
        void respondDocumentIndexChange(int documentIndex);
        /**
         * 处理完主持人发送过来的消息后，如果是开始的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “开会中”。
         * @param isAgendaChange    议程是否改变
         */
        void respondMeetingBegin(boolean isAgendaChange);

        /**
         * 处理完主持人发送过来的消息后，如果是暂停的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “暂停中”。
         */
        void respondMeetingPause();

        /**
         * 处理完主持人发送过来的消息后，如果是继续的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “开会中”。
         * @param isAgendaChange    议程是否改变
         */
        void respondMeetingContinue(boolean isAgendaChange);

        /**
         * 处理完主持人发送过来的消息后，如果是结束的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “已结束”。
         */
        void respondMeetingEnd();

        /**
         * 处理主持人发来的消息，
         * 处理开始投票
         * @param voteId
         */
        void respondVoteBegin(int voteId);

        /**
         * 处理主持人发来的更新消息，
         * 马上进行文件更新
         */
        void respondUpdate();

        /**
         * 文件有更新，需要界面提示
         */
        void fileHasUpdate();

        /**
         * 文件更新有误，界面提示
         * @param error 服务器返回的错误信息
         */
        void fileUpdateError(String error);
    }

    interface Presenter extends BasePresenter{
        /**
         * 返回上一页
         */
        void fallback();

        /**
         * 隐藏侧滑菜单
         * @param v  侧滑菜单
         */
        void slideLeft(View v);

        void slideRight(View v);

        /**
         * 加载文件
         * @param documentModel  文件实体
         */
        void loadFile(DocumentModel documentModel);

        /**
         * 主持人点击结束，立即发起结束会议的请求，成功后调用 endMeetingSuccess
         * @param IMEI
         * @param meetingId
         */
        void startEndMeeting(String IMEI,int meetingId);

        /**
         * 开始的操作
         * @param controllerInfoBean   发送的信息实体
         * @param meetingState    会议状态
         * @param agendaIndex     议程序号
         * @param DocumentIndex   文件序号
         * @param upLevelText     上一级标题
         */
        void begin(ControllerInfoBean controllerInfoBean, int meetingState, int agendaIndex, int DocumentIndex, String upLevelText);
        /**
         * 结束的操作
         * @param controllerInfoBean    发送的信息实体
         * @param meetingState     会议状态
         */
        void ending(ControllerInfoBean controllerInfoBean, int meetingState);
        /**
         * 暂停的操作
         * @param controllerInfoBean    发送的信息实体
         * @param meetingState     会议状态
         */
        void pause(ControllerInfoBean controllerInfoBean, int meetingState);
        /**
         * 继续 的操作
         * @param controllerInfoBean   发送的信息实体
         * @param meetingState    会议状态
         * @param agendaIndex     议程序号
         * @param DocumentIndex   文件序号
         * @param upLevelText     上一级标题
         * @param isAgendaChange   议程是否已经改变
         * @param isAgendaTimeCountDown 议程已经开始，并且已经倒计时过了
         * @param min              倒计时的分
         * @param sec              倒计时的秒
         */
        void meetingContinue(ControllerInfoBean controllerInfoBean, int meetingState, int agendaIndex,
                             int DocumentIndex, String upLevelText, boolean isAgendaChange,
                             boolean isAgendaTimeCountDown,String min,String sec);

        /**
         * 发消息，让临时进入会议的人员的平板进入受控状态  的操作
         * @param deviceIp      当前人员所持设备的ip
         * @param controllerInfoBean   发送的信息实体
         * @param meetingState    会议状态
         * @param agendaIndex     议程序号
         * @param DocumentIndex   文件序号
         * @param upLevelText     上一级标题
         * @param isAgendaChange   议程是否已经改变
         * @param isAgendaTimeCountDown 议程已经开始，并且已经倒计时过了
         * @param min              倒计时的分
         * @param sec              倒计时的秒
         */
        void controlTempPeopleIn(String deviceIp,ControllerInfoBean controllerInfoBean, int meetingState,
                    int agendaIndex,int DocumentIndex, String upLevelText, boolean isAgendaChange,
                    boolean isAgendaTimeCountDown,String min,String sec);

        /**
         * 启动投票，只有启动成功，才能开始去投票界面投票
         * @param IMEI          设备id
         * @param meetingId     会议id
         * @param voteId        投票id
         * @param startOrEnd    0：表示开启投票，  -1：表示关闭投票
         */
        void launchVote(String IMEI, int meetingId, final int voteId, final int startOrEnd);

        /**
         * 开始投票
         * @param controllerInfoBean
         * @param voteId      投票文件的id
         * @param meetingState   会议状态
         */
        void startVote(ControllerInfoBean controllerInfoBean,int voteId, int meetingState);
        /**
         * 获取投票结果
         * @param controllerInfoBean
         * @param voteId      投票文件的id
         * @param meetingState   会议状态
         */
        void fetchVoteResult(ControllerInfoBean controllerInfoBean,int voteId, int meetingState);

        /**
         * 上一个议程，主持人端调用的方法
         * @param controllerInfoBean    发送的信息实体
         * @param meetingState     会议状态
         * @param agendaIndex      议程序号
         */
        void previousAgendaForHost(ControllerInfoBean controllerInfoBean, int meetingState, int agendaIndex);
        /**
         * 下一个议程，主持人端调用的方法
         * @param controllerInfoBean    发送的信息实体
         * @param meetingState     会议状态
         * @param agendaIndex      议程序号
         */
        void nextAgendaForHost(ControllerInfoBean controllerInfoBean, int meetingState, int agendaIndex);

        /**
         * 上一个议程，其他参会人员调用的方法
         * @param agendaIndex  议程序号
         */
        void previousAgenda(int agendaIndex);

        /**
         * 下一个议程，其他参会人员调用的方法
         * @param agendaIndex   议程序号
         */
        void nextAgenda(int agendaIndex);

        /**
         * 处理主持人发送过来的消息
         * @param data  消息实体
         */
        void handleMessageFromHost(ControllerInfoBean data);

        /**
         * 处理支持人点击切换文件的操作
         * @param controllerInfoBean   消息实体，切换后将信息填充到该实体类，再进行发送
         * @param meetingState    会议状态
         * @param agendaIndex     议程序号
         * @param documentPosition 点击的文件序号
         */
        void handleFileClickFromHost(ControllerInfoBean controllerInfoBean, int meetingState, int agendaIndex, int documentPosition);

        /**
         * 访问服务器，获取最新数据并更新。
         * @param controllerInfoBean   消息实体，切换后将信息填充到该实体类，再进行发送。
         * @param meetingState    会议状态。
         */
        void updateTheNewDate(ControllerInfoBean controllerInfoBean, int meetingState);

    }
}
