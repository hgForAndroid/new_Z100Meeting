package com.gzz100.Z100_HuiYi.data.vote;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Vote;

import java.util.List;

/**
 * Created by Lee on 2016/9/3.
 */
public interface VoteDataSource {
    interface LoadVoteDetailCallback{
        void onVoteDetailLoaded(Vote vote);
        void onDataNotAvailable();
    }

    interface LoadAllVoteInfCallBack{
        void onAllVoteLoaded(List<Vote> voteList);
        void onDataNotAvailable();
    }

    /**
     * 获取所有会议列表
     * @param meetingID    会议id
     * @param callback
     */
    void getAllVoteInf(String meetingID, @NonNull LoadAllVoteInfCallBack callback);

    void getVoteDetail(String IMEI, String userID, String agendaIndex, @NonNull LoadVoteDetailCallback callback);
}
