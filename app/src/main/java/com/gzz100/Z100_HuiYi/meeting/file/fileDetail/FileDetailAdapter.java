package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Document;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/8/30.
 */
public class FileDetailAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Document> documents;
    private Context mContext;
    private LayoutInflater mInflater;

    public FileDetailAdapter(List<Document> documents, Context context) {
        this.documents = documents;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    private OnFileClick mOnFileClick;

    public void setOnFileClick(OnFileClick onFileClick) {
        this.mOnFileClick = onFileClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_file_name, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.e("点击了单个  ===","position =="+position);
        holder.mTvFileName.setText(documents.get(position).getDocumentName());
        holder.mTvFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFileClick.onFileClick(holder.getAdapterPosition());
            }
        });

        if (position == 0) {
            holder.mAll.setBackgroundColor(mContext.getResources().getColor(R.color.color_tab_selected));
            holder.mTvFileName .setTextColor(mContext.getResources().getColor(R.color.color_white));
        }

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }


}
class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.id_ll_file_name_backgroud)
    LinearLayout mAll;
    @BindView(R.id.id_tv_item_file_name)
    TextView mTvFileName;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

