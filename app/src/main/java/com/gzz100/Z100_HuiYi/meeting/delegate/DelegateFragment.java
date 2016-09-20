package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail.DelegateDetailActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 参会代表
 * @author DELL
 * create at 2016/9/2
 */
public class DelegateFragment extends Fragment implements DelegateContract.View,OnRoleItemClickListener,OnDelegateNameItemClickListener {
    EditText mEdtSearchContent;
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
//        Log.e("DelegateFragment -->","onCreateView");
        mEdtSearchContent=(EditText) view.findViewById(R.id.id_edt_fgm_delegate);
        mBntSearch=(Button)view.findViewById(R.id.id_btn_fgm_delegate);
        mRoleListRecView=(RecyclerView)view.findViewById(R.id.id_rev_fgm_tab);
        mDelegateListRecView=(RecyclerView)view.findViewById(R.id.id_rev_fgm_delegate_list);
        mPresenter.start();
        mPresenter.setFirstLoad(false);
        return view;
    }
    @Override
    public void onDestroyView() {
        Log.e("DelegateFragment -->","onDestroyView");
        super.onDestroyView();
        mPresenter.setFirstLoad(true);
        mLastClickedRoleItemPositon=Constant.DEFAULT_SPEAKER;
    }

    @Override
    public void onDestroy() {
//        Log.e("DelegateFragment -->","onDestroy");
        super.onDestroy();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Log.e("DelegateFragment -->","onActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
//        Log.e("DelegateFragment -->","onStart");
        super.onStart();
        EventBus.getDefault().register(this);


    }

    @Override
    public void onPause() {
     Log.e("DelegateFragment -->","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("DelegateFragment -->","onStop");
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
//        Log.e("DelegateFragment -->","onDetach");
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
//        Log.e("DelegateFragment -->","onAttach");
        super.onAttach(context);
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
    public void showDelegateList(List<DelegateBean> delegateBean) {
        DelegateListAdapter mDelegateListAdapter;
        mDelegateListAdapter=new DelegateListAdapter(getContext(),delegateBean);
        mDelegateListRecView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mDelegateListRecView.setAdapter(mDelegateListAdapter);
        mDelegateListAdapter.setDelegateBeanItemClickListener(this);
    }

    @Override
    public void showNoDelegateList() {

    }


    @Override
    public void showDelegateDetail(DelegateBean delegateBean,int delegateDetailPos) {
        DelegateDetailActivity.showDelegateDetailActivity(getActivity(),delegateBean ,"人员");

        }


    @Override
    public void showNoDelegateDetail() {

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

        mRoleListRecView.getChildAt(lastClickedRoleItemPositon).findViewById(R.id.id_item_role_layout).setBackgroundColor(getResources().getColor(R.color.color_tab_normal));
        ( (TextView)mRoleListRecView.getChildAt(lastClickedRoleItemPositon).findViewById(R.id.id_item_role_name)).setTextColor(getResources().getColor(R.color.color_black));

        mRoleListRecView.getChildAt(currPosition).findViewById(R.id.id_item_role_layout).setBackgroundColor(getResources().getColor(R.color.color_tab_selected));
        ( (TextView)mRoleListRecView.getChildAt(currPosition).findViewById(R.id.id_item_role_name)).setTextColor(getResources().getColor(R.color.color_white));

        mLastClickedRoleItemPositon=currPosition;
    }
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void setTabToOtherDelegate(Boolean showOtherDelegate){
        mPresenter.fetchDelegateList(Constant.OTHER_DELEGATE);
        setRoleItemBackgroundColor(mLastClickedRoleItemPositon,Constant.OTHER_DELEGATE);
    }
}
