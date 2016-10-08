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
    }
}
