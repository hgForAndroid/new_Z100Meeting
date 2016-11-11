package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.network.ApiService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieQXiong on 2016/9/28.
 */
public class EndMeetingPost extends BaseEntity {
    private Subscriber mSubscriber;
    private String IMEI;
    private int meetingId;
    public EndMeetingPost(Subscriber subscriber, String imei, int meetingId) {
        mSubscriber = subscriber;
        IMEI = imei;
        this.meetingId = meetingId;
    }

    @Override
    public Observable getObservable(ApiService methods) {
        return methods.endMeeting(IMEI,meetingId);
    }

    @Override
    public Subscriber getSubscriber() {
        return mSubscriber;
    }

}
