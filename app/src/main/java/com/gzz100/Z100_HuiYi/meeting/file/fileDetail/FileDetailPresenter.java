package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.os.Environment;
import android.view.View;

import com.gzz100.Z100_HuiYi.data.file.FileDetailRepository;

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
        java.io.File file = new java.io.File(Environment.getExternalStorageDirectory().getPath()
                + "/" +  fileName + ".pdf");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        mView.loadFile(file);
    }
}
