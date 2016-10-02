package com.gzz100.Z100_HuiYi.multicast;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Lee on 2016/9/7.
 */
public class MulticastController {
    private MulticastSocket socket;
    private InetAddress address;

    /**
     * portNumber 端口号
     * ipAddress ip地址
     * TTL TimeToLive
     */
    private int portNumber = 30001;
    private String ipAddress = "239.0.0.1";
    private int TTL = 15;

    private static MulticastController INSTANCE;

    private MulticastController(int portNumber, String ipAddress, int TTL){
        this.portNumber = portNumber;
        this.ipAddress = ipAddress;
        this.TTL = TTL;

        try {
            initSocket();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initSocket() throws IOException{
        socket = new MulticastSocket(portNumber);
        address = InetAddress.getByName(ipAddress);
        socket.joinGroup(address);//加入多播组，发送方和接受方处于同一组时，接收方可抓取多播报文信息
        socket.setTimeToLive(TTL);//设定TTL
    }

    /**
     * 得到默认的控制器
     * @return 组播控制器
     */
    public static MulticastController getDefault(){
        if(INSTANCE == null){
            INSTANCE = new MulticastController(30001, "239.0.0.1", 15);
        }
        return INSTANCE;
    }

    /**
     * 创建自定义控制器
     * @param portNumber 端口号
     * @param ipAddress ip地址
     * @param TTL TTL
     * @return 组播控制器
     */
    public static MulticastController buildNew(int portNumber, String ipAddress, int TTL){
        if(INSTANCE == null){
            INSTANCE = new MulticastController(portNumber, ipAddress, TTL);
        }
        return INSTANCE;
    }

    /**
     * 发送组播信息
     * @param msg 信息内容
     */
    public void sendMessage(String msg){
        try {
            byte[] buff = msg.getBytes("utf-8");//设定多播报文的数据

            // 设定UDP报文（内容，内容长度，多播组，端口）
            DatagramPacket packet = new DatagramPacket(buff,buff.length,address,30001);
            socket.send(packet);//发送报文

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 发送组播信息
     * @param msg 信息内容
     */
    public void sendMulticaseBean(MulticaseBean msg){
        try {
            byte[] bytes = ParseObject.objectToBytes(msg);
//            byte[] buff = msg.getBytes("utf-8");//设定多播报文的数据

            // 设定UDP报文（内容，内容长度，多播组，端口）
            DatagramPacket packet = new DatagramPacket(bytes,bytes.length,address,30001);
            socket.send(packet);//发送报文

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 当程序结束之后关闭Socket
     */
    public void closeController(){
        if(socket != null)
            socket.close();
    }
}
