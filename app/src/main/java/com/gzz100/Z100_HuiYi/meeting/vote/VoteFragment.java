package com.gzz100.Z100_HuiYi.meeting.vote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Vote;

import java.util.ArrayList;
import java.util.List;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class VoteFragment extends Fragment implements VoteContract.VoteView,
        OnVoteOptionClickListener, OnAllVoteItemClickListener{
    private VoteContract.Presenter mPresenter;

    private List<Vote> mAllVoteList;
    private Vote mVote;

    private RecyclerView mAllVoteInfRecyclerView;
    private RecyclerView mVoteOptionRecyclerView;
    private TextView mVoteTitleTextView;
    private TextView mVoteQuestionTextView;
    private TextView mVoteNumberNeededTextView;
    private TextView mVoteNumberSelectedTextView;
    private Button mVoteSubmitButton;
    private LinearLayout mVoteMainLayout;
    private TextView mVoteFinishedInfTextView;
    private TextView mVoteNotBeginInfTextView;

    private int optionNeededNumber = 0;
    private int optionSelectedNumber = 0;
    private List<Boolean> optionStateList;

    public static VoteFragment newInstance(){return new VoteFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }
    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.start();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote, null);
        mAllVoteInfRecyclerView = (RecyclerView) view.findViewById(R.id.id_rev_all_vote_inf_list);
        mVoteOptionRecyclerView = (RecyclerView) view.findViewById(R.id.id_rev_vote_options);
        mVoteTitleTextView = (TextView) view.findViewById(R.id.id_text_view_vote_title);
        mVoteQuestionTextView = (TextView) view.findViewById(R.id.id_text_view_vote_question);
        mVoteNumberNeededTextView = (TextView) view.findViewById(R.id.id_text_view_vote_needed_number);
        mVoteNumberSelectedTextView = (TextView) view.findViewById(R.id.id_text_view_vote_selected_number);
        mVoteSubmitButton = (Button) view.findViewById(R.id.id_btn_submit_vote);
        mVoteMainLayout = (LinearLayout) view.findViewById(R.id.id_layout_vote_main);
        mVoteFinishedInfTextView = (TextView) view.findViewById(R.id.id_text_view_vote_finished_inf);
        mVoteNotBeginInfTextView = (TextView) view.findViewById(R.id.id_iv_vote_fragment_not_begin);
        initViews();
//        ButterKnife.bind(getActivity(),view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.setFirstLoad(true);
        optionNeededNumber = optionSelectedNumber = 0; //防止当销毁之后，选择的数目不归O导致无法选择的BUG
    }

    private void initViews(){
        mVoteSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckDialog();
            }
        });
    }

    @Override
    public void setPresenter(VoteContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setVoteInf(Vote vote) {
        this.mVote = vote;
    }

    @Override
    public void setAllVoteInf(List<Vote> voteList) {
        this.mAllVoteList = voteList;
    }

    @Override
    public void showAllVoteInf() {
        mAllVoteInfRecyclerView.setVisibility(View.VISIBLE);
        //隐藏掉两个在最下面布局的控件，避免已经显示而覆盖掉主布局
        mVoteFinishedInfTextView.setVisibility(View.GONE);
        mVoteNotBeginInfTextView.setVisibility(View.GONE);
        mVoteMainLayout.setVisibility(View.INVISIBLE);
        AllVoteListAdapter adapter = new AllVoteListAdapter(getContext(), mAllVoteList);
        adapter.setmOnAllVoteItemClickListener(this);
        mAllVoteInfRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAllVoteInfRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showVoteInf() {
        mAllVoteInfRecyclerView.setVisibility(View.INVISIBLE);
        //隐藏掉两个在最下面布局的控件，避免已经显示而覆盖掉主布局
        mVoteFinishedInfTextView.setVisibility(View.GONE);
        mVoteNotBeginInfTextView.setVisibility(View.GONE);
        mVoteMainLayout.setVisibility(View.VISIBLE);
        optionStateList = new ArrayList<Boolean>();
        for(int i = 0; i < mVote.getVoteOptionsList().size(); i++){
            optionStateList.add(false);
        }

        //显示投票标题
        mVoteTitleTextView.setText(mVote.getVoteTitle());

        //显示投票问题
        mVoteQuestionTextView.setText(mVote.getVoteContent());

        //显示需要选择的数量
        mVoteNumberNeededTextView.setText("请选择" + mVote.getVoteNumberNeeded() + "项:");
        optionNeededNumber = mVote.getVoteNumberNeeded();

        //显示选项
        VoteOptionListAdapter adapter = new VoteOptionListAdapter(getContext(), mVote.getVoteOptionsList());
        adapter.setOnVoteOptionClickListener(this);
        mVoteOptionRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mVoteOptionRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showCheckDialog() {
        if (optionSelectedNumber != optionNeededNumber){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("投票未完成");
            builder.setMessage("您只选择了" + optionSelectedNumber + "项，请继续选择");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("您的选择是");
        List<String> listSelected = new ArrayList<String>();
        final List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < optionStateList.size(); i++){
            if (optionStateList.get(i).equals(true)){
                listSelected.add(mVote.getVoteOptionsList().get(i).getOptionItem());
                ids.add(mVote.getVoteOptionsList().get(i).getOptionID());
            }
        }
        //builder.setMessage("您的选择是：\n");

        final String items[] = listSelected.toArray(new String[listSelected.size()]);
        builder.setItems(items, null);
        DialogInterface.OnClickListener dialogOnclickListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        dialog.dismiss();
                        //
                        mPresenter.submitVoteResult(ids);
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        builder.setPositiveButton("确定", dialogOnclickListener);
        builder.setNegativeButton("取消", dialogOnclickListener);
        builder.create().show();
    }

    @Override
    public void showVoteFinishedInf(boolean isSuccessful) {
        mVoteMainLayout.setVisibility(View.INVISIBLE);
        mVoteNotBeginInfTextView.setVisibility(View.GONE);
        if(isSuccessful){
            mVoteFinishedInfTextView.setText("投票成功，等待主持人下一步操作");
        } else {
            mVoteFinishedInfTextView.setText("投票失败，请确认网络环境是否正常");
        }
        mVoteFinishedInfTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showVoteNotBegin(String showText) {
        mVoteFinishedInfTextView.setVisibility(View.GONE);
        mVoteMainLayout.setVisibility(View.GONE);
        mVoteNotBeginInfTextView.setVisibility(View.VISIBLE);
        mVoteNotBeginInfTextView.setText(showText);
    }

    @Override
    public void onVoteStartStopButtonClick(View view, int position) {
        mPresenter.startVote(mAllVoteList.get(position));
    }

    @Override
    public void onCheckResultButtonClick(View view, int position) {
        mPresenter.checkVoteResult(mAllVoteList.get(position));
    }

    @Override
    public void onVoteOptionClick(View view, int position) {
        if(optionStateList.get(position).equals(false)){
            if(optionSelectedNumber == optionNeededNumber){
                //选择数量大于可以选择的数量，不能选择
                return;
            }
            optionSelectedNumber++;
            refreshSelectedNumberTextView();
            optionStateList.set(position, true);
            setOptionState(position, false);
        } else {
            optionSelectedNumber--;
            refreshSelectedNumberTextView();
            optionStateList.set(position, false);
            setOptionState(position, true);
        }
    }

    private void setOptionState(int position, boolean state){
        if(!state){
            mVoteOptionRecyclerView.getChildAt(position).findViewById(R.id.id_item_vote_option_layout)
                    .setBackgroundColor(0xFF4FC3F7);
            ((TextView) mVoteOptionRecyclerView.getChildAt(position).findViewById(R.id.id_text_view_option_number))
                    .setTextColor(0xFFFFFFFF);
            ((TextView) mVoteOptionRecyclerView.getChildAt(position).findViewById(R.id.id_text_view_option_text))
                    .setTextColor(0xFFFFFFFF);

        } else {
            mVoteOptionRecyclerView.getChildAt(position).findViewById(R.id.id_item_vote_option_layout)
                    .setBackgroundColor(0xFFFFFFFF);
            ((TextView) mVoteOptionRecyclerView.getChildAt(position).findViewById(R.id.id_text_view_option_number))
                    .setTextColor(0xFF000000);
            ((TextView) mVoteOptionRecyclerView.getChildAt(position).findViewById(R.id.id_text_view_option_text))
                    .setTextColor(0xFF000000);
        }
    }

    private void refreshSelectedNumberTextView(){
        mVoteNumberSelectedTextView.setText("(已经选择" + optionSelectedNumber + "项)");
    }
}
