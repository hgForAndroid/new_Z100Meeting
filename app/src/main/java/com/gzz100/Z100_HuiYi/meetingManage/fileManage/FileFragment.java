package com.gzz100.Z100_HuiYi.meetingManage.fileManage;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.adapter.AgendaListAdapter;
import com.gzz100.Z100_HuiYi.adapter.FileListAdapter;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.FileBean;
import com.gzz100.Z100_HuiYi.meetingManage.fileManage.fileDetailManage.FileDetailActivity;
import com.gzz100.Z100_HuiYi.meetingManage.ICommunicate;

import java.util.List;

import butterknife.OnClick;

/**
 * 文件详情
 *
 * @author XieQXiong
 *         create at 2016/8/23 17:01
 */

public class FileFragment extends Fragment implements FileContract.View, OnAgendaItemClickListener, OnFileItemClickListener {
    //    @BindView(R.id.id_edt_fgm_file)
    EditText mEdtSearchContent;
    //    @BindView(R.id.id_btn_fgm_file)
    Button mBtnSearch;
    //    @BindView(R.id.id_rev_fgm_tab)
    RecyclerView mAgendaListRecView;
    //    @BindView(R.id.id_rev_fgm_file_list)
    RecyclerView mFileListRecView;
    private FileContract.Presenter mPresenter;

    private List<Agenda> mAgendas;
    private AgendaListAdapter mAgendaAdapter;
    private FileListAdapter mFileListAdapter;

    private List<FileBean> mFileBeen;
    private int mAgendasSum;
    private int mAgendaIndex;
    private int mFileIndex;
    private String mAgendaTime;

    private ICommunicate mMainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (ICommunicate) context;

    }

    public static FileFragment newInstance() {
        return new FileFragment();
    }

    @Override
    public void setPresenter(FileContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        Log.e("FileFragment -->", "onResume");
        mPresenter.start();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
//        Log.e("FileFragment -->","onCreateView");
        mEdtSearchContent = (EditText) view.findViewById(R.id.id_edt_fgm_file);
        mBtnSearch = (Button) view.findViewById(R.id.id_btn_fgm_file);
        mAgendaListRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_tab);
        mFileListRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_file_list);

//        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mUnbinder.unbind();
        mPresenter.setFirstLoad(true);
    }

    @OnClick(R.id.id_btn_fgm_file)
    void onClick() {
        mPresenter.searchFileOrName(mEdtSearchContent.getText().toString().trim());
    }

    @Override
    public void showAgendaList(List<Agenda> agendas) {
        mAgendas = agendas;
        mAgendaAdapter = new AgendaListAdapter(getContext(), agendas);
        //横向展示
        mAgendaListRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAgendaListRecView.setAdapter(mAgendaAdapter);
        mAgendaAdapter.setOnItemClickListener(this);

    }

    @Override
    public void showFilesList(List<FileBean> fileBeen) {
//        mFileBeen = fileBeen;
        mFileListAdapter = new FileListAdapter(getContext(), fileBeen);
        //纵向展示
        mFileListRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mFileListRecView.setAdapter(mFileListAdapter);
        mFileListAdapter.setOnItemClickListener(this);

    }

    @Override
    public void showSearchResult(List<FileBean> fileBeen) {

    }

    @Override
    public void showFileDetail() {
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.showFileDetailActivity(getActivity(), mAgendasSum, mAgendaIndex,
                mFileBeen, mFileIndex, mAgendaTime, currentTitle);

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

    @Override
    public void setAgendasSum(int size) {
        mAgendasSum = size;
    }

    @Override
    public void setAgendaIndex(int index) {
        mAgendaIndex = index;
    }

    @Override
    public void setFileList(List<FileBean> files) {
        mFileBeen = files;
    }

    @Override
    public void setFileIndex(int fileIndex) {
        mFileIndex = fileIndex;

    }

    @Override
    public void setAgendaTime(String time) {
        mAgendaTime = time;
    }

    @Override
    public void onAgendaItemClick(View v, int position) {
        mPresenter.setAgendaTime(mAgendas.get(position).getAgendaTime());
        mPresenter.fetchFileList(true, position + 1);
        int childCount = mAgendaListRecView.getChildCount();
        setBackgroundColor(childCount, position);

    }

    private void setBackgroundColor(int childCount, int position) {
        for (int i = 0; i < childCount; i++) {
            ((LinearLayout) mAgendaListRecView.getChildAt(i)
                    .findViewById(R.id.id_item_agenda_layout)).setBackgroundColor(
                    getResources().getColor(R.color.color_tab_normal));
            ((TextView) mAgendaListRecView.getChildAt(i).findViewById(R.id.id_item_agenda_index)).
                    setTextColor(getResources().getColor(R.color.color_black));
            ((TextView) mAgendaListRecView.getChildAt(i).findViewById(R.id.id_item_agenda_title)).
                    setTextColor(getResources().getColor(R.color.color_black));

        }
        ((LinearLayout) mAgendaListRecView.getChildAt(position)
                .findViewById(R.id.id_item_agenda_layout)).setBackgroundColor(
                getResources().getColor(R.color.color_tab_selected));
        ((TextView) mAgendaListRecView.getChildAt(position).findViewById(R.id.id_item_agenda_index)).
                setTextColor(getResources().getColor(R.color.color_white));
        ((TextView) mAgendaListRecView.getChildAt(position).findViewById(R.id.id_item_agenda_title)).
                setTextColor(getResources().getColor(R.color.color_white));

    }


    @Override
    public void onFileItemClick(int position) {
        mPresenter.showFileDetail(position);
    }
}
