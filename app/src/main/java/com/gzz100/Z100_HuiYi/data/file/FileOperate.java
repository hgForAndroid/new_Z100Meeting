package com.gzz100.Z100_HuiYi.data.file;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gzz100.Z100_HuiYi.data.AgendaModel;
import com.gzz100.Z100_HuiYi.data.DocumentModel;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.data.db.PersistenceContract;
import com.gzz100.Z100_HuiYi.data.file.local.ObjectTransverter;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.List;
/**
* 文件界面 --- 数据库操作方法类
* @author XieQXiong
* create at 2016/9/7 15:32
*/

public class FileOperate {
    private static FileOperate instance;
    private DBHelper mDBHelper;
//    private Context mContext;

    private SQLiteDatabase mDatabase;

    private FileOperate(Context context) {
        mDBHelper = DBHelper.getInstance(context);
    }
    public static FileOperate getInstance(Context context){
        if (instance == null){
            synchronized (FileOperate.class){
                if (instance == null){
                    instance = new FileOperate(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * 箱数据库中插入 议程序号对应的文件列表
     * @param agendaIndex  议程序号
     * @param documentList 文件列表
     */
    public void insertFileList(int agendaIndex,List<DocumentModel> documentList){
        byte[] data = ObjectTransverter.ListToByteArr(documentList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_AGENDA_INDEX,agendaIndex);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_FILE_LIST,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.insert(PersistenceContract.ColumnsName.TABLE_NAME_FILE,null,values);
//        mDatabase.close();
    }

    /**
     * 根据议程序号，向数据库中查询对应的文件列表
     * @param agendaIndex  议程序号
     * @return  文件列表
     */
    public List<DocumentModel> queryFileList(int agendaIndex){
        List<DocumentModel> documents = null;
        mDatabase = mDBHelper.getReadableDatabase();
        String sql = "select * from " + PersistenceContract.ColumnsName.TABLE_NAME_FILE + " where " +
                PersistenceContract.ColumnsName.COLUMN_NAME_AGENDA_INDEX + " = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{agendaIndex + ""});
        if ( cursor.moveToFirst()){
            byte[] data = cursor.getBlob(cursor.getColumnIndex(
                    PersistenceContract.ColumnsName.COLUMN_NAME_FILE_LIST));
            documents = (List<DocumentModel>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
//        mDatabase.close();
        return documents;
    }


    /**
     * 向数据库中插入 议程序号对应的文件列表
     * 在数据库中插入对应的字段的值为 ： Constant.COLUMNS_AGENDAS
     * @param agendaList  议程列表
     */
    public void insertAgendaList( List<AgendaModel> agendaList){
        byte[] data = ObjectTransverter.ListToByteArr(agendaList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_AGENDAS,Constant.COLUMNS_AGENDAS);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_AGENDA_LIST,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.insert(PersistenceContract.ColumnsName.TABLE_NAME_AGENDA,null,values);
//        mDatabase.close();
    }
    /**
     * 向数据库中更新 议程序号对应的文件列表
     * 在数据库中更新对应的字段的值为 ： Constant.COLUMNS_AGENDAS
     * @param agendaList  议程列表
     */
    public void updateAgendaList(List<AgendaModel> agendaList){
        byte[] data = ObjectTransverter.ListToByteArr(agendaList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_AGENDAS,Constant.COLUMNS_AGENDAS);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_AGENDA_LIST,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.insert(PersistenceContract.ColumnsName.TABLE_NAME_AGENDA,null,values);
//        mDatabase.close();
    }
    /**
     * 查询议程列表
     * @return  议程列表，在数据苦衷对应查询的列的值是 Constant.COLUMNS_AGENDAS
     */
    public List<AgendaModel> queryAgendaList(){
        List<AgendaModel> baseBeen = null;
        mDatabase = mDBHelper.getReadableDatabase();
        String sql = "select * from " + PersistenceContract.ColumnsName.TABLE_NAME_AGENDA + " where " +
                PersistenceContract.ColumnsName.COLUMN_NAME_AGENDAS + " = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{Constant.COLUMNS_AGENDAS + ""});
        if ( cursor.moveToFirst()){
            byte[] data = cursor.getBlob(cursor.getColumnIndex(
                    PersistenceContract.ColumnsName.COLUMN_NAME_AGENDA_LIST));
            baseBeen = (List<AgendaModel>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
//        mDatabase.close();
        return baseBeen;
    }
}
