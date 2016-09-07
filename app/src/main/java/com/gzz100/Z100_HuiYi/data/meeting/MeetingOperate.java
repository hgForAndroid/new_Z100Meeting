package com.gzz100.Z100_HuiYi.data.meeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.Document;
import com.gzz100.Z100_HuiYi.data.UserBean;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.data.db.PersistenceContract;
import com.gzz100.Z100_HuiYi.data.file.local.ObjectTransverter;

import java.util.List;

/**
* 文件界面 --- 数据库操作方法类
* @author XieQXiong
* create at 2016/9/7 15:32
*/

public class MeetingOperate {
    private static MeetingOperate instance;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    private MeetingOperate(Context context) {
        mDBHelper = DBHelper.getInstance(context);
    }
    public static MeetingOperate getInstance(Context context){
        if (instance == null){
            synchronized (MeetingOperate.class){
                if (instance == null){
                    instance = new MeetingOperate(context);
                }
            }
        }
        return instance;
    }

    /**
     * 向数据库中插入， 全部参会人员列表
     * @param userColumn  议程序号
     * @param userList 文件列表
     */
    public void insertUserList(int userColumn,List<UserBean> userList){
        byte[] data = ObjectTransverter.ListToByteArr(userList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_USERS,userColumn);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_USERS_LIST,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.insert(PersistenceContract.ColumnsName.TABLE_NAME,null,values);
    }

    /**
     * 向2数据库查询参会人员列表
     * @param userColumn   数据库中参会人员列表对应的字段值，Constant.COLUMNS_USER
     * @return   参会人员列表
     */
    public List<UserBean> queryUserList(int userColumn){
        List<UserBean> userList = null;
        mDatabase = mDBHelper.getReadableDatabase();
        String sql = "select * from " + PersistenceContract.ColumnsName.TABLE_NAME + " where " +
                PersistenceContract.ColumnsName.COLUMN_NAME_USERS + " = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{userColumn + ""});
        if ( cursor.moveToFirst()){
            byte[] data = cursor.getBlob(cursor.getColumnIndex(
                    PersistenceContract.ColumnsName.COLUMN_NAME_USERS_LIST));
            userList = (List<UserBean>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
        return userList;
    }

}
