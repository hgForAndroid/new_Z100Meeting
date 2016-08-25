package com.gzz100.Z100_HuiYi.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by XieQXiong on 2016/8/25.
 */
public class FilePersistenceContract {

    public static abstract class FileListEntry implements BaseColumns {
        public static final String TABLE_NAME = "fileList";
        public static final String COLUMN_NAME_AGENDA_INDEX = "agenda_index";
        public static final String COLUMN_NAME_FILE_LIST = "file_list";
    }
}
