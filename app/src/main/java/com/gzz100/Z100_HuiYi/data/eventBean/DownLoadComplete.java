package com.gzz100.Z100_HuiYi.data.eventBean;

/**
 * 文件下载完成，EventBus发送该实体类作为标识
 * Created by XieQXiong on 2016/11/11.
 */

public class DownLoadComplete {
    private boolean isDone;
    private String fileName;

    public DownLoadComplete(boolean isDone, String fileName) {
        this.isDone = isDone;
        this.fileName = fileName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
