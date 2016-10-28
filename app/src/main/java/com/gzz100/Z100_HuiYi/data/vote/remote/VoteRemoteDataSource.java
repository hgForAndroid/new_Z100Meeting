package com.gzz100.Z100_HuiYi.data.vote.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/4.
 */
public class VoteRemoteDataSource implements VoteDataSource{
    private static VoteRemoteDataSource INSTANCE = null;

    private VoteRemoteDataSource(@NonNull Context context){
        checkNotNull(context, "Context cannot be null");
    }

    public static VoteRemoteDataSource getInstance(@NonNull Context context){
        if(INSTANCE == null){
            INSTANCE = new VoteRemoteDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getVoteDetail(String IMEI, String userID, String agendaIndex, @NonNull LoadVoteDetailCallback callback) {
        //从网络取数据
    }

    @Override
    public void getAllVoteInf(String meetingID, @NonNull LoadAllVoteInfCallBack callback) {
        //从网络取数据
    }
}
