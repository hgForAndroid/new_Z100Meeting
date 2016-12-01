package com.gzz100.Z100_HuiYi.data.eventBean;

/**
 * 会议进行中，有人临时进入会议
 * Created by XieQXiong on 2016/11/18.
 */

public class PeopleIn {
    private boolean isPeopleIn;
    private String deviceIp;

    /**
     * 会中临时有人进入，提醒主持人端设备，在FileDetailActivity中会接收到信息，
     * 进行处理
     * @param isPeopleIn  是否有人进入。
     * @param deviceIp    该人员所持设备的IP，作为key，在存储的ip的Map中查找到Socket，进行消息发送。
     */
    public PeopleIn(boolean isPeopleIn, String deviceIp) {
        this.isPeopleIn = isPeopleIn;
        this.deviceIp = deviceIp;
    }

    /**
     * true,会议进行中，有人进入
     * @return
     */
    public boolean isPeopleIn() {
        return isPeopleIn;
    }

    public void setPeopleIn(boolean peopleIn) {
        isPeopleIn = peopleIn;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
}
