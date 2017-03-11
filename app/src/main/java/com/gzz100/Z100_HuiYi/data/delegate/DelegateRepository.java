package com.gzz100.Z100_HuiYi.data.delegate;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.delegate.local.DelegateLocalDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.remote.DelegateRemoteDataSource;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.List;

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

    public void getRoleList(final LoadRoleListCallback callback) {
             mDelegateLocalDataSource.getRoleList(callback);
    }

    @Override
    public void getDelegateList(final int rolePos, @NonNull final LoadDelegateListCallback callback) {
        checkNotNull(callback);

        int roleNum=rolePosConvertToRoleNum(rolePos);

            mDelegateLocalDataSource.getDelegateList(roleNum, new LoadDelegateListCallback() {
                @Override
                public void onDelegateListLoaded(List<DelegateModel> delegateBeans) {
                    callback.onDelegateListLoaded(delegateBeans);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
    }

    @Override
    public void getAllDelegateList(LoadDelegateListCallback callback) {
        mDelegateLocalDataSource.getAllDelegateList(callback);
    }

    public static int rolePosConvertToRoleNum(int rolePos){
        int roleNum;
        switch (rolePos)
        {
            case 0:
                roleNum= Constant.DEFAULT_SPEAKER;
                break;
            case 1:
                roleNum= Constant.HOSTS;
                break;
            case 2:
                roleNum= Constant.OTHER_DELEGATE;
                break;
            default:
                roleNum=-1;
                break;
        }
        return roleNum;
    }


    @Override
    public void getDelegateNameHint(LoadDelegateNameHintCallback callback) {
        mDelegateLocalDataSource.getDelegateNameHint(callback);

    }


}
