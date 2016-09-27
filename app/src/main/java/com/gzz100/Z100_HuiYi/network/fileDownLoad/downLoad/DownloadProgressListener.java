package com.gzz100.Z100_HuiYi.network.fileDownLoad.downLoad;

/**
* 文件下载进度监听器
* @author XieQXiong
* create at 2016/9/27 16:15
*/

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
