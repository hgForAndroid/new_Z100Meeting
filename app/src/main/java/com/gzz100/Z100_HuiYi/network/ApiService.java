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
    Observable<BaseResultEntity<String>> startMeeting(@Query("meetingID") int meetingID,@Query("IMEI") String IMEI);

    @GET("api/Host/EndMeeting")
    Observable<BaseResultEntity<String>> endMeeting(@Query("IMEI") String IMEI,@Query("meetingID") int meetingID);

    /**
     * 验证用户，当前设备对应的用户
     * @param IMEI          设备id
     * @param meetingID     会议id
     * @return
     */
    @GET("/api/Common/CheckUser")
    Observable<BaseResultEntity<UserBean>> checkUser(@Query("IMEI") String IMEI,@Query("meetingID") int meetingID);

    /**
     * 签到
     * @param IMEI          设备id
     * @param meetingID     会议id
     * @return
     */
    @GET("/api/Common/SignIn")
    Observable<BaseResultEntity<MeetingSummaryBean>> signIn(@Query("IMEI") String IMEI, @Query("meetingID") int meetingID);

    /**
     * 获取投票结果
     * @param voteId    投票id
     * @return
     */
    @GET("/api/Common/FetchVoteResult")
    Observable<BaseResultEntity<List<VoteResult>>> fetchVoteResult(@Query("voteID") int voteId);

    /**
     * 上传投票结果，该方法为POST方法，使用的参数是   @Body UpLoadVote data
     * @param data   UpLoadVote对象
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("api/Common/UploadVoteResult")
//    @FormUrlEncoded
    Observable<BaseResultEntity<String>> uploadVoteResult(@Body UpLoadVote data);

    /**
     * 开启或结束投票
     * @param IMEI              设备id
     * @param meetingID         会议id
     * @param voteID            投票id
     * @param startOrEnd        0：开启投票，  -1：结束投票
     * @return
     */
    @GET("/api/Host/StartOrEndVote")
    Observable<BaseResultEntity<String>> startOrEndVote(@Query("IMEI")String IMEI,
              @Query("meetingID") int meetingID, @Query("voteID") int voteID, @Query("startOrEnd") int startOrEnd);

    /**
     * 检查更新
     * @param IMEI          设备id
     * @param meetingID     会议id
     * @return
     */
    @GET("api/Host/CheckUpdate")
    Observable<BaseResultEntity<Update>> checkUpdate(@Query("IMEI")String IMEI,@Query("meetingID")int meetingID);
}
