package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/7.
 */
public class AgendasPost extends BaseEntity {
    private Subscriber mSubscriber;
    private String IMEI;
    private String userId;

    public AgendasPost(Subscriber subscriber,String IMEI, String userId) {
        mSubscriber = subscriber;
        this.IMEI = IMEI;
        this.userId = userId;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.login(IMEI,userId);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
