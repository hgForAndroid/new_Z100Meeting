package com.gzz100.Z100_HuiYi.data.vote;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.VoteResult;
import com.gzz100.Z100_HuiYi.meeting.vote.UpLoadVote;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/3.
 */
public class VoteRepository implements VoteDataSource{
    private static VoteRepository INSTANCE = null;

    private final VoteDataSource mVoteRemoteDataSource;

    private final VoteDataSource mVoteLocalDataSource;
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

    @Override
    public void getVoteDetail(final String IMEI, final String userID, final int voteId, @NonNull final LoadVoteDetailCallback callback) {
        checkNotNull(callback, "Callback cannot be null");
        mVoteLocalDataSource.getVoteDetail(IMEI, userID, voteId, new LoadVoteDetailCallback() {
            @Override
            public void onVoteDetailLoaded(Vote vote) {
                callback.onVoteDetailLoaded(vote);
            }

            @Override
            public void onDataNotAvailable() {
                mVoteRemoteDataSource.getVoteDetail(IMEI,userID,voteId,callback);
            }
        });
    }

    @Override
    public void getVoteResult(int voteId, @NonNull final LoadVoteResultCallback callback) {
        mVoteRemoteDataSource.getVoteResult(voteId, new LoadVoteResultCallback() {
            @Override
            public void onVoteResultLoaded(List<VoteResult> voteResults) {
                callback.onVoteResultLoaded(voteResults);
            }

            @Override
            public void onDataNotAvailable(String errorMessage) {
                callback.onDataNotAvailable(errorMessage);
            }
        });
    }

    @Override
    public void submitVote(UpLoadVote data, @NonNull final SubmitCallback callback) {
        mVoteRemoteDataSource.submitVote(data,new SubmitCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFail() {
                callback.onFail();
            }
        });
    }

    @Override
    public void startOrEndVote(String IMEI, int meetingID, int voteID, int startOrEnd, @NonNull final SubmitCallback callback) {
        mVoteRemoteDataSource.startOrEndVote(IMEI, meetingID, voteID, startOrEnd, new SubmitCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFail() {
                callback.onFail();
            }
        });
    }

    @Override
    public void getAllVoteInf(final String meetingID, @NonNull final LoadAllVoteInfCallBack callback) {
        checkNotNull(callback, "Callback cannot be null");
        mVoteLocalDataSource.getAllVoteInf(meetingID,new LoadAllVoteInfCallBack(){
            @Override
            public void onAllVoteLoaded(List<Vote> voteList) {
                callback.onAllVoteLoaded(voteList);
            }

            @Override
            public void onDataNotAvailable() {
                mVoteRemoteDataSource.getAllVoteInf(meetingID,callback);
            }
        });

    }
}
