package com.gzz100.Z100_HuiYi.meeting.file.fileDetail;

import android.view.View;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;

import java.io.File;

/**
* 文件详情的契约接口
* @author XieQXiong
* create at 2016/8/30 10:43
*/

public interface FileDetailContract {
    interface DetailView extends BaseView<Presenter>{
        void fallback();
        void slideLeft(int distanceX);
        void slideRight(int distanceX);
        void loadFile(File file);
    }

    interface Presenter extends BasePresenter{
        /**
         * 返回上一页
         */
        void fallback();

        /**
         * 隐藏侧滑菜单
         * @param v  侧滑菜单
         */
        void slideLeft(View v);

        void slideRight(View v);
        void loadFile(String fileName);
    }
}
