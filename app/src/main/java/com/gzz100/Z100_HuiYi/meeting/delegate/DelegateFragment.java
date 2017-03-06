package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.delegate.DelegateRepository;
import com.gzz100.Z100_HuiYi.meeting.MainActivity;
import com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail.DelegateDetailActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 参会代表
 * @author DELL
 * create at 2016/9/2
 */
public class DelegateFragment extends Fragment implements  DelegateContract.View,
        OnRoleItemClickListener,OnDelegateNameItemClickListener {
    AutoCompleteTextView mEdtSearchContent;
    Button mBntSearch;
    RecyclerView mRoleListRecView;
    RecyclerView mDelegateListRecView;
    private DelegatePresenter mPresenter;

    public static DelegateFragment newInstance(){return new DelegateFragment();}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("DelegateFragment -->","onResume");
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delegate, container,false);
        mEdtSearchContent=(AutoCompleteTextView) view.findViewById(R.id.id_edt_fgm_delegate);
        mBntSearch=(Button)view.findViewById(R.id.id_btn_fgm_delegate);
        mRoleListRecView=(RecyclerView)view.findViewById(R.id.id_rev_fgm_tab);
        mDelegateListRecView=(RecyclerView)view.findViewById(R.id.id_rev_fgm_delegate_list);
        mPresenter.start();
        mPresenter.setFirstLoad(false);
        setSearchButton();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.setFirstLoad(true);
        mLastClickedRoleItemPositon= DelegateRepository.rolePosConvertToRoleNum(Constant.DEFAULT_SPEAKER);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setPresenter(Object presenter) {
        mPresenter=(DelegatePresenter) presenter;
    }

    @Override
    public void showRoleList(List<String> roleList) {
        RoleListAdapter mRoleListAdapter;
        mRoleListAdapter=new RoleListAdapter(getContext(),roleList);
        mRoleListRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        mRoleListRecView.setAdapter(mRoleListAdapter);
        mRoleListAdapter.setRoleItemOnClickListener(this);
    }

    @Override
    public void showNoRoleList() {

    }

    @Override
    public void showRoleItemDecoration() {
        int space = getResources().getDimensionPixelSize(R.dimen.distance_one_hundred_dp);
        mRoleListRecView.addItemDecoration(new RoleItemSpaceDecoration(space));
    }

    @Override
    public void showDelegateNameGridItemDecoration() {
        int space = getResources().getDimensionPixelSize(R.dimen.distance_twenty_dp);
        mDelegateListRecView.addItemDecoration(new DelegateItemSpaceDecoration(space));
    }

    @Override
    public void setAutoCompleteTextView(final List<DelegateModel> delegateBeans) {

        final List<String> delegateNames=new ArrayList<>();
        for(DelegateModel delegateBean: delegateBeans){
            delegateNames.add(delegateBean.getDelegateName());
        }
        ArrayAdapter<DelegateBean> arrayAdapter=new ArrayAdapter(getContext(),R.layout.item_delegate_search_list,delegateNames);
        mEdtSearchContent.setAdapter(arrayAdapter);
        mEdtSearchContent.setThreshold(1);
        mEdtSearchContent.setDropDownHeight(100);
        mEdtSearchContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
               position与indexOf(Object)是不同的
                Log.e("position", " "+position,null );
                Log.e("index of position", " "+delegateNames.indexOf(parent.getItemAtPosition(position)),null );
*/
               showDelegateDetail(delegateBeans.get(delegateNames.indexOf(parent.getItemAtPosition(position))));
            }
        });
     mEdtSearchContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
         @Override
         public void onFocusChange(View v, boolean hasFocus) {
             if (hasFocus) {
                 mEdtSearchContent.showDropDown();
             }
             else{
                 mEdtSearchContent.dismissDropDown();
             }
         }
     });

    }

    public void setSearchButton(){
        mBntSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.searchByName((mEdtSearchContent.getText()).toString());
            }
        });

    }


    @Override
    public void showDelegateList(List<DelegateModel> delegateBean) {
        DelegateListAdapter mDelegateListAdapter;
        mDelegateListAdapter=new DelegateListAdapter(getContext(),delegateBean);
        mDelegateListRecView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mDelegateListRecView.setAdapter(mDelegateListAdapter);
        mDelegateListAdapter.setDelegateBeanItemClickListener(this);

        mDelegateListRecView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoDelegateList() {
        mDelegateListRecView.setVisibility(View.INVISIBLE);

    }


    @Override
    public void showDelegateDetail(DelegateModel delegateBean) {
        DelegateDetailActivity.showDelegateDetailActivity(getActivity(),delegateBean ,"人员");

        }


    @Override
    public void showNoDelegateDetail() {
        Toast hintToast=Toast.makeText(getContext(),"不存在该人员",Toast.LENGTH_SHORT);
        hintToast.show();
    }


    private static int mLastClickedRoleItemPositon;
    @Override
    public void onRoleItemClickListener(int currPositon) {
        mPresenter.fetchDelegateList(currPositon);
        setRoleItemBackgroundColor(mLastClickedRoleItemPositon,currPositon);
    }


    @Override
    public void onDelegateNameItemClickListener(int position) {
        mPresenter.showDelegateDetail(position);
    }

    public void setRoleItemBackgroundColor( int lastClickedRoleItemPositon,int currPosition){

        //mRoleListRecView.getChildAt多的时候会返回空指针，因为已经被Recycle了

        mRoleListRecView.getChildAt(lastClickedRoleItemPositon).findViewById(R.id.id_item_role_layout).setBackgroundColor(getResources().getColor(R.color.color_tab_normal));
        ((TextView) mRoleListRecView.getChildAt(lastClickedRoleItemPositon).findViewById(R.id.id_item_role_name)).setTextColor(getResources().getColor(R.color.color_black));


        mRoleListRecView.getChildAt(currPosition).findViewById(R.id.id_item_role_layout).setBackgroundColor(getResources().getColor(R.color.color_tab_selected));
        ((TextView) mRoleListRecView.getChildAt(currPosition).findViewById(R.id.id_item_role_name)).setTextColor(getResources().getColor(R.color.color_white));

        mLastClickedRoleItemPositon = currPosition;
    }

    /**
     * 从主场景界面(MeetingFragment)中点击其他参会人员按钮后，最终会到达该方法。
     * 上一级是在{@link MainActivity#showDelegate(Boolean)}中调用。
     * @param showOtherDelegate
     */
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void setTabToOtherDelegate(Boolean showOtherDelegate){
        mPresenter.fetchDelegateList(Constant.OTHER_DELEGATE);
        setRoleItemBackgroundColor(mLastClickedRoleItemPositon,Constant.OTHER_DELEGATE);
    }
}
