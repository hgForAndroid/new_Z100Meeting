package com.gzz100.Z100_HuiYi.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.gzz100.Z100_HuiYi.network.entity.BaseEntity;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieQXiong on 2016/9/1.
 */
public class HttpManager {
    private static final int DEFAULT_TIMEOUT = 6;
    private ApiService mApiService;
    private volatile static HttpManager INSTANCE;

    private Context mContext;

    public HttpManager(Context context) {
        mContext = context;
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getServerIP())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }
    //获取单例
    public static HttpManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(BaseEntity basePar) {
        Observable observable = basePar.getObservable(mApiService)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(basePar);
        observable.subscribe(basePar.getSubscriber());
    }

    private String getServerIP(){
        Log.e("xqx"," ======================"+SharedPreferencesUtil.getInstance(mContext).getString(Constant.CURRENT_IP,""));
        return "http://"+SharedPreferencesUtil.getInstance(mContext).getString(Constant.CURRENT_IP,"");
    }

}
