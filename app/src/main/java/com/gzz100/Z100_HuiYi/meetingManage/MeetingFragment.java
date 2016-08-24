package com.gzz100.Z100_HuiYi.meetingManage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzz100.Z100_HuiYi.R;

import butterknife.ButterKnife;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class MeetingFragment extends Fragment {

    public static MeetingFragment newInstance(){return new MeetingFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("MeetingFragment -->","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("MeetingFragment -->","onActivityCreated");
        if (savedInstanceState != null){
            savedInstanceState.getString("key");
            Log.e("onActivityCreated","savedInstanceState.getString(key)"+savedInstanceState.getString("key"));
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e("MeetingFragment -->","onStart");



        super.onStart();
    }

    @Override
    public void onPause() {
        Log.e("MeetingFragment -->","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("MeetingFragment -->","onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.e("MeetingFragment -->","onDetach");
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        Log.e("MeetingFragment -->","onAttach");
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        Log.e("MeetingFragment -->","onResume");
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting, null);
//        ButterKnife.bind(getActivity(),view);
        Log.e("MeetingFragment -->","onCreateView");

        if (savedInstanceState != null){
            savedInstanceState.getString("key");
            Log.e("onCreateView","savedInstanceState.getString(key)"+savedInstanceState.getString("key"));
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        Log.e("MeetingFragment -->","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e("MeetingFragment -->","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("MeetingFragment -->","onSaveInstanceState");
        outState.putString("key","saveBundle");

    }


}
