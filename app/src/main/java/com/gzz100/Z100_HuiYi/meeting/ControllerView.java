package com.gzz100.Z100_HuiYi.meeting;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gzz100.Z100_HuiYi.BaseActivity;
import com.gzz100.Z100_HuiYi.R;

/**
 * Created by XieQXiong on 2016/10/26.
 */

public class ControllerView extends LinearLayout implements View.OnClickListener {

    private ImageView mIvSlideToRight;
    private Button mBtnStartMeeting;
    private Button mBtnPauseMeeting;
    private Button mBtnStartVote;
    private Button mBtnVoteResult;
    private View mController_view;

    public ControllerView(Context context) {
        super(context);
        initView(context);
    }

    private static ControllerView instance = null;

    public static ControllerView getInstance(Context context) {
        if (instance == null){
            synchronized (ControllerView.class) {
                if (instance == null) {
                    instance = new ControllerView(context);
                }
            }
        }
        return instance;
    }
    private void initView(Context context) {
        mController_view = View.inflate(context, R.layout.meeting_control_slide_layout, this);
        mIvSlideToRight = (ImageView) findViewById(R.id.id_iv_slide_to_right);
        mBtnStartMeeting = (Button) findViewById(R.id.id_btn_start_meeting);
        mBtnPauseMeeting = (Button) findViewById(R.id.id_btn_pause_meeting);
        mBtnStartVote = (Button) findViewById(R.id.id_btn_start_vote);
        mBtnVoteResult = (Button) findViewById(R.id.id_btn_vote_result);

        mIvSlideToRight.setOnClickListener(this);
        mBtnStartMeeting.setOnClickListener(this);
        mBtnPauseMeeting.setOnClickListener(this);
        mBtnStartVote.setOnClickListener(this);
        mBtnVoteResult.setOnClickListener(this);
    }

    /**
     * 设置开始按钮的文字显示
     * @param content
     */
    public void setBeginAndEndText(String content){
        mBtnStartMeeting.setText(content);
    }
    /**
     * 设置暂停按钮的文字显示
     * @param content
     */
    public void setPauseAndContinueText(String content){
        mBtnPauseMeeting.setText(content);
    }
    /**
     * 设置投票按钮的文字显示
     * @param content
     */
    public void setVoteAndEndVoteText(String content){
        mBtnStartVote.setText(content);
    }

    /**
     * 获取投票/结束投票的按钮当前显示的文字
     * @return
     */
    public String getVoteAndEndVoteText(){
        return mBtnStartVote.getText().toString();
    }
    public String getBeginAndEndText(){
        return mBtnStartMeeting.getText().toString();
    }

    public void setStartAndEndButtonNotClickable(boolean clickable){
        mBtnStartMeeting.setClickable(clickable);
    }
    public void setPauseAndContinueButtonNotClickable(boolean clickable){
        mBtnPauseMeeting.setClickable(clickable);
    }
    public void setStartVoteButtonNotClickable(boolean clickable){
        mBtnStartVote.setClickable(clickable);
    }
    public void setVoteResultButtonNotClickable(boolean clickable){
        mBtnVoteResult.setClickable(clickable);
    }



    //是否已经隐藏
    private boolean isHide = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_iv_slide_to_right://向右缩

                if (isHide){//滑出
                    ObjectAnimator.ofFloat(mController_view,"translationX",
                            mController_view.getWidth()-v.getWidth(),0.0f).setDuration(300).start();
                    ((ImageView)v).setImageResource(R.drawable.file_detail_slide_to_right);
                }else {//隐藏
                    ObjectAnimator.ofFloat(mController_view,"translationX",0.0f,
                            mController_view.getWidth()-v.getWidth()).setDuration(300).start();
                    ((ImageView)v).setImageResource(R.drawable.file_detail_slide_to_left);
                }
                isHide = !isHide;
                break;
            case R.id.id_btn_start_meeting://开始
                mIOnControllerListener.startMeeting(v);
                break;
            case R.id.id_btn_pause_meeting://暂停
                mIOnControllerListener.pauseMeeting(v);
                break;
            case R.id.id_btn_start_vote://投票
                mIOnControllerListener.startVote(v);
                break;
            case R.id.id_btn_vote_result://投票结果
                mIOnControllerListener.voteResult(v);
                break;

        }
    }

    public interface IOnControllerListener{
        /**
         * 开始会议，点击后控件文字变为“结束”
         * @param view
         */
        void startMeeting(View view);
        /**
         * 暂停会议，点击后控件文字变为“继续”
         * @param view
         */
        void pauseMeeting(View view);
        /**
         * 投票，如果在主界面点击投票，则文字要变为结束投票，
         * 若在文件详情界面点击，则不变，而跳转到到主界面的投票界面
         * @param view
         */
        void startVote(View view);
        /**
         * 投票结果，显示投票结果
         * @param view
         */
        void voteResult(View view);
    }
    private IOnControllerListener mIOnControllerListener;
    public void setIOnControllerListener(IOnControllerListener IOnControllerListener) {
        mIOnControllerListener = IOnControllerListener;
    }
}
