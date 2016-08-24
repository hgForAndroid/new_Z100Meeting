package com.gzz100.Z100_HuiYi.meetingManage.fileManage;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.FileInfo;

import java.util.List;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public interface FileContract {
    public interface View extends BaseView<Presenter>{
        /**
         * 显示搜索的结果
         * @param fileInfos 搜索结果列表
         */
        void showSearchResult(List<FileInfo> fileInfos);

        /**
         * 显示切换议程后对应的所有文件
         * @param fileInfos  切换议程后对应的文件列表
         */
        void showSwitchAgendaResult(List<FileInfo> fileInfos);

        /**
         *初始化文件内容界面
         */
        void showFileDeatil();

    }

    public interface Presenter extends BasePresenter{
        /**
         * 根据文件名/人名搜索
         * @param fileOrName  文件名/人名
         */
        void searchFileOrName(String fileOrName);

        /**
         * 切换议程，查看议程所包含的文件
         * @param position  议程序号
         */
        void switchAgendaByPosition(int position);

        /**
         * 展示文件内容
         */
        void showFileDetail();

    }
}
