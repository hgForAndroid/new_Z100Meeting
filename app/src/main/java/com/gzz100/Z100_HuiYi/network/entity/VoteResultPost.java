package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/7.
 */
public class VoteResultPost extends BaseEntity {
    private Subscriber mSubscriber;
    private int voteId;

    public VoteResultPost(Subscriber subscriber, int voteId) {
        this.voteId = voteId;
        mSubscriber = subscriber;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.fetchVoteResult(voteId);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
