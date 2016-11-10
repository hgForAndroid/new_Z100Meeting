package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/28.
 */
public class StartPost extends BaseEntity {
    private Subscriber mSubscriber;
    private int meetingID;
    private String IMEI;

    public StartPost(Subscriber subscriber, int meetingID, String imei) {
        mSubscriber = subscriber;
        this.meetingID = meetingID;
        IMEI = imei;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.startMeeting(meetingID,IMEI);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
