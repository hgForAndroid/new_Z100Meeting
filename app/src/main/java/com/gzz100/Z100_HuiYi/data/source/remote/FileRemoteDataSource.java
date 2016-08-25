package com.gzz100.Z100_HuiYi.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.source.FileDataSource;
import com.gzz100.Z100_HuiYi.data.source.local.FileDBHelper;

import static com.google.common.base.Preconditions.checkNotNull;
/**
* 加载服务器数据
* @author XieQXiong
* create at 2016/8/25 14:42
*/

public class FileRemoteDataSource implements FileDataSource {
    private static FileRemoteDataSource INSTANCE;
    private final FileDBHelper mDbHelper;

    private FileRemoteDataSource(@NonNull Context context) {
        mDbHelper = FileDBHelper.getInstance(context);
    }

    public static FileRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FileRemoteDataSource(context);
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
