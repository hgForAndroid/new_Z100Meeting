package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.utils.Constant;

import java.util.List;

/**
 * Created by XieQXiong on 2016/10/31.
 */

public class DelegateModel extends BaseBean{
    private int delegateID;
    private String delegateName;
    private String delegateDepartment;
    private String delegateJob;
    private int delegateRole;
    private String delegateDetailInfo;
    private List<Integer> delegateAgendaIndexList;

    public int getSeatIndex() {
        return seatIndex;
    }

    public void setSeatIndex(int seatIndex) {
        this.seatIndex = seatIndex;
    }

    private int seatIndex;

    public int getDelegateID() {
        return delegateID;
    }

    public void setDelegateID(int delegateID) {
        this.delegateID = delegateID;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getDelegateDepartment() {
        return delegateDepartment;
    }

    public void setDelegateDepartment(String delegateDepartment) {
        this.delegateDepartment = delegateDepartment;
    }

    public String getDelegateJob() {
        return delegateJob;
    }

    public void setDelegateJob(String delegateJob) {
        this.delegateJob = delegateJob;
    }

    public int getDelegateRole() {
        return delegateRole;
    }

    public void setDelegateRole(int delegateRole) {
        this.delegateRole = delegateRole;
    }

    public String getDelegateDetailInfo() {
        return delegateDetailInfo;
    }

    public void setDelegateDetailInfo(String delegateDetailInfo) {
        this.delegateDetailInfo = delegateDetailInfo;
    }

    public List<Integer> getDelegateAgendaIndexList() {
        return delegateAgendaIndexList;
    }

    public void setDelegateAgendaIndexList(List<Integer> delegateAgendaIndexList) {
        this.delegateAgendaIndexList = delegateAgendaIndexList;
    }



    /**
     * 为不同类型的用户设置不同的图片
     * @param context  上下文，用于加载Drawable
     * @param textView  显示当前人员的TextView
     */
    public void setPicForUser(Context context, TextView textView){
        Drawable drawable = null;
        if (getDelegateRole() == Constant.HOSTS){//主持人
            drawable = context.getResources().getDrawable(R.drawable.icon_host_normal);

        }else if(getDelegateRole() == Constant.DEFAULT_SPEAKER){//主讲人
            drawable = context.getResources().getDrawable(R.drawable.icon_speaker_normal);

        }else {//普通参会人员
            drawable = context.getResources().getDrawable(R.drawable.icon_delagate_normal);

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
        if (getDelegateRole() == Constant.HOSTS){//主持人
            drawable = context.getResources().getDrawable(R.drawable.icon_host_selected);

        }else if(getDelegateRole() == Constant.DEFAULT_SPEAKER){//主讲人
            drawable = context.getResources().getDrawable(R.drawable.icon_speaker_selected);

        }else {//普通参会人员
            drawable = context.getResources().getDrawable(R.drawable.icon_delagate_selected);

        }

        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        textView.setCompoundDrawables(null,drawable,null,null);

    }
}
