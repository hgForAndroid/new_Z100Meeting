package com.gzz100.Z100_HuiYi.meeting.vote;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/4.
 */
public class VotePresenter implements VoteContract.Presenter{
    private VoteRepository mVoteRepository;
    private VoteContract.VoteView mVoteView;

    private boolean mFirstLoad = true;
    public enum VoteState {HOST_STATE, PEOPLE_STATE}
    private VoteState mVoteState = VoteState.HOST_STATE;

    public VotePresenter(@NonNull VoteRepository voteRepository, @NonNull VoteContract.VoteView voteView){
        this.mVoteRepository = checkNotNull(voteRepository, "VoteRepository cannot be null");
        this.mVoteView = checkNotNull(voteView, "VoteView cannot be null");
        voteView.setPresenter(this);
    }

    @Override
    public void start() {
        if(MyAPP.getInstance().getUserRole() == 1){
            setmVoteState(VoteState.HOST_STATE);
        } else {
            setmVoteState(VoteState.PEOPLE_STATE);
        }
        switch (mVoteState){
            case PEOPLE_STATE:
                fetchVoteInf(false, "", "", "2");
                break;
            case HOST_STATE:
                fetchAllVoteInf(false, "1");
                break;
            default:
                fetchVoteInf(false, "", "", "1");
                break;
        }
    }

    private void setmVoteState(VoteState voteState){
        mVoteState = voteState;
    }

    @Override
    public void fetchAllVoteInf(boolean forceUpdate, String meetingID) {
        if(forceUpdate || mFirstLoad){
            mFirstLoad = false;
            mVoteRepository.getAllVoteInf(meetingID, new VoteDataSource.LoadAllVoteInfCallBack() {
                @Override
                public void onAllVoteLoaded(List<Vote> voteList) {
                    if(!mVoteView.isActive()){
                        return;
                    }
                    mVoteView.setAllVoteInf(voteList);
                    mVoteView.showAllVoteInf();
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
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
    public void startVote(Vote vote){

    }

    @Override
    public void checkVoteResult(Vote vote){

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
