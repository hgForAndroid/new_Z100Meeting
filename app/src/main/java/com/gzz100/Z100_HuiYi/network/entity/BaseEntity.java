package com.gzz100.Z100_HuiYi.network.entity;

import android.util.Log;
import android.widget.Toast;

import com.gzz100.Z100_HuiYi.network.ApiService;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
* 请求数据统一封装类
* @author XieQXiong
* create at 2016/9/1 15:28
*/


public abstract class BaseEntity<T> implements Func1<BaseResultEntity<T>, T> {
    /**
     * 设置参数
     *
     * @param methods
     * @return
     */
    public abstract Observable getObservable(ApiService methods);

    /**
     * 设置回调sub
     *
     * @return
     */
    public abstract Subscriber getSubscriber();


    @Override
    public T call(BaseResultEntity<T> httpResult) {
        if (httpResult.getCode() == -1) {//如果取值不成功，调用该方法，之后
            // TODO: 2016/10/31  服务器返回码为-1，代表失败，显示错误信息，是否该仍异常？
            ToastUtil.showMessage(httpResult.getMsg(), Toast.LENGTH_LONG);
            Log.e("q请求码 ===== ",httpResult.getCode()+"   消息  ：："+httpResult.getMsg());
//            throw new HttpTimeException(httpResult.getMsg());
            return null;
        }else {
            return httpResult.getResult();
        }
    }

}
