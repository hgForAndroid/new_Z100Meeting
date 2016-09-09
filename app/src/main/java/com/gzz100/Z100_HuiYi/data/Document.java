package com.gzz100.Z100_HuiYi.data;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class Document  extends BaseBean {
    private String agendaIndex;
    private String fileIndex;
    private String fileName;
    private String fileSize;
    private String fileURL;
    private String fileSpeaker;

    public String getAgendaIndex() {
        return agendaIndex;
    }

    public void setAgendaIndex(String agendaIndex) {
        this.agendaIndex = agendaIndex;
    }

    public String getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(String fileIndex) {
        this.fileIndex = fileIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getFileSpeaker() {
        return fileSpeaker;
    }

    public void setFileSpeaker(String fileSpeaker) {
        this.fileSpeaker = fileSpeaker;
    }

    public void setImageForFile(Context context,ImageView imageView){
        if (!this.fileName.isEmpty() && this.fileName.contains(".txt")){
            Picasso.with(context).load("http://p3.so.qhmsg.com/bdr/_240_/t0146723952b5d73a1d.png").into(imageView);
        }else if (!this.fileName.isEmpty() &&
                (this.fileName.contains(".doc") || this.fileName.contains(".docx"))){
            Picasso.with(context).load("http://p1.so.qhmsg.com/bdr/_240_/t01b16d40e4a51d6241.png").into(imageView);
        }else if (!this.fileName.isEmpty() && this.fileName.contains(".xlsx")){
            Picasso.with(context).load("http://p0.so.qhmsg.com/bdr/_240_/t01d509fc4c1cb60a9a.png").into(imageView);
        }
    }


}
