package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/6.
 */
public class MeetingInfoPost extends BaseEntity {
    private Subscriber mSubscriber;
    public MeetingInfoPost(Subscriber subscriber) {
        mSubscriber = subscriber;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.getMeetingInfo();
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }
}
