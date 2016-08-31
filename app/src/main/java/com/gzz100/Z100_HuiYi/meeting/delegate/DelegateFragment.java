package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzz100.Z100_HuiYi.R;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class DelegateFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_delegate, null);
//        Log.e("DelegateFragment -->","onCreateView");
        return view;
    }
    @Override
    public void onDestroyView() {
        Log.e("DelegateFragment -->","onDestroyView");
        super.onDestroyView();
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
    }

    @Override
    public void onPause() {
//        Log.e("DelegateFragment -->","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
//        Log.e("DelegateFragment -->","onStop");
        super.onStop();
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

}
