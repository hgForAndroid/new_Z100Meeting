package com.gzz100.Z100_HuiYi.meeting.agenda;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.source.FileDataSource;
import com.gzz100.Z100_HuiYi.data.source.FileRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lee on 2016/8/31.
 */
public class AgendaPresenter implements AgendaContract.Presenter{
    private FileRepository mFileRepository;
    private AgendaContract.View mAgendaView;

    private boolean mFirstLoad = true;

    public AgendaPresenter(@NonNull FileRepository fileRepository, @NonNull AgendaContract.View agendaView){
        mFileRepository = fileRepository;
        mAgendaView = agendaView;
        mAgendaView.setPresenter(this);
    }

    @Override
    public void start() {
        fetchAgendaList(false, "", "");
    }

    @Override
    public void fetchAgendaList(boolean forceUpdate, String IMEI, String userId) {
        if (forceUpdate || mFirstLoad){
//            mFirstLoad = false;
            mFileRepository.getAgendaList(IMEI, userId, new FileDataSource.LoadAgendaListCallback() {
                @Override
                public void onAgendaListLoaded(List<Agenda> agendas) {
                    if (!mAgendaView.isActive()) {
                        return;
                    }
                    mAgendaView.showAgendasList(agendas);
                }

                @Override
                public void onDataNotAvailable() {
                    mAgendaView.showNoAgendaList();

                }
            });

        }
    }

    @Override
    public void fetchFileListAndShow(boolean forceUpdate, int agendaPos) {
        mFirstLoad = false;
//            fileModel.getFileListByAgendaPos(agendaPos);
        mFileRepository.getFileList(agendaPos, new FileDataSource.LoadFileListCallback() {
            @Override
            public void onFileListLoaded(List<Document> documents) {

                if (!mAgendaView.isActive()) {
                    return;
                }
                mAgendaView.setFileList(documents);
                mAgendaView.showFileDetail();
            }

            @Override
            public void onDataNotAvailable() {
                mAgendaView.showNoFileList();

            }
        });
    }

    @Override
    public void showAgendaDetail(Agenda agenda) {
        mAgendaView.showAgendaDetail(agenda);
    }

    @Override
    public void setFirstLoad(boolean reLoad) {
        mFirstLoad = true;
    }
}
