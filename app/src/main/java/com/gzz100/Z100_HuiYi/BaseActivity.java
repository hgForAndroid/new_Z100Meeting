package com.gzz100.Z100_HuiYi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gzz100.Z100_HuiYi.utils.ActivityStackManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.push(this);//加入Activity栈
//        setContentView(R.layout.activity_base);
    }

//    @Override
//    public void setContentView(int layoutResID) {
//        View mContentLayout = getLayoutInflater().inflate(R.layout.activity_base, null);
//        LinearLayout rootContent = (LinearLayout) mContentLayout.findViewById(R.id.id_base_linearlayout);
//        View contentView = getLayoutInflater().inflate(layoutResID, null);
//        //添加内容布局
//        if (null != contentView) {
//            rootContent.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        }
//        super.setContentView(mContentLayout);
//    }
}
