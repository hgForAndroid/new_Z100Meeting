package com.gzz100.Z100_HuiYi.data.vote.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/3.
 */
public class VoteLocalDataSource implements VoteDataSource{
    private static VoteLocalDataSource INSTANCE = null;

    private VoteLocalDataSource(@NonNull Context context){
        checkNotNull(context, "Context cannot be null");
    }

    public static VoteLocalDataSource getInstance(@NonNull Context context){
        if(INSTANCE == null){
            INSTANCE = new VoteLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getVoteDetail(String IMEI, String userID, String agendaIndex, @NonNull LoadVoteDetailCallback callback) {
        checkNotNull(callback, "Callback cannot be null");

        //fake data
        Vote vote = FakeDataProvider.getVoteDetailByIndex(Integer.valueOf(agendaIndex));
        if(vote != null){
            callback.onVoteDetailLoaded(vote);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllVoteInf(String meetingID, @NonNull LoadAllVoteInfCallBack callback) {
        checkNotNull(callback, "Callback cannot be null");
        //fake data

        List<Vote> list = FakeDataProvider.getAllVoteByMeetingID(meetingID);
        if(list.size() == 0){
            callback.onDataNotAvailable();
        } else {
            callback.onAllVoteLoaded(list);
        }
    }
}
