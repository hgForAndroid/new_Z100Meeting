package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/8/31.
 */
public class DelegateBean extends BaseBean{
    private int delegateId;
    private String delegateName;
    private String delegateDepartment;
    private String delegateJob;
    private int role;
    private String delegateDetailInfo;
    private ArrayList<Integer> delegateAgendaList;

    public int getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(int delegateId) {
        this.delegateId = delegateId;
    }

    public String getDelegateJob() {
        return delegateJob;
    }

    public void setDelegateJob(String delegateJob) {
        this.delegateJob = delegateJob;
    }

    public String getDelegateDetailInfo() {
        return delegateDetailInfo;
    }

    public void setDelegateDetailInfo(String delegateDetailInfo) {
        this.delegateDetailInfo = delegateDetailInfo;
    }

    public ArrayList getDelegateAgendaList() {

        return delegateAgendaList;
    }

    public void setDelegateAgendaList(ArrayList delegateAgendaList) {
        this.delegateAgendaList = delegateAgendaList;
    }

    public String getDelegateDepartment() {

        return delegateDepartment;
    }

    public void setDelegateDepartment(String delegateDepartment) {
        this.delegateDepartment = delegateDepartment;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDelegateName() {

        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }


    /**
     * 为不同类型的用户设置不同的图片
     * @param context  上下文，用于加载Drawable
     * @param textView  显示当前人员的TextView
     */
    public void setPicForUser(Context context, TextView textView){
        Drawable drawable = null;
        if (getRole() == Constant.HOST){//主持人
            drawable = context.getResources().getDrawable(R.drawable.ic_host);

        }else if(getRole() == Constant.KEYNOTE_SPEAKER){//主讲人
            drawable = context.getResources().getDrawable(R.drawable.ic_keynote_speaker);

        }else {//普通参会人员
            drawable = context.getResources().getDrawable(R.drawable.ic_normal_delegate);

        }

        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        textView.setCompoundDrawables(null,drawable,null,null);

    }

    /**
     * 为当前参会人员设置选中的图片
     * @param context  上下文，用于加载Drawable
     * @param textView 显示当前人员的TextView
     */
    public void setCurrentDelegate(Context context, TextView textView){
        Drawable drawable = null;
        if (getRole() == Constant.HOST){//主持人
            drawable = context.getResources().getDrawable(R.drawable.ic_host_selected);

        }else if(getRole() == Constant.KEYNOTE_SPEAKER){//主讲人
            drawable = context.getResources().getDrawable(R.drawable.ic_keynote_speaker_selected);

        }else {//普通参会人员
            drawable = context.getResources().getDrawable(R.drawable.ic_normal_delegate_selected);

        }

        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        textView.setCompoundDrawables(null,drawable,null,null);

    }

}
