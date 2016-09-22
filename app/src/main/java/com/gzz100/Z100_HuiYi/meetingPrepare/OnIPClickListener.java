package com.gzz100.Z100_HuiYi.meetingPrepare;

/**
 * 输入IP地址界面，IP历史记录的点击监听
 * Created by XieQXiong on 2016/9/22.
 */
public interface OnIPClickListener {
    /**
     * 点击 序号为 position 的IP的监听
     * @param position
     */
    void onIPClick(int position);
    void onDelete(int position);
}
