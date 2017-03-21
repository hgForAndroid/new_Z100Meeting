package com.gzz100.Z100_HuiYi.timerService;

/**
 * @author XieQingXiong
 * @description 控制会议时间的命令
 * @packageName com.gzz100.Z100_HuiYi.timerService
 * @className
 * @time 2017/3/16   8:50
 */

public class MeetingOrder {
    private int orderType;
    private int hour;
    private int min;

    /**
     *
     * @param orderType  命令类型
     */
    public MeetingOrder(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderType() {
        return orderType;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
