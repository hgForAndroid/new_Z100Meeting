package com.gzz100.Z100_HuiYi.meeting.vote;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.RepositoryUtil;
import com.gzz100.Z100_HuiYi.data.VoteResult;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.utils.ScreenSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee on 2016/10/31.
 */

public class VoteResultDialog extends AlertDialog{
    //widget
    private Context mContext;
    private BarChart mBarChart;//柱状图
    private TextView mTvShowError;//错误提示
    private TextView mTvShowResult;//投票结果

    private int voteId;//投票id
    private List<BarDataSet> mBarDataSets;//柱状图数据集
    private List<String> mOptionNames;//每个选项的名称，放于柱状图的下边
    private StringBuilder mStringBuilder;//所有的选项与得到的票数的字符串
    private LinearLayout mRootLayout;

    public VoteResultDialog(Context context, int voteId){
        super(context);
        mContext = context;
        this.voteId = voteId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vote_result);
        mRootLayout = (LinearLayout) findViewById(R.id.id_vote_result_root_layout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ScreenSize.getScreenWidth(mContext)*4/5,ScreenSize.getScreenHeight(mContext)*3/4);
        mRootLayout.setLayoutParams(params);
        //取得柱状图
        mBarChart = (BarChart) findViewById(R.id.id_vote_result_chart);
        mTvShowError = (TextView) findViewById(R.id.id_tv_vote_result_show_error);
        mTvShowResult = (TextView) findViewById(R.id.id_tv_vote_result_show_result);
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
        mStringBuilder = new StringBuilder();
        //提取选项的名称，之后放置于柱状图的下方
        mOptionNames = new ArrayList<>();
        //将票数赋值给柱状图
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0;i < voteResults.size();i++){
            mOptionNames.add(voteResults.get(i).getOptionItem());
            //BarEntry  前面放的是值，后面数索引
            BarEntry barEntry = new BarEntry(9, 1);
            barEntries.add(new BarEntry(voteResults.get(i).getVoteNum(),i));
            mStringBuilder.append(voteResults.get(i).getOptionItem()+":"+voteResults.get(i).getVoteNum()+"票    ");
        }
        mBarDataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        //转换柱状图的值格式
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value+"票";
            }
        });
        barDataSet.setValueTextSize(20);
        //设置柱状图的值显示，如该值不设置为true，则不会显示
        barDataSet.setDrawValues(true);
        mBarDataSets.add(barDataSet);
    }

    private void showVoteResult() {
        if (mTvShowError.getVisibility() == View.VISIBLE){
            mTvShowError.setVisibility(View.GONE);
            mBarChart.setVisibility(View.VISIBLE);
            mTvShowResult.setVisibility(View.VISIBLE);
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
        mBarChart.getXAxis().setTextSize(20);
        //去掉左右两边的Y轴值
        mBarChart.getAxisLeft().setEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);
        //设置XY轴初始化的动画时间
        mBarChart.animateXY(2000, 2000);

        //刷新
        mBarChart.invalidate();
        //显示投票结果
        mTvShowResult.setText("投票结果："+mStringBuilder.toString());
    }

    private void showErrorMessage(String errorMessage){
        mBarChart.setVisibility(View.GONE);
        mTvShowResult.setVisibility(View.GONE);
        mTvShowError.setVisibility(View.VISIBLE);
        mTvShowError.setText(errorMessage);
    }
}
