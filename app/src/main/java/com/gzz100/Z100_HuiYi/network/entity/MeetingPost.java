package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/28.
 */
public class MeetingPost extends BaseEntity {
    private Subscriber mSubscriber;
    private String IMEI;
    public MeetingPost(Subscriber subscriber, String imei) {
        mSubscriber = subscriber;
        IMEI = imei;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.getMeetingList(IMEI);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
