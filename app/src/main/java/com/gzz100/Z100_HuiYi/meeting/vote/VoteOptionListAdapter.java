package com.gzz100.Z100_HuiYi.meeting.vote;

import android.content.Context;
import android.graphics.Path;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Option;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lee on 2016/9/4.
 */
public class VoteOptionListAdapter extends RecyclerView.Adapter<VoteOptionHolder> {
    private OnVoteOptionClickListener mOnVoteOptionClickListener;
    public void setOnVoteOptionClickListener(OnVoteOptionClickListener onVoteOptionClickListener){
        this.mOnVoteOptionClickListener = onVoteOptionClickListener;
    }

    private Context mContext;
    private List<Option> mOptions;
    private LayoutInflater mLayoutInflater;

    public VoteOptionListAdapter(Context context, List<Option> options){
        this.mContext = context;
        this.mOptions = options;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public VoteOptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_vote_option_list, parent,false);
        VoteOptionHolder holder = new VoteOptionHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VoteOptionHolder holder, final int position) {
        holder.mOptionNumberTextView.setText(position + 1 + ":");
        holder.mOptionTextTextView.setText(mOptions.get(position).getOptionItem());
        holder.mOptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnVoteOptionClickListener != null){
                    mOnVoteOptionClickListener.onVoteOptionClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }
}

class VoteOptionHolder extends RecyclerView.ViewHolder {
     @BindView(R.id.id_item_vote_option_layout)
    LinearLayout mOptionLayout;

    @BindView(R.id.id_text_view_option_number)
    TextView mOptionNumberTextView;

    @BindView(R.id.id_text_view_option_text)
    TextView mOptionTextTextView;


    public VoteOptionHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
