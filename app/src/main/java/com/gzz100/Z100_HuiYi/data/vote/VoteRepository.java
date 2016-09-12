package com.gzz100.Z100_HuiYi.data.vote;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/3.
 */
public class VoteRepository implements VoteDataSource{
    private static VoteRepository INSTANCE = null;

    private final VoteDataSource mVoteRemoteDataSource;

    private final VoteDataSource mVoteLocalDataSource;

    //投票信息是否已有本地缓存  TODO   测试为true，实际使用应一直为false
    boolean mHaveVoteCache = true;

    private VoteRepository(@NonNull VoteDataSource voteRemoteDataSource,
                           @NonNull VoteDataSource voteLocalDataSource){
        mVoteRemoteDataSource = checkNotNull(voteRemoteDataSource, "DataSource cannot be null");
        mVoteLocalDataSource = checkNotNull(voteLocalDataSource, "DataSource cannot be null");
    }

    public static VoteRepository getInstance(@NonNull VoteDataSource voteRemoteDataSource,
                                             @NonNull VoteDataSource voteLocalDataSource){
        if(INSTANCE == null){
            INSTANCE = new VoteRepository(voteRemoteDataSource, voteLocalDataSource);
        }
        return INSTANCE;
    }

    public void setHaveVoteCache(boolean mHaveVoteCache) {
        this.mHaveVoteCache = mHaveVoteCache;
    }

    @Override
    public void getVoteDetail(String IMEI, String userID, String agendaIndex, @NonNull LoadVoteDetailCallback callback) {
        checkNotNull(callback, "Callback cannot be null");
        if(mHaveVoteCache){
            mVoteLocalDataSource.getVoteDetail(IMEI, userID, agendaIndex, callback);
        } else {
            mVoteRemoteDataSource.getVoteDetail(IMEI, userID, agendaIndex, callback);
            setHaveVoteCache(true); //不从本地取数据 实际使用应一直设为false
        }
    }
}
