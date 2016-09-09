package com.gzz100.Z100_HuiYi.data.db;

import android.provider.BaseColumns;

/**
 * 存放数据库中的字段
 * Created by XieQXiong on 2016/8/25.
 */
public class PersistenceContract {

    public static abstract class ColumnsName implements BaseColumns {
        public static final String TABLE_NAME = "meeting";
        public static final String COLUMN_NAME_AGENDA_INDEX = "agenda_index";
        public static final String COLUMN_NAME_FILE_LIST = "file_list";
        public static final String COLUMN_NAME_AGENDAS = "agendas";
        public static final String COLUMN_NAME_AGENDA_LIST = "agenda_list";
        public static final String COLUMN_NAME_USERS = "users";
        public static final String COLUMN_NAME_USERS_LIST = "user_list";
        public static final String COLUMN_NAME_MEETING_INFO = "info";
        public static final String COLUMN_NAME_MEETING_INFO_DATA = "meeting_info";
    }
}
