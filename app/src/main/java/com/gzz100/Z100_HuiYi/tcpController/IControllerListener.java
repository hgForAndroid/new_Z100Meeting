package com.gzz100.Z100_HuiYi.tcpController;
/**
* TCP服务器端发送数据的接口，在TcpService服务中实现
* @author XieQXiong
* create at 2016/10/18 11:38
*/

public interface IControllerListener {
    /**
     * 发送数据，json字符串形式，由实体类Bean转成
     * @param message   发送的实体类json字符串
     */
    void sendMessage(String message);

    /**
     * 会议有人临时进入，发送消息给该人员设备，让其进入会议状态
     * @param deviceIp  设备ip
     * @param message   信息
     */
    void sendLastSocketMessage(String deviceIp,String message);
}
