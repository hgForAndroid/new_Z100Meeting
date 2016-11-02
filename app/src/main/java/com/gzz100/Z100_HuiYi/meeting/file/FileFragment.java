package com.gzz100.Z100_HuiYi.meeting.file;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;

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
    private AutoCompleteTextView mEdtSearchContent;
    //    @BindView(R.id.id_btn_fgm_file)
    private Button mBtnSearch;
    private Button mBtnSearchClear;
    //    @BindView(R.id.id_rev_fgm_tab)
    private RecyclerView mAgendaListRecView;
    //    @BindView(R.id.id_rev_fgm_file_list)
    private RecyclerView mFileListRecView;
    private RecyclerView mSearchResultRecView;
    private FileContract.Presenter mPresenter;
    private RelativeLayout mFileAttrListView;

    private List<AgendaModel> mAgendas;
    private AgendaListTabAdapter mAgendaAdapter;
    private FileListAdapter mFileListAdapter;
    private SearchResultAdapter mSearchResultAdapter;

    private List<DocumentModel> mDocumentBeen;
    private List<DocumentModel> mResultDocumentBeen;
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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
//        Log.e("FileFragment -->","onCreateView");
        mEdtSearchContent = (AutoCompleteTextView) view.findViewById(R.id.id_edt_fgm_file);
        mBtnSearch = (Button) view.findViewById(R.id.id_btn_fgm_file);
        mBtnSearchClear = (Button) view.findViewById(R.id.id_btn_fgm_file_clear);
        mAgendaListRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_tab);
        mFileListRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_file_list);
        mSearchResultRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_file_search_result);
        mFileAttrListView = (RelativeLayout) view.findViewById(R.id.id_rl_fgm_file);

        mRlNormal = (RelativeLayout) view.findViewById(R.id.id_rl_fgm_file_normal);
        mLlSearchResult = (LinearLayout) view.findViewById(R.id.id_ll_fgm_file_search_result);

        mBtnSearch.setOnClickListener(this);
        mBtnSearchClear.setOnClickListener(this);

        mPresenter.start();
//        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mUnbinder.unbind();
        mPresenter.setFirstLoad(true);
    }

    @OnClick({R.id.id_btn_fgm_file, R.id.id_btn_fgm_file_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_fgm_file:
                String content = mEdtSearchContent.getText().toString().trim();
                if (TextUtils.isEmpty(content))
                    Toast.makeText(getActivity(), "请输入搜索关键字", Toast.LENGTH_SHORT).show();

                else
                    mPresenter.searchFileOrName(content);
                break;
            case R.id.id_btn_fgm_file_clear:
                mEdtSearchContent.setText(null);
                mEdtSearchContent.clearFocus();
                break;
        }
    }

    @Override
    public void showAgendaList(List<AgendaModel> agendas) {
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
    public void showFilesList(List<DocumentModel> documentBeen) {
        mDocumentBeen = documentBeen;
        mFileListAdapter = new FileListAdapter(getContext(), documentBeen);
        //纵向展示
        mFileListRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mFileListRecView.setAdapter(mFileListAdapter);
        mFileListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void showSearchResult(List<DocumentModel> documentBeen) {
        mResultDocumentBeen = documentBeen;
        mSearchResultAdapter = new SearchResultAdapter(getActivity(), documentBeen);
        mSearchResultRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSearchResultRecView.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnSearchItemClickListener(this);


        mAgendaListRecView.setVisibility(View.GONE);
        mFileListRecView.setVisibility(View.GONE);
        mFileAttrListView.setVisibility(View.GONE);

        mLlSearchResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFileDetail() {
        //通知主界面移除控制View，否则进入FileDetailActivity后无法添加控制View,导致报错
        mMainActivity.removeControllerView();
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.start(getActivity(), mAgendaIndex, mFileIndex, currentTitle, false, false, false, "", "");
    }

    @Override
    public void showSearchFileDetail() {
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.start(getActivity(), mSearchAgendaIndex, mSearchFileIndex1, currentTitle, false, false, false, "", "");
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
    public void setAutoCompleteTextView(List<String> fileSearchNameHintList) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.item_delegate_search_list, fileSearchNameHintList);
        mEdtSearchContent.setAdapter(arrayAdapter);
        mEdtSearchContent.setDropDownHeight(100);
        mEdtSearchContent.setThreshold(1);
        mEdtSearchContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.showAutoCompleteTVSelectionFileDetail(parent.getItemAtPosition(position).toString());
            }
        });
        mEdtSearchContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEdtSearchContent.showDropDown();
                }
            }
        });
        mEdtSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!(mEdtSearchContent.getText().toString().isEmpty())) {
                    mBtnSearchClear.setVisibility(View.VISIBLE);
                } else {
                   fileListNSearchFileListSwitch();
                    mBtnSearchClear.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void fileListNSearchFileListSwitch() {
        mAgendaListRecView.setVisibility(View.VISIBLE);
        mFileListRecView.setVisibility(View.VISIBLE);
        mFileAttrListView.setVisibility(View.VISIBLE);
        mLlSearchResult.setVisibility(View.GONE);

    }


    @Override
    public void showNoSearchResult() {

        Toast hintToast = Toast.makeText(getContext(), "不存在该文件或该主讲人", Toast.LENGTH_SHORT);
        hintToast.show();

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
    public void setFileList(List<DocumentModel> documents) {
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
        mPresenter.setAgendaTime(mAgendas.get(position).getAgendaDuration()+"");
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
        if (mResultDocumentBeen != null && mResultDocumentBeen.size() > 0) {
            mSearchFileIndex1 = Integer.valueOf(mResultDocumentBeen.get(position).getDocumentIndex());
            mSearchAgendaIndex = Integer.valueOf(mResultDocumentBeen.get(position).getDocumentAgendaIndex());
            mPresenter.showSearchFileDetail(mSearchFileIndex1, mSearchAgendaIndex);
        }
    }

}
