package com.gzz100.Z100_HuiYi.data.signIn;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateBean;

/**
 * Created by XieQXiong on 2016/9/23.
 */
public interface SignInDataSource {
    interface LoadDelegateCallback{
        void onDelegateLoaded(DelegateBean delegate);
        void onDataNotAvailable();
    }

    /**
     * 获取当前设备对应的参会人员
     * @param IMEI        设备id
     * @param meetingID   会议id
     * @param callback    获取结果回调函数
     */
    void fetchDelegate(String IMEI, String meetingID, @NonNull LoadDelegateCallback callback);
}
