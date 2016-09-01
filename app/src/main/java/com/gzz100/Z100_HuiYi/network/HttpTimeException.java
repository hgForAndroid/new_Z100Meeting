package com.gzz100.Z100_HuiYi.network;
/**
* 自定义错误信息，统一处理返回处理
* @author XieQXiong
* create at 2016/9/1 15:29
*/


public class HttpTimeException extends RuntimeException {

    public static final int NO_DATA = 0;

    public HttpTimeException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 转换错误数据
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case NO_DATA:
                message = "无数据";
                break;
            default:
                message = "error";
                break;

        }
        return message;
    }
}

