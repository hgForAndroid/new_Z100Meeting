package com.gzz100.Z100_HuiYi.timerService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.gzz100.Z100_HuiYi.data.eventBean.KillService;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

public class MeetingTimerService extends Service {

    public static final int MEETING_TIME_START = 200;
    public static final int TEMP_PEOPLE_IN_TIME_START = 201;
    public static final int ADJUST_MEETING_TIME_FOR_ALL_CLIENT = 202;
    private int hour = 0;
    private int min = 0;
    private MyHandler mHandler;

    private int waitingTime = 60000;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mHandler = new MyHandler(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        hour = intent.getIntExtra("hour",-1);
//        min = intent.getIntExtra("min",-1);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static class MyHandler extends Handler {

        private WeakReference<MeetingTimerService> weakRef;

        public MyHandler(MeetingTimerService service) {
            weakRef = new WeakReference<MeetingTimerService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakRef.get() != null) {
                if (msg.what == 0x02) {

                    weakRef.get().min++;
                    MeetingTimer timer = new MeetingTimer();
                    timer.setMin(weakRef.get().getMin());
                    timer.setHour(weakRef.get().getHour());
                    EventBus.getDefault().post(timer);
                    saveMeetingTime(weakRef.get().getHour(),weakRef.get().getMin());
                    //一分钟发送一次
                    this.sendEmptyMessageDelayed(0x02, weakRef.get().waitingTime);
                }
            }
        }
        private void saveMeetingTime(String hour, String min) {
            SharedPreferencesUtil.getInstance(weakRef.get().getApplicationContext()).putString(Constant.MEETING_BEGIN_TIME_HOUR,hour);
            SharedPreferencesUtil.getInstance(weakRef.get().getApplicationContext()).putString(Constant.MEETING_BEGIN_TIME_MIN,min);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMeetingOrder(MeetingOrder order) {
        switch (order.getOrderType()) {
            case MEETING_TIME_START://开始计时
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(0x02, waitingTime);
                break;
            case TEMP_PEOPLE_IN_TIME_START://临时有人进来，让时间跟主持人的时间一致
                mHandler.removeCallbacksAndMessages(null);
                hour = order.getHour();
                min = order.getMin();
                mHandler.sendEmptyMessageDelayed(0x02, waitingTime);
                break;
            case Constant.MEETING_STATE_ENDING://会议结束
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
                break;
            case ADJUST_MEETING_TIME_FOR_ALL_CLIENT://给所有的客户端校正时间，使得所有客户端时间均跟主持人的时间一致。
                mHandler.removeCallbacksAndMessages(null);
                hour = order.getHour();
                min = order.getMin();
                mHandler.sendEmptyMessageDelayed(0x02, waitingTime);
                break;
            default:
                break;
        }
    }

    /**
     * 获取小时的字符串，0-9，在数字前面加0，其他的不加。
     *
     * @return
     */
    public String getHour() {
        String newHour = hour >= 10 ? "" + hour : "0" + hour;
        return newHour;
    }

    /**
     * 获取分钟的字符串，0-9，在数字前面加0，其他的不变。如果到了60，直接变为00。
     *
     * @return
     */
    public String getMin() {
        String newMin = min >= 10 ? "" + min : "0" + min;
        if (newMin.equals("60")) {
            hour++;
            min = 0;
            return "00";
        }
        return newMin;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kill(KillService k) {
        if (k.getName().equals("MeetingTimerService"))
            stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

}
