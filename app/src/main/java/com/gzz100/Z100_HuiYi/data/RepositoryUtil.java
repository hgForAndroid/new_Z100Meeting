package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.delegate.DelegateRepository;
import com.gzz100.Z100_HuiYi.data.delegate.local.DelegateLocalDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.remote.DelegateRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.source.FileRepository;
import com.gzz100.Z100_HuiYi.data.source.local.FileLocalDataSource;
import com.gzz100.Z100_HuiYi.data.source.remote.FileRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteRepository;
import com.gzz100.Z100_HuiYi.data.vote.local.VoteLocalDataSource;
import com.gzz100.Z100_HuiYi.data.vote.remote.VoteRemoteDataSource;

/**
 * Created by XieQXiong on 2016/8/26.
 */
public class RepositoryUtil {
    public static FileRepository getFileRepository(@NonNull Context context){
        return FileRepository.getInstance(FileRemoteDataSource.getInstance(context),
                FileLocalDataSource.getInstance(context));
    }

    public static DelegateRepository getDelegateRepository(@NonNull Context context){
        return DelegateRepository.getInstance(DelegateRemoteDataSource.getInstance(context),
                DelegateLocalDataSource.getInstance(context));
    }
    public static VoteRepository getVoteRepository(@NonNull Context context){
        return VoteRepository.getInstance(VoteRemoteDataSource.getInstance(context),
                VoteLocalDataSource.getInstance(context));
    }
}
