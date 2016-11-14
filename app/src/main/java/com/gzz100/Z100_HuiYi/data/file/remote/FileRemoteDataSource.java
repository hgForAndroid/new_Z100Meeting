package com.gzz100.Z100_HuiYi.data.file.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.file.FileDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
* 加载服务器数据
* @author XieQXiong
* create at 2016/8/25 14:42
*/

public class FileRemoteDataSource implements FileDataSource {
    private static FileRemoteDataSource INSTANCE;
    private FileOperate mFileOperate;
    private static Context mContext;

    private FileRemoteDataSource(@NonNull Context context) {
        mFileOperate = FileOperate.getInstance(context);
    }

    public static FileRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FileRemoteDataSource(context);
            mContext = context;
        }
        return INSTANCE;
    }
    @Override
    public void getFileList(final int agendaPos, @NonNull final LoadFileListCallback callback) {
        checkNotNull(callback);
        //测试存数据库
//        List<DocumentModel> fileListByIndex = FakeDataProvider.getFileListByindex(agendaPos);
//        callback.onFileListLoaded(fileListByIndex);
//        mFileOperate.insertFileList(agendaPos,fileListByIndex);
        //加载服务器数据

        //去完数据需存在本地
    }


    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull final LoadAgendaListCallback callback) {
        checkNotNull(callback);
        //应该是访问服务器
//        List<AgendaModel> agendas = FileOperate.getInstance(mContext).queryAgendaList();
//        if (agendas != null && agendas.size() > 0){
//            callback.onAgendaListLoaded(agendas);
//            mFileOperate.insertAgendaList(agendas);
//        }else {
//            callback.onDataNotAvailable();
//        }
    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull LoadFileListCallback callback) {

    }

    @Override
    public void getSearchNameHint(LoadFileSearchNameCallback callback) {

    }

    @Override
    public void getFileByName(String fileName, getFileByNameCallback callback) {

    }

    @Override
    public boolean isFileName(String inputString) {
        return false;
    }
}
