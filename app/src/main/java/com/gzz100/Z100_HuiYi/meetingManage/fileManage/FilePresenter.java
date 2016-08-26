package com.gzz100.Z100_HuiYi.meetingManage.fileManage;

import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.FileBean;
import com.gzz100.Z100_HuiYi.data.source.FileDataSource;
import com.gzz100.Z100_HuiYi.data.source.FileRepository;

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
        mFileRepository.getSearchResult(fileOrName, new FileDataSource.loadFileListCallback() {
            @Override
            public void onFileListLoaded(List<FileBean> files) {

                if (!mFileView.isActive()) {
                    return;
                }
                mFileView.showFilesList(files);

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
            mFirstLoad = false;
            mFileRepository.getAgendaList(IMEI, userId, new FileDataSource.loadAgendaListCallback() {
                @Override
                public void onAgendaListLoaded(List<Agenda> agendas) {
                    if (!mFileView.isActive()) {
                        return;
                    }

                    mFileView.showAgendaList(agendas);
                    mFileView.setAgendasSum(agendas.size());
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
            mFileRepository.getFileList(agendaPos, new FileDataSource.loadFileListCallback() {
                @Override
                public void onFileListLoaded(List<FileBean> files) {

                    if (!mFileView.isActive()) {
                        return;
                    }
                    mFileView.setFileList(files);
                    mFileView.showFilesList(files);
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
    public void start() {
        fetchAgendaList(false,"","");
        fetchFileList(false,0);
    }

}
