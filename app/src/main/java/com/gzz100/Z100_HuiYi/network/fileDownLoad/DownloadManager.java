package com.gzz100.Z100_HuiYi.network.fileDownLoad;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gzz100.Z100_HuiYi.network.fileDownLoad.downLoad.DownloadProgressInterceptor;
import com.gzz100.Z100_HuiYi.network.fileDownLoad.downLoad.DownloadProgressListener;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 下载管理类
 * @author XieQXiong
 *         create at 2016/9/27 16:20
 */


public class DownloadManager {
    private static final String TAG = "DownloadManager";
    private static final int DEFAULT_TIMEOUT = 15;
    public Retrofit retrofit;


    public DownloadManager(String url, DownloadProgressListener listener) {

        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public void downloadFile(@NonNull String url, final File file, Subscriber subscriber) {
        if (Constant.DEBUG)
            Log.d(TAG, "downloadFile: " + url);

        retrofit.create(DownloadApi.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            FileUtils.writeFile(inputStream, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
