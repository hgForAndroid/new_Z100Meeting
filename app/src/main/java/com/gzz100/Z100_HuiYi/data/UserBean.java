package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.widget.ImageView;

import com.gzz100.Z100_HuiYi.R;
import com.squareup.picasso.Picasso;

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

    public void setPicForUser(Context context, ImageView imageView){
        if (getType() == 1){//主持人
            Picasso.with(context).load(R.mipmap.ic_launcher).into(imageView);
        }else if (getType() == 2){//主讲人
            Picasso.with(context).load(R.mipmap.ic_launcher).into(imageView);
        }else {//其他参会人员
            Picasso.with(context).load(R.mipmap.ic_launcher).into(imageView);
        }

    }
}
