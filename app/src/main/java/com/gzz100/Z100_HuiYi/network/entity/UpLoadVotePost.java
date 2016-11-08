package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.meeting.vote.UpLoadVote;
import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/7.
 */
public class UpLoadVotePost extends BaseEntity {
    private Subscriber mSubscriber;
    private UpLoadVote data;

    public UpLoadVotePost(Subscriber subscriber,UpLoadVote data) {
        mSubscriber = subscriber;
        this.data = data;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.uploadVoteResult(data);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
