package com.gzz100.Z100_HuiYi.meetingManage.fileManage;

import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.FileBean;

import java.util.List;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public interface FileContract {
    public interface View extends BaseView<Presenter>{
        /**
         * 显示议程列表
         * @param agendas  议程列表
         */
        void showAgendaList(List<Agenda> agendas);

        /**
         * 显示搜索的结果
         * @param fileBean 搜索结果列表
         */
        void showSearchResult(List<FileBean> fileBean);

        /**
         * 显示切换议程后对应的所有文件
         * @param fileBean  切换议程后对应的文件列表
         */
        void showFilesResult(List<FileBean> fileBean);

        /**
         *初始化文件内容界面
         */
        void showFileDetail();

        /**
         * 用于判断Fragment是否已经添加到其对应的Activity上，
         * @return  true 已经添加，有更新UI的作用
         *           false 无更新UI的作用
         */
        boolean isActive();

        void showNoFileList();
        void showNoAgendaList();
        void showNoSearchResult();

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
        void showFileDetail();

    }
}
