package com.gzz100.Z100_HuiYi.meeting;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gzz100.Z100_HuiYi.R;

/**
 * Created by XieQXiong on 2016/10/26.
 */

public class ControllerView extends LinearLayout implements View.OnClickListener {

    private ImageView mIvSlideToRight;
    private Button mBtnStartMeeting;
    private Button mBtnPauseMeeting;
    private Button mBtnStartVote;
    private View mController_view;
    private Drawable drawable = null;


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

        mIvSlideToRight.setOnClickListener(this);
        mBtnStartMeeting.setOnClickListener(this);
        mBtnPauseMeeting.setOnClickListener(this);
        mBtnStartVote.setOnClickListener(this);
    }

    /**
     * 设置开始按钮的文字显示
     * @param content
     */
    public void setBeginAndEndText(String content){
        if ("开始".equals(content)){
            drawable = getResources().getDrawable(R.drawable.play_normal);
        }else {//结束
            drawable = getResources().getDrawable(R.drawable.end_normal);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        mBtnStartMeeting.setCompoundDrawables(null,drawable,null,null);
        mBtnStartMeeting.setText(content);
    }
    /**
     * 设置暂停按钮的文字显示
     * @param content
     */
    public void setPauseAndContinueText(String content){
        if ("暂停".equals(content)){
            drawable = getResources().getDrawable(R.drawable.pause_normal);
        }else {//继续/开会
            drawable = getResources().getDrawable(R.drawable.play_normal);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        mBtnPauseMeeting.setCompoundDrawables(null,drawable,null,null);
        mBtnPauseMeeting.setText(content);
    }
    /**
     * 设置投票按钮的文字显示
     * @param content
     */
    public void setVoteAndEndVoteText(String content){
//        if ("投票".equals(content)){
//            drawable = getResources().getDrawable(R.drawable.icon_vote_selected);
//        }else {//结束投票
//            drawable = getResources().getDrawable(R.drawable.icon_vote_selected);
//        }
//        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//        mBtnStartVote.setCompoundDrawables(null,drawable,null,null);
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

    public View getShowOrHideView(){
        if (mIvSlideToRight != null){
            return mIvSlideToRight;
        }
        return null;
    }

    public void showOrHide(View v){
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
    }

    //是否已经隐藏
    private boolean isHide = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_iv_slide_to_right://向右缩
                mOnHideControllerViewListener.hide(v);
//                showOrHide(v);
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

        }
    }

    public interface IOnHideControllerViewListener{
        void hide(View view);
    }
    private IOnHideControllerViewListener mOnHideControllerViewListener;

    public void setOnHideControllerViewListener(IOnHideControllerViewListener onHideControllerViewListener) {
        mOnHideControllerViewListener = onHideControllerViewListener;
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
    }
    private IOnControllerListener mIOnControllerListener;
    public void setIOnControllerListener(IOnControllerListener IOnControllerListener) {
        mIOnControllerListener = IOnControllerListener;
    }
}
