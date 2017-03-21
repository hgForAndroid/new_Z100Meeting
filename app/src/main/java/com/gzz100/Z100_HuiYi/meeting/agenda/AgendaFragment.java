package com.gzz100.Z100_HuiYi.meeting.agenda;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.MyAPP;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import java.util.List;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class AgendaFragment extends Fragment implements AgendaContract.View, OnAgendaClickListener, OnFileDetailClickListener {
    RecyclerView mAgendaRecyclerView;
    RecyclerView mAgendaDetailRecyclerView;
    TextView mAgendaTitleTextView;
    TextView mAgendaSpeakerTextView;
    TextView mAgendaTimeTextView;
    Button mAgendaShowFileButton;
    LinearLayout mAgendaDetailLayout;

    private float mX;
    private float mY;

    private AgendaContract.Presenter mPresenter;

    private List<AgendaModel> mAgendasList;
    private int currentAgendaPositon;
    private AgendaListAdapter mAgendaListAdapter;
    private AgendaDetailListAdapter mAgendaDetailAdapter;

    private List<DocumentModel> mDocumentsList;

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
        mPresenter.start();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, null);
        mAgendaRecyclerView = (RecyclerView) view.findViewById(R.id.id_rev_agenda);
        mAgendaDetailRecyclerView = (RecyclerView) view.findViewById(R.id.id_rev_agenda_detail);
        mAgendaTitleTextView = (TextView) view.findViewById(R.id.id_text_view_agenda_title);
        mAgendaSpeakerTextView = (TextView) view.findViewById(R.id.id_text_view_agenda_speaker);
        mAgendaTimeTextView = (TextView) view.findViewById(R.id.id_text_view_agenda_time);
        mAgendaShowFileButton = (Button) view.findViewById(R.id.id_btn_show_agenda_file);
        mAgendaShowFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.fetchFileListAndShow(true, currentAgendaPositon+1);
            }
        });
        mAgendaDetailLayout = (LinearLayout) view.findViewById(R.id.id_agenda_detail_layout);
        mAgendaDetailLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mX = event.getX();
                    mY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getY()-mY > 50){
                        Log.d("test", "DOWN!");
                        ObjectAnimator animator = ObjectAnimator
                                .ofFloat(v, "translationY", v.getTranslationY(), v.getTranslationY()+200f, v.getTranslationY())
                                .setDuration(150);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                onAgendaItemClick((currentAgendaPositon + 1) % mAgendasList.size());
                            }
                        });
                        animator.start();
                    } else if(mY - event.getY() > 50){
                        Log.d("test" ," UP!");
                        ObjectAnimator animator = ObjectAnimator
                                .ofFloat(v, "translationY", v.getTranslationY(), v.getTranslationY()-200f, v.getTranslationY())
                                .setDuration(150);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                onAgendaItemClick((currentAgendaPositon-1 >= 0 ? currentAgendaPositon-1 : mAgendasList.size()-1) % mAgendasList.size());
                            }
                        });
                        animator.start();
                    }
                }
                return true;
            }
        });
        return view;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showAgendasList(List<AgendaModel> agendas) {
        mAgendasList = agendas;
        mAgendaListAdapter = new AgendaListAdapter(getContext(), mAgendasList);
        mAgendaRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAgendaRecyclerView.setAdapter(mAgendaListAdapter);
        mAgendaListAdapter.setOnItemClickListener(this);
        mPresenter.showAgendaDetail(agendas.get(0));
        mAgendaDetailAdapter = new AgendaDetailListAdapter(getContext(), mAgendasList);
        mAgendaDetailAdapter.setOnFileDetailClickListener(this);
        mAgendaDetailRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAgendaDetailRecyclerView.setAdapter(mAgendaDetailAdapter);
        mAgendaDetailRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mX = event.getX();
                    mY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getY() - mY > 25) {
                        Log.d("test", "Scroll to" + currentAgendaPositon);
                        onAgendaItemClick(currentAgendaPositon - 1 >= 0 ? --currentAgendaPositon
                        : 0);
                    } else if (mY - event.getY() > 25) {
                        onAgendaItemClick(currentAgendaPositon + 1  < mAgendasList.size() ? ++currentAgendaPositon
                                : currentAgendaPositon);
                        Log.d("test", "Scroll to" + currentAgendaPositon);
                    }
                }

                return false;
            }
        });
    }

    @Override
    public void showAgendaDetail(AgendaModel agenda) {
        mAgendaDetailRecyclerView.smoothScrollToPosition(currentAgendaPositon);
        mAgendaTitleTextView.setText(agenda.getAgendaName());
        mAgendaSpeakerTextView.setText("主讲人：" + agenda.getAgendaSpeaker());
        mAgendaTimeTextView.setText("议程时长：" + agenda.getAgendaDuration());
    }

    @Override
    public void showFileDetail() {
        if (MyAPP.getInstance().isVoting()){
            ToastUtil.showMessage("投票还未结束");
            return;
        }
        if (MyAPP.getInstance().getUserRole() == 1){
            org.greenrobot.eventbus.EventBus.getDefault().post(new RemoveControlViewEvent());
        }
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.start(getActivity(),currentAgendaPositon + 1,0,currentTitle,false,false,false,"","");
    }

    @Override
    public void setFileList(List<DocumentModel> documents) {
        mDocumentsList = documents;
    }

    @Override
    public void showNoAgendaList() {

    }

    @Override
    public void showNoFileList() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.setFirstLoad(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (ICommunicate) context;
    }

    @Override
    public void onAgendaItemClick(int position) {
        currentAgendaPositon = position;
        mAgendaListAdapter.setSelectedPosition(position);
        mAgendaListAdapter.notifyDataSetChanged();
        Log.d("test", "Click:" + currentAgendaPositon);
        mPresenter.showAgendaDetail(mAgendasList.get(position));
    }

    @Override
    public void onFileDetailClickListener(int position) {
        mPresenter.fetchFileListAndShow(true, position+1);
    }
}
