package com.gzz100.Z100_HuiYi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.push(this);//加入Activity栈
//        setContentView(R.layout.activity_base);
    }
    // 这里应该可以不用，之后需要注释测试
//    @Override
//    public void setContentView(int layoutResID) {
//        View mContentLayout = getLayoutInflater().inflate(R.layout.activity_base, null);
//        FrameLayout rootContent = (FrameLayout) mContentLayout.findViewById(R.id.id_fl_base_layout);
////        LinearLayout rootContent = (LinearLayout) mContentLayout.findViewById(R.id.id_base_layout);
//        View contentView = getLayoutInflater().inflate(layoutResID, null);
//        //添加内容布局
//        if (null != contentView) {
//            rootContent.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//        }
//
//        super.setContentView(mContentLayout);

//    }
}
