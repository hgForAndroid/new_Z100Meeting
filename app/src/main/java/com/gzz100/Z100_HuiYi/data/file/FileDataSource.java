package com.gzz100.Z100_HuiYi.data.file;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;

import java.util.List;
/**
* @author XieQXiong
* create at 2016/8/25 14:56
*/


public interface FileDataSource {
    interface LoadFileListCallback {
        void onFileListLoaded(List<Document> documents);

        void onDataNotAvailable();
    }
    interface LoadAgendaListCallback {
        void onAgendaListLoaded(List<Agenda> files);

        void onDataNotAvailable();
    }

    /**
     * 获取文件列表文件
     * @param agendaPos   议程序号，根据该序号获取
     * @param callback    获取结果回调函数
     */
    void getFileList(int agendaPos,@NonNull LoadFileListCallback callback);

    /**
     * 获取议程列表
     * @param IMEI     设备标识码
     * @param userId   用户id
     * @param callback 获取结果回调函数
     */
    void getAgendaList(String IMEI,String userId,@NonNull LoadAgendaListCallback callback);

    /**
     * 获取关键字搜索结果
     * @param fileOrName   文件名/人名
     * @param callback     获取结果回调函数
     */
    void getSearchResult(String fileOrName,@NonNull LoadFileListCallback callback);
}