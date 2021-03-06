package com.gzz100.Z100_HuiYi.meeting.file;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;

import java.util.List;
/**
* FileFragment  和  FilePresenter  的实现接口类
* @author XieQXiong
* create at 2016/8/26 15:56
*/

public interface FileContract {
    public interface View extends BaseView<Presenter>{
        /**
         * 显示议程列表
         * @param agendas  议程列表
         */
        void showAgendaList(List<AgendaModel> agendas);

        /**
         * 显示搜索的结果
         * @param document 搜索结果列表
         */
        void showSearchResult(List<DocumentModel> document);

        /**
         * 显示切换议程后对应的所有文件
         * @param document  切换议程后对应的文件列表
         */
        void showFilesList(List<DocumentModel> document);

        /**
         *跳转文件详情界面
         */
        void showFileDetail();
        /**
         *跳转文件详情界面
         */
        void showSearchFileDetail();

        /**
         * 用于判断Fragment是否已经添加到其对应的Activity上，
         * @return  true 已经添加，有更新UI的作用
         *           false 无更新UI的作用
         */
        boolean isActive();

        /**
         * 点议程没有对应的文件列表
         */
        void showNoFileList();

        /**
         * 刚开始加载后，没有议程列表
         */
        void showNoAgendaList();

        /**
         * 搜索无结果
         */
        void setAutoCompleteTextView(List<String>fileSearchNameHintList);
        void fileListNSearchFileListSwitch();

        void showNoSearchResult();
        //以下的参数设置，是跳转到文件详情时所需的参数
        void setAgendasSum(int size);
        void setAgendaIndex(int index);
        void setFileList(List<DocumentModel> documents);
        void setFileIndex(int fileIndex);
        void setAgendaTime(String time);

    }

    public interface Presenter extends BasePresenter{
        /**
         * 根据文件名/人名搜索
         * @param fileOrName  文件名/人名
         */
        void searchFileOrName(String fileOrName);

        /**
         * 获取议程列表
         * @param forceUpdate  是否强制更新
         * @param IMEI    设备标识码
         * @param userId  用户id
         */
        void fetchAgendaList(boolean forceUpdate,String IMEI,String userId);

        /**
         * 根据议程序号获取文件列表
         *  @param forceUpdate  是否强制更新
         * @param agendaPos  议程序号
         */
        void fetchFileList(boolean forceUpdate,int agendaPos);

        /**
         * 展示文件内容
         */
        void showFileDetail(int fileIndex);

        void showSearchFileDetail(int fileIndex,int agendaIndex);

        /**
         * 根据搜索的议程号搜索对应的所有文件
         * @param agendaIndex 议程序号
         */

        void fetchSearchFileList(int agendaIndex);


        void setFileSearchAutoCompleteTextViewHint();

        /**
         * 设置议程时间
         * @param time
         */
        void setAgendaTime(String time);

        /**
         * 当Fragment销毁后，重新设置加载
         * @param reLoad
         */
        void setFirstLoad(boolean reLoad);

        void showAutoCompleteTVSelectionFileDetail(String fileName);


    }
}
