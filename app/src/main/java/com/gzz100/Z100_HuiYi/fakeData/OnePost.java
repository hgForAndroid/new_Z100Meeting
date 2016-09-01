package com.gzz100.Z100_HuiYi.fakeData;

import com.gzz100.Z100_HuiYi.network.ApiService;
import com.gzz100.Z100_HuiYi.network.entity.BaseEntity;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/1.
 */
public class OnePost extends BaseEntity {

    private Subscriber mSubscriber;


    public OnePost(Subscriber subscriber) {
        this.mSubscriber = subscriber;
    }
    @Override
    public Observable getObservable(ApiService methods) {
        return methods.getTitles("android","title");
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }
}
