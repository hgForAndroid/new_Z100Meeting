package com.gzz100.Z100_HuiYi.meeting.agenda;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class AgendaFragment extends Fragment implements AgendaContract.View, OnAgendaClickListener {
    RecyclerView mAgendaRecyclerView;
    TextView mAgendaTitleTextView;
    TextView mAgendaSpeakerTextView;
    TextView mAgendaTimeTextView;
    Button mAgendaShowFileButton;

    private AgendaContract.Presenter mPresenter;

    private List<Agenda> mAgendasList;
    private int currentAgendaPositon;
    private AgendaListAdapter mAgendaListAdapter;

    private List<Document> mDocumentsList;

    private ICommunicate mMainActivity;


    public static AgendaFragment newInstance(){ return new AgendaFragment(); }

    @Override
    public void setPresenter(AgendaContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("AgendaFragment -->","onResume");
        mPresenter.start();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, null);
        //ButterKnife.bind(this,view);
//        Log.e("AgendaFragment -->","onCreateView");
        mAgendaRecyclerView = (RecyclerView) view.findViewById(R.id.id_rev_agenda);
        mAgendaTitleTextView = (TextView) view.findViewById(R.id.id_text_view_agenda_title);
        mAgendaSpeakerTextView = (TextView) view.findViewById(R.id.id_text_view_agenda_speaker);
        mAgendaTimeTextView = (TextView) view.findViewById(R.id.id_text_view_agenda_time);
        mAgendaShowFileButton = (Button) view.findViewById(R.id.id_btn_show_agenda_file);
        mAgendaShowFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.fetchFileListAndShow(false, currentAgendaPositon+1);
            }
        });
        return view;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showAgendasList(List<Agenda> agendas) {
        mAgendasList = agendas;
        mAgendaListAdapter = new AgendaListAdapter(getContext(), mAgendasList);
        mAgendaRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAgendaRecyclerView.setAdapter(mAgendaListAdapter);
        mAgendaListAdapter.setOnItemClickListener(this);
        mPresenter.showAgendaDetail(agendas.get(0));
    }

    @Override
    public void showAgendaDetail(Agenda agenda) {
        mAgendaTitleTextView.setText(agenda.getAgendaName());
        mAgendaSpeakerTextView.setText("主讲人：" + agenda.getAgendaSpeaker());
        mAgendaTimeTextView.setText("议程时长：" + agenda.getAgendaTime());
    }

    @Override
    public void showFileDetail() {
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.showFileDetailActivity(getActivity(), mAgendasList.size(), currentAgendaPositon + 1,
                mDocumentsList, 0, mAgendasList.get(currentAgendaPositon).getAgendaTime(), currentTitle);
    }

    @Override
    public void setFileList(List<Document> documents) {
        mDocumentsList = documents;
    }

    private void setBackgroundColor(int childCount, int position) {
        for (int i = 0; i < childCount; i++) {
            ((LinearLayout) mAgendaRecyclerView.getChildAt(i)
                    .findViewById(R.id.id_item_agenda_name_layout)).setBackgroundColor(
                    getResources().getColor(R.color.color_tab_normal));
            ((TextView) mAgendaRecyclerView.getChildAt(i).findViewById(R.id.id_item_agenda_name_title)).
                    setTextColor(getResources().getColor(R.color.color_black));

        }
        ((LinearLayout) mAgendaRecyclerView.getChildAt(position)
                .findViewById(R.id.id_item_agenda_name_layout)).setBackgroundColor(
                getResources().getColor(R.color.color_tab_selected));
        ((TextView) mAgendaRecyclerView.getChildAt(position).findViewById(R.id.id_item_agenda_name_title)).
                setTextColor(getResources().getColor(R.color.color_white));

    }

    @Override
    public void showNoAgendaList() {

    }

    @Override
    public void showNoFileList() {

    }

    @Override
    public void onDestroyView() {
//        Log.e("AgendaFragment -->","onDestroyView");
        super.onDestroyView();
        mPresenter.setFirstLoad(true);
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
        mMainActivity = (ICommunicate) context;
    }

    @Override
    public void onAgendaItemClick(View view, int position) {
        int childCount = mAgendaRecyclerView.getChildCount();
        setBackgroundColor(childCount, position);
        currentAgendaPositon = position;
        mPresenter.showAgendaDetail(mAgendasList.get(position));
    }
}
