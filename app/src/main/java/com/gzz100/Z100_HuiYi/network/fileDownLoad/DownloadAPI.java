package com.gzz100.Z100_HuiYi.network.fileDownLoad;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
/**
 * 下载文件的接口
* @author XieQXiong
* create at 2016/9/27 16:36
*/


public interface DownloadApi {
    
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
