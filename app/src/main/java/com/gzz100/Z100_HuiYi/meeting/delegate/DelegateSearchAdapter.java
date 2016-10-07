package com.gzz100.Z100_HuiYi.meeting.delegate;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.gzz100.Z100_HuiYi.data.DelegateBean;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by DELL on 2016/10/1.
 */
public class DelegateSearchAdapter implements ListAdapter {
    private List<DelegateBean> mDelegateBean;
    private View mView;
    public DelegateSearchAdapter(Context context,int ID ,List<DelegateBean> delegateBeans) {
        mDelegateBean=delegateBeans;
        mView= LayoutInflater.from(context).inflate(ID,null);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mDelegateBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mDelegateBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
