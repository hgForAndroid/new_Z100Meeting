package com.gzz100.Z100_HuiYi.data.vote.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Start;
import com.gzz100.Z100_HuiYi.data.VoteResult;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.meeting.vote.UpLoadVote;
import com.gzz100.Z100_HuiYi.network.HttpManager;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.MySubscriber;
import com.gzz100.Z100_HuiYi.network.entity.StartOrEndVotePost;
import com.gzz100.Z100_HuiYi.network.entity.UpLoadVotePost;
import com.gzz100.Z100_HuiYi.network.entity.VoteResultPost;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/9/4.
 */
public class VoteRemoteDataSource implements VoteDataSource{
    private static VoteRemoteDataSource INSTANCE = null;
    private Context mContext;

    private VoteRemoteDataSource(@NonNull Context context){
        checkNotNull(context, "Context cannot be null");
        mContext = context;
    }

    public static VoteRemoteDataSource getInstance(@NonNull Context context){
        if(INSTANCE == null){
            INSTANCE = new VoteRemoteDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getVoteDetail(String IMEI, String userID, int agendaIndex, @NonNull LoadVoteDetailCallback callback) {
        //从网络取数据
    }

    @Override
    public void getVoteResult(int voteId, @NonNull final LoadVoteResultCallback callback) {
        VoteResultPost voteResultPost = new VoteResultPost(
                new MySubscriber(new HttpRxCallbackListener<List<VoteResult>>() {
            @Override
            public void onNext(List<VoteResult> voteResults) {
                callback.onVoteResultLoaded(voteResults);
            }

            @Override
            public void onError(String errorMsg) {
                callback.onDataNotAvailable(errorMsg);
            }
        },mContext),voteId);
        HttpManager.getInstance(mContext).doHttpDeal(voteResultPost);
    }

    @Override
    public void submitVote(UpLoadVote data, @NonNull final SubmitCallback callback) {
        UpLoadVotePost upLoadVotePost = new UpLoadVotePost(
                new MySubscriber(new HttpRxCallbackListener<UpLoadVote>(){
                    @Override
                    public void onNext(UpLoadVote s) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        callback.onFail();
                    }
                },mContext),data
        );
        HttpManager.getInstance(mContext).doHttpDeal(upLoadVotePost);
    }

    @Override
    public void startOrEndVote(String IMEI, String meetingID, int voteID, int startOrEnd,
                               @NonNull final SubmitCallback callback) {
        StartOrEndVotePost startOrEndVotePost = new StartOrEndVotePost(
                new MySubscriber(new HttpRxCallbackListener<String>(){

                    @Override
                    public void onNext(String s) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        callback.onFail();
                    }
                },mContext)
                ,IMEI,meetingID,voteID,startOrEnd);
        HttpManager.getInstance(mContext).doHttpDeal(startOrEndVotePost);
    }

    @Override
    public void getAllVoteInf(String meetingID, @NonNull LoadAllVoteInfCallBack callback) {
        //从网络取数据
    }
}
