package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.view.View;

import com.google.gson.Gson;
import com.gzz100.Z100_HuiYi.data.file.FileDetailRepository;
import com.gzz100.Z100_HuiYi.tcpController.ControllerInfoBean;
import com.gzz100.Z100_HuiYi.tcpController.ControllerUtil;
import com.gzz100.Z100_HuiYi.utils.Constant;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/8/30.
 */
public class FileDetailPresenter implements FileDetailContract.Presenter {
    private FileDetailRepository mRepository;
    private FileDetailContract.DetailView mView;
    private final Gson mGson;

    public FileDetailPresenter(FileDetailRepository repository, FileDetailContract.DetailView view) {
//        this.mRepository = checkNotNull(repository);
        this.mView = checkNotNull(view);
        this.mView.setPresenter(this);

        mGson = new Gson();
    }

    @Override
    public void fallback() {
        mView.fallback();
    }

    @Override
    public void slideLeft(View v) {
        mView.slideLeft(0);

    }

    @Override
    public void slideRight(View v) {
        mView.slideRight(0);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadFile(String fileName) {
//        java.io.File file = new java.io.File(Environment.getExternalStorageDirectory().getPath()
//                + "/" +  fileName + ".pdf");
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//        mView.loadFile(file);
    }

    @Override
    public void begin(final ControllerInfoBean controllerInfoBean, final int meetingState,
                      final int agendaIndex, final int DocumentIndex, final String upLevelText) {
        try {
            ControllerInfoBean mControllerInfoBean = controllerInfoBean.clone();
            mControllerInfoBean.setMeetingState(meetingState);
            mControllerInfoBean.setAgendaIndex(agendaIndex);
            mControllerInfoBean.setDocumentIndex(DocumentIndex);
            mControllerInfoBean.setUpLevelTitle(upLevelText);

            String json = mGson.toJson(mControllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mView.meetingBegin();
    }

    @Override
    public void ending(final ControllerInfoBean controllerInfoBean, final int meetingState) {
        try {
            ControllerInfoBean mControllerInfoBean = controllerInfoBean.clone();
            mControllerInfoBean.setMeetingState(meetingState);

            String json = mGson.toJson(mControllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mView.meetingEnding();
    }

    @Override
    public void pause(final ControllerInfoBean controllerInfoBean, final int meetingState) {
        try {
            ControllerInfoBean mControllerInfoBean = controllerInfoBean.clone();
            mControllerInfoBean.setMeetingState(meetingState);

            String json = mGson.toJson(mControllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mView.meetingPause();
    }

    @Override
    public void meetingContinue(final ControllerInfoBean controllerInfoBean, final int meetingState,
                                final int agendaIndex, final int DocumentIndex, final String upLevelText,
                                boolean isAgendaChange) {
        try {
            ControllerInfoBean mControllerInfoBean = controllerInfoBean.clone();
            mControllerInfoBean.setMeetingState(meetingState);
            mControllerInfoBean.setAgendaIndex(agendaIndex);
            mControllerInfoBean.setDocumentIndex(DocumentIndex);
            mControllerInfoBean.setUpLevelTitle(upLevelText);
            mControllerInfoBean.setAgendaChange(isAgendaChange);

            String json = mGson.toJson(mControllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mView.meetingContinue();
    }

    @Override
    public void previousAgendaForHost(final ControllerInfoBean controllerInfoBean, final int meetingState, int agendaIndex) {
        agendaIndex -= 1;
        mView.resetAgendaTimeCounting(agendaIndex);
        mView.resetAgendaContent(agendaIndex);
        final int finalAgendaIndex = agendaIndex;
        try {
            ControllerInfoBean mControllerInfoBean = controllerInfoBean.clone();
            //已开始  2
            mControllerInfoBean.setMeetingState(meetingState);
            mControllerInfoBean.setAgendaIndex(finalAgendaIndex);
            mControllerInfoBean.setDocumentIndex(0);
            mControllerInfoBean.setAgendaChange(true);

            String json = mGson.toJson(mControllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextAgendaForHost(final ControllerInfoBean controllerInfoBean, final int meetingState, int agendaIndex) {
        agendaIndex += 1;
        mView.resetAgendaTimeCounting(agendaIndex);
        mView.resetAgendaContent(agendaIndex);
        final int finalAgendaIndex = agendaIndex;
        try {
            ControllerInfoBean mControllerInfoBean = controllerInfoBean.clone();
            mControllerInfoBean.setMeetingState(meetingState);
            mControllerInfoBean.setAgendaIndex(finalAgendaIndex);
            mControllerInfoBean.setDocumentIndex(0);
            mControllerInfoBean.setAgendaChange(true);

            String json = mGson.toJson(mControllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void previousAgenda(int agendaIndex) {
        agendaIndex -= 1;
        mView.resetAgendaTimeCounting(agendaIndex);
        mView.resetAgendaContent(agendaIndex);
    }

    @Override
    public void nextAgenda(int agendaIndex) {
        agendaIndex += 1;
        mView.resetAgendaTimeCounting(agendaIndex);
        mView.resetAgendaContent(agendaIndex);
    }

    @Override
    public void handleMessageFromHost(ControllerInfoBean data) {

        //会议开始状态才响应
        if (data.getMeetingState() == Constant.MEETING_STATE_BEGIN) {
            mView.respondMeetingBegin(data.isAgendaChange());
            if (data.getAgendaIndex() > 0) {
                mView.respondAgendaIndexChange(data.getAgendaIndex());
            }
            if (data.getDocumentIndex() >= 0) {
                mView.respondDocumentIndexChange(data.getDocumentIndex());
            }

        }
        //会议暂停
        else if (data.getMeetingState() == Constant.MEETING_STATE_PAUSE) {
            mView.respondMeetingPause();
        }
        //会议结束
        else if (data.getMeetingState() == Constant.MEETING_STATE_ENDING) {
            mView.respondMeetingEnd();
        }
        //会议继续
        else if (data.getMeetingState() == Constant.MEETING_STATE_CONTINUE) {
            mView.respondMeetingContinue(data.isAgendaChange());

            if (data.getAgendaIndex() > 0) {
                mView.respondAgendaIndexChange(data.getAgendaIndex());
            }
            if (data.getDocumentIndex() >= 0) {
                mView.respondDocumentIndexChange(data.getDocumentIndex());
            }

        }

    }

    @Override
    public void handleFileClickFromHost(final ControllerInfoBean mControllerInfoBean, final int meetingState,
                                        final int agendaIndex, final int documentPosition) {
        try {
            ControllerInfoBean controllerInfoBean = mControllerInfoBean.clone();
            ///如果是继续状态，将状态改为开始，为了重新开始计时不混乱
            if (meetingState == Constant.MEETING_STATE_CONTINUE) {
                mView.setMeetingState(Constant.MEETING_STATE_BEGIN);
                controllerInfoBean.setMeetingState(Constant.MEETING_STATE_BEGIN);
                //这里设置议程序号为0.切换文件时，忽略切换议程
                controllerInfoBean.setAgendaIndex(agendaIndex);
                controllerInfoBean.setDocumentIndex(documentPosition);
            } else {
                controllerInfoBean.setMeetingState(meetingState);
                //这里设置议程序号为0.切换文件时，忽略切换议程
                controllerInfoBean.setAgendaIndex(0);
                controllerInfoBean.setDocumentIndex(documentPosition);
            }

            String json = mGson.toJson(controllerInfoBean);
            ControllerUtil.getInstance().sendMessage(json);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
