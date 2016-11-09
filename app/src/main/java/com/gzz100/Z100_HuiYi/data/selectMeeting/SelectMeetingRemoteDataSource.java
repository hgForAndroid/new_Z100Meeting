package com.gzz100.Z100_HuiYi.data.selectMeeting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.data.file.local.ObjectTransverter;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;
import com.gzz100.Z100_HuiYi.network.HttpManager;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.MySubscriber;
import com.gzz100.Z100_HuiYi.network.ProgressSubscriber;
import com.gzz100.Z100_HuiYi.network.entity.MeetingPost;
import com.gzz100.Z100_HuiYi.network.entity.StartMeetingPost;
import com.gzz100.Z100_HuiYi.network.entity.StartPost;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public class SelectMeetingRemoteDataSource implements SelectMeetingDataSource {
    private static SelectMeetingRemoteDataSource mInstance;
    private Context mContext;
    private SelectMeetingRemoteDataSource(Context context) {
        this.mContext = context;
    }
    public static SelectMeetingRemoteDataSource getInstance(Context context){
        if (mInstance == null){
            synchronized (SelectMeetingRemoteDataSource.class){
                if (mInstance == null) {
                    mInstance = new SelectMeetingRemoteDataSource(context);
                }
            }
        }
        return mInstance;
    }


    @Override
    public void fetchMeetingList(@NonNull final LoadMeetingListCallback callback,String IMEI) {
        checkNotNull(callback);
//        List<MeetingBean> meetings = FakeDataProvider.getMeetings();
//        callback.onMeetingListLoaded(meetings);

        //加载服务器数据
        MeetingPost meetingPost = new MeetingPost(
                new ProgressSubscriber(new HttpRxCallbackListener<List<MeetingBean>>(){
                    @Override
                    public void onNext(List<MeetingBean> meetings) {
                        callback.onMeetingListLoaded(meetings);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        callback.onDataNotAvailable(errorMsg);
                    }
                }, mContext), IMEI);

        HttpManager httpManager = new HttpManager(mContext);
        httpManager.doHttpDeal(meetingPost);
        //因为输入的服务器IP，不能保证客户一定输入正确，所以当输入错误IP后，
        // 需要返回输入IP界面重新输入IP，所以在输入IP后的第一个请求不能使用单例
        //之后的请求可以使用单例
//        HttpManager.getInstance(mContext).doHttpDeal(meetingPost);
    }

    @Override
    public void startMeeting(@NonNull final StartMeetingCallback callback, String IMEI, String meetingID) {

//        加载服务器数据
        StartMeetingPost startMeetingPost = new StartMeetingPost(
                new MySubscriber(new HttpRxCallbackListener<String>() {
                    @Override
                    public void onNext(String o) {
                        callback.onStartMeetingSuccess();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        callback.onFail(errorMsg);
                    }
                }, mContext),IMEI,meetingID);
        HttpManager.getInstance(mContext).doHttpDeal(startMeetingPost);

//        HttpManager.getInstance(mContext).getApiService().startMeeting(IMEI,meetingID)
//                .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Subscriber() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                callback.onFail();
//            }
//
//            @Override
//            public void onNext(Object o) {
//                callback.onStartMeetingSuccess();
//            }
//        });
    }
}
