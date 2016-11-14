package com.gzz100.Z100_HuiYi.meeting.agenda;


import com.gzz100.Z100_HuiYi.BasePresenter;
import com.gzz100.Z100_HuiYi.BaseView;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;

import java.util.List;

/**
 * Created by Lee on 2016/8/31.
 */
public interface AgendaContract {
    public interface View extends BaseView<Presenter>{
        /**
         * 显示议程列表
         */
        void showAgendasList(List<AgendaModel> agendas);

        /**
         * 判断是否已经添加到Activity
         */
        boolean isActive();

        /**
         * 议程没有对应的文件列表
         */
        void showNoAgendaList();

        void showNoFileList();

        /**
        *  显示议程详细信息
        */
        void showAgendaDetail(AgendaModel agenda);

        /**
         *跳转文件详情界面
         */
        void showFileDetail();
        void setFileList(List<DocumentModel> documents);
    }

    public interface Presenter extends BasePresenter{
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
        void fetchFileListAndShow(boolean forceUpdate,int agendaPos);

        /**
         *  显示议程详细信息
         *  @param agenda 需要显示的议程
         */
        void showAgendaDetail(AgendaModel agenda);

        /**
         * 当Fragment销毁后，重新设置加载
         * @param reLoad
         */
        void setFirstLoad(boolean reLoad);
    }
}
