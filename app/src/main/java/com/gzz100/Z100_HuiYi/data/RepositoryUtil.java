package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.file.FileRepository;
import com.gzz100.Z100_HuiYi.data.file.local.FileLocalDataSource;
import com.gzz100.Z100_HuiYi.data.file.remote.FileRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingRepository;
import com.gzz100.Z100_HuiYi.data.meeting.local.MeetingLocalDataSource;
import com.gzz100.Z100_HuiYi.data.meeting.remote.MeetingRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateRepository;
import com.gzz100.Z100_HuiYi.data.delegate.local.DelegateLocalDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.remote.DelegateRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileRepository;
import com.gzz100.Z100_HuiYi.data.file.local.FileLocalDataSource;
import com.gzz100.Z100_HuiYi.data.file.remote.FileRemoteDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteRepository;
import com.gzz100.Z100_HuiYi.data.vote.local.VoteLocalDataSource;
import com.gzz100.Z100_HuiYi.data.vote.remote.VoteRemoteDataSource;

/**
 * Created by XieQXiong on 2016/8/26.
 */
public class RepositoryUtil {
    public static FileRepository getFileRepository(@NonNull Context context){
        return FileRepository.getInstance(FileRemoteDataSource.getInstance(context),
                FileLocalDataSource.getInstance(context),context);
    }

    public static DelegateRepository getDelegateRepository(@NonNull Context context){
        return DelegateRepository.getInstance(DelegateRemoteDataSource.getInstance(context),
                DelegateLocalDataSource.getInstance(context));
    }
    public static VoteRepository getVoteRepository(@NonNull Context context){
        return VoteRepository.getInstance(VoteRemoteDataSource.getInstance(context),
                VoteLocalDataSource.getInstance(context));
    }
    public static MeetingRepository getMeetingRepository(@NonNull Context context){
        return MeetingRepository.getInstance(MeetingRemoteDataSource.getInstance(context),
                MeetingLocalDataSource.getInstance(context),context);
    }
}
