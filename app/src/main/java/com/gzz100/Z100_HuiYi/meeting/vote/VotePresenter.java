package com.gzz100.Z100_HuiYi.meeting.vote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteRepository;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

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
    private Context mContext;

    public VotePresenter(@NonNull VoteRepository voteRepository, @NonNull VoteContract.VoteView voteView, Context context){
        this.mVoteRepository = checkNotNull(voteRepository, "VoteRepository cannot be null");
        this.mVoteView = checkNotNull(voteView, "VoteView cannot be null");
        this.mContext = context;
        voteView.setPresenter(this);

    }

    @Override
    public void start() {
//        if (mFirstLoad){
            int voteId = SharedPreferencesUtil.getInstance(mContext).getInt(Constant.BEGIN_VOTE_ID, -1);
            boolean isVoteBegin = SharedPreferencesUtil.getInstance(mContext).getBoolean(Constant.IS_VOTE_BEGIN, false);
            if (isVoteBegin && voteId != -1){//投票开始
                if(MyAPP.getInstance().getUserRole() == 1){
                    setmVoteState(VoteState.HOST_STATE);
                } else {
                    setmVoteState(VoteState.PEOPLE_STATE);
                }
                switch (mVoteState){
                    case PEOPLE_STATE:
                        fetchVoteInf(false, "", "", voteId);
                        break;
                    case HOST_STATE:
                        fetchVoteInf(false, "", "", voteId);
//                fetchAllVoteInf(false, "1");
                        break;
                    default:
                        fetchVoteInf(false, "", "", voteId);
                        break;
                }
            }else {
                if (MyAPP.getInstance().getUserRole() == 1){
                    mVoteView.showVoteNotBegin("您还未开启投票");
                }else {
                    mVoteView.showVoteNotBegin("等待投票");
                }
            }
//        }
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
    public void fetchVoteInf(boolean forceUpdate, String IMEI, String userID,int voteId) {
        if(forceUpdate || mFirstLoad){
            mFirstLoad = false;
            mVoteRepository.getVoteDetail(IMEI, userID, voteId, new VoteDataSource.LoadVoteDetailCallback() {
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
