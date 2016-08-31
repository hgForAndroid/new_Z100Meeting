package com.gzz100.Z100_HuiYi.meeting.file.fileDetailManage;

import android.view.View;

import com.gzz100.Z100_HuiYi.data.source.FileDetailRepository;

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
    public void setContent(String content) {
        mView.setContent(content);
    }

    @Override
    public void start() {

    }
}
