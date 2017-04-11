package com.gzz100.Z100_HuiYi.multicast;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.data.Update;
import com.gzz100.Z100_HuiYi.data.eventBean.KillService;
import com.gzz100.Z100_HuiYi.network.HttpManager;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.MySubscriber;
import com.gzz100.Z100_HuiYi.network.entity.UpdatePost;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class SendMulticastService extends Service {
    private static final String TAG = "SendMulticastService";
    private String mKeyInfoJson;
    private Handler mHandler;
    private String mCurrentIP;
    private int mMeetingId;
    private String mDeviceIMEI;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDeviceIMEI = MPhone.getDeviceIMEI(this.getApplicationContext());
        mCurrentIP = SharedPreferencesUtil.getInstance(this).getString(Constant.CURRENT_IP, "");
        mMeetingId = SharedPreferencesUtil.getInstance(this).getInt(Constant.MEETING_ID, -1);
        String tcpServerIP = intent.getStringExtra("localIpAddress");
        String meetingName = intent.getStringExtra("meetingName");
        KeyInfoBean keyInfoBean = new KeyInfoBean(mCurrentIP, mMeetingId, tcpServerIP);
        keyInfoBean.setMeetingName(meetingName);
        Gson gson = new Gson();
        mKeyInfoJson = gson.toJson(keyInfoBean);

        // 通过Handler启动线程
        HandlerThread handlerThread = new HandlerThread("SendMulticastService");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(mRunnable);

        //检查更新
//        new Thread(checkUpdateRunnable).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    SystemClock.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //因为是udp组播发送，循环发送这些值到客户端，确保接收到
                MulticastController.getDefault().sendMessage(mKeyInfoJson);
            }
        }
    };

    private Runnable checkUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    SystemClock.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UpdatePost updatePost = new UpdatePost(
                        new MySubscriber(new HttpRxCallbackListener<Update>() {
                            @Override
                            public void onNext(Update update) {
                                if (update.getIsUpdate() == 0) {
                                    //有更新
                                }
                                Log.e(TAG, "onNext: 调用了CheckUpdate");
                            }

                            @Override
                            public void onError(String errorMsg) {

                            }
                        }
                                , SendMulticastService.this.getApplicationContext()),
                        mDeviceIMEI, mMeetingId);
                HttpManager.getInstance(SendMulticastService.this).doHttpDeal(updatePost);
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kill(KillService k) {
        if (k.getName().equals("SendMulticastService"))
            stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
