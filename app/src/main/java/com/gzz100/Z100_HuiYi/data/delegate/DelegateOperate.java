package com.gzz100.Z100_HuiYi.data.delegate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gzz100.Z100_HuiYi.data.DelegateModel;
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

    public static DelegateOperate getInstance(Context context) {
        if (INSTANCE == null)
            return INSTANCE = new DelegateOperate(context.getApplicationContext());
        return INSTANCE;
    }

    private DelegateOperate(Context context) {
        mDBHelper = DBHelper.getInstance(context);
    }


    /**
     * @param rolePos 根据角色返回delegateBean对象列表
     * @return
     */
    public List<DelegateModel> queryDelelgateListByRole(int rolePos) {

        List<DelegateModel> delegateBeanList = new ArrayList<>();
        mDataBase = mDBHelper.getReadableDatabase();
        String sql = "select " + PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE + " from " + PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE;

        Cursor cursor = mDataBase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            byte[] data = cursor.getBlob(cursor.getColumnIndex(PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE));
            delegateBeanList = (List<DelegateModel>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
//        mDataBase.close();
        List<DelegateModel> returnList = new ArrayList<>();
        for (DelegateModel delegateModel : delegateBeanList) {
            if (delegateModel.getDelegateRole() == rolePos)
                returnList.add(delegateModel);
        }
        return returnList;
    }

    public List<DelegateModel> queryAllDelegate() {
        List<DelegateModel> allDelegate = new ArrayList<>();
        mDataBase = mDBHelper.getReadableDatabase();
        String sql = "select " + PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE + " from " + PersistenceContract.ColumnsName.TABLE_NAME_DELEGATE;

        Cursor cursor = mDataBase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {

            byte[] data = cursor.getBlob(cursor.getColumnIndex(PersistenceContract.ColumnsName.COLUMN_NAME_DELEGATE));
            allDelegate = (List<DelegateModel>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
//        mDataBase.close();
        return allDelegate;
    }
}
