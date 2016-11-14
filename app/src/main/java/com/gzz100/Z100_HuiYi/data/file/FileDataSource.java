package com.gzz100.Z100_HuiYi.data.file;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;

import java.util.List;
/**
* @author XieQXiong
* create at 2016/8/25 14:56
*/


public interface FileDataSource {
    interface LoadFileListCallback {
        void onFileListLoaded(List<DocumentModel> documents);

        void onDataNotAvailable();
    }
    interface LoadAgendaListCallback {
        void onAgendaListLoaded(List<AgendaModel> files);

        void onDataNotAvailable();
    }

    interface LoadFileSearchNameCallback{
        void onNameHintLoaded(List<String> fileSearchNameHintList);

        void onDataNotAvailable();
    }

    interface getFileByNameCallback{
        void fileDidGet(Document document);
        void fileNotGet();
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

    void getSearchNameHint(LoadFileSearchNameCallback callback);

    void getFileByName(String fileName, getFileByNameCallback callback);

    boolean isFileName(String inputString);
}
