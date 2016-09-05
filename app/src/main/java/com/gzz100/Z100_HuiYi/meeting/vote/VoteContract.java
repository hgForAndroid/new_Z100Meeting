package com.gzz100.Z100_HuiYi.meeting.vote;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.Vote;

/**
 * Created by Lee on 2016/9/1.
 */
public interface VoteContract {
    public interface VoteView extends BaseView<Presenter>{
        /**
         * 判断是否已经添加到Activity
         */
        boolean isActive();

        /**
         * 设置投票信息
         */
        void setVoteInf(Vote vote);

        /**
         * 显示投票信息
         */
        void showVoteInf();
    }

    public interface Presenter extends BasePresenter{
        /**
         * 获取投票信息
         * @param IMEI 设备编号
         * @param userID 用户ID
         * @param agendaIndex  议程编号
         */
        void fetchVoteInf(boolean forceUpdate, String IMEI,String userID, String agendaIndex);

        /**
         * 提交投票结果
         */
        void submitVoteResult();

        /**
         * 设置页面重新加载
         * @param reLoad 重新加载
         */
        void setFirstLoad(Boolean reLoad);
    }
}
