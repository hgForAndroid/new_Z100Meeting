package com.gzz100.Z100_HuiYi.meeting.agenda;

import android.animation.Animator;
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

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.meeting.ICommunicate;
import com.gzz100.Z100_HuiYi.meeting.file.fileDetail.FileDetailActivity;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.List;

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
    LinearLayout mAgendaDetailLayout;

    private float mX;
    private float mY;

    private AgendaContract.Presenter mPresenter;

    private List<Agenda> mAgendasList;
    private int currentAgendaPosition;
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
        if (Constant.DEBUG)
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
                mPresenter.fetchFileListAndShow(true, currentAgendaPosition +1);
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
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                onAgendaItemClick((currentAgendaPosition + 1) % mAgendasList.size());
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.start();
                    } else if(mY - event.getY() > 50){
                        Log.d("test" ," UP!");
                        ObjectAnimator animator = ObjectAnimator
                                .ofFloat(v, "translationY", v.getTranslationY(), v.getTranslationY()-200f, v.getTranslationY())
                                .setDuration(150);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                onAgendaItemClick((currentAgendaPosition -1 >= 0 ? currentAgendaPosition -1 : mAgendasList.size()-1) % mAgendasList.size());
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

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
        mAgendaTimeTextView.setText("议程时长：" + agenda.getAgendaDuration());
    }

    @Override
    public void showFileDetail() {
        String currentTitle = mMainActivity.getCurrentTitle();
        FileDetailActivity.showFileDetailActivity(getActivity(), mAgendasList.size(), currentAgendaPosition + 1,
                mDocumentsList, 0, mAgendasList.get(currentAgendaPosition).getAgendaDuration(), currentTitle);
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
    public void onAttach(Context context) {
//        Log.e("AgendaFragment -->","onAttach");
        super.onAttach(context);
        mMainActivity = (ICommunicate) context;
    }

    @Override
    public void onAgendaItemClick(int position) {
        int childCount = mAgendaRecyclerView.getChildCount();
        setBackgroundColor(childCount, position);
        currentAgendaPosition = position;
        mPresenter.showAgendaDetail(mAgendasList.get(position));
    }
}
