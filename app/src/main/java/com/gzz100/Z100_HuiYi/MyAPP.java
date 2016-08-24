package com.gzz100.Z100_HuiYi;

import android.app.Application;

/**
* 应用类
* @author XieQXiong
* create at 2016/8/22 16:32
*/

public class MyAPP extends Application {
    private static MyAPP mInstance;
    public static MyAPP getInstance(){
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
