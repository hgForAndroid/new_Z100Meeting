package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/28.
 */
public class UserBeanPost extends BaseEntity {
    private String IMEI;
    private int meetingID;
    private Subscriber mSubscriber;

    public UserBeanPost(Subscriber subscriber,String imei, int meetingID) {
        IMEI = imei;
        this.meetingID = meetingID;
        mSubscriber = subscriber;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.checkUser(IMEI,meetingID);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }
}
