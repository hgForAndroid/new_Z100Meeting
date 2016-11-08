package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/7.
 */
public class StartOrEndVotePost extends BaseEntity {
    private Subscriber mSubscriber;
    private String IMEI;
    private String meetingID;
    private int voteID;
    private int startOrEnd;

    public StartOrEndVotePost(Subscriber subscriber, String imei, String meetingID, int voteID, int startOrEnd) {
        mSubscriber = subscriber;
        IMEI = imei;
        this.meetingID = meetingID;
        this.voteID = voteID;
        this.startOrEnd = startOrEnd;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.startOrEndVote(IMEI,meetingID,voteID,startOrEnd);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
