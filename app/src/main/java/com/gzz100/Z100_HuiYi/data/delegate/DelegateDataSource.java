package com.gzz100.Z100_HuiYi.data.delegate;



import com.gzz100.Z100_HuiYi.data.DelegateBean;

import java.util.List;

/**
 * Created by DELL on 2016/8/31.
 */
public interface DelegateDataSource {

    /**
     * Created by DELL on 2016/9/2.
     */
    interface LoadRoleListCallback{
        void onRoleListLoaded(List<String> roleList);

        void onDataNotAvailable();
    }
    interface LoadDelegateListCallback {
        void onDelegateListLoaded(List<DelegateBean> delegateBeans);

        void onDataNotAvailable();
    }
    interface LoadDelegateDetailCallback{
        void onDelegateDetailLoaded(DelegateBean delegateBeans);

        void onDataNotAvailable();
    }
    void getRoleList(DelegateDataSource.LoadRoleListCallback callback);
    void getDelegateList(int rolePos, DelegateDataSource.LoadDelegateListCallback callback);
    void getDelegateBean(int delegateNamePos, DelegateDataSource.LoadDelegateDetailCallback callback);
}
