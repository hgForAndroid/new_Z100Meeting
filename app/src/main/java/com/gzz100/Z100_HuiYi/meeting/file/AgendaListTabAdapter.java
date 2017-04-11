package com.gzz100.Z100_HuiYi.meeting.file;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.AgendaModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/8/26.
 */
public class AgendaListTabAdapter extends RecyclerView.Adapter<AgendaHolder>{
    private OnAgendaTabClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnAgendaTabClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    private Context mContext;
    private List<AgendaModel> mAgendas;
    private LayoutInflater mInflater;

    public AgendaListTabAdapter(Context context, List<AgendaModel> agendas) {
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
            holder.mAgendaIndex.setTextColor(mContext.getResources().getColor(R.color.color_navBar_bg));
//            holder.mAgendaTitle.setTextColor(mContext.getResources().getColor(R.color.color_white));

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_file_selected);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            holder.mAgendaIndex.setCompoundDrawables(null,drawable,null,null);
//            holder.mLayout.setBackgroundColor(mContext.
//                    getResources().getColor(R.color.color_tab_selected));
        }
        if (selectedPosition == position){
            holder.mAgendaIndex.setTextColor(mContext.getResources().getColor(R.color.color_navBar_bg));
//            holder.mAgendaTitle.setTextColor(mContext.getResources().getColor(R.color.color_white));

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_file_selected);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            holder.mAgendaIndex.setCompoundDrawables(null,drawable,null,null);
//            holder.mLayout.setBackgroundColor(mContext.
//                    getResources().getColor(R.color.color_tab_selected));
        }else {
            holder.mAgendaIndex.setTextColor(mContext.getResources().getColor(R.color.color_agenda_item_text));
            holder.mAgendaTitle.setTextColor(mContext.getResources().getColor(R.color.color_black));

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_file_normal);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            holder.mAgendaIndex.setCompoundDrawables(null,drawable,null,null);


//            holder.mLayout.setBackgroundColor(mContext.
//                    getResources().getColor(R.color.color_tab_normal));
        }

    }

    @Override
    public int getItemCount() {
        return mAgendas.size();
    }

    private int selectedPosition;
    public void setSelectedItem(int position){
        selectedPosition = position;
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
