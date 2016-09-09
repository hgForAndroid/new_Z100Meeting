package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateDataSource;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateRepository;


import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DELL on 2016/8/31.
 */
public class DelegatePresenter implements DelegateContract.Presenter {
    private final DelegateRepository mDelegateRepository;
    private final DelegateContract.View mDelegateView;

    private boolean mRoleListIsFirstLoad =true;
    private boolean mDelegateListIsFirstLoad=true;

    public DelegatePresenter (@NonNull DelegateRepository delegateRepository, DelegateContract.View delegateView) {
        this.mDelegateRepository = checkNotNull(delegateRepository,"delegateRepository cannot be null");
        this.mDelegateView = checkNotNull(delegateView,"delegateView cannot be null");
        mDelegateView.setPresenter(this);
    }
    @Override
    public void start() {
        fetchRoleList();
        fetchDelegateList(true,0);
    }


    @Override
    public void fetchRoleList() {
//        if(mRoleListIsFirstLoad) {
            mDelegateRepository.getRoleList(new DelegateDataSource.LoadRoleListCallback() {
                @Override
                public void onRoleListLoaded(List<String> roleList) {
                    mDelegateView.showRoleList(roleList);
                }

                @Override
                public void onDataNotAvailable() {
                    mDelegateView.showNoRoleList();
                }
            });

//            setRoleListFirstLoad(mRoleListIsFirstLoad,false);
   /*     }
        else
            return;*/


    }

    @Override
    public void fetchDelegateList(boolean forceUpdate,int rolePos) {
        //保留给强制刷新和第一次加载(虽然不更新)

//        if(mDelegateListIsFirstLoad) {
            mDelegateRepository.getDelegateList(rolePos, new DelegateDataSource.LoadDelegateListCallback() {
                @Override
                public void onDelegateListLoaded(List<DelegateBean> delegateBeans) {
                    mDelegateView.showDelegateList(delegateBeans);
                }

                @Override
                public void onDataNotAvailable() {
                    mDelegateView.showNoDelegateList();
                }
            });
//            setDelegateListFirstLoad(mDelegateListIsFirstLoad,false);
  /*      }
        else
            return;*/

    }

    @Override
    public void searchByName() {

    }

    @Override
    public void showDelegateDetail(boolean forceUpdate, final int delegateDetailPos) {
        mDelegateRepository.getDelegateBean(delegateDetailPos,new DelegateDataSource.LoadDelegateDetailCallback() {

            @Override
            public void onDelegateDetailLoaded(DelegateBean delegateBeans) {
                mDelegateView.showDelegateDetail(delegateBeans,delegateDetailPos);
            }

            @Override
            public void onDataNotAvailable() {
                mDelegateView.showNoDelegateDetail();
            }
        });



    }

    @Override
    public void switchRole() {

    }

    @Override
    public void setRoleListFirstLoad(boolean roleListFirstLoad,boolean reLoad) {
        mRoleListIsFirstLoad=reLoad;
    }

    @Override
    public void setDelegateListFirstLoad(boolean delegateListFirstLoad, boolean reLoad) {
        mDelegateListIsFirstLoad=reLoad;
    }
}
