package com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/9/13.
 */
public class SpeakerAgendaAdapter extends RecyclerView.Adapter<SpeakerAgendaAdapter.AgendaHolder> {
    private Context mContext;
    private List<AgendaModel> agendaList;
    private LayoutInflater mInflater;
    public SpeakerAgendaAdapter(Context context, List<AgendaModel> agendaList) {
        mContext = context;
        this.agendaList = agendaList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public AgendaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_delegate_detail_agenda, parent,false);
        AgendaHolder holder = new AgendaHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AgendaHolder holder, int position) {
        holder.mTvAgenda.setText("议程"+agendaList.get(position).getAgendaIndex());
        holder.mTvAgendaContent.setText(agendaList.get(position).getAgendaName());
    }

    @Override
    public int getItemCount() {
        return agendaList.size();
    }

    class AgendaHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.id_tv_item_delegate_detail)
        TextView mTvAgenda;
        @BindView(R.id.id_tv_item_delegate_detail_agenda_content)
        TextView mTvAgendaContent;
        public AgendaHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
