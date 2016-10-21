package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.view.View;

import com.gzz100.Z100_HuiYi.data.file.FileDetailRepository;
import com.gzz100.Z100_HuiYi.multicast.MulticastBean;
import com.gzz100.Z100_HuiYi.multicast.MulticastController;
import com.gzz100.Z100_HuiYi.utils.Constant;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/8/30.
 */
public class FileDetailPresenter implements FileDetailContract.Presenter {
    private FileDetailRepository mRepository;
    private FileDetailContract.DetailView mView;

    public FileDetailPresenter(FileDetailRepository repository, FileDetailContract.DetailView view) {
//        this.mRepository = checkNotNull(repository);
        this.mView = checkNotNull(view);
        this.mView.setPresenter(this);

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
    public void begin(final MulticastBean multicastBean, final int meetingState, final int agendaIndex, final int DocumentIndex, final String upLevelText) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastBean mMulticastBean = multicastBean.clone();
                    mMulticastBean.setMeetingState(meetingState);
                    mMulticastBean.setAgendaIndex(agendaIndex);
                    mMulticastBean.setDocumentIndex(DocumentIndex);
                    mMulticastBean.setUpLevelTitle(upLevelText);
                    MulticastController.getDefault().sendMulticastBean(mMulticastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mView.meetingBegin();
    }

    @Override
    public void ending(final MulticastBean multicastBean, final int meetingState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastBean mMulticastBean = multicastBean.clone();
                    mMulticastBean.setMeetingState(meetingState);
                    MulticastController.getDefault().sendMulticastBean(mMulticastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mView.meetingEnding();
    }

    @Override
    public void pause(final MulticastBean multicastBean, final int meetingState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastBean mMulticastBean = multicastBean.clone();
                    mMulticastBean.setMeetingState(meetingState);
                    MulticastController.getDefault().sendMulticastBean(mMulticastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mView.meetingPause();
    }

    @Override
    public void meetingContinue(final MulticastBean multicastBean, final int meetingState, final int agendaIndex, final int DocumentIndex, final String upLevelText) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastBean mMulticastBean = multicastBean.clone();
                    mMulticastBean.setMeetingState(meetingState);
                    mMulticastBean.setAgendaIndex(agendaIndex);
                    mMulticastBean.setDocumentIndex(DocumentIndex);
                    mMulticastBean.setUpLevelTitle(upLevelText);
                    MulticastController.getDefault().sendMulticastBean(mMulticastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mView.meetingContinue();
    }

    @Override
    public void previousAgenda(final MulticastBean multicastBean, final int meetingState, int agendaIndex) {
        agendaIndex -= 1;
        mView.resetAgendaTimeCounting(agendaIndex);
        mView.resetAgendaContent(agendaIndex);
        final int finalAgendaIndex = agendaIndex;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastBean mMulticastBean = multicastBean.clone();
                    //已开始  2
                    mMulticastBean.setMeetingState(meetingState);
                    mMulticastBean.setAgendaIndex(finalAgendaIndex);
                    MulticastController.getDefault().sendMulticastBean(mMulticastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void nextAgenda(final MulticastBean multicastBean, final int meetingState, int agendaIndex) {
        agendaIndex += 1;
        mView.resetAgendaTimeCounting(agendaIndex);
        mView.resetAgendaContent(agendaIndex);
        final int finalAgendaIndex = agendaIndex;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastBean mMulticastBean = multicastBean.clone();
                    mMulticastBean.setMeetingState(meetingState);
                    mMulticastBean.setAgendaIndex(finalAgendaIndex);
                    MulticastController.getDefault().sendMulticastBean(mMulticastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void handleMessageFromHost(MulticastBean data) {

        //会议开始状态才响应
        if (data.getMeetingState() == Constant.MEETING_STATE_BEGIN) {
            if (data.getAgendaIndex() > 0) {
                mView.respondAgendaIndexChange(data.getAgendaIndex());
            }
            if (data.getDocumentIndex() >= 0) {
                mView.respondDocumentIndexChange(data.getDocumentIndex());
            }
            mView.respondMeetingBegin();
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

            if (data.getAgendaIndex() > 0) {
                mView.respondAgendaIndexChange(data.getAgendaIndex());
            }
            if (data.getDocumentIndex() >= 0) {
                mView.respondDocumentIndexChange(data.getDocumentIndex());
            }
            mView.respondMeetingContinue();
        }

    }

    @Override
    public void handleFileClickFromHost(final MulticastBean mMulticastBean, final int meetingState,
                                        final int agendaIndex, final int documentPosition) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MulticastBean multicastBean = mMulticastBean.clone();
                    ///如果是继续状态，将状态改为开始，为了重新开始计时不混乱
                    if (meetingState == Constant.MEETING_STATE_CONTINUE) {
                        mView.setMeetingState(Constant.MEETING_STATE_BEGIN);
                        multicastBean.setMeetingState(Constant.MEETING_STATE_BEGIN);
                        //这里设置议程序号为0.切换文件时，忽略切换议程
                        multicastBean.setAgendaIndex(agendaIndex);
                        multicastBean.setDocumentIndex(documentPosition);
                    } else {
                        multicastBean.setMeetingState(meetingState);
                        //这里设置议程序号为0.切换文件时，忽略切换议程
                        multicastBean.setAgendaIndex(0);
                        multicastBean.setDocumentIndex(documentPosition);
                    }
                    MulticastController.getDefault().sendMulticastBean(multicastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
