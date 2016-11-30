package com.gzz100.Z100_HuiYi.data.checkUpdate;

import com.gzz100.Z100_HuiYi.data.MeetingSummaryBean;

import org.jetbrains.annotations.NotNull;

/**
 * 描述：检查更新的数据接口
 * 包名：com.gzz100.Z100_HuiYi.data.checkUpdate
 * 类名：CheckUpdateDataSource
 *
 * @author XieQingXiong
 * @创建时间 2016/11/30   9:51
 */
public interface CheckUpdateDataSource {
    /**
     * 访问数据库，是否有更新数据的回调接口
     */
    interface CheckUpdateCallback{
        /**
         * 有数据更新
         */
        void update();

        /**
         * 无数据更新
         */
        void notUpdate();
    }

    /**
     * 获取最新数据的接口
     */
    interface LoadUpdateDataCallback{
        /**
         * 获取成功
         * @param summaryBean
         */
        void onUpdateDataLoaded(MeetingSummaryBean summaryBean);

        /**
         *
         * @param error
         */
        void onDataNotAvailable(String error);
    }

    /**
     * 检查文件是否有更新
     * @param IMEI          设备IMEI
     * @param meetingID     会议id
     * @param callback      更新与否回调接口
     */
    void checkUpdate(String IMEI,int meetingID,@NotNull CheckUpdateCallback callback);

    void fetchUpdateData(String IMEI,int meetingID,@NotNull LoadUpdateDataCallback callback);
}
