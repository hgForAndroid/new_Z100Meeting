package com.gzz100.Z100_HuiYi.network;

import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.fakeData.OneTitle;
import com.gzz100.Z100_HuiYi.network.entity.BaseResultEntity;
import com.gzz100.Z100_HuiYi.fakeData.TestResultEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XieQXiong on 2016/9/1.
 */
public interface ApiService {
    @GET("fileList")
    Observable<BaseResultEntity<List<Document>>> getFileList(@Query("agendaIndex")int agendaIndex);

    @GET("search")
    Observable<TestResultEntity<List<OneTitle>>> getTitles(@Query("q") String q,@Query("fields") String fields);
}
