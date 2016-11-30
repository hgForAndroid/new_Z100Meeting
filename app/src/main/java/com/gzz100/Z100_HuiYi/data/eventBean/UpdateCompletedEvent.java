package com.gzz100.Z100_HuiYi.data.eventBean;

/**
 * 描述：数据更新完成，作为EventBus发送的对象类。避免使用基本数据类型，导致代码阅读性差
 * 包名：com.gzz100.Z100_HuiYi.data.eventBean
 * 类名：UpdateCompletedEvent
 *
 * @author XieQingXiong
 * @创建时间 2016/11/30   10:56
 */
public class UpdateCompletedEvent {
    /**
     * 更新完成
     */
    boolean updateCompleted;

    /**
     * 更新完成，该对象从WriteDatabaseService类中发出，由FileDetailActivity接收处理
     * @param updateCompleted  更新完成标识
     */
    public UpdateCompletedEvent(boolean updateCompleted) {
        this.updateCompleted = updateCompleted;
    }

    public boolean isUpdateCompleted() {
        return updateCompleted;
    }

    public void setUpdateCompleted(boolean updateCompleted) {
        this.updateCompleted = updateCompleted;
    }
}
