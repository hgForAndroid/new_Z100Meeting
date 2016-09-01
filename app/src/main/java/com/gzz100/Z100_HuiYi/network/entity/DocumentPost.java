package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/1.
 */
public class DocumentPost extends BaseEntity {

    private Subscriber mSubscriber;
    private boolean all;
    private int mAgendaIndex;


    public DocumentPost(Subscriber subscriber, boolean all,int agendaIndex) {
        this.mSubscriber = subscriber;
        this.all = all;
        this.mAgendaIndex = agendaIndex;
    }
    @Override
    public Observable getObservable(ApiService methods) {
        return methods.getFileList(mAgendaIndex);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }
}
