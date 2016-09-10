package com.gzz100.Z100_HuiYi.network;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.fakeData.OneTitle;
import com.gzz100.Z100_HuiYi.network.entity.BaseResultEntity;
import com.gzz100.Z100_HuiYi.fakeData.TestResultEntity;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XieQXiong on 2016/9/1.
 */
public interface ApiService {
    /**
     * 获取文件列表
     * @param agendaIndex  议程序号
     * @return   文件列表
     */
    @GET("fileList")
    Observable<BaseResultEntity<List<Document>>> getFileList(@Query("agendaIndex")int agendaIndex);

    //测试用
    @GET("search")
    Observable<TestResultEntity<List<OneTitle>>> getTitles(@Query("q") String q,@Query("fields") String fields);

    /**
     * 获取用户
     * @param IMEI  设备标识码
     * @return   用户
     */
    @POST("selectUser")
    Observable<BaseResultEntity<UserBean>> getUser(@Field("IMEI") String IMEI);

    @POST("login")
    Observable<BaseResultEntity<List<Agenda>>> login(@Field("IMEI") String IMEI, @Field("userId") String userId);

    @GET("getMeetingInfo")
    Observable<BaseResultEntity<MeetingInfo>> getMeetingInfo();


}
