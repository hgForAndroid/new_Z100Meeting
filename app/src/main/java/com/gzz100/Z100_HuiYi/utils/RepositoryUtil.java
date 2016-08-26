package com.gzz100.Z100_HuiYi.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.source.FileRepository;
import com.gzz100.Z100_HuiYi.data.source.local.FileLocalDataSource;
import com.gzz100.Z100_HuiYi.data.source.remote.FileRemoteDataSource;

/**
 * Created by XieQXiong on 2016/8/26.
 */
public class RepositoryUtil {
    public static FileRepository getFileRepository(@NonNull Context context){
        return FileRepository.getInstance(FileRemoteDataSource.getInstance(context),
                FileLocalDataSource.getInstance(context));
    }
}
