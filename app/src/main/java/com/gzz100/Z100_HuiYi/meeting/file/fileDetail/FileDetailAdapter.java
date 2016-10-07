package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.Document;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XieQXiong on 2016/8/30.
 */
public class FileDetailAdapter extends BaseAdapter {
    private List<Document> documents;
    private Context mContext;
    private LayoutInflater mInflater;

    public FileDetailAdapter(List<Document> documents, Context context) {
        this.documents = documents;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return documents.size();
    }

    @Override
    public Document getItem(int position) {
        return documents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_file_name,null);

            viewHolder.mAll = (LinearLayout) convertView.findViewById(R.id.id_all);
            viewHolder.mTvName = (TextView) convertView.findViewById(R.id.id_tv_item_file_name);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTvName.setText(documents.get(position).getDocumentName());
//        if (position == 0){
//            viewHolder.mAll.setSelected(true);
//            viewHolder.mAll.setActivated(true);
//        }

        if (position == selectedItem){
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_tab_selected));
            viewHolder.mTvName.setTextColor(Color.WHITE);
        }else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));
            viewHolder.mTvName.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    public void setSelectedItem(int position){
        selectedItem = position;
    }
    private int selectedItem = 0;
    class ViewHolder{
        public LinearLayout mAll;
        public TextView mTvName;
    }
}
