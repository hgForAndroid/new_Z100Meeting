package com.gzz100.Z100_HuiYi.data;

import java.io.Serializable;

/**
 * Created by XieQXiong on 2016/8/24.
 */
public class FileBean implements Serializable{
    private String agendaIndex;
    private String fileIndex;
    private String fileName;
    private String fileSize;
    private String fileAddress;
    private String keyNoteSpeaker;

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

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public String getKeyNoteSpeaker() {
        return keyNoteSpeaker;
    }

    public void setKeyNoteSpeaker(String keyNoteSpeaker) {
        this.keyNoteSpeaker = keyNoteSpeaker;
    }




}
