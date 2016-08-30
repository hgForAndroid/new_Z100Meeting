package com.gzz100.Z100_HuiYi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.FileBean;
import com.gzz100.Z100_HuiYi.meetingManage.fileManage.OnFileItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/8/26.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileBeanHolder>{
    private OnFileItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnFileItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }


    private Context mContext;
    private List<FileBean> mFiles;
    private LayoutInflater mInflater;
    public FileListAdapter(Context context, List<FileBean> files) {
        mContext = context;
        mFiles = files;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FileBeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_file_list, null);
        return new FileBeanHolder(view);
    }

    @Override
    public void onBindViewHolder(FileBeanHolder holder, final int position) {
//        holder.mFileImage.setImageResource(mContext.getResources(),R.);
        holder.mFileName.setText(mFiles.get(position).getFileName());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onFileItemClick(position);
            }
        });
        holder.mFileSize.setText(mFiles.get(position).getFileSize());
        holder.mKeynoteSpeaker.setText(mFiles.get(position).getKeyNoteSpeaker());

    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }
}
class FileBeanHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.id_item_file_list) RelativeLayout mLayout;
    @BindView(R.id.id_item_file_image) ImageView mFileImage;
    @BindView(R.id.id_item_file_name) TextView mFileName;
    @BindView(R.id.id_item_file_size) TextView mFileSize;
    @BindView(R.id.id_item_file_keynote_speaker) TextView mKeynoteSpeaker;
    public FileBeanHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
