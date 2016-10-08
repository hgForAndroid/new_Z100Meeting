package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.os.Environment;
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
                    MulticastController.getDefault().sendMulticaseBean(mMulticastBean);
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
                    MulticastController.getDefault().sendMulticaseBean(mMulticastBean);
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
                    MulticastController.getDefault().sendMulticaseBean(mMulticastBean);
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
                    MulticastController.getDefault().sendMulticaseBean(mMulticastBean);
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
                    MulticastController.getDefault().sendMulticaseBean(mMulticastBean);
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
                    MulticastController.getDefault().sendMulticaseBean(mMulticastBean);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
