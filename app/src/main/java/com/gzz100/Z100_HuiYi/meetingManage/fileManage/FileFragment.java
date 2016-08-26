package com.gzz100.Z100_HuiYi.meetingManage.fileManage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.adapter.AgendaListAdapter;
import com.gzz100.Z100_HuiYi.adapter.FileListAdapter;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.FileBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class FileFragment extends Fragment implements FileContract.View{
    @BindView(R.id.id_edt_fgm_file) EditText mEdtSearchContent;
    @BindView(R.id.id_btn_fgm_file) Button mBtnSearch;
    @BindView(R.id.id_rev_fgm_tab) RecyclerView mAgendaListRecView;
    @BindView(R.id.id_rev_fgm_file_list) RecyclerView mFileListRecView;
    private FileContract.Presenter mPresenter;
    public static FileFragment newInstance(){return new FileFragment();}


    private List<Agenda> mAgendas ;
    private List<FileBean> mFileBeen;
    private AgendaListAdapter mAgendaAdapter;
    private FileListAdapter mFileListAdapter;

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
        View view = inflater.inflate(R.layout.fragment_file, container,false);
        ButterKnife.bind(this,view);
        Log.e("FileFragment -->","onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        Log.e("FileFragment -->","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void showAgendaList(List<Agenda> agendas) {
        mAgendas = agendas;
        mAgendaAdapter = new AgendaListAdapter(getContext(),agendas);
        //横向展示
        mAgendaListRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mAgendaListRecView.setAdapter(mAgendaAdapter);


    }

    @Override
    public void showFilesList(List<FileBean> fileBeen) {
        mFileBeen = fileBeen;
        mFileListAdapter = new FileListAdapter(getContext(),fileBeen);
        mFileListRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mFileListRecView.setAdapter(mFileListAdapter);

    }

    @Override
    public void showSearchResult(List<FileBean> fileBeen) {

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
