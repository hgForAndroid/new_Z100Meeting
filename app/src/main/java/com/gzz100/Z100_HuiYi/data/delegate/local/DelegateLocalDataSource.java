package com.gzz100.Z100_HuiYi.data.delegate.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateDataSource;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DELL on 2016/8/31.
 */
public class DelegateLocalDataSource implements DelegateDataSource {
    private static DelegateLocalDataSource INSTANCE;
    private String speaker ="主讲人";
    private String host="主持人";
    private String otherDelegate="其他参会代表";

    private List<String> roleList=new ArrayList<>();
    private boolean mRoleListIsInit =false;

    private DelegateLocalDataSource(@NonNull Context context){
        super();

    }
    public static DelegateLocalDataSource getInstance(@NonNull Context context)
    {
        if(INSTANCE==null)
            INSTANCE=new DelegateLocalDataSource(context);
        return INSTANCE;
    }


    public void initRoleList() {
  /*      if(mRoleListIsInit)
            return;
        else {*/
            roleList.add(speaker);
            roleList.add(host);
            roleList.add(otherDelegate);
       /* }*/

    }

    @Override
    public void getRoleList(@NonNull LoadRoleListCallback callback) {
        checkNotNull(callback);
        if(!mRoleListIsInit) {
            initRoleList();
            mRoleListIsInit=true;
        }
        callback.onRoleListLoaded(roleList);
    }

    @Override
    public void getDelegateList(int rolePos,@NonNull LoadDelegateListCallback callback) {
        checkNotNull(callback);
        //假数据
        List<DelegateBean> delegateBean= FakeDataProvider.getDelegateBeanByRolePos(rolePos);

        if(delegateBean!=null&&delegateBean.size()>0)
            callback.onDelegateListLoaded(delegateBean);
        else
            callback.onDataNotAvailable();
    }

    @Override
    public void getDelegateBean(int delegateNamePos,@NonNull LoadDelegateDetailCallback callback) {
        checkNotNull(callback);

        //假数据
        DelegateBean delegateBean= FakeDataProvider.getDelegateDetailByNamePos(delegateNamePos);

        if(delegateBean!=null)
            callback.onDelegateDetailLoaded(delegateBean);
        else
            callback.onDataNotAvailable();

    }
}
