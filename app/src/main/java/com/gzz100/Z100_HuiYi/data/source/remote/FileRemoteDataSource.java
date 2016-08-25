package com.gzz100.Z100_HuiYi.data.source.remote;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.source.FileDataSource;

import static com.google.common.base.Preconditions.checkNotNull;
/**
* 加载服务器数据
* @author XieQXiong
* create at 2016/8/25 14:42
*/

public class FileRemoteDataSource implements FileDataSource {
    private static FileRemoteDataSource INSTANCE;

    private FileRemoteDataSource() {
    }

    public static FileRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileRemoteDataSource();
        }
        return INSTANCE;
    }
    @Override
    public void getFileList(int agendaPos, @NonNull loadFileListCallback callback) {
        checkNotNull(callback);
        //加载服务器数据


        //去完数据需存在本地
    }

    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull loadAgendaListCallback callback) {

    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull loadFileListCallback callback) {

    }
}
