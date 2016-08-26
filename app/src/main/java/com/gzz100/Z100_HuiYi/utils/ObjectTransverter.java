package com.gzz100.Z100_HuiYi.utils;

import com.gzz100.Z100_HuiYi.data.FileBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieQXiong on 2016/8/25.
 */
public class ObjectTransverter {

    public static byte[] FileListToByteArr(List<FileBean> filelList){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(filelList);
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

    public static List<FileBean> byteArrToFileList(byte[] data){
        List<FileBean> files = null;
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            files = (List<FileBean>) inputStream.readObject();
            inputStream.close();
            arrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }
}