package com.gzz100.Z100_HuiYi.network.fileDownLoad.downLoad;

/**
 * 下载进度listener
 * Created by JokAr on 16/5/11.
 */
public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
