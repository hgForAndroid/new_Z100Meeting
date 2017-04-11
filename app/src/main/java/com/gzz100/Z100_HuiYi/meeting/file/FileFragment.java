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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件详情
 *
 * @author XieQXiong
 *         create at 2016/8/23 17:01
 */

public class FileFragment extends Fragment implements FileContract.View, OnAgendaTabClickListener,
        OnFileItemClickListener, OnSearchItemClickListener, View.OnClickListener {
    private AutoCompleteTextView mEdtSearchContent;
    private Button mBtnSearch;
    private Button mBtnSearchClear;
    private RecyclerView mAgendaListRecView;
    private RecyclerView mFileListRecView;
    private RecyclerView mSearchResultRecView;
    private FileContract.Presenter mPresenter;
    private LinearLayout mFileAttrListView;

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
    private LinearLayout mLlSearchBar;
    //搜索后的结果的当前议程序号
    private int mSearchAgendaIndex;
    //搜索后的文件序号
    private int mSearchFileIndex1;
    //文件界面的根布局
    private LinearLayout mFileRootLayout;

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
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        mEdtSearchContent = (AutoCompleteTextView) view.findViewById(R.id.id_edt_fgm_file);
        mBtnSearch = (Button) view.findViewById(R.id.id_btn_fgm_file);
        mBtnSearchClear = (Button) view.findViewById(R.id.id_btn_fgm_file_clear);
        mAgendaListRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_tab);
        mFileListRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_file_list);
        mSearchResultRecView = (RecyclerView) view.findViewById(R.id.id_rev_fgm_file_search_result);
        mFileAttrListView = (LinearLayout) view.findViewById(R.id.id_rl_fgm_file);

        mRlNormal = (RelativeLayout) view.findViewById(R.id.id_rl_fgm_file_normal);
        mLlSearchResult = (LinearLayout) view.findViewById(R.id.id_ll_fgm_file_search_result);
        mLlSearchBar = (LinearLayout) view.findViewById(R.id.id_fragment_file_search_bar);

        mFileRootLayout = (LinearLayout) view.findViewById(R.id.id_file_fragment_root_layout);

        mBtnSearch.setOnClickListener(this);
        mBtnSearchClear.setOnClickListener(this);
        mFileRootLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.setFirstLoad(true);
    }

    @OnClick({R.id.id_btn_fgm_file, R.id.id_btn_fgm_file_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_fgm_file:
                String content = mEdtSearchContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
//                    Toast.makeText(getActivity(), R.string.string_input_search_key_word, Toast.LENGTH_SHORT).show();
                    mAgendaListRecView.setVisibility(View.VISIBLE);
                    mFileListRecView.setVisibility(View.VISIBLE);
                    mFileAttrListView.setVisibility(View.VISIBLE);
                    mRlNormal.setVisibility(View.VISIBLE);

                    mLlSearchResult.setVisibility(View.GONE);
                } else {
                    mPresenter.searchFileOrName(content);
                }
                break;
            case R.id.id_btn_fgm_file_clear:
                mEdtSearchContent.setText(null);
                mEdtSearchContent.clearFocus();
                break;
            case R.id.id_file_fragment_root_layout:
                hideSoftInput(view);
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏软件盘
     *
     * @param view
     */
    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 当前会议有议程时，调用该方法显示议程列表。
     *
     * @param agendas 议程列表
     */
    @Override
    public void showAgendaList(List<AgendaModel> agendas) {
        mAgendaListRecView.setVisibility(View.VISIBLE);
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

    /**
     * 切换议程，议程存在文件，调用该方法。
     *
     * @param documentBeen
     */
    @Override
    public void showFilesList(List<DocumentModel> documentBeen) {
        mFileListRecView.setVisibility(View.VISIBLE);
        mRlNormal.setVisibility(View.VISIBLE);
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
        //文件名、大小 ，主讲人那一条布局
        mFileAttrListView.setVisibility(View.GONE);

        mRlNormal.setVisibility(View.GONE);

        mLlSearchResult.setVisibility(View.VISIBLE);
        mLlSearchBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFileDetail() {
        if (MyAPP.getInstance().isVoting()) {
            ToastUtil.showMessage("投票还未结束");
            return;
        }
        if (MyAPP.getInstance().getUserRole() == 1) {
            //通知主界面移除控制View，否则进入FileDetailActivity后无法添加控制View,导致报错
            mMainActivity.removeControllerView();
        }
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.start(getActivity(), mAgendaIndex, mFileIndex, currentTitle, false, false, false, "", "");
    }

    @Override
    public void showSearchFileDetail() {
        if (MyAPP.getInstance().isVoting()) {
            ToastUtil.showMessage("投票还未结束");
            return;
        }
        if (MyAPP.getInstance().getUserRole() == 1) {
            //通知主界面移除控制View，否则进入FileDetailActivity后无法添加控制View,导致报错
            mMainActivity.removeControllerView();
        }

        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.start(getActivity(), mSearchAgendaIndex, mSearchFileIndex1 - 1, currentTitle, false, false, false, "", "");
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * 切换议程时，如果该议程没有对应的议程文件，则调用该方法。
     */
    @Override
    public void showNoFileList() {
        mFileListRecView.setVisibility(View.GONE);
        ToastUtil.showMessage(R.string.string_no_file);
    }

    /**
     * 当前会议没有议程时，调用该方法。
     */
    @Override
    public void showNoAgendaList() {
        mAgendaListRecView.setVisibility(View.GONE);
        ToastUtil.showMessage(R.string.string_no_agendas);
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
        mRlNormal.setVisibility(View.VISIBLE);
        Toast hintToast = Toast.makeText(getContext(), R.string.string_file_or_speaker_not_find, Toast.LENGTH_SHORT);
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
        mPresenter.setAgendaTime(mAgendas.get(position).getAgendaDuration() + "");
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
