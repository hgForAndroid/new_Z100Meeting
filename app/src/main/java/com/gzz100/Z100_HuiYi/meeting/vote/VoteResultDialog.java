package com.gzz100.Z100_HuiYi.meeting.vote;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.RepositoryUtil;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.VoteResult;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2016/10/31.
 */

public class VoteResultDialog extends AlertDialog{
    //widget
    private Context mContext;
    private BarChart mBarChart;
    private TextView mTvShowError;

    //data
    private List<VoteResult> mVoteResults;

    private int voteId;
    private List<BarDataSet> mBarDataSets;
    private List<String> mOptionNames;

    public VoteResultDialog(Context context, int voteId){
        super(context);
        mContext = context;
        this.voteId = voteId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vote_result);
        //取得柱状图
        mBarChart = (BarChart) findViewById(R.id.id_vote_result_chart);
        RepositoryUtil.getVoteRepository(mContext).getVoteResult(voteId, new VoteDataSource.LoadVoteResultCallback() {
            @Override
            public void onVoteResultLoaded(List<VoteResult> voteResults) {
                handleVoteResult(voteResults);
                showVoteResult();
            }
            @Override
            public void onDataNotAvailable(String errorString) {
                showErrorMessage(errorString);
            }
        });
    }

    private void handleVoteResult(List<VoteResult> voteResults) {
        //提取选项的名称，之后放置于柱状图的下方
        mOptionNames = new ArrayList<>();
        //将票数赋值给柱状图
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0;i < voteResults.size();i++){
            mOptionNames.add(voteResults.get(i).getOptionItem());
            barEntries.add(new BarEntry(i,voteResults.get(i).getVoteNum()));
        }
        mBarDataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(barEntries,"voteResult");
        //转换柱状图的值格式
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value+"票";
            }
        });
        //设置柱状图的值显示，如该值不设置为true，则不会显示
        barDataSet.setDrawValues(true);
        mBarDataSets.add(barDataSet);
    }

    private void showVoteResult() {
        if (mTvShowError.getVisibility() == View.VISIBLE){
            mTvShowError.setVisibility(View.GONE);
        }
        BarData barData = new BarData(mOptionNames,mBarDataSets);
        //给柱状图赋值
        mBarChart.setData(barData);
        //描述，内容会显示在柱状图的右下角
        mBarChart.setDescription("");
        //禁止双击缩放
        mBarChart.setDoubleTapToZoomEnabled(false);
        //禁止双指缩放
        mBarChart.setScaleEnabled(false);
        //将选项的名称放置于柱状图的下方
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //去掉左右两边的Y轴值
        mBarChart.getAxisLeft().setEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);
        //设置XY轴初始化的动画时间
        mBarChart.animateXY(2000, 2000);
        //刷新
        mBarChart.invalidate();
    }

    private void showErrorMessage(String errorMessage){
        mTvShowError.setVisibility(View.VISIBLE);
        mTvShowError.setText(errorMessage);
    }
}
