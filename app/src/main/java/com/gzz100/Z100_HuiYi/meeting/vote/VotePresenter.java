package com.gzz100.Z100_HuiYi.meeting.vote;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteRepository;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/4.
 */
public class VotePresenter implements VoteContract.Presenter{
    private VoteRepository mVoteRepository;
    private VoteContract.VoteView mVoteView;

    private boolean mFirstLoad = true;

    public VotePresenter(@NonNull VoteRepository voteRepository, @NonNull VoteContract.VoteView voteView){
        this.mVoteRepository = checkNotNull(voteRepository, "VoteRepository cannot be null");
        this.mVoteView = checkNotNull(voteView, "VoteView cannot be null");
        voteView.setPresenter(this);
    }

    @Override
    public void start() {
        fetchVoteInf(false, "", "", "2");
    }

    @Override
    public void fetchVoteInf(boolean forceUpdate, String IMEI, String userID, String agendaIndex) {
        if(forceUpdate || mFirstLoad){
            mFirstLoad = false;
            mVoteRepository.getVoteDetail(IMEI, userID, agendaIndex, new VoteDataSource.LoadVoteDetailCallback() {
                @Override
                public void onVoteDetailLoaded(Vote vote) {
                    if(!mVoteView.isActive()){
                        return;
                    }
                    mVoteView.setVoteInf(vote);
                    mVoteView.showVoteInf();
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }

    @Override
    public void submitVoteResult() {
        //提交投票结果（通过网络层）
        mVoteView.showVoteFinishedInf(true);
    }

    @Override
    public void setFirstLoad(Boolean reLoad) {
        mFirstLoad = reLoad;
    }
}
