package com.gzz100.Z100_HuiYi.meeting;

import android.content.Context;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.data.RepositoryUtil;
import com.gzz100.Z100_HuiYi.data.meeting.MeetingDataSource;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;
import com.gzz100.Z100_HuiYi.tcpController.ControllerUtil;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

/**
 * Created by XieQXiong on 2016/11/7.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mMainView;
    private Context mContext;
    private final Gson mGson;

    public MainPresenter(MainContract.View mainView, Context context) {
        mMainView = mainView;
        mContext = context;
        mMainView.setPresenter(this);
        mGson = new Gson();
    }

    @Override
    public void handleControllerInfoBean(ControllerInfoBean controllerInfoBean) {
        int agendaIndex = controllerInfoBean.getAgendaIndex();
        int documentIndex = controllerInfoBean.getDocumentIndex();
        String upLevelTitle = controllerInfoBean.getUpLevelTitle();
        //开始
        if (controllerInfoBean.getMeetingState() == Constant.MEETING_STATE_BEGIN) {
            if (controllerInfoBean.isVoteBegin()) {//开会开始，并且发起的是投票
                //该值在投票开始时为true
                SharedPreferencesUtil.getInstance(mContext).putBoolean(Constant.IS_VOTE_BEGIN, true);
                //存储投票id
                SharedPreferencesUtil.getInstance(mContext).putInt(Constant.BEGIN_VOTE_ID, controllerInfoBean.getVoteId());

                mMainView.clientResponseMeetingVote(controllerInfoBean.getVoteId());

            } else {

                //该值在投票开始时为true
                SharedPreferencesUtil.getInstance(mContext).putBoolean(Constant.IS_VOTE_BEGIN, false);
                SharedPreferencesUtil.getInstance(mContext).remove(Constant.BEGIN_VOTE_ID);
                mMainView.clientResponseMeetingBegin(agendaIndex, documentIndex, "文件");
            }
        }
        //继续
        else if (controllerInfoBean.getMeetingState() == Constant.MEETING_STATE_CONTINUE) {

            //结束投票，将该值重置为false，该值只有正在投票才为true
            SharedPreferencesUtil.getInstance(mContext).putBoolean(Constant.IS_VOTE_BEGIN, false);
            SharedPreferencesUtil.getInstance(mContext).putInt(Constant.BEGIN_VOTE_ID, -1);

            mMainView.clientResponseMeetingContinue(agendaIndex, documentIndex, "文件", controllerInfoBean.getCountdingMin(),
                    controllerInfoBean.getCountdingSec());

        }
        //暂停
        else if (controllerInfoBean.getMeetingState() == Constant.MEETING_STATE_PAUSE) {
            mMainView.clientResponseMeetingPause();
        } else if (controllerInfoBean.getMeetingState() == Constant.MEETING_STATE_ENDING) {
            SharedPreferencesUtil.getInstance(mContext).putBoolean(Constant.IS_MEETING_END, true);
            mMainView.clientResponseMeetingEnd();
        }

    }

    @Override
    public void hostStartMeeting(ControllerInfoBean mControllerInfoBean, int meetingState) {
        try {
            ControllerInfoBean controllerInfoBean = mControllerInfoBean.clone();
            controllerInfoBean.setMeetingState(meetingState);
            controllerInfoBean.setAgendaIndex(1);
            controllerInfoBean.setDocumentIndex(0);
            controllerInfoBean.setUpLevelTitle("文件");
            String json = mGson.toJson(controllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);
            //主持人跳转到文件详情界面
            mMainView.hostResponseMeetingBegin(controllerInfoBean.getAgendaIndex(),
                    controllerInfoBean.getDocumentIndex(), controllerInfoBean.getUpLevelTitle());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void hostStartEndMeeting(String IMEI, int meetingId) {
        RepositoryUtil.getMeetingRepository(mContext).endMeeting(IMEI, meetingId,
                new MeetingDataSource.EndMeetingCallback() {
                    @Override
                    public void onEndMeetingSuccess() {
                        mMainView.hostEndMeetingSuccess();
                    }
                    @Override
                    public void onEndMeetingFail(String errorMsg) {
                        ToastUtil.showMessage(errorMsg);
                    }
                });
    }

    @Override
    public void hostEndMeeting(ControllerInfoBean mControllerInfoBean, int meetingState) {
        try {
            ControllerInfoBean controllerInfoBean = mControllerInfoBean.clone();
            controllerInfoBean.setMeetingState(meetingState);
            String json = mGson.toJson(controllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);
            mMainView.hostResponseMeetingEnd();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hostPauseMeeting(ControllerInfoBean mControllerInfoBean, int meetingState) {
        try {
            ControllerInfoBean controllerInfoBean = mControllerInfoBean.clone();
            controllerInfoBean.setMeetingState(meetingState);
            String json = mGson.toJson(controllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);
            mMainView.hostResponseMeetingPause();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hostContinueMeeting(ControllerInfoBean mControllerInfoBean, int meetingState) {
        String tempCountingMin = SharedPreferencesUtil.getInstance(mContext)
                .getString(Constant.COUNTING_MIN, "");
        String tempCountingSec = SharedPreferencesUtil.getInstance(mContext)
                .getString(Constant.COUNTING_SEC, "");
        int pauseAgendaIndex = SharedPreferencesUtil.getInstance(mContext)
                .getInt(Constant.PAUSE_AGENDA_INDEX, 0);
        int pauseDocumentIndex = SharedPreferencesUtil.getInstance(mContext)
                .getInt(Constant.PAUSE_DOCUMENT_INDEX, 0);
        try {
            ControllerInfoBean controllerInfoBean = mControllerInfoBean.clone();
            controllerInfoBean.setMeetingState(meetingState);
            controllerInfoBean.setAgendaIndex(pauseAgendaIndex);
            controllerInfoBean.setDocumentIndex(pauseDocumentIndex);
            controllerInfoBean.setCountdingMin(tempCountingMin);
            controllerInfoBean.setCountdingSec(tempCountingSec);
            controllerInfoBean.setAgendaChange(false);
            controllerInfoBean.setAgendaTimeCountDown(true);
            controllerInfoBean.setUpLevelTitle("文件");
            String json = mGson.toJson(controllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);

            mMainView.hostResponseMeetingContinue(pauseAgendaIndex, pauseDocumentIndex,
                    controllerInfoBean.getUpLevelTitle(), tempCountingMin, tempCountingSec);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void hostEndVote(ControllerInfoBean mControllerInfoBean, int meetingState) {

        String tempCountingMin = SharedPreferencesUtil.getInstance(this.mContext)
                .getString(Constant.COUNTING_MIN, "");
        String tempCountingSec = SharedPreferencesUtil.getInstance(this.mContext)
                .getString(Constant.COUNTING_SEC, "");
        int pauseAgendaIndex = SharedPreferencesUtil.getInstance(this.mContext)
                .getInt(Constant.PAUSE_AGENDA_INDEX, -1);
        int pauseDocumentIndex = SharedPreferencesUtil.getInstance(this.mContext)
                .getInt(Constant.PAUSE_DOCUMENT_INDEX, -1);
        String json = null;
        try {
            ControllerInfoBean controllerInfoBean = mControllerInfoBean.clone();
            if (pauseAgendaIndex == -1) {
                //启动后没有先开启过议程，则没有倒计时的时间，结束投票需要从第一个议程开始
                controllerInfoBean.setMeetingState(meetingState);
                controllerInfoBean.setAgendaIndex(1);
                controllerInfoBean.setDocumentIndex(0);
                controllerInfoBean.setUpLevelTitle("文件");
                json = mGson.toJson(controllerInfoBean);

                mMainView.hostResponseEndVoteWithoutStart(controllerInfoBean.getAgendaIndex(),
                        controllerInfoBean.getDocumentIndex(), controllerInfoBean.getUpLevelTitle());
            } else {
                controllerInfoBean.setMeetingState(meetingState);
                controllerInfoBean.setAgendaIndex(pauseAgendaIndex);
                controllerInfoBean.setDocumentIndex(pauseDocumentIndex);
                controllerInfoBean.setCountdingMin(tempCountingMin);
                controllerInfoBean.setCountdingSec(tempCountingSec);
                controllerInfoBean.setAgendaChange(false);
                controllerInfoBean.setAgendaTimeCountDown(true);
                controllerInfoBean.setUpLevelTitle("文件");
                controllerInfoBean.setVoteBegin(false);
                json = mGson.toJson(controllerInfoBean);

                mMainView.hostResponseEndVoteAlreadyStart(controllerInfoBean.getAgendaIndex(),
                        controllerInfoBean.getDocumentIndex(), controllerInfoBean.getUpLevelTitle(),
                        controllerInfoBean.getCountdingMin(), controllerInfoBean.getCountdingSec());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        //结束投票，将该值重置为false，该值只有正在投票才为true
        SharedPreferencesUtil.getInstance(mContext).putBoolean(Constant.IS_VOTE_BEGIN, false);
        SharedPreferencesUtil.getInstance(mContext).remove(Constant.BEGIN_VOTE_ID);
        ControllerUtil.getInstance().sendMessage(json);
        mMainView.hostResponseVoteEnd();
    }

    @Override
    public void hostLaunchOrCloseVote(String IMEI, int meetingId, final int voteId, final int startOrEnd,
                                      final ControllerInfoBean mControllerInfoBean, final int meetingState) {
        RepositoryUtil.getVoteRepository(mContext).startOrEndVote(IMEI, meetingId, voteId, startOrEnd,
                new VoteDataSource.SubmitCallback() {
                    @Override
                    public void onSuccess() {
                        //开启投票成功
                        if (startOrEnd == 0){
                            //该值在投票开始时为true
                            SharedPreferencesUtil.getInstance(mContext).putBoolean(Constant.IS_VOTE_BEGIN,true);
                            //存储投票id
                            SharedPreferencesUtil.getInstance(mContext).putInt(Constant.BEGIN_VOTE_ID, voteId);
                            mMainView.hostResponseLaunchVoteSuccess();//开启投票成功

                            //发送消息给客户端
                            try {
                                ControllerInfoBean controllerInfoBean = mControllerInfoBean.clone();
                                controllerInfoBean.setMeetingState(meetingState);
                                controllerInfoBean.setVoteBegin(true);
                                controllerInfoBean.setVoteId(voteId);
                                String json = mGson.toJson(controllerInfoBean);
                                ControllerUtil.getInstance().sendMessage(json);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }

                        }else if (startOrEnd == -1){//关闭投票成功
                            mMainView.hostResponseCloseVoteSuccess();
                        }
                    }

                    @Override
                    public void onFail() {
                        mMainView.hostResponseLaunchOrCloseVoteFail(startOrEnd);
                    }
                });
    }

    @Override
    public void start() {

    }
}
