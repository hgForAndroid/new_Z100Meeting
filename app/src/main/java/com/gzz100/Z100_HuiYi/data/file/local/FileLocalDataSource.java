package com.gzz100.Z100_HuiYi.data.file.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.file.FileDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
* 加载本地数据
* @author XieQXiong
* create at 2016/8/25 14:43
*/

public class FileLocalDataSource implements FileDataSource {
    private static FileLocalDataSource INSTANCE;
//    private final DBHelper mDbHelper;
    private FileOperate mFileOperate;

    private FileLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
//        mDbHelper = DBHelper.getInstance(context);
        mFileOperate = FileOperate.getInstance(context);
    }

    public static FileLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FileLocalDataSource(context);
        }
        return INSTANCE;
    }
    @Override
    public void getFileList(int agendaPos, @NonNull LoadFileListCallback callback) {
        checkNotNull(callback);
        //加载本地数据
        List<DocumentModel> documents = mFileOperate.queryFileList(agendaPos);
        if (documents != null && documents.size() > 0){
            callback.onFileListLoaded(documents);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull LoadAgendaListCallback callback) {
        checkNotNull(callback);
        //加载本地数据
        List<AgendaModel> agendasList = mFileOperate.queryAgendaList(Constant.COLUMNS_AGENDAS);
        if (agendasList != null && agendasList.size() > 0){
            callback.onAgendaListLoaded(agendasList);
        }else {
            callback.onDataNotAvailable();
        }

    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull LoadFileListCallback callback) {

    }
}
