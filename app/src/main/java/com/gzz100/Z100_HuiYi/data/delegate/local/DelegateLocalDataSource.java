package com.gzz100.Z100_HuiYi.data.delegate.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateOperate;
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

    private boolean mIsFakeData=false;

    private Context mContext;

    private DelegateLocalDataSource(@NonNull Context context){
        super();
        mContext=context;
    }
    public static DelegateLocalDataSource getInstance(@NonNull Context context)
    {
        if(INSTANCE==null)
            INSTANCE=new DelegateLocalDataSource(context);
        return INSTANCE;
    }

    public void getRoleList(@NonNull LoadRoleListCallback callback) {
        checkNotNull(callback);

        List<String> roleList=new ArrayList<>();
        roleList.add(speaker);
        roleList.add(host);
        roleList.add(otherDelegate);

        callback.onRoleListLoaded(roleList);
    }

    @Override
    public void getDelegateList(int rolePos,@NonNull LoadDelegateListCallback callback) {
        checkNotNull(callback);

//        if(mIsFakeData) {
//            //假数据
//            List<DelegateBean> delegateBeanList = FakeDataProvider.getDelegateBeanByRolePos(rolePos);
//
//            if (delegateBeanList != null && delegateBeanList.size() > 0)
//                callback.onDelegateListLoaded(delegateBeanList);
//            else
//                callback.onDataNotAvailable();
//        }
//        //留给DB
//        else
//        {
//            List<DelegateBean> delegateBeanList = DelegateOperate.getInstance(mContext).queryDelelgateListByRole(rolePos);
//
//            if(delegateBeanList!=null&&delegateBeanList.size()>0){
//                callback.onDelegateListLoaded(delegateBeanList);
//            }
//            else
//                callback.onDataNotAvailable();
//        }

    }

    @Override
    public void getDelegateNameHint(LoadDelegateNameHintCallback callback) {

//        if (mIsFakeData)
//        {
//            //假数据
//            List<DelegateBean> allDelegate = new ArrayList<>();
//            for (int i = 0; i < 3; i++) {
//                List<DelegateBean> delegateBeanList = FakeDataProvider.getDelegateBeanByRolePos(i);
//                for (DelegateBean delegate : delegateBeanList) {
//                    allDelegate.add(delegate);
//                }
//            }
//            if (allDelegate != null && allDelegate.size() > 0) {
//                callback.onDelegateNameHintLoaded(allDelegate);
//            } else {
//                callback.onDataNotAvailable();
//            }
//        }
//        else
//        {
//            List<DelegateBean> allDelegate = DelegateOperate.getInstance(mContext).queryAllDelegate();
//
//            if (allDelegate != null && allDelegate.size() > 0) {
//                callback.onDelegateNameHintLoaded(allDelegate);
//            } else {
//                callback.onDataNotAvailable();
//            }
//        }
    }
}
