package com.gzz100.Z100_HuiYi.data.meeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.DelegateModel;
import com.gzz100.Z100_HuiYi.data.MeetingInfo;
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
                    instance = new MeetingOperate(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * 向数据库中插入， 全部参会人员列表
     * @param userColumn  查询取参会人员列表需用到的 那一列对应的 值  Constant.COLUMNS_USER
     * @param userList    参会人员列表
     */
    public void insertUserList(int userColumn,List<DelegateModel> userList){
        byte[] data = ObjectTransverter.ListToByteArr(userList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_ROLE,userColumn);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.insert(PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE,null,values);
    }

    /**
     * 向数据库查询参会人员列表
     * @param userColumn   数据库中参会人员列表对应的字段值，Constant.COLUMNS_USER
     * @return   参会人员列表
     */
    public List<DelegateModel> queryUserList(int userColumn){
        List<DelegateModel> userList = null;
        mDatabase = mDBHelper.getReadableDatabase();
        String sql = "select * from " + PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE + " where " +
                PersistenceContract.ColumnsName.COLUMN_NAME_ROLE + " = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{userColumn + ""});
        if ( cursor.moveToFirst()){
            byte[] data = cursor.getBlob(cursor.getColumnIndex(
                    PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE));
            userList = (List<DelegateModel>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
        return userList;
    }

    /**
     * 向数据库中插入 会议概况
     * @param infoColumn     数据库中会议概况对应的字段值  Constant.COLUMNS_MEETING_INFO
     * @param meetingInfo    会议概况对象
     */
    public void insertMeetingInfo(int infoColumn, MeetingInfo meetingInfo){
        byte[] data = ObjectTransverter.ObjectToByte(meetingInfo);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_MEETING_INFO,infoColumn);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_MEETING_INFO_DATA,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.insert(PersistenceContract.ColumnsName.TABLE_NAME_MEETING_INFO,null,values);
    }

    /**
     * 向数据库中 查询会议概况
     * @param infoColumn   数据库中会议概况对应的字段值 Constant.COLUMNS_MEETING_INFO
     * @return    会议概况对象
     */
    public MeetingInfo queryMeetingInfo(int infoColumn){
        MeetingInfo meetingInfo = null;
        mDatabase = mDBHelper.getReadableDatabase();
        String sql = "select * from " + PersistenceContract.ColumnsName.TABLE_NAME_MEETING_INFO + " where " +
                PersistenceContract.ColumnsName.COLUMN_NAME_MEETING_INFO + " = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{infoColumn + ""});
        if ( cursor.moveToFirst()){
            byte[] data = cursor.getBlob(cursor.getColumnIndex(
                    PersistenceContract.ColumnsName.COLUMN_NAME_MEETING_INFO_DATA));
            meetingInfo = (MeetingInfo) ObjectTransverter.ByteToObject(data);
        }
        cursor.close();
        return meetingInfo;
    }

}
