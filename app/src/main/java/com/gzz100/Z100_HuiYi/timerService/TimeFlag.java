package com.gzz100.Z100_HuiYi.timerService;

/**
 * @author XieQingXiong
 * @description  倒计时时间是否发送到文件详情页显示的带有标识值的类。
 * @packageName com.gzz100.Z100_HuiYi.timerService
 * @className
 * @time 2017/3/14   15:35
 */

public class TimeFlag {
    private boolean flag;

    /**
     * 倒计时时间是否发送到文件详情页显示的带有标识值的类。
     * @param flag   true，需要将消息发送给文件详情页显示；false则相反。
     */
    public TimeFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * true，需要将消息发送给文件详情页显示；false则相反。
     * @return
     */
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
