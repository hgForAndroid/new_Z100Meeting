package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/28.
 */
public class UpdatePost extends BaseEntity {
    private Subscriber mSubscriber;
    private String meetingID;

    public UpdatePost(Subscriber subscriber, String meetingID) {
        this.mSubscriber = subscriber;
        this.meetingID = meetingID;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.checkUpdate(meetingID);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
