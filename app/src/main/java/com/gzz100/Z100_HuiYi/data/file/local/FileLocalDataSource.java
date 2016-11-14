package com.gzz100.Z100_HuiYi.data.file.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.file.FileDataSource;
import com.gzz100.Z100_HuiYi.data.file.FileOperate;

import java.util.ArrayList;
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
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAgendaList(String IMEI, String userId, @NonNull LoadAgendaListCallback callback) {
        checkNotNull(callback);
        //加载本地数据
        List<AgendaModel> agendasList = mFileOperate.queryAgendaList();
        if (agendasList != null && agendasList.size() > 0){
            callback.onAgendaListLoaded(agendasList);
        } else {
            callback.onDataNotAvailable();
        }

    }


    @Override
    public void getSearchResult(String fileOrName, @NonNull LoadFileListCallback callback) {
        List<DocumentModel> documentList = new ArrayList<>();
        List<AgendaModel> agendasList = mFileOperate.queryAgendaList(Constant.COLUMNS_AGENDAS);

        for (int i = 0; i < agendasList.size(); i++) {
            List<DocumentModel> documents = mFileOperate.queryFileList(i);
            if (documents != null && documents.size() > 0) {
                for (DocumentModel document : documents) {
                    if (document.getDocumentName().contains(fileOrName)) {
                        documentList.add(document);
                    }
                    if (agendasList.get(document.getDocumentAgendaIndex()-1).getAgendaSpeaker().contains(fileOrName)) {
                        documentList.add(document);
                    }
                }
            }
        }
        if (documentList != null && documentList.size() > 0) {
            callback.onFileListLoaded(documentList);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getSearchNameHint(LoadFileSearchNameCallback callback) {
        List<String> nameHintList = new ArrayList<>();
        List<AgendaModel> agendasList = mFileOperate.queryAgendaList(Constant.COLUMNS_AGENDAS);
            for (AgendaModel agenda : agendasList) {
                nameHintList.add(agenda.getAgendaSpeaker());
        }
        for (int i = 0; i < agendasList.size(); i++) {
            List<DocumentModel> documents = mFileOperate.queryFileList(i);
            if (documents != null && documents.size() > 0) {
                for (DocumentModel document : documents) {
                    nameHintList.add(document.getDocumentName());
                }
            }
            callback.onNameHintLoaded(nameHintList);
        }


    }

    @Override
    public void getFileByName(String fileName, getFileByNameCallback callback) {
        List<AgendaModel> agendasList = mFileOperate.queryAgendaList(Constant.COLUMNS_AGENDAS);
        for (int i = 0; i < agendasList.size(); i++) {
            List<DocumentModel> documents = mFileOperate.queryFileList(i);
            if (documents != null && documents.size() > 0) {
                for (DocumentModel document : documents) {
                    if (document.getDocumentName().equals(fileName)) {
                        callback.fileDidGet(document);
                        break;
                    }
                }
                callback.fileNotGet();
            }
        }
    }

    @Override
    public boolean isFileName(String inputString) {
        List<AgendaModel> agendasList = mFileOperate.queryAgendaList(Constant.COLUMNS_AGENDAS);
        for (int i = 0; i < agendasList.size(); i++) {
            List<DocumentModel> documents = mFileOperate.queryFileList(i);
            if (documents != null && documents.size() > 0) {
                for (DocumentModel document : documents) {
                    if(document.getDocumentName().equals(inputString)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

