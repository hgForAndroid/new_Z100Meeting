package com.gzz100.Z100_HuiYi.tcpController;

/**
 * Created by XieQXiong on 2016/11/15.
 */

public class ClientSendMessageUtil {
    private static ClientSendMessageUtil instance = null;

    private ClientSendMessageUtil() {
    }

    public static ClientSendMessageUtil getInstance() {
        if (instance == null) {
            synchronized (ClientSendMessageUtil.class) {
                if (instance == null) {
                    instance = new ClientSendMessageUtil();
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
}
