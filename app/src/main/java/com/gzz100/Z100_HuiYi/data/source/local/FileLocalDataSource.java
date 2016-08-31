package com.gzz100.Z100_HuiYi.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.File;
import com.gzz100.Z100_HuiYi.data.source.FileDataSource;
import com.gzz100.Z100_HuiYi.fakeData.FakeDataProvider;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
* 加载本地数据
* @author XieQXiong
* create at 2016/8/25 14:43
*/

public class FileLocalDataSource implements FileDataSource {
    private static FileLocalDataSource INSTANCE;
    private final FileDBHelper mDbHelper;

    private FileLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = FileDBHelper.getInstance(context);
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

        //假数据
        List<File> list = FakeDataProvider.getFileListByindex(agendaPos);
        if (list != null && list.size()>0){
            callback.onFileListLoaded(list);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull loadAgendaListCallback callback) {
        checkNotNull(callback);
        //假数据
        List<Agenda> agendas = FakeDataProvider.getAgendas();
        if (agendas != null && agendas.size() > 0){
            callback.onAgendaListLoaded(agendas);
        }else {
            callback.onDataNotAvailable();
        }

    }

    @Override
    public void getSearchResult(String fileOrName, @NonNull loadFileListCallback callback) {

    }
}
