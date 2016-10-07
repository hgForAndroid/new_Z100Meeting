package com.gzz100.Z100_HuiYi.data.delegate.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateOperate;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;

import java.util.List;


/**
 * Created by DELL on 2016/8/31.
 */
public class DelegateRemoteDataSource implements DelegateDataSource{

    private static DelegateRemoteDataSource INSTANCE;
    private DelegateOperate mDelelgateOperate;

    private DelegateRemoteDataSource(@NonNull Context context)
    {
        super();
        mDelelgateOperate=DelegateOperate.getInstance(context);
    }

    public static DelegateRemoteDataSource getInstance(@NonNull Context context){
        if(INSTANCE==null)
            INSTANCE=new DelegateRemoteDataSource(context);
        return INSTANCE;
    }

    @Override
    public void getDelegateList(int rolePos, LoadDelegateListCallback callback) {
        //测试数据库
        //// TODO: 2016/10/7 此处假数据库的逻辑应优化...虽然其实不用
        List<DelegateBean> delegateBeanList = FakeDataProvider.getDelegateBeanByRolePos(rolePos);

        callback.onDelegateListLoaded(delegateBeanList);
        mDelelgateOperate.insertDelegateList(rolePos,delegateBeanList);

        List<DelegateBean> delegateBeanList2 = FakeDataProvider.getDelegateBeanByRolePos(rolePos+1);

        mDelelgateOperate.insertDelegateList(rolePos+1,delegateBeanList2);

        List<DelegateBean> delegateBeanList3 = FakeDataProvider.getDelegateBeanByRolePos(rolePos+2);

        mDelelgateOperate.insertDelegateList(rolePos+2,delegateBeanList3);


    }

    @Override
    public void getDelegateNameHint(LoadDelegateNameHintCallback callback) {

    }

}
