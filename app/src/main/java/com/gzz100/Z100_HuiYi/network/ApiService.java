package com.gzz100.Z100_HuiYi.network;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
import com.gzz100.Z100_HuiYi.data.MeetingSummary;
import com.gzz100.Z100_HuiYi.data.MeetingSummaryBean;
import com.gzz100.Z100_HuiYi.data.Update;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.fakeData.OneTitle;
import com.gzz100.Z100_HuiYi.network.entity.BaseResultEntity;
import com.gzz100.Z100_HuiYi.fakeData.TestResultEntity;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
     * 获取会议列表
     * @param IMEI    设备id
     * @return    会议列表
     */
    @GET("/api/Common/RequestMeetingList")
    Observable<BaseResultEntity<List<MeetingBean>>> getMeetingList(@Query("IMEI") String IMEI);

    /**
     * 开启会议
     * @param meetingID     会议id
     * @param IMEI          设备id
     * @return
     */
    @GET("/api/Host/StartMeeting")
    Observable<BaseResultEntity<String>> startMeeting(@Query("meetingID") String meetingID,@Query("IMEI") String IMEI);

//    @FormUrlEncoded
    @GET("/api/Common/CheckUser")
    Observable<BaseResultEntity<UserBean>> checkUser(@Query("IMEI") String IMEI,@Query("meetingID") String meetingID);

    @GET("/CheckUpdate")
    Observable<BaseResultEntity<Update>> checkUpdate(@Query("meetingID") String meetingID);

    @GET("/api/Common/SignIn")
    Observable<BaseResultEntity<MeetingSummaryBean>> signIn(@Query("IMEI") String IMEI, @Query("meetingID") String meetingID);

    @GET("login")
    Observable<BaseResultEntity<List<Agenda>>> login(@Query("IMEI") String IMEI, @Query("userId") String userId);

    @GET("getMeetingInfo")
    Observable<BaseResultEntity<MeetingInfo>> getMeetingInfo();


}
