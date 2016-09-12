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

        void showNoDelegateDetail();

        void showNoDelegateList();

        void showNoRoleList();
    }
    interface Presenter extends BasePresenter {

        void fetchRoleList();

        void fetchDelegateList(int position);

        void showDelegateDetail( int position);

        void searchByName();


    }
}
