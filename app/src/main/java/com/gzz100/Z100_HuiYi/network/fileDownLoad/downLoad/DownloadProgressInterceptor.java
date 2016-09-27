package com.gzz100.Z100_HuiYi.network.fileDownLoad.downLoad;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
/**
* 文件下载进度拦截器
* @author XieQXiong
* create at 2016/9/27 16:14
*/

public class DownloadProgressInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public DownloadProgressInterceptor(DownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadProgressResponseBody(originalResponse.body(), listener))
                .build();
    }
}
