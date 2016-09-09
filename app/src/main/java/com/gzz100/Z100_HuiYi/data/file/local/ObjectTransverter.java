package com.gzz100.Z100_HuiYi.data.file.local;

import com.gzz100.Z100_HuiYi.data.Agenda;
import com.gzz100.Z100_HuiYi.data.BaseBean;
import com.gzz100.Z100_HuiYi.data.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by XieQXiong on 2016/8/25.
 */
public class ObjectTransverter {

    /**
     * 将对象数组转换成字节数组
     * @param objectList 对象数组
     * @return   字节数组
     */
    public static byte[] ListToByteArr(List<? extends BaseBean> objectList){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(objectList);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            return data ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 强字节数组转换成对象数组
     * @param data   字节数组
     * @return     对象数组
     */
    public static List<? extends BaseBean> byteArrToList(byte[] data){
        List<? extends BaseBean> documents = null;
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            documents = (List<? extends BaseBean>) inputStream.readObject();
            inputStream.close();
            arrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }

    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            obj = inputStream.readObject();
            inputStream.close();
            arrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static byte[] ObjectToByte(java.lang.Object obj) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            return data ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}