package com.gzz100.Z100_HuiYi.meeting.vote;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.Vote;

import java.util.List;

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
         * 设置Host所有Vote信息
         * @param voteList 相应会议的全部投票列表
         */
        void setAllVoteInf(List<Vote> voteList);

        /**
         * 设置投票信息
         */
        void setVoteInf(Vote vote);

        /**
         * 显示Host所有Vote信息
         */
        void showAllVoteInf();

        /**
         * 显示投票信息
         */
        void showVoteInf();

        /**
         * 显示投票确认对话框
         */
        void showCheckDialog();

        /**
         * 显示投票完成信息
         * @param isSuccessful 是否投票成功
         */
        void showVoteFinishedInf(boolean isSuccessful);

        /**
         * 投票未开始，通知投票界面显示
         * @param showText     投票未开始时，界面显示的内容，主持人和其他与会显示不同内容
         */
        void showVoteNotBegin(String showText);
    }

    public interface Presenter extends BasePresenter{

        void fetchAllVoteInf(boolean forceUpdate, String meetingID);

        /**
         * 获取投票信息
         * @param IMEI 设备编号
         * @param userID 用户ID
         * @param voteId  投票id
         */
        void fetchVoteInf(boolean forceUpdate, String IMEI,String userID, int voteId);

        /**
         * 提交投票结果
         */
        void submitVoteResult();

        /**
         * 设置页面重新加载
         * @param reLoad 重新加载
         */
        void setFirstLoad(Boolean reLoad);

        /**
         * 主持人发起投票
         * @param vote 投票对象
         */
        void startVote(Vote vote);

        /**
         * 主持人查看投票结果
         * @param vote 投票对象
         */
        void checkVoteResult(Vote vote);
    }
}
