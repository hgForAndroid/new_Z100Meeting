package com.gzz100.Z100_HuiYi.data.delegate.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.delegate.DelegateDataSource;


/**
 * Created by DELL on 2016/8/31.
 */
public class DelegateRemoteDataSource implements DelegateDataSource{

    private static DelegateRemoteDataSource INSTANCE;


    private DelegateRemoteDataSource(@NonNull Context context)
    {
        super();
    }

    public static DelegateRemoteDataSource getInstance(@NonNull Context context){
        if(INSTANCE==null)
            INSTANCE=new DelegateRemoteDataSource(context);
        return INSTANCE;
    }

    @Override
    public void getRoleList(DelegateDataSource.LoadRoleListCallback callback) {

    }

    @Override
    public void getDelegateList(int rolePos, LoadDelegateListCallback callback) {

    }

    @Override
    public void getDelegateBean(int delegateNamePos, LoadDelegateDetailCallback callback) {

    }
}
