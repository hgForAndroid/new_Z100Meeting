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
    interface LoadDelegateNameHintCallback {
        void onDelegateNameHintLoaded(List<DelegateBean> delegataBeanList);

        void onDataNotAvailable();
    }

    void getDelegateList(int rolePos, DelegateDataSource.LoadDelegateListCallback callback);

    void getDelegateNameHint(LoadDelegateNameHintCallback callback);
}
