package com.gzz100.Z100_HuiYi.data.delegate;

import android.support.annotation.NonNull;


import com.gzz100.Z100_HuiYi.data.delegate.local.DelegateLocalDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.remote.DelegateRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DELL on 2016/8/31.
 */
public class DelegateRepository implements DelegateDataSource {
    private static DelegateRepository INSTANCE;


    private final DelegateRemoteDataSource mDelegateRemoteDataSource;
    private final DelegateLocalDataSource mDelegateLocalDataSource;

    public  DelegateRepository (@NonNull DelegateRemoteDataSource delegateRemoteDataSource, @NonNull DelegateLocalDataSource delegateLocalDataSource){
        this.mDelegateRemoteDataSource=checkNotNull(delegateRemoteDataSource);
        this.mDelegateLocalDataSource=checkNotNull(delegateLocalDataSource);

    }

   public static DelegateRepository getInstance(@NonNull DelegateRemoteDataSource delegateRemoteDataSource, @NonNull DelegateLocalDataSource delegateLocalDataSource)
   {
       if(INSTANCE==null)
           return new DelegateRepository(delegateRemoteDataSource,delegateLocalDataSource);
       return INSTANCE;
   }

    @Override
    public void getRoleList(final LoadRoleListCallback callback) {
             mDelegateLocalDataSource.getRoleList(callback);
    }

    @Override
    public void getDelegateList(int rolePos,@NonNull LoadDelegateListCallback callback) {
        checkNotNull(callback);
            mDelegateLocalDataSource.getDelegateList(rolePos,callback);
    }

    @Override
    public void getDelegateBean(int delegateDetailPos, LoadDelegateDetailCallback callback) {

          mDelegateLocalDataSource.getDelegateBean(delegateDetailPos,callback);

    }


}
