package com.gzz100.Z100_HuiYi.meeting.file;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.DocumentModel;

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
    private List<DocumentModel> mDocuments;
    private LayoutInflater mInflater;
    public FileListAdapter(Context context, List<DocumentModel> documents) {
        mContext = context;
        mDocuments = documents;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FileBeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_file_list, null);
        return new FileBeanHolder(view);
    }

    @Override
    public void onBindViewHolder(final FileBeanHolder holder, int position) {
//        holder.mFileImage.setImageResource(mContext.getResources(),R.);
        holder.mFileName.setText(mDocuments.get(position).getDocumentName());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onFileItemClick(holder.getAdapterPosition());
            }
        });
        holder.mFileSize.setText(mDocuments.get(position).getDocumentSize()+"kb");

        mDocuments.get(position).setImageForFile(mContext,holder.mFileImage);
        mDocuments.get(position).setDocumentSpeaker(mContext,holder.mKeynoteSpeaker);

    }

    @Override
    public int getItemCount() {
        return mDocuments.size();
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
