package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.gzz100.Z100_HuiYi.R;
import com.gzz100.Z100_HuiYi.utils.Constant;

/**
 * Created by XieQXiong on 2016/9/2.
 */
public class UserBean {
    private int userId;
    private String userName;
    private String userTitle;
    private int type;

    public UserBean(int userId, String userName, String userTitle, int type) {
        this.userId = userId;
        this.userName = userName;
        this.userTitle = userTitle;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 为不同类型的用户设置不同的图片
     * @param context  上下文，用于加载Drawable
     * @param textView  显示当前人员的TextView
     */
    public void setPicForUser(Context context, TextView textView){
        Drawable drawable = null;
        if (getType() == Constant.HOST){//主持人
            drawable = context.getResources().getDrawable(R.drawable.ic_host);

        }else if(getType() == Constant.KEYNOTE_SPEAKER){//主讲人
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
        if (getType() == Constant.HOST){//主持人
            drawable = context.getResources().getDrawable(R.drawable.ic_host_selected);

        }else if(getType() == Constant.KEYNOTE_SPEAKER){//主讲人
            drawable = context.getResources().getDrawable(R.drawable.ic_keynote_speaker_selected);

        }else {//普通参会人员
            drawable = context.getResources().getDrawable(R.drawable.ic_normal_delegate_selected);

        }

        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        textView.setCompoundDrawables(null,drawable,null,null);

    }
}
