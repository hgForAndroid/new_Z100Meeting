package com.gzz100.Z100_HuiYi.meetingManage.fileManage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.FileBean;

import java.util.List;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class FileFragment extends Fragment implements FileContract.View{
    private FileContract.Presenter mPresenter;

    public static FileFragment newInstance(){return new FileFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("FileFragment -->","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e("FileFragment -->","onStart");
        super.onStart();
    }

    @Override
    public void setPresenter(FileContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        Log.e("FileFragment -->","onResume");
        mPresenter.start();
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
    public void showAgendaList(List<Agenda> agendas) {

    }

    @Override
    public void showSearchResult(List<FileBean> fileBeen) {

    }

    @Override
    public void showFilesResult(List<FileBean> fileBeen) {

    }

    @Override
    public void showFileDetail() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoFileList() {

    }

    @Override
    public void showNoAgendaList() {

    }

    @Override
    public void showNoSearchResult() {

    }

}
