package com.gzz100.Z100_HuiYi.data.delegate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gzz100.Z100_HuiYi.data.DelegateBean;
import com.gzz100.Z100_HuiYi.data.db.DBHelper;
import com.gzz100.Z100_HuiYi.data.db.PersistenceContract;
import com.gzz100.Z100_HuiYi.data.file.local.ObjectTransverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2016/10/7.
 */
public class DelegateOperate {
    private static DelegateOperate INSTANCE;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDataBase;

    public static DelegateOperate getInstance(Context context){
        if(INSTANCE==null)
            return INSTANCE=new DelegateOperate(context);
        return INSTANCE;
    }

    private DelegateOperate(Context context) {
        mDBHelper=DBHelper.getInstance(context);
    }

    /**
     *
     * @param role
     * @param delegateBeanList
     */
    public void insertDelegateList(int role,List<DelegateBean> delegateBeanList){
        byte[] data=ObjectTransverter.ListToByteArr(delegateBeanList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_ROLE,role);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE,data);
        mDataBase=mDBHelper.getReadableDatabase();
        mDataBase.insert(PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE,null,values);
    }




    /**
     *
     * @param rolePos 根据角色返回delegateBean对象列表
     * @return
     */
    public   List<DelegateBean> queryDelelgateListByRole(int rolePos){
        List<DelegateBean> delegateBeanList=new ArrayList<>();
        mDataBase=mDBHelper.getReadableDatabase();
        String sql="select "+ PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE+" from "+PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE
                +" where "+PersistenceContract.ColumnsName.COLUMN_NAME_ROLE+" = ?";
        String [] selectionArgs={rolePos+""};
        Cursor cursor=mDataBase.rawQuery(sql,selectionArgs);
        if(cursor.moveToFirst()){
            byte[] data=cursor.getBlob(cursor.getColumnIndex(PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE));
            delegateBeanList=(List<DelegateBean>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
        return delegateBeanList;
    }

    public List<DelegateBean> queryAllDelegate(){
        List<DelegateBean> allDelegate=new ArrayList<>();
        mDataBase=mDBHelper.getReadableDatabase();
        String sql="select "+PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE+" from "+PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE;

        Cursor cursor=mDataBase.rawQuery(sql,null);
        if(cursor.moveToFirst()){
               do{
                    byte[] data = cursor.getBlob(cursor.getColumnIndex(PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE));
                    for (DelegateBean delegateBean : (List<DelegateBean>) ObjectTransverter.byteArrToList(data)) {
                        allDelegate.add(delegateBean);
                    }
                    cursor.moveToNext();
                } while(!cursor.isAfterLast());

        }
        cursor.close();
        return allDelegate;
    }
}
