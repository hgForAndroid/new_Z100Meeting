package com.gzz100.Z100_HuiYi.meeting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;

/**
* 标题栏View
* @author XieQXiong
* create at 2016/8/23 15:39
*/

public class NavBarView extends RelativeLayout {
    //返回，在个人信息，议程文件内才显示，默认为隐藏
    public LinearLayout mLlFallBack;
    //时间和状态，默认显示，在个人信息界面隐藏
    public LinearLayout mLlRight;
    public TextView mTvTitle;
    public TextView mStateOrAgenda;
    public TextView mTvTimeHour;
    public TextView mTvTimeMin;
    public TextView mTvUpLevel;
    public TextView mTvState;

    public NavBarView(Context context) {
        super(context);
        initChildView(context);
    }

    public NavBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChildView(context);
    }

    public NavBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildView(context);
    }

    private void initChildView(Context context) {
        View.inflate(context, R.layout.layout_nav_bar, this);
        mLlFallBack = (LinearLayout) findViewById(R.id.id_ll_navBar_fallBack);
        mLlRight = (LinearLayout) findViewById(R.id.id_ll_bavBar_right);
        mTvTitle = (TextView) findViewById(R.id.id_tv_navBar_title);
        mTvState = (TextView) findViewById(R.id.id_tv_navBar_meeting_state);
        mStateOrAgenda = (TextView) findViewById(R.id.id_tv_navBar_state);
        mTvTimeHour = (TextView) findViewById(R.id.id_tv_navBar_time_hour);
        mTvTimeMin = (TextView) findViewById(R.id.id_tv_navBar_time_min);
        mTvUpLevel = (TextView) findViewById(R.id.id_tv_up_level);
    }

    public void setFallBackListener(OnClickListener listener){
        mLlFallBack.setOnClickListener(listener);
    }

    /**
     * 导航栏返回按钮的显示，在 个人信息，议程文件详情 中应该显示
     * @param flag  true:显示   false:不显示
     */
    public void setFallBackDisplay(boolean flag){
        if (flag){
            mLlFallBack.setVisibility(View.VISIBLE);
        }else {
            mLlFallBack.setVisibility(View.GONE);
        }
    }
    /**
     * 右侧时间和会议状态的显示 ，在个人信息界面应该隐藏
     * @param flag  true:显示   false:不显示
     */
    public void setTimeAndStateDisplay(boolean flag){
        if (flag){
            mLlRight.setVisibility(View.VISIBLE);
        }else {
            mLlRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置导航栏标题
     * @param title  标题名称
     */
    public void setTitle(String title){
        mTvTitle.setText(title);
    }
    /**
     * 设置导航栏 右侧状态
     * @param state  主界面：状态  文件详情：第几个议程
     */
    public void setMeetingStateOrAgendaState(String state){
        mStateOrAgenda.setText(state);
    }

    public String getMeetingStateOrAgendaState(){
        return mStateOrAgenda.getText().toString();
    }
    /**
     * 设置导航栏 返回中上一级的标题
     * @param upLevelText  上一级标题
     */
    public void setUpLevelText(String upLevelText){
        mTvUpLevel.setText(upLevelText);
    }

    public void setTimeHour(String time){
        mTvTimeHour.setText(time);
    }

    public String getTimeHour() {
        return mTvTimeHour.getText().toString();
    }


    public void setTimeMin(String time){
        mTvTimeMin.setText(time);
    }
    public String getTimeMin() {
        return mTvTimeMin.getText().toString();
    }

    public void setCurrentMeetingState(String currentState){
        mTvState.setText(currentState);
        mTvState.setVisibility(View.VISIBLE);
    }
    public String getCurrentMeetingStateString(){
        return mTvState.getText().toString();
    }
}
