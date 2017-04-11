package com.gzz100.Z100_HuiYi.meeting.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.meeting.NavBarView;
import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    public static void startAboutActivity(Context context){
        context.startActivity(new Intent(context,AboutActivity.class));
    }

    @BindView(R.id.id_activity_about_nbv)
    NavBarView mNavBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        mNavBarView.setFallBackDisplay(true);
        mNavBarView.setTimeAndStateDisplay(false);
        mNavBarView.setTitleWithSpace("关于");
        mNavBarView.setFallBackListener(this);
        mNavBarView.setMeetingAndTimeDisplay(false);
    }

    @Override
    public void onClick(View v) {
        //返回上一级
        ActivityStackManager.pop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN){
            ActivityStackManager.pop();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
