package com.gzz100.Z100_HuiYi.meeting.file;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;
import com.gzz100.Z100_HuiYi.multicast.MulticastBean;
import com.gzz100.Z100_HuiYi.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.OnClick;

/**
 * 文件详情
 *
 * @author XieQXiong
 *         create at 2016/8/23 17:01
 */

public class FileFragment extends Fragment implements FileContract.View, OnAgendaTabClickListener,
        OnFileItemClickListener, OnSearchItemClickListener, View.OnClickListener {
    //    @BindView(R.id.id_edt_fgm_file)
    private EditText mEdtSearchContent;
    //    @BindView(R.id.id_btn_fgm_file)
    private Button mBtnSearch;
    //    @BindView(R.id.id_rev_fgm_tab)
    private RecyclerView mAgendaListRecView;
    //    @BindView(R.id.id_rev_fgm_file_list)
    private RecyclerView mFileListRecView;
    private RecyclerView mSearchResultRecView;
    private FileContract.Presenter mPresenter;

    private List<Agenda> mAgendas;
    private AgendaListTabAdapter mAgendaAdapter;
    private FileListAdapter mFileListAdapter;
    private SearchResultAdapter mSearchResultAdapter;

    private List<Document> mDocumentBeen;
    private List<Document> mResultDocumentBeen;
    private int mAgendasSum;
    private int mAgendaIndex;
    private int mFileIndex;
    private String mAgendaTime;

    private ICommunicate mMainActivity;
    //正常显示结果的布局
    private RelativeLayout mRlNormal;
    //搜索后的布局
    private LinearLayout mLlSearchResult;
    //搜索后的结果的当前议程序号
    private Integer mSearchAgendaIndex;
    //搜索后的文件序号
    private int mSearchFileIndex1;

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
        super.onResume();
        if (Constant.DEBUG)
            Log.e("FileFragment -->", "onResume");
        mPresenter.start();
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
        mSearchResultRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_file_search_result);

        mRlNormal = (RelativeLayout) view.findViewById(R.id.id_rl_fgm_file_normal);
        mLlSearchResult = (LinearLayout) view.findViewById(R.id.id_ll_fgm_file_search_result);

        mBtnSearch.setOnClickListener(this);

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
        mAgendaAdapter = new AgendaListTabAdapter(getContext(), agendas);
        //横向展示
        mAgendaListRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAgendaListRecView.setAdapter(mAgendaAdapter);
        mAgendaAdapter.setOnItemClickListener(this);
        mAgendaAdapter.setSelectedItem(0);
        mAgendaAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFilesList(List<Document> documentBeen) {
        mDocumentBeen = documentBeen;
        mFileListAdapter = new FileListAdapter(getContext(), documentBeen);
        //纵向展示
        mFileListRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mFileListRecView.setAdapter(mFileListAdapter);
        mFileListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void showSearchResult(List<Document> documentBeen) {
        mResultDocumentBeen = documentBeen;
        mSearchResultAdapter = new SearchResultAdapter(getActivity(),documentBeen);

        mSearchResultRecView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mSearchResultRecView.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnSearchItemClickListener(this);
    }

    @Override
    public void showFileDetail() {
        //通知主界面移除控制View，否则进入FileDetailActivity后无法添加控制View,导致报错
        mMainActivity.removeControllerView();
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.start(getActivity(),mAgendaIndex,mFileIndex,currentTitle,false,false);
    }

    @Override
    public void showSearchFileDetail() {
        String currentTitle = mMainActivity.getCurrentTitle();
//        FileDetailActivity.showFileDetailActivity(getActivity(), mAgendasSum, mSearchAgendaIndex,
//                mResultDocumentBeen, mSearchFileIndex1, mAgendas.get(mSearchAgendaIndex).getAgendaDuration(), currentTitle);
        FileDetailActivity.start(getActivity(),mSearchAgendaIndex,mSearchFileIndex1,currentTitle,false,false);
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
    public void setFileList(List<Document> documents) {
        mDocumentBeen = documents;
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
        mPresenter.setAgendaTime(mAgendas.get(position).getAgendaDuration());
        mPresenter.fetchFileList(true, position + 1);
        mAgendaAdapter.setSelectedItem(position);
        mAgendaAdapter.notifyDataSetChanged();
    }
    @Override
    public void onFileItemClick(int position) {
        mPresenter.showFileDetail(position);
    }
    @Override
    public void onSearchClick(int position) {
        if (mResultDocumentBeen != null && mResultDocumentBeen.size() > 0){
            mSearchFileIndex1 = Integer.valueOf(mResultDocumentBeen.get(position).getDocumentIndex());
            mSearchAgendaIndex = Integer.valueOf(mResultDocumentBeen.get(position).getDocumentAgendaIndex());
            mPresenter.showSearchFileDetail(mSearchFileIndex1,mSearchAgendaIndex);
        }
    }
    //搜索
    @Override
    public void onClick(View v) {
        String content = mEdtSearchContent.getText().toString();
        if (TextUtils.isEmpty(content))
            Toast.makeText(getActivity(), "请输入搜索关键字", Toast.LENGTH_SHORT).show();
    }
}
