package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DelegateModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2016/9/2.
 */
public class DelegateListAdapter extends RecyclerView.Adapter<DelegateNameHolder>{

    List<DelegateModel> mDelegateBean;
    OnDelegateNameItemClickListener mListener;
    LayoutInflater mInflate;
    private Context mContext;


    public void setDelegateBeanItemClickListener(OnDelegateNameItemClickListener listener){
        this.mListener=listener;
    }

    public DelegateListAdapter(Context context, List<DelegateModel> delegateBean) {
        mDelegateBean=delegateBean;
        mInflate= LayoutInflater.from(context);
        mContext = context;

    }

    @Override
    public DelegateNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflate.inflate(R.layout.item_delegate_list,null);
        return new DelegateNameHolder(view);
    }

    @Override
    public void onBindViewHolder(DelegateNameHolder holder, final int position) {
        holder.mDelegateName.setText(mDelegateBean.get(position).getDelegateName());
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                mListener.onDelegateNameItemClickListener(position);
            }
        });
        mDelegateBean.get(position).setPicForUserLeft(mContext,holder.mDelegateName);

    }

    @Override
    public int getItemCount() {
        return mDelegateBean.size();
    }
}

class DelegateNameHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.id_item_delegate_name)
    TextView mDelegateName;
    @BindView(R.id.id_item_delegate_list_layout)
    LinearLayout mLinearLayout;

    public DelegateNameHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
}
class DelegateItemSpaceDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public DelegateItemSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top=space;
//        outRect.bottom=space;
    }
}
