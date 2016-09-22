package com.gzz100.Z100_HuiYi.meetingPrepare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/9/22.
 */
public class IPAdapter extends RecyclerView.Adapter<IPAdapter.IPHolder>{
    private OnIPClickListener mOnIPClickListener;
    public void setOnIPClickListener(OnIPClickListener onIPClickListener){
        this.mOnIPClickListener = onIPClickListener;
    }
    private Context mContext;
    private List<String> ips;
    private LayoutInflater mInflater;

    public IPAdapter(Context context, List<String> ips) {
        this.mContext = context;
        this.ips = ips;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public IPHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_ip,null);
        return new IPHolder(view);
    }

    @Override
    public void onBindViewHolder(IPHolder holder, final int position) {
        holder.mTvIp.setText(ips.get(position));
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnIPClickListener.onDelete(position);
            }
        });
        holder.mRlIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnIPClickListener.onIPClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ips.size();
    }

    class IPHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.id_ip_all)
        RelativeLayout mRlIP;
        @BindView(R.id.id_tv_item_ip)
        TextView mTvIp;
        @BindView(R.id.id_iv_delete_item_ip)
        ImageView mIvDelete;
        public IPHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

