package com.gzz100.Z100_HuiYi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.meetingManage.fileManage.OnAgendaItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/8/26.
 */
public class AgendaListAdapter extends RecyclerView.Adapter<AgendaHolder>{
    private OnAgendaItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnAgendaItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
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
        View view = mInflater.inflate(R.layout.item_agenda_list, null);
        AgendaHolder holder = new AgendaHolder(view);
//        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(AgendaHolder holder, final int position) {
        holder.mAgendaIndex.setText("议程"+(position+1));
        holder.mAgendaTitle.setText(mAgendas.get(position).getAgendaName());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                mOnItemClickListener.onAgendaItemClick(v,position);
            }
        });
        if (position == 0){
            holder.mAgendaIndex.setTextColor(mContext.getResources().getColor(R.color.color_white));
            holder.mAgendaTitle.setTextColor(mContext.getResources().getColor(R.color.color_white));
            holder.mLayout.setBackgroundColor(mContext.
                    getResources().getColor(R.color.color_tab_selected));
        }

    }

    @Override
    public int getItemCount() {
        return mAgendas.size();
    }

}
class AgendaHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.id_item_agenda_index)
    TextView mAgendaIndex;
    @BindView(R.id.id_item_agenda_title)
    TextView mAgendaTitle;
    @BindView(R.id.id_item_agenda_layout)
    LinearLayout mLayout;


    public AgendaHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
//        mAgendaIndex = (TextView) itemView.findViewById(R.id.id_item_agenda_index);
//        mAgendaTitle = (TextView) itemView.findViewById(R.id.id_item_agenda_title);
//        mLayout = (LinearLayout) itemView.findViewById(R.id.id_item_agenda_layout);

    }
}
