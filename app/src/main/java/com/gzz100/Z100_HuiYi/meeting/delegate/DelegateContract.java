package com.gzz100.Z100_HuiYi.meeting.delegate;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.DelegateBean;

import java.util.List;

/**
 * Created by DELL on 2016/8/31.
 */
public interface DelegateContract {
    interface View extends BaseView {


        void showRoleList(List<String> roleList);

        void showDelegateDetail(DelegateBean delegateBean, int delegateDetailPos);

        void showDelegateList(List<DelegateBean> delegateBean);

        void showRoleSwitch();

        void showNoDelegateDetail();

        void showNoDelegateList();

        void showNoRoleList();
    }
    interface Presenter extends BasePresenter {

        void fetchRoleList();

        void fetchDelegateList(boolean forceUpdate, int position);

        void showDelegateDetail(boolean forceUpdate, int position);

        void searchByName();

        void switchRole();
        void setRoleListFirstLoad(boolean roleListFirstLoad, boolean reLoad);
        void setDelegateListFirstLoad(boolean delegateListFirstLoad, boolean reLoad);



    }
}
