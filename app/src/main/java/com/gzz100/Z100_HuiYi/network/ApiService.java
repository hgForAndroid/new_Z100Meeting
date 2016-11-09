package com.gzz100.Z100_HuiYi.network;

import com.gzz100.Z100_HuiYi.data.MeetingBean;
import com.gzz100.Z100_HuiYi.data.MeetingSummaryBean;
import com.gzz100.Z100_HuiYi.data.Update;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.VoteResult;
import com.gzz100.Z100_HuiYi.meeting.vote.UpLoadVote;
import com.gzz100.Z100_HuiYi.network.entity.BaseResultEntity;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XieQXiong on 2016/9/1.
 */
public interface ApiService {
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

    @GET("/api/Common/FetchVoteResult")
    Observable<BaseResultEntity<List<VoteResult>>> fetchVoteResult(@Query("voteID") int voteId);

    @Headers("Content-Type: application/json")
    @POST("api/Common/UploadVoteResult")
//    @FormUrlEncoded
    Observable<BaseResultEntity<UpLoadVote>> uploadVoteResult(@Body UpLoadVote data);


    @GET("/api/Host/StartOrEndVote")
    Observable<BaseResultEntity<String>> startOrEndVote(@Query("IMEI")String IMEI,
              @Query("meetingID") String meetingID, @Query("voteID") int voteID, @Query("startOrEnd") int startOrEnd);


}
