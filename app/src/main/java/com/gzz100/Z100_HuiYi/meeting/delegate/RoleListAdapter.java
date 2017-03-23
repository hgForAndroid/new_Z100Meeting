package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.content.Context;
import android.graphics.Rect;
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
 * Created by DELL on 2016/9/2.
 */
public class RoleListAdapter extends RecyclerView.Adapter<RoleHolder> {

    Context mContext;
    private OnRoleItemClickListener mOnClickListener;
    public void setRoleItemOnClickListener(OnRoleItemClickListener onRoleItemClickListener)
    {
        this.mOnClickListener=onRoleItemClickListener;
    }

    private List<String> mRoleList;
    private LayoutInflater mInflater;


    public RoleListAdapter(Context context, List<String> roleList) {

        mRoleList=roleList;
        mInflater=LayoutInflater.from(context);
        mContext=context;
    }

    @Override
    public RoleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_role_list,null);

        return new RoleHolder(view);
    }

    @Override
    public void onBindViewHolder(RoleHolder holder, final int position) {

        if (mRoleList.get(position).equals("主持人")){
            holder.mRoleImage.setImageResource(R.drawable.icon_host_normal);
        }
        if (mRoleList.get(position).equals("主讲人")){
            holder.mRoleImage.setImageResource(R.drawable.icon_speaker_normal);
        }
        if (mRoleList.get(position).equals("其他参会代表")){
            holder.mRoleImage.setImageResource(R.drawable.icon_delagate_normal);
        }
        holder.mRoleName.setText(mRoleList.get(position));
        holder.mRoleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onRoleItemClickListener(position);
            }
        });
        if(position==0) {
            holder.mRoleName.setTextColor(mContext.getResources().getColor(R.color.color_white));
            holder.mRoleLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_tab_selected));
        }
    }

    @Override
    public int getItemCount() {
        return mRoleList.size();
    }

}

class RoleHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.id_item_role_image)
    ImageView mRoleImage;
    @BindView(R.id.id_item_role_name)
    TextView mRoleName;
    @BindView(R.id.id_item_role_layout)
    RelativeLayout mRoleLayout;
    public RoleHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
class RoleItemSpaceDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public RoleItemSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.right=space;
    }
}
