package com.gzz100.Z100_HuiYi.meetingManage.agendaManage;

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

public class AgendaFragment extends Fragment {
    public static AgendaFragment newInstance(){return new AgendaFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("AgendaFragment -->","onResume");
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, null);
//        ButterKnife.bind(getActivity(),view);
//        Log.e("AgendaFragment -->","onCreateView");
        return view;
    }

    @Override
    public void onDestroyView() {
//        Log.e("AgendaFragment -->","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
//        Log.e("AgendaFragment -->","onDestroy");
        super.onDestroy();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Log.e("AgendaFragment -->","onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
//        Log.e("AgendaFragment -->","onStart");
        super.onStart();
    }

    @Override
    public void onPause() {
//        Log.e("AgendaFragment -->","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
//        Log.e("AgendaFragment -->","onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
//        Log.e("AgendaFragment -->","onDetach");
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
//        Log.e("AgendaFragment -->","onAttach");
        super.onAttach(context);
    }

}
