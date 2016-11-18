package com.gzz100.Z100_HuiYi.data.eventBean;

/**
 * 会议进行中，有人临时进入会议
 * Created by XieQXiong on 2016/11/18.
 */

public class PeopleIn {
    private boolean isPeopleIn;

    public PeopleIn(boolean isPeopleIn) {
        this.isPeopleIn = isPeopleIn;
    }

    /**
     * true,会议进行中，有人进入
     * @return
     */
    public boolean isPeopleIn() {
        return isPeopleIn;
    }

    public void setPeopleIn(boolean peopleIn) {
        isPeopleIn = peopleIn;
    }
}
