package com.gzz100.Z100_HuiYi.data.file.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.data.file.FileDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;
import com.gzz100.Z100_HuiYi.network.HttpManager;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.ProgressSubscriber;
import com.gzz100.Z100_HuiYi.network.entity.DocumentPost;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
* 加载服务器数据
* @author XieQXiong
* create at 2016/8/25 14:42
*/

public class FileRemoteDataSource implements FileDataSource {
    private static FileRemoteDataSource INSTANCE;
    private FileOperate mFileOperate;
    private static Context mContext;

    private FileRemoteDataSource(@NonNull Context context) {
        mFileOperate = FileOperate.getInstance(context);
    }

    public static FileRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FileRemoteDataSource(context);
            mContext = context;
        }
        return INSTANCE;
    }
    @Override
    public void getFileList(final int agendaPos, @NonNull final LoadFileListCallback callback) {
        checkNotNull(callback);
        //测试存数据库
//        List<DocumentModel> fileListByIndex = FakeDataProvider.getFileListByindex(agendaPos);
//        callback.onFileListLoaded(fileListByIndex);
//        mFileOperate.insertFileList(agendaPos,fileListByIndex);
        //加载服务器数据
        DocumentPost documentPost = new DocumentPost(
                new ProgressSubscriber(new HttpRxCallbackListener<List<DocumentModel>>(){
                    @Override
                    public void onNext(List<DocumentModel> documents) {
                        callback.onFileListLoaded(documents);
                        //存数据库
                        FileOperate.getInstance(mContext).insertFileList(agendaPos,documents);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        callback.onDataNotAvailable();
                    }
                }, mContext), agendaPos);
        HttpManager.getInstance(mContext).doHttpDeal(documentPost);

        //去完数据需存在本地
    }


    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull final LoadAgendaListCallback callback) {
        checkNotNull(callback);
        List<AgendaModel> agendas = FileOperate.getInstance(mContext).queryAgendaList(Constant.COLUMNS_AGENDAS);
        if (agendas != null && agendas.size() > 0){
            callback.onAgendaListLoaded(agendas);
            mFileOperate.insertAgendaList(Constant.COLUMNS_AGENDAS,agendas);
        }else {
            callback.onDataNotAvailable();
        }

        //正式
//        AgendasPost agendasPost = new AgendasPost(new ProgressSubscriber(new HttpRxCallbackListener<List<Agenda>>(){
//            @Override
//            public void onNext(List<Agenda> agendas) {
//                callback.onAgendaListLoaded(agendas);
//                mDbHelper.insertAgendaList(Constant.COLUMNS_AGENDAS,agendas);
//            }
//        },mContext),IMEI,userId);
    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull LoadFileListCallback callback) {

    }
}
