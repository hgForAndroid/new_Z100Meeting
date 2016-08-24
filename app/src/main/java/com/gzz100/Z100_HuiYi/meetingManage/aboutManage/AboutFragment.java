package com.gzz100.Z100_HuiYi.meetingManage.aboutManage;

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

public class AboutFragment extends Fragment {
    public static AboutFragment newInstance(){return new AboutFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);
//        ButterKnife.bind(getActivity(),view);
        return view;
    }
    @Override
    public void onDestroyView() {
        Log.e("AboutFragment -->","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e("AboutFragment -->","onDestroy");
        super.onDestroy();
    }
}
