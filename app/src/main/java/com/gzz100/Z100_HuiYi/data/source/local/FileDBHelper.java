package com.gzz100.Z100_HuiYi.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gzz100.Z100_HuiYi.data.FileBean;
import com.gzz100.Z100_HuiYi.utils.ObjectParser;

import java.util.List;

/**
 * Created by XieQXiong on 2016/8/25.
 */
public class FileDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private  static final String DB_NAME = "file.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + FilePersistenceContract.FileListEntry.TABLE_NAME+ " (" +
            FilePersistenceContract.FileListEntry._ID + TEXT_TYPE + " PRIMARY KEY,"  +
            FilePersistenceContract.FileListEntry.COLUMN_NAME_AGENDA_INDEX + INTEGER_TYPE
            + COMMA_SEP + FilePersistenceContract.FileListEntry.COLUMN_NAME_FILE_LIST + TEXT_TYPE
            + ")";
    private SQLiteDatabase mDatabase;

    private FileDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    private static FileDBHelper instance;

    public static FileDBHelper getInstance(Context context){
        if (instance == null){
            synchronized (FileDBHelper.class){
                if (instance == null){
                    instance = new FileDBHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertFileList(int agendaIndex,List<FileBean> fileList){
        byte[] data = ObjectParser.FileListToByteArr(fileList);
        ContentValues values =new ContentValues();
        values.put(FilePersistenceContract.FileListEntry.COLUMN_NAME_AGENDA_INDEX,agendaIndex);
        values.put(FilePersistenceContract.FileListEntry.COLUMN_NAME_FILE_LIST,data);
        mDatabase = instance.getReadableDatabase();
        mDatabase.insert(FilePersistenceContract.FileListEntry.TABLE_NAME,null,values);
    }

    public List<FileBean> queryFileList(int agendaIndex){
        List<FileBean> files = null;
        mDatabase = instance.getReadableDatabase();
        String sql = "select * from " + FilePersistenceContract.FileListEntry.TABLE_NAME + " where " +
                FilePersistenceContract.FileListEntry.COLUMN_NAME_AGENDA_INDEX + " = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{agendaIndex + ""});
        if ( cursor.moveToFirst()){
            byte[] data = cursor.getBlob(cursor.getColumnIndex(
                    FilePersistenceContract.FileListEntry.COLUMN_NAME_FILE_LIST));
            files = ObjectParser.byteArrToFileList(data);
        }
        return  files;

    }
}
