package com.gzz100.Z100_HuiYi.tcpController;

/**
 * 发送数据的控制器，要使用该类，必须在TcpService中onCreate方法中先调用setIControllerListener方法后，
 * 才能调用该类中的  sendMessage 方法进行发送数据的操作
 *
 * @author XieQXiong
 *         create at 2016/10/18 11:40
 */

public class ControllerUtil {
    private static ControllerUtil instance = null;

    private ControllerUtil() {
    }

    public static ControllerUtil getInstance() {
        if (instance == null) {
            synchronized (ControllerUtil.class) {
                if (instance == null) {
                    instance = new ControllerUtil();
                }
            }
        }
        return instance;
    }

    private IControllerListener mIControllerListener;

    /**
     * 这个方法必须先让实现了IControllerListener接口的类先调用
     * 后续才能调用该类中的sendMessage方法
     *
     * @param controllerListener 发送数据的接口
     */
    public void setIControllerListener(IControllerListener controllerListener) {
        mIControllerListener = controllerListener;
    }

    public IControllerListener getIControllerListener() {
        return mIControllerListener;
    }

    /**
     * 发送数据，数据类型为json字符串
     *
     * @param jsonBean json字符串
     */
    public void sendMessage(String jsonBean) {
        if (mIControllerListener != null)
            mIControllerListener.sendMessage(jsonBean);
    }
    /**
     * 向最近加入的设备发送消息发送数据，数据类型为json字符串
     *
     * @param jsonBean json字符串
     */
    public void sendLastSocketMessage(String deviceIp,String jsonBean) {
        if (mIControllerListener != null)
            mIControllerListener.sendLastSocketMessage(deviceIp,jsonBean);
    }

}
