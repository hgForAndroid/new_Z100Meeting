package com.gzz100.Z100_HuiYi.meetingPrepare.selectMeeting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.MeetingBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingHolder>{
    private OnMeetingClickListener mOnClickListener;
    public void setOnMeetingClickListener(OnMeetingClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }
    private Context mContext;
    private List<MeetingBean> meetings;
    private LayoutInflater mInflater;

    public MeetingAdapter(Context context, List<MeetingBean> meetings) {
        this.mContext = context;
        this.meetings = meetings;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MeetingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_select_meeting,null);
        return new MeetingHolder(view);
    }

    @Override
    public void onBindViewHolder(MeetingHolder holder, final int position) {
        holder.mTvName.setText(meetings.get(position).getMeetingName());
        holder.mTvTime.setText(meetings.get(position).getMeetingBeginTime());
        meetings.get(position).setMeetingState(holder.mTvState);
        holder.mLlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onMeetingClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    class MeetingHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.id_ll_meeting_all)
        LinearLayout mLlAll;
        @BindView(R.id.id_tv_name_item_select_meeting)
        TextView mTvName;
        @BindView(R.id.id_tv_time_item_select_meeting)
        TextView mTvTime;
        @BindView(R.id.id_tv_state_item_select_meeting)
        TextView mTvState;
        public MeetingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

