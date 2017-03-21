package com.gzz100.Z100_HuiYi.timerService;

/**
 * @author XieQingXiong
 * @description  控制议程时间的命令
 * @packageName com.gzz100.Z100_HuiYi.timerService
 * @className
 * @time 2017/3/13   14:53
 */

public class AgendaOrder {
    private int flag;
    private int agendaIndex;
    private int fileIndex;

    private int continueMin;
    private int continueSec;

    public int getContinueMin() {
        return continueMin;
    }

    public void setContinueMin(int continueMin) {
        this.continueMin = continueMin;
    }

    public int getContinueSec() {
        return continueSec;
    }

    public void setContinueSec(int continueSec) {
        this.continueSec = continueSec;
    }

    public int getAgendaIndex() {
        return agendaIndex;
    }

    public void setAgendaIndex(int agendaIndex) {
        this.agendaIndex = agendaIndex;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    public AgendaOrder(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
