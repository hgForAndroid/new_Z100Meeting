package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.view.View;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.multicast.MulticastBean;

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
         * 暂停
         */
        void meetingPause();
        /**
         * 继续
         */
        void meetingContinue();

        /**
         * 重设议程时间
         * @param agendaIndex   议程序号
         */
        void resetAgendaTimeCounting(int agendaIndex);

        /**
         * 重设议程所有显示的信息
         * @param agendaIndex    议程序号
         */
        void resetAgendaContent(int agendaIndex);

        /**
         * 主持人发送过来的消息中，议程序号变化了，进行响应
         * @param agendaIndex    变化的议程序号
         */
        void respondAgendaIndexChange(int agendaIndex);
        /**
         * 主持人发送过来的消息中，文件序号变化了，进行响应
         * @param documentIndex    变化的文件序号
         */
        void respondDocumentIndexChange(int documentIndex);
        /**
         * 处理完主持人发送过来的消息后，如果是开始的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “开会中”
         */
        void respondMeetingBegin();

        /**
         * 处理完主持人发送过来的消息后，如果是暂停的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “暂停中”
         */
        void respondMeetingPause();

        /**
         * 处理完主持人发送过来的消息后，如果是继续的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “开会中”
         */
        void respondMeetingContinue();

        /**
         * 处理完主持人发送过来的消息后，如果是结束的消息，则继续响应会议开始的其他操作，
         * 如设置  当前会议状态为   “已结束”
         */
        void respondMeetingEnd();
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
        void loadFile(String fileName);

        /**
         * 开始的操作
         * @param multicastBean   组播实体
         * @param meetingState    会议状态
         * @param agendaIndex     议程序号
         * @param DocumentIndex   文件序号
         * @param upLevelText     上一级标题
         */
        void begin(MulticastBean multicastBean,int meetingState,int agendaIndex,int DocumentIndex,String upLevelText);
        /**
         * 结束的操作
         * @param multicastBean    组播实体
         * @param meetingState     会议状态
         */
        void ending(MulticastBean multicastBean,int meetingState);
        /**
         * 暂停的操作
         * @param multicastBean    组播实体
         * @param meetingState     会议状态
         */
        void pause(MulticastBean multicastBean,int meetingState);
        /**
         * 继续 的操作
         * @param multicastBean   组播实体
         * @param meetingState    会议状态
         * @param agendaIndex     议程序号
         * @param DocumentIndex   文件序号
         * @param upLevelText     上一级标题
         */
        void meetingContinue(MulticastBean multicastBean,int meetingState,int agendaIndex,int DocumentIndex,String upLevelText);

        /**
         * 上一个议程
         * @param multicastBean    组播实体
         * @param meetingState     会议状态
         * @param agendaIndex      议程序号
         */
        void previousAgenda(MulticastBean multicastBean,int meetingState,int agendaIndex);
        /**
         * 下一个议程
         * @param multicastBean    组播实体
         * @param meetingState     会议状态
         * @param agendaIndex      议程序号
         */
        void nextAgenda(MulticastBean multicastBean,int meetingState,int agendaIndex);

        /**
         * 处理主持人发送过来的消息
         * @param data  消息实体
         */
        void handleMessageFromHost(MulticastBean data);

        /**
         * 处理支持人点击切换文件的操作
         * @param multicastBean   消息实体，切换后将信息填充到该实体类，再进行发送
         * @param meetingState    会议状态
         * @param agendaIndex     议程序号
         * @param documentPosition 点击的文件序号
         */
        void handleFileClickFromHost(MulticastBean multicastBean,int meetingState,int agendaIndex,int documentPosition);
    }
}
