package com.gzz100.Z100_HuiYi.meeting.vote;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.data.RepositoryUtil;
import com.gzz100.Z100_HuiYi.data.Vote;
import com.gzz100.Z100_HuiYi.data.vote.VoteDataSource;
import com.gzz100.Z100_HuiYi.meeting.MainContract;
import com.gzz100.Z100_HuiYi.utils.Constant;
import com.gzz100.Z100_HuiYi.utils.MPhone;
import com.gzz100.Z100_HuiYi.utils.ScreenSize;
import com.gzz100.Z100_HuiYi.utils.SharedPreferencesUtil;
import com.gzz100.Z100_HuiYi.utils.ToastUtil;

import java.util.List;

/**
 * Created by Lee on 2016/10/31.
 */

public class VoteListDialog extends AlertDialog implements OnAllVoteItemClickListener{
    //widget
    private RecyclerView voteListRecyclerView;
    private Context mContext;
    private OnAllVoteItemClickListener mOnAllVoteItemClickListener;
    private LinearLayout mRootLayout;
    private ImageView mImgDeleteDialog;
    //投票对话框上的 X 按钮的接口
    private IVoteDialogDelete mIVoteDialogDelete;

    public IVoteDialogDelete getIVoteDialogDelete() {
        return mIVoteDialogDelete;
    }

    public void setIVoteDialogDelete(IVoteDialogDelete IVoteDialogDelete) {
        mIVoteDialogDelete = IVoteDialogDelete;
    }

    public void setOnAllVoteItemClickListener(OnAllVoteItemClickListener onAllVoteItemClickListener) {
        mOnAllVoteItemClickListener = onAllVoteItemClickListener;
    }

    //data
    private List<Vote> mVoteList;

    private int voteId;

    public VoteListDialog(Context context){
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vote_list);
        mRootLayout = (LinearLayout) findViewById(R.id.id_vote_list_root_layout);
        mImgDeleteDialog = (ImageView) mRootLayout.findViewById(R.id.id_img_delete_vote_dialog);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ScreenSize.getScreenWidth(mContext)*4/5,ScreenSize.getScreenHeight(mContext)*3/4);
        mRootLayout.setLayoutParams(params);
        RepositoryUtil.getVoteRepository(mContext).getAllVoteInf("", new VoteDataSource.LoadAllVoteInfCallBack() {
            @Override
            public void onAllVoteLoaded(List<Vote> voteList) {
                mVoteList = voteList;
                showAllVoteInf();
            }

            @Override
            public void onDataNotAvailable() {
                ToastUtil.showMessage("没有投票文件。");
            }
        });

        mImgDeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIVoteDialogDelete != null){
                    mIVoteDialogDelete.deleteVoteDialog(v);
                }

            }
        });
    }

    private void showAllVoteInf(){
        voteListRecyclerView = (RecyclerView) findViewById(R.id.id_rev_vote_list);
        AllVoteListAdapter adapter = new AllVoteListAdapter(getContext(), mVoteList);
        adapter.setmOnAllVoteItemClickListener(this);
        voteListRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        voteListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onVoteStartStopButtonClick(final View view, final int position) {
        voteId = mVoteList.get(position).getVoteID();
        //开启投票
        mOnAllVoteItemClickListener.onVoteStartStopButtonClick(view,position);
    }

    @Override
    public void onCheckResultButtonClick(View view, int position) {
        voteId = mVoteList.get(position).getVoteID();
        //查看结果
        mOnAllVoteItemClickListener.onCheckResultButtonClick(view,position);
    }
    //获取当前选择的投票id
    public int getVoteId(){
        return this.voteId;
    }
}
