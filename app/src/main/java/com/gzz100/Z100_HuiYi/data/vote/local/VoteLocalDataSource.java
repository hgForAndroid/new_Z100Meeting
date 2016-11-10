package com.gzz100.Z100_HuiYi.data.vote.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteOperate;
import com.gzz100.Z100_HuiYi.meeting.vote.UpLoadVote;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/3.
 */
public class VoteLocalDataSource implements VoteDataSource{
    private static VoteLocalDataSource INSTANCE = null;
    private final VoteOperate mVoteOperate;

    private VoteLocalDataSource(@NonNull Context context){
        checkNotNull(context, "Context cannot be null");
        mVoteOperate = VoteOperate.getInstance(context);
    }

    public static VoteLocalDataSource getInstance(@NonNull Context context){
        if(INSTANCE == null){
            INSTANCE = new VoteLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getVoteDetail(String IMEI, String userID, int voteId, @NonNull LoadVoteDetailCallback callback) {
        checkNotNull(callback, "Callback cannot be null");

        List<Vote> votes = mVoteOperate.queryVoteList();
        if (votes != null && votes.size() > 0){
            boolean isHasVote = false;
            Vote vote = null;
            for (int i = 0; i < votes.size(); i++) {
                int voteID = votes.get(i).getVoteID();
                if (voteID == voteId){
                    isHasVote = true;
                    vote = votes.get(i);
                    break;
                }
            }
            if (isHasVote){
                callback.onVoteDetailLoaded(vote);
            }else {
                callback.onDataNotAvailable();
            }
        }


//        //fake data
//        Vote vote = FakeDataProvider.getVoteDetailByIndex(Integer.valueOf(agendaIndex));
//        if(vote != null){
//            callback.onVoteDetailLoaded(vote);
//        } else {
//            callback.onDataNotAvailable();
//        }
    }

    @Override
    public void getVoteResult(int voteId, @NonNull LoadVoteResultCallback callback) {
        //不用实现，获取投票结果都是从服务器获取的
    }

    @Override
    public void submitVote(UpLoadVote data, @NonNull SubmitCallback callback) {

    }

    @Override
    public void startOrEndVote(String IMEI, int meetingID, int voteID, int startOrEnd, @NonNull SubmitCallback callback) {

    }

    @Override
    public void getAllVoteInf(String meetingID, @NonNull LoadAllVoteInfCallBack callback) {
        checkNotNull(callback, "Callback cannot be null");
        //本地数据
        List<Vote> votes = mVoteOperate.queryVoteList();
//        List<Vote> votes = FakeDataProvider.getFakeVotes();
        if (votes != null && votes.size() > 0){
            callback.onAllVoteLoaded(votes);
        }else {
            callback.onDataNotAvailable();
        }
    }
}
