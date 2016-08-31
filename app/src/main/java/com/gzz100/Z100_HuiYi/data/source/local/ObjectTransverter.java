package com.gzz100.Z100_HuiYi.data.source.local;

import com.gzz100.Z100_HuiYi.data.File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by XieQXiong on 2016/8/25.
 */
public class ObjectTransverter {

    public static byte[] FileListToByteArr(List<File> filelList){
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

    public static List<File> byteArrToFileList(byte[] data){
        List<File> files = null;
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            files = (List<File>) inputStream.readObject();
            inputStream.close();
            arrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }
}