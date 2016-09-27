package com.gzz100.Z100_HuiYi.meetingPrepare.signIn;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.signIn.SignInDataSource;
import com.gzz100.Z100_HuiYi.data.signIn.SignInRemoteDataSource;

/**
 * Created by XieQXiong on 2016/9/23.
 */
public class SignInPresenter implements SignInContract.Presenter {
    private SignInContract.View mView;

    public SignInPresenter(SignInContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    private boolean isFirst = true;
    @Override
    public void fetchCurrentDelegate(boolean fourUpdate,String IMEI, String meetingID) {
        if (isFirst || fourUpdate){
            SignInRemoteDataSource.getInstance().fetchDelegate(IMEI, meetingID, new SignInDataSource.LoadDelegateCallback() {
                @Override
                public void onDelegateLoaded(DelegateBean delegate) {
                    mView.showDelegate(delegate);
                }

                @Override
                public void onDataNotAvailable() {
                    mView.showNoDelegate();
                }
            });
        }

    }

    @Override
    public void showMainActivity() {
        //这里需要签到获取数据，获取的数据格式等服务器
        mView.showMainActivity();
    }

    @Override
    public void start() {
        //这里开始取数据需要传递参数，所以这个方法先不采用，直接调用  fetchCurrentDelegate
    }
}
