package com.gzz100.Z100_HuiYi.network;
/**
* 成功回调处理
* @author XieQXiong
* create at 2016/9/1 15:28
*/

public interface HttpRxCallbackListener<T> {
    void onNext(T t);
    void onError(String errorMsg);
}
