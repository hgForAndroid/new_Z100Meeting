package com.gzz100.Z100_HuiYi.data.signIn;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/9/23.
 */
public class SignInRemoteDataSource implements SignInDataSource {
    private static SignInRemoteDataSource mInstance;
    private SignInRemoteDataSource() {
    }
    public static SignInRemoteDataSource getInstance(){
        if (mInstance == null){
            synchronized (SignInRemoteDataSource.class){
                if (mInstance == null) {
                    mInstance = new SignInRemoteDataSource();
                }
            }
        }
        return mInstance;
    }
    @Override
    public void fetchDelegate(String IMEI, String meetingID, @NonNull LoadDelegateCallback callback) {
        checkNotNull(callback);
//        callback.onDataNotAvailable();
        
    }
}
