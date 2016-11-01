package com.gzz100.Z100_HuiYi.meeting.delegate;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;

import java.util.List;

/**
 * Created by DELL on 2016/8/31.
 */
public interface DelegateContract {
    interface View extends BaseView {

        void showRoleList(List<String> roleList);

        void showDelegateDetail(DelegateModel delegateBean);

        void showDelegateList(List<DelegateModel> delegateBean);

        void showNoDelegateDetail();

        void showNoDelegateList();

        void showNoRoleList();

        void showRoleItemDecoration();

        void showDelegateNameGridItemDecoration();

        void setAutoCompleteTextView(List<DelegateModel> delegateBeans);

    }
    interface Presenter extends BasePresenter {

        void fetchRoleList();

        void fetchDelegateList(int position);

        void showDelegateDetail( int position);

        void setDelegateSearchAutoCompleteTextViewHint();

        void setFirstLoad(boolean reLoad);

        void searchByName(String nameInput);

    }
}
