package com.gzz100.Z100_HuiYi.meeting.file;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.file.FileDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class FilePresenter implements FileContract.Presenter {
    private FileRepository mFileRepository;
    private FileContract.View mFileView;

    private boolean mFirstLoad = true;

    public FilePresenter(@NonNull FileRepository fileRepository, @NonNull FileContract.View fileView) {
        this.mFileRepository = checkNotNull(fileRepository,"fileRepository cannot be null");
        this.mFileView = checkNotNull(fileView,"fileView cannot be null");
        this.mFileView.setPresenter(this);
    }

    @Override
    public void searchFileOrName(String fileOrName) {
        mFileRepository.getSearchResult(fileOrName, new FileDataSource.LoadFileListCallback() {
            @Override
            public void onFileListLoaded(List<Document> documents) {

                if (!mFileView.isActive()) {
                    return;
                }
                mFileView.showFilesList(documents);

            }

            @Override
            public void onDataNotAvailable() {
                mFileView.showNoSearchResult();
            }
        });


        mFileView.showSearchResult(null);
    }

    @Override
    public void fetchAgendaList(boolean forceUpdate, String IMEI, String userId) {
        if (forceUpdate || mFirstLoad){
//            mFirstLoad = false;
            mFileRepository.getAgendaList(IMEI, userId, new FileDataSource.LoadAgendaListCallback() {
                @Override
                public void onAgendaListLoaded(List<Agenda> agendas) {
                    if (!mFileView.isActive()) {
                        return;
                    }
                    mFileView.showAgendaList(agendas);
                    mFileView.setAgendasSum(agendas.size());
                    if (mFirstLoad){
                        mFileView.setAgendaTime(agendas.get(0).getAgendaDuration());
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    mFileView.showNoAgendaList();

                }
            });

        }

    }

    @Override
    public void fetchFileList(boolean forceUpdate, final int agendaPos) {
        if (forceUpdate || mFirstLoad){
            mFirstLoad = false;
//            fileModel.getFileListByAgendaPos(agendaPos);
            mFileRepository.getFileList(agendaPos, new FileDataSource.LoadFileListCallback() {
                @Override
                public void onFileListLoaded(List<Document> documents) {

                    if (!mFileView.isActive()) {
                        return;
                    }
                    mFileView.showFilesList(documents);
                    mFileView.setFileList(documents);
                    mFileView.setAgendaIndex(agendaPos);

                }

                @Override
                public void onDataNotAvailable() {

                    mFileView.showNoFileList();

                }
            });

        }
    }

    @Override
    public void showFileDetail(int fileIndex) {
        mFileView.setFileIndex(fileIndex);
        mFileView.showFileDetail();
    }

    @Override
    public void setAgendaTime(String time) {
        mFileView.setAgendaTime(time);
    }

    @Override
    public void setFirstLoad(boolean reLoad) {
        mFirstLoad = reLoad;
    }


    @Override
    public void start() {
        fetchAgendaList(false,"","");
        fetchFileList(false,1);
    }

}
