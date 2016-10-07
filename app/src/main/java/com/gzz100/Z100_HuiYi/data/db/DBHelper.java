package com.gzz100.Z100_HuiYi.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
* 数据库
* @author XieQXiong
* create at 2016/9/7 15:22
*/

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private  static final String DB_NAME = "meeting.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String CREATE_TABLE_FILE =
            "CREATE TABLE " + PersistenceContract.ColumnsName.TABLE_NAME_FILE+ " (" +
            PersistenceContract.ColumnsName._ID + TEXT_TYPE + " PRIMARY KEY,"  +
            PersistenceContract.ColumnsName.COLUMN_NAME_AGENDA_INDEX + INTEGER_TYPE
            + COMMA_SEP + PersistenceContract.ColumnsName.COLUMN_NAME_FILE_LIST + TEXT_TYPE
            + ")";

    private static final String CREATE_TABLE_AGENDA =
            "CREATE TABLE " + PersistenceContract.ColumnsName.TABLE_NAME_AGENDA+ " (" +
                    PersistenceContract.ColumnsName._ID + TEXT_TYPE + " PRIMARY KEY,"  +
                    PersistenceContract.ColumnsName.COLUMN_NAME_AGENDAS + INTEGER_TYPE
                    + COMMA_SEP + PersistenceContract.ColumnsName.COLUMN_NAME_AGENDA_LIST + TEXT_TYPE
                    + ")";
/*    private static final String CREATE_TABLE_DELEGATE =
            "CREATE TABLE " + PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE+ " (" +
                    PersistenceContract.ColumnsName._ID + TEXT_TYPE + " PRIMARY KEY,"  +
                    PersistenceContract.ColumnsName.COLUMN_NAME_USERS + INTEGER_TYPE
                    + COMMA_SEP + PersistenceContract.ColumnsName.COLUMN_NAME_USERS_LIST + TEXT_TYPE
                    + ")";*/
private static final String CREATE_TABLE_DELEGATE =
        "CREATE TABLE " + PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE+ " (" +
                PersistenceContract.ColumnsName._ID + TEXT_TYPE + " PRIMARY KEY,"  +
                PersistenceContract.ColumnsName.COLUMN_NAME_ROLE + INTEGER_TYPE
                + COMMA_SEP + PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE
                + ")";
    private static final String CREATE_TABLE_MEETING_SUMMARY =
            "CREATE TABLE " + PersistenceContract.ColumnsName.TABLE_NAME_MEETING_INFO+ " (" +
                    PersistenceContract.ColumnsName._ID + TEXT_TYPE + " PRIMARY KEY,"  +
                    PersistenceContract.ColumnsName.COLUMN_NAME_MEETING_INFO + INTEGER_TYPE
                    + COMMA_SEP + PersistenceContract.ColumnsName.COLUMN_NAME_MEETING_INFO_DATA + TEXT_TYPE
                    + ")";
    private SQLiteDatabase mDatabase;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    private static DBHelper instance;

    public static DBHelper getInstance(Context context){
        if (instance == null){
            synchronized (DBHelper.class){
                if (instance == null){
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FILE);
        db.execSQL(CREATE_TABLE_AGENDA);
        db.execSQL(CREATE_TABLE_DELEGATE);
        db.execSQL(CREATE_TABLE_MEETING_SUMMARY);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 删除表，在有新增议程时调用
     */
    public void deleteTable(){
        mDatabase = instance.getReadableDatabase();
        String sql1 = "DROP TABLE "+PersistenceContract.ColumnsName.TABLE_NAME_FILE;
        String sql2 = "DROP TABLE "+PersistenceContract.ColumnsName.TABLE_NAME_AGENDA;
        String sql3 = "DROP TABLE "+PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE;
        String sql4 = "DROP TABLE "+PersistenceContract.ColumnsName.TABLE_NAME_MEETING_INFO;
        mDatabase.execSQL(sql1);
        mDatabase.execSQL(sql2);
        mDatabase.execSQL(sql3);
        mDatabase.execSQL(sql4);
    }
}
