package com.gzz100.Z100_HuiYi.network.entity;
/**
* 回调信息统一封装类
* @author XieQXiong
* create at 2016/9/1 15:28
*/


public class BaseResultEntity<T> {
    //  判断标示
    private int code;
    //    提示信息
    private String msg;
    //显示数据（用户需要关心的数据）
    private T result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
