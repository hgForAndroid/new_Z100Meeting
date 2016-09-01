package com.gzz100.Z100_HuiYi.network.entity;

import com.gzz100.Z100_HuiYi.fakeData.TestResultEntity;
import com.gzz100.Z100_HuiYi.network.ApiService;
import com.gzz100.Z100_HuiYi.network.HttpTimeException;

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
        if (httpResult.getCode() == 0) {
            throw new HttpTimeException(httpResult.getMsg());
        }
        return httpResult.getResult();
    }

//
//    @Override
//    public T call(TestResultEntity<T> httpResult) {
//        if (httpResult.getCount() == 0) {
//            throw new HttpTimeException(httpResult.getCount());
//        }
//        return httpResult.getBooks();
//    }

}
