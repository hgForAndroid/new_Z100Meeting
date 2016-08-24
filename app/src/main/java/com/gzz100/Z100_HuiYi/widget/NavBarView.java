package com.gzz100.Z100_HuiYi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    public TextView mMeetingState;
    public TextView mTvTime;

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
        mMeetingState = (TextView) findViewById(R.id.id_tv_navBar_state);
        mTvTime = (TextView) findViewById(R.id.id_tv_navBar_time);
    }

    public void setFallBackListener(OnClickListener listener){
        mLlFallBack.setOnClickListener(listener);
    }
}
