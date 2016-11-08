package com.gzz100.Z100_HuiYi.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 无加载框的Subscriber
 * 处理onError以及onNext
 */
public class MySubscriber<T> extends Subscriber<T> {
    //    回调接口
    private HttpRxCallbackListener mSubscriberOnNextListener;
    //    弱引用防止内存泄露
    private WeakReference<Context> mActivity;

    public MySubscriber(HttpRxCallbackListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mActivity = new WeakReference<>(context);
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {

    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Context context = mActivity.get();
        String errorMsg;
        if (context == null) return;
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
            errorMsg = "连接超时，请检查网络";
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            errorMsg = "网络中断，请检查您的网络状态";
        } else {
            Toast.makeText(context, "错误 ====== " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("tag", "error----------->" + e.toString());
            errorMsg = e.getMessage();
//            errorMsg = "服务器出错，请检查服务器ip是否正确！";
        }
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(errorMsg);
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null && t != null) {
            mSubscriberOnNextListener.onNext(t);
        }else {
            Log.e("onNext中的T  ==== ","null");
            mSubscriberOnNextListener.onError("返回为null,请联系服务器人员");
        }
    }
}