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

    void getAllVoteInf(String meetingID, @NonNull LoadAllVoteInfCallBack callback);

    void getVoteDetail(String IMEI, String userID, String agendaIndex, @NonNull LoadVoteDetailCallback callback);
}
