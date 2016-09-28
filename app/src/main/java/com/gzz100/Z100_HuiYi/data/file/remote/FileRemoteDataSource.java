package com.gzz100.Z100_HuiYi.data.file.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.file.FileDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;
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
//    private final DBHelper mDbHelper;
    private FileOperate mFileOperate;
    private static Context mContext;

    private FileRemoteDataSource(@NonNull Context context) {
//        mDbHelper = DBHelper.getInstance(context);
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
        List<Document> fileListByIndex = FakeDataProvider.getFileListByindex(agendaPos);
        callback.onFileListLoaded(fileListByIndex);
        mFileOperate.insertFileList(agendaPos,fileListByIndex);
        //加载服务器数据
//        DocumentPost documentPost = new DocumentPost(
//                new ProgressSubscriber(new HttpRxCallbackListener<List<Document>>(){
//                    @Override
//                    public void onNext(List<Document> documents) {
//                        callback.onFileListLoaded(documents);
//                        //存数据库
//                        mDbHelper.insertFileList(agendaPos,documents);
//                    }
//                }, mContext), agendaPos);
//        HttpManager.getInstance(mContext).doHttpDeal(documentPost);

        //测试
//        OnePost onePost = new OnePost(new ProgressSubscriber(new HttpRxCallbackListener<List<OneTitle>>() {
//            @Override
//            public void onNext(List<OneTitle> oneTitles) {
//                Log.e("getList ===", oneTitles.size() + "");
//            }
//        }, mContext));
//        HttpManager.getInstance().doHttpDeal(onePost);

        //去完数据需存在本地
    }


    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull final LoadAgendaListCallback callback) {
        checkNotNull(callback);
        List<Agenda> agendas = FakeDataProvider.getAgendas();
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
