package com.gzz100.Z100_HuiYi.multicast;
/**
* 该类为主持人端得到服务器ip,会议id，以及主持人端平板的IP地址后
 * 使用组播来发送到各个客户端的实体类
 * 该类包含了上面三个信息
* @author XieQXiong
* create at 2016/10/18 14:47
*/

public class KeyInfoBean {
    private String serverIP;
    private String meetingId;
    private String tcpServerIP;

    public KeyInfoBean(String serverIP, String meetingId, String tcpServerIP) {
        this.serverIP = serverIP;
        this.meetingId = meetingId;
        this.tcpServerIP = tcpServerIP;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getTcpServerIP() {
        return tcpServerIP;
    }

    public void setTcpServerIP(String tcpServerIP) {
        this.tcpServerIP = tcpServerIP;
    }
}
