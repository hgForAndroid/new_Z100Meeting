package com.gzz100.Z100_HuiYi.meeting.vote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Vote;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lee on 2016/10/24.
 */

public class AllVoteListAdapter extends RecyclerView.Adapter<AllVoteListAdapter.AllVoteListHolder>{
    private OnAllVoteItemClickListener mOnAllVoteItemClickListener;
    public void setmOnAllVoteItemClickListener(OnAllVoteItemClickListener listener){
        this.mOnAllVoteItemClickListener = listener;
    }

    private Context mContext;
    private List<Vote> mVoteList;
    private LayoutInflater mLayoutInflater;

    public AllVoteListAdapter(Context context, List<Vote> voteList){
        this.mContext = context;
        this.mVoteList = voteList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AllVoteListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_vote_list, parent, false);
        return new AllVoteListAdapter.AllVoteListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllVoteListHolder holder, final int position) {
        holder.mVoteTitleTextView.setText(mVoteList.get(position).getVoteTitle());
        holder.mVoteQuestionTextView.setText(mVoteList.get(position).getVoteContent());
        holder.mVoteStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnAllVoteItemClickListener != null){
                    mOnAllVoteItemClickListener.onVoteStartStopButtonClick(v, position);
                }
            }
        });
        holder.mCheckResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnAllVoteItemClickListener != null){
                    mOnAllVoteItemClickListener.onCheckResultButtonClick(v, position);
                }
            }
        });
        if (mVoteList.get(position).getVoteState() == 0){//未投
            holder.mVoteStartStopButton.setVisibility(View.VISIBLE);
            holder.mCheckResultButton.setVisibility(View.GONE);
        }else {
            holder.mVoteStartStopButton.setVisibility(View.GONE);
            holder.mCheckResultButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mVoteList.size();
    }

    public static class AllVoteListHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.id_tv_vote_title)
        TextView mVoteTitleTextView;

        @BindView(R.id.id_tv_vote_question)
        TextView mVoteQuestionTextView;

        @BindView(R.id.id_btn_vote_start_or_stop)
        Button mVoteStartStopButton;

        @BindView(R.id.id_btn_check_result)
        Button mCheckResultButton;

        public AllVoteListHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
