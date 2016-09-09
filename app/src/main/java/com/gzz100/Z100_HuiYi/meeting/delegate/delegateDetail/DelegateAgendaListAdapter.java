package com.gzz100.Z100_HuiYi.meeting.delegate.delegateDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/9/7.
 */
public class DelegateAgendaListAdapter extends RecyclerView.Adapter<DelegateAgendaVoewHolder>{




    public DelegateAgendaListAdapter(Context context, ArrayList<Integer> delegateAgendaList) {

    }

    @Override
    public DelegateAgendaVoewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DelegateAgendaVoewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class DelegateAgendaVoewHolder extends RecyclerView.ViewHolder{

    public DelegateAgendaVoewHolder(View itemView) {
        super(itemView);
    }
}
