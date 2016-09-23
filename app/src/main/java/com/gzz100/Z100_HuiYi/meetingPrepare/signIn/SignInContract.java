package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.DelegateBean;

/**
 * Created by XieQXiong on 2016/9/23.
 */
public interface SignInContract {
    interface View extends BaseView<Presenter>{
        /**
         * 显示参会人员信息
         * @param delegate  参会人员实体类
         */
        void showDelegate(DelegateBean delegate);
        void showNoDelegate();
        /**
         * 前往主界面，这里还需要参数，未定
         */
        void showMainActivity();

    }

    interface Presenter extends BasePresenter{
        /**
         * 获取当前设备对应的当前参会人员
         * @param IMEI        设备id
         * @param meetingID   会议id
         */
        void fetchCurrentDelegate(String IMEI, String meetingID);

        /**
         * 调用   SignInContract.View  的  showMainActivity
         * 跳转到主界面，这里需要取得会议全部信息后再跳转
         */
        void showMainActivity();

    }
}
