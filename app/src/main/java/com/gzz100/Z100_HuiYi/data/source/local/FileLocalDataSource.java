package com.gzz100.Z100_HuiYi.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.source.FileDataSource;

import static com.google.common.base.Preconditions.checkNotNull;
/**
* 加载本地数据
* @author XieQXiong
* create at 2016/8/25 14:43
*/

public class FileLocalDataSource implements FileDataSource {
    private static FileLocalDataSource INSTANCE;

    private FileLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
//        mDbHelper = new TasksDbHelper(context);
    }

    public static FileLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FileLocalDataSource(context);
        }
        return INSTANCE;
    }
    @Override
    public void getFileList(int agendaPos, @NonNull loadFileListCallback callback) {
        checkNotNull(callback);
        //加载本地数据
    }

    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull loadAgendaListCallback callback) {
        checkNotNull(callback);

    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull loadFileListCallback callback) {

    }
}
