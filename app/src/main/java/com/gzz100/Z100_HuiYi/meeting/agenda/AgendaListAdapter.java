package com.gzz100.Z100_HuiYi.meeting.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.meeting.file.OnAgendaTabClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lee on 2016/8/31.
 */
public class AgendaListAdapter extends RecyclerView.Adapter<AgendaHolder> {
    private OnAgendaClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnAgendaClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    private int selectedPosition;
    public void setSelectedPosition(int position){
        this.selectedPosition = position;
    }

    private Context mContext;
    private List<Agenda> mAgendas;
    private LayoutInflater mInflater;

    public AgendaListAdapter(Context context, List<Agenda> agendas) {
        mContext = context;
        mAgendas = agendas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public AgendaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_agenda_name_list, null);
        AgendaHolder holder = new AgendaHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AgendaHolder holder, final int position) {
        holder.mAgendaTitle.setText(mAgendas.get(position).getAgendaName());
        holder.mAgendaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onAgendaItemClick(position);
                }
            }
        });
        if(position == 0){
            holder.mAgendaTitle.setTextColor(mContext.getResources().getColor(R.color.color_white));
            holder.mAgendaLayout.setBackgroundColor(mContext.
                    getResources().getColor(R.color.color_tab_selected));
        }
        if (selectedPosition == position){
            holder.mAgendaTitle.setTextColor(mContext.getResources().getColor(R.color.color_white));
            holder.mAgendaLayout.setBackgroundColor(mContext.
                    getResources().getColor(R.color.color_tab_selected));
        }else {
            holder.mAgendaTitle.setTextColor(mContext.getResources().getColor(R.color.color_black));
            holder.mAgendaLayout.setBackgroundColor(mContext.
                    getResources().getColor(R.color.color_tab_normal));
        }
    }

    @Override
    public int getItemCount() {
        return mAgendas.size();
    }
}

class AgendaHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.id_item_agenda_name_layout)
    LinearLayout mAgendaLayout;
    @BindView(R.id.id_item_agenda_name_title)
    TextView mAgendaTitle;

    public AgendaHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}