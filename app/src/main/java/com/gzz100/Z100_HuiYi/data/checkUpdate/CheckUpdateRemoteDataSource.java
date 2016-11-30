package com.gzz100.Z100_HuiYi.data.checkUpdate;

import android.content.Context;

import com.gzz100.Z100_HuiYi.data.Update;
import com.gzz100.Z100_HuiYi.network.HttpManager;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.MySubscriber;
import com.gzz100.Z100_HuiYi.network.entity.UpdatePost;

import org.jetbrains.annotations.NotNull;

/**
 * 描述：
 * 包名：com.gzz100.Z100_HuiYi.data.checkUpdate
 * 类名：CheckUpdateRemoteDataSource
 *
 * @author XieQingXiong
 * @创建时间 2016/11/30   9:51
 */
public class CheckUpdateRemoteDataSource implements CheckUpdateDataSource{
    private static CheckUpdateRemoteDataSource instance = null;
    private Context mContext;

    private CheckUpdateRemoteDataSource(Context context){
        this.mContext = context;
    }

    public static CheckUpdateRemoteDataSource getInstance(Context context) {
        if (instance == null){
            synchronized (CheckUpdateRemoteDataSource.class) {
                if (instance == null) {
                    instance = new CheckUpdateRemoteDataSource(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @Override
    public void checkUpdate(String IMEI, int meetingID, @NotNull final CheckUpdateCallback callback) {
        UpdatePost updatePost = new UpdatePost(
                new MySubscriber(new HttpRxCallbackListener<Update>() {
                    @Override
                    public void onNext(Update update) {
                        int isUpdate = update.getIsUpdate();
                        if (isUpdate == 1){
                            callback.update();
                        }else {
                            callback.notUpdate();
                        }
                    }

                    @Override
                    public void onError(String errorMsg) {
                        callback.notUpdate();
                    }
                }, mContext),IMEI,meetingID);
        HttpManager.getInstance(mContext).doHttpDeal(updatePost);
    }

    @Override
    public void fetchUpdateData(String IMEI, int meetingID, @NotNull LoadUpdateDataCallback callback) {
        //获取最新数据
    }
}
