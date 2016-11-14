package com.gzz100.Z100_HuiYi.meeting.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lee on 2016/9/12.
 */
public class AgendaDetailListAdapter extends RecyclerView.Adapter<AgendaDetailHolder>{
    private OnFileDetailClickListener mOnFileDetailClickListener;
    public void setOnFileDetailClickListener(OnFileDetailClickListener listener){
        this.mOnFileDetailClickListener = listener;
    }

    private Context mContext;
    private List<AgendaModel> mAgendaList;
    private LayoutInflater mInflater;

    public AgendaDetailListAdapter(Context context, List<AgendaModel> list){
        mContext = context;
        mAgendaList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public AgendaDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_agenda_detail, parent, false);
        AgendaDetailHolder holder = new AgendaDetailHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AgendaDetailHolder holder, final int position) {
        holder.mAgendaTitleTextView.setText(mAgendaList.get(position).getAgendaName());
        holder.mAgendaSperkerTextView.setText(mAgendaList.get(position).getAgendaSpeaker());
        holder.mAgendaTimeTextView.setText(mAgendaList.get(position).getAgendaDuration()+"");
        holder.mAgendaFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnFileDetailClickListener != null){
                    mOnFileDetailClickListener.onFileDetailClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAgendaList.size();
    }
}

class AgendaDetailHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.id_text_view_agenda_title)
    TextView mAgendaTitleTextView;
    @BindView(R.id.id_text_view_agenda_speaker)
    TextView mAgendaSperkerTextView;
    @BindView(R.id.id_text_view_agenda_time)
    TextView mAgendaTimeTextView;
    @BindView(R.id.id_btn_show_agenda_file)
    Button mAgendaFileButton;

    public AgendaDetailHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
