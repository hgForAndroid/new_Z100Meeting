package com.gzz100.Z100_HuiYi.meeting.file;

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
import com.gzz100.Z100_HuiYi.data.Document;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/9/20.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<ResultHolder>{
    private OnSearchItemClickListener mOnItemClickListener;
    public void setOnSearchItemClickListener(OnSearchItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }


    private Context mContext;
    private List<Document> mDocuments;
    private LayoutInflater mInflater;
    public SearchResultAdapter(Context context, List<Document> documents) {
        mContext = context;
        mDocuments = documents;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_result_file, null);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, final int position) {
//        holder.mFileImage.setImageResource(mContext.getResources(),R.);
        holder.mFileName.setText(mDocuments.get(position).getDocumentName());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onSearchClick(position);
            }
        });
        holder.mFileSize.setText(mDocuments.get(position).getDocumentSize()+"kb");
        holder.mKeynoteSpeaker.setText(mDocuments.get(position).getDocumentSpeaker());
        mDocuments.get(position).setImageForFile(mContext,holder.mFileImage);
        holder.mAgenda.setText("议程"+mDocuments.get(position).getDocumentAgendaIndex());

    }

    @Override
    public int getItemCount() {
        return mDocuments.size();
    }
}
class ResultHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.id_ll_item_file_search_result)
    LinearLayout mLayout;
    @BindView(R.id.id_iv_item_file_image_result)
    ImageView mFileImage;
    @BindView(R.id.id_tv_item_file_name)
    TextView mFileName;
    @BindView(R.id.id_tv_item_file_size_result) TextView mFileSize;
    @BindView(R.id.id_tv_item_file_key_note_speaker_result) TextView mKeynoteSpeaker;
    @BindView(R.id.id_tv_item_file_agenda_result) TextView mAgenda;
    public ResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}