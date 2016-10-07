package com.gzz100.Z100_HuiYi.data.db;

import android.provider.BaseColumns;

/**
 * 存放数据库中的字段
 * Created by XieQXiong on 2016/8/25.
 */
public class PersistenceContract {

    public static abstract class ColumnsName implements BaseColumns {
        //文件表，放文件列表
        public static final String TABLE_NAME_FILE = "file";
        public static final String COLUMN_NAME_AGENDA_INDEX = "files";
        public static final String COLUMN_NAME_FILE_LIST = "file_list";
        //议程表，放议程列表
        public static final String TABLE_NAME_AGENDA = "agenda";
        public static final String COLUMN_NAME_AGENDAS = "agendas";
        public static final String COLUMN_NAME_AGENDA_LIST = "agenda_list";
/*        //人员表，放参会人员列表
        public static final String TABLE_NAME_DELEGATE = "delegate";
        public static final String COLUMN_NAME_USERS = "users";
        public static final String COLUMN_NAME_USERS_LIST = "user_list";*/
        //人员表，放参会人员列表
        public static final String TABLE_NAME_DELEGATE = "delegate";
        public static final String COLUMN_NAME_ROLE = "role";
        public static final String COLUMN_NAME_DELEGATE = "delegate";
        //会议概况表，放会议概况
        public static final String TABLE_NAME_MEETING_INFO = "meetingInfo";
        public static final String COLUMN_NAME_MEETING_INFO = "info";
        public static final String COLUMN_NAME_MEETING_INFO_DATA = "meeting_info";
    }
}
