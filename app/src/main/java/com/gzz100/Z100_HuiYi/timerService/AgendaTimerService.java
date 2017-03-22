package com.gzz100.Z100_HuiYi.timerService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.data.eventBean.KillService;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

/**
 * @author XieQingXiong
 * @description 议程倒计时的服务
 * @packageName com.gzz100.Z100_HuiYi.timerService
 * @className
 * @time 2017/3/13   14:43
 */

public class AgendaTimerService extends Service {

    public static final int AGENDA_TIME_FINISH = 99;
    public static final int AGENDA_INDEX_CHANGE = 100;
    public static final int HOST_BACK_TO_MAIN = 101;
    private MyHandler mHandler;
    private int mMin, mSec;
    //标识值，如果该值为true，则不将消息发到文件详情页面显示
    private boolean flag = true;

    private int waitingTime = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mHandler = new MyHandler(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMin = intent.getExtras().getInt("min");
        mSec = intent.getExtras().getInt("sec");
        return super.onStartCommand(intent, flags, startId);
    }

    private static class MyHandler extends Handler {

        private WeakReference<AgendaTimerService> weakRef;

        public MyHandler(AgendaTimerService service) {
            weakRef = new WeakReference<AgendaTimerService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakRef.get() != null) {
                if (msg.what == 0x01) {
                    AgendaTimer timer = new AgendaTimer();
                    timer.setMin(weakRef.get().getMin());
                    timer.setSec(weakRef.get().getSec(false));
//                    Log.e("xqx", "handleMessage: 分："+weakRef.get().getMin()+", 秒："+weakRef.get().getSec(false));
                    if (weakRef.get().mMin < 0 ){
                        return;
                    }
                    weakRef.get().mSec--;
                    if (weakRef.get().flag) {
//                        Log.e("xqx", "handleMessage: 有发消息到文件详情页面");
                        EventBus.getDefault().post(timer);
                    }
                    if (MyAPP.getInstance().getHostOutOfMeeting()){
                        weakRef.get().saveCountingMinAndSec(weakRef.get().getMin(),weakRef.get().getSec(false));
                    }
                    this.sendEmptyMessageDelayed(0x01, weakRef.get().waitingTime);
                }
            }
        }
    }

    /**
     * 设置分钟的显示值，如1-9，则在前面加上“0”，否则直接返回该值的字符串。
     *
     * @return 重新设置后的新的值
     */
    private String getMin() {
        String newHour = mMin >= 10 ? "" + mMin : "0" + mMin;
        return newHour;
    }

    /**
     * 设置秒钟的显示值，如1-9，则在前面加上“0”，否则不加
     * 然后，判断该值是否等于“00”，是则代表过了一分钟，将分钟数减1，秒钟重新设置为60，继续倒计时。
     *
     * @param flag 会议进行中主动切换议程。主持人为true，其他为false。
     * @return 重新设置后的新的值。
     */
    private String getSec(boolean flag) {
        String newSex = mSec >= 10 ? "" + mSec : "0" + mSec;
        if (newSex.equals("00")) {
            mMin--;
            if (flag) {
                mSec = 59;
            } else {
                mSec = 60;
            }
            return "00";
        }
        return newSex;
    }

    /**
     * 暂停时，需要保存倒计时的分和秒
     */
    private void saveCountingMinAndSec(String min, String sec) {
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putString(Constant.COUNTING_MIN, min);
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putString(Constant.COUNTING_SEC, sec);
    }

    /**
     * 暂停时，需要保存议程序号与议程文件序号
     *
     * @param agendaIndex 议程序号
     * @param fileIndex   议程文件序号
     */
    private void savePauseAgendaIndexAndDocumentIndex(int agendaIndex, int fileIndex) {
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putInt(Constant.PAUSE_AGENDA_INDEX, agendaIndex);
        SharedPreferencesUtil.getInstance(this.getApplicationContext())
                .putInt(Constant.PAUSE_DOCUMENT_INDEX, fileIndex);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleOrder(AgendaOrder agendaOrder) {
        switch (agendaOrder.getFlag()) {
            case Constant.MEETING_STATE_BEGIN://begin
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(0x01, waitingTime);
                break;
            case Constant.MEETING_STATE_CONTINUE://continue
                mHandler.removeCallbacksAndMessages(null);
                mMin = agendaOrder.getContinueMin();
                mSec = agendaOrder.getContinueSec();
                mHandler.sendEmptyMessage(0x01);
                break;
            case Constant.MEETING_STATE_PAUSE://pause
                mHandler.removeCallbacksAndMessages(null);
                saveCountingMinAndSec(resetNum(agendaOrder.getContinueMin()), resetNum(agendaOrder.getContinueSec()));
                savePauseAgendaIndexAndDocumentIndex(agendaOrder.getAgendaIndex(), agendaOrder.getFileIndex());
                break;
            case Constant.MEETING_STATE_ENDING://endingMeeting
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
                break;
            case AGENDA_TIME_FINISH://议程时间结束
                mHandler.removeCallbacksAndMessages(null);
                break;
            case AGENDA_INDEX_CHANGE:
                //切换议程，延迟一秒后倒计时，跟上面 继续 的代码一样，主要是为了区分不同功能，所以没有写在一起
                mHandler.removeCallbacksAndMessages(null);
                mMin = agendaOrder.getContinueMin();
                mSec = agendaOrder.getContinueSec();
                mHandler.sendEmptyMessageDelayed(0x01, waitingTime);
                break;
            case HOST_BACK_TO_MAIN:
                //主持人退出文件详情界面，需要保存推出前的会议进行状态，时间不需要保存，直接在这里的服务继续计时，只是不发送到文件详情界面进行显示
                savePauseAgendaIndexAndDocumentIndex(agendaOrder.getAgendaIndex(), agendaOrder.getFileIndex());
                break;
            default:
                break;
        }
    }

    /**
     * 是否让当前倒计时的时间发送到文件详情页面进行显示。
     * 从{@link FileDetailActivity#hostBackToHomeInProgress(boolean)} ()}中调用。
     *
     * @param timeFlag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleTimeFlag(TimeFlag timeFlag) {
        flag = timeFlag.isFlag();
    }

    /**
     * 重新设置数字，如果是1-9，则在前面加上“0”，否则返回原值（字符串格式）
     *
     * @param num 需设置的值
     * @return 重新设置的值
     */
    public static String resetNum(int num) {
        return num > 9 ? num + "" : "0" + num;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kill(KillService k) {
        if (k.getName().equals("AgendaTimerService"))
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
