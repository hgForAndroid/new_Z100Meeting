package com.gzz100.Z100_HuiYi.data.vote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gzz100.Z100_HuiYi.data.Vote;
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



public class VoteOperate {
    private static VoteOperate instance;
    private DBHelper mDBHelper;
//    private Context mContext;

    private SQLiteDatabase mDatabase;

    private VoteOperate(Context context) {
        mDBHelper = DBHelper.getInstance(context);
    }
    public static VoteOperate getInstance(Context context){
        if (instance == null){
            synchronized (VoteOperate.class){
                if (instance == null){
                    instance = new VoteOperate(context);
                }
            }
        }
        return instance;
    }

    /**
     * 向数据库中插入 投票列表的索引以及投票列表
     * @param voteList 投票列表
     */
    public void insertVoteList(List<Vote> voteList){
        byte[] data = ObjectTransverter.ListToByteArr(voteList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_INDEX, Constant.COLUMNS_VOTE_INDEX);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_LIST,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.insert(PersistenceContract.ColumnsName.TABLE_NAME_VOTE,null,values);

        mDatabase.update(PersistenceContract.ColumnsName.TABLE_NAME_VOTE,values,
                PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_INDEX+"=?",new String[]{Constant.COLUMNS_VOTE_INDEX+""});
    }

    /**
     * 向数据库中更新 投票列表的索引以及投票列表
     * @param voteList 投票列表
     */
    public void updateVoteList(List<Vote> voteList){
        byte[] data = ObjectTransverter.ListToByteArr(voteList);
        ContentValues values =new ContentValues();
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_INDEX, Constant.COLUMNS_VOTE_INDEX);
        values.put(PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_LIST,data);
        mDatabase = mDBHelper.getReadableDatabase();
        mDatabase.update(PersistenceContract.ColumnsName.TABLE_NAME_VOTE,values,
                PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_INDEX+"=?",new String[]{Constant.COLUMNS_VOTE_INDEX+""});
    }

    /**
     * 根据投票列表的索引，向数据库中查询对应的投票列表
//     * @param voteIndex  投票列表的索引  值应传   Constant.COLUMNS_VOTE_INDEX
     * @return  投票列表
     */
    public List<Vote> queryVoteList(){
        List<Vote> documents = null;
        mDatabase = mDBHelper.getReadableDatabase();
        String sql = "select * from " + PersistenceContract.ColumnsName.TABLE_NAME_VOTE+ " where " +
                PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_INDEX + " = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{Constant.COLUMNS_VOTE_INDEX + ""});
        if ( cursor.moveToFirst()){
            byte[] data = cursor.getBlob(cursor.getColumnIndex(
                    PersistenceContract.ColumnsName.COLUMN_NAME_VOTE_LIST));
            documents = (List<Vote>) ObjectTransverter.byteArrToList(data);
        }
        cursor.close();
        return documents;
    }

}
