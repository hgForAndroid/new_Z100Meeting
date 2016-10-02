package com.gzz100.Z100_HuiYi;

import android.app.Application;

import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

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

    /**
     * 获取当前用户角色类型
     * @return   角色类型  ，  1为主持人，2为听众（普通参会人员）
     */
    public int getUserRole(){
        return SharedPreferencesUtil.getInstance(mInstance).getInt(Constant.USER_ROLE,2);
    }
}
