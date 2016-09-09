package com.gzz100.Z100_HuiYi.meeting.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Vote;

import java.util.ArrayList;
import java.util.List;

/**
* 文件详情
* @author XieQXiong
* create at 2016/8/23 17:01
*/

public class VoteFragment extends Fragment implements VoteContract.VoteView, OnVoteOptionClickListener{
    private VoteContract.Presenter mPresenter;
    private Vote mVote;

    private RecyclerView mVoteOptionRecyclerView;
    private TextView mVoteTitleTextView;
    private TextView mVoteQuestionTextView;
    private TextView mVoteNumberNeededTextView;
    private TextView mVoteNumberSelectedTextView;
    private Button mVoteSubmitButton;

    public int optionNeededNumber = 0;
    private int optionSelectedNumber = 0;
    private List<Boolean> optionStateList;

    public static VoteFragment newInstance(){return new VoteFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote, null);
        mVoteOptionRecyclerView = (RecyclerView) view.findViewById(R.id.id_rev_vote_options);
        mVoteTitleTextView = (TextView) view.findViewById(R.id.id_text_view_vote_title);
        mVoteQuestionTextView = (TextView) view.findViewById(R.id.id_text_view_vote_question);
        mVoteNumberNeededTextView = (TextView) view.findViewById(R.id.id_text_view_vote_needed_number);
        mVoteNumberSelectedTextView = (TextView) view.findViewById(R.id.id_text_view_vote_selected_number);
        mVoteSubmitButton = (Button) view.findViewById(R.id.id_btn_submit_vote);
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
                mPresenter.submitVoteResult();
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
    public void showVoteInf() {
        optionStateList = new ArrayList<Boolean>();
        for(int i = 0; i < mVote.getVoteOptionsList().size(); i++){
            optionStateList.add(false);
        }

        //显示投票标题
        mVoteTitleTextView.setText(mVote.getVoteTitle());

        //显示投票问题
        mVoteQuestionTextView.setText(mVote.getVoteQuestion());

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
