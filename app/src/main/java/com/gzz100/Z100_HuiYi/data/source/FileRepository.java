package com.gzz100.Z100_HuiYi.data.source;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/8/25.
 */
public class FileRepository implements FileDataSource {
    private static FileRepository INSTANCE = null;

    private final FileDataSource mFileRemoteDataSource;

    private final FileDataSource mFileLocalDataSource;

    //文件列表是否已有本地缓存  TODO   测试为true
    boolean mFileListCacheIsDirty = true;
    //议程列表是否已有本地缓存  TODO   测试为true
    boolean mAgendaListCacheIsDirty = true;

    private FileRepository(@NonNull FileDataSource fileRemoteDataSource,
                            @NonNull FileDataSource fileLocalDataSource) {
        mFileRemoteDataSource = checkNotNull(fileRemoteDataSource);
        mFileLocalDataSource = checkNotNull(fileLocalDataSource);
    }

    public static FileRepository getInstance(@NonNull FileDataSource fileRemoteDataSource,
                                             @NonNull FileDataSource fileLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FileRepository(fileRemoteDataSource, fileLocalDataSource);
        }
        return INSTANCE;
    }


    public void setFileListCacheIsDirty(boolean cacheIsDirty){
        this.mFileListCacheIsDirty = cacheIsDirty;
    }
    //如果有临时添加议程，在发起获取 包括新议程的全部议程 的请求时，需设置该值为false
    public void setAgendaListCacheIsDirty(boolean cacheIsDirty){
        this.mAgendaListCacheIsDirty = cacheIsDirty;
    }

    @Override
    public void getFileList(int agendaPos, @NonNull LoadFileListCallback callback) {
        checkNotNull(callback);
        if (mFileListCacheIsDirty){
            mFileLocalDataSource.getFileList(agendaPos,callback);
        }else{
            mFileRemoteDataSource.getFileList(agendaPos,callback);
            //注明已存本地
            setFileListCacheIsDirty(true);
        }
    }

    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull LoadAgendaListCallback callback) {
        checkNotNull(callback);
        if (mAgendaListCacheIsDirty){
            mFileLocalDataSource.getAgendaList(IMEI,userId,callback);
        }else{
            mFileRemoteDataSource.getAgendaList(IMEI,userId,callback);
            //注明已存本地
            setAgendaListCacheIsDirty(true);
        }
    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull LoadFileListCallback callback) {
        checkNotNull(callback);
    }
}
