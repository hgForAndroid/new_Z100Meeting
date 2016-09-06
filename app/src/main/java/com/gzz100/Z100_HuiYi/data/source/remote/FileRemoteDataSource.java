package com.gzz100.Z100_HuiYi.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.source.FileDataSource;
import com.gzz100.Z100_HuiYi.data.source.local.FileDBHelper;
import com.gzz100.Z100_HuiYi.network.HttpManager;
import com.gzz100.Z100_HuiYi.network.HttpRxCallbackListener;
import com.gzz100.Z100_HuiYi.network.ProgressSubscriber;
import com.gzz100.Z100_HuiYi.network.entity.DocumentPost;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
* 加载服务器数据
* @author XieQXiong
* create at 2016/8/25 14:42
*/

public class FileRemoteDataSource implements FileDataSource {
    private static FileRemoteDataSource INSTANCE;
    private final FileDBHelper mDbHelper;
    private static Context mContext;

    private FileRemoteDataSource(@NonNull Context context) {
        mDbHelper = FileDBHelper.getInstance(context);
    }

    public static FileRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FileRemoteDataSource(context);
            mContext = context;
        }
        return INSTANCE;
    }
    @Override
    public void getFileList(int agendaPos, @NonNull final LoadFileListCallback callback) {
        checkNotNull(callback);
        //加载服务器数据
        DocumentPost documentPost = new DocumentPost(
                new ProgressSubscriber(new HttpRxCallbackListener<List<Document>>(){
                    @Override
                    public void onNext(List<Document> documents) {
                        callback.onFileListLoaded(documents);
                    }
                }, mContext), agendaPos);
        HttpManager.getInstance().doHttpDeal(documentPost);

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
    public void getAgendaList(String IMEI, String userId, @NonNull LoadAgendaListCallback callback) {

    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull LoadFileListCallback callback) {

    }
}
