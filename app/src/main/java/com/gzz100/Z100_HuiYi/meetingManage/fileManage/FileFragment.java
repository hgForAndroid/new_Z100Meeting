package com.gzz100.Z100_HuiYi.meetingManage.fileManage;

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

public class FileFragment extends Fragment {
    public static FileFragment newInstance(){return new FileFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("FileFragment -->","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("FileFragment -->","onResume");
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, null);
//        ButterKnife.bind(getActivity(),view);
        Log.e("FileFragment -->","onCreateView");
        return view;
    }
    @Override
    public void onDestroyView() {
        Log.e("FileFragment -->","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e("FileFragment -->","onDestroy");
        super.onDestroy();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("FileFragment -->","onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e("FileFragment -->","onStart");
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.e("FileFragment -->","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("FileFragment -->","onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.e("FileFragment -->","onDetach");
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        Log.e("FileFragment -->","onAttach");
        super.onAttach(context);
    }
}
