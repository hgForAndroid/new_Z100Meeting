package com.gzz100.Z100_HuiYi.data;

/**
 * Created by XieQXiong on 2016/11/2.
 */

public class Option extends BaseBean{
    private int optionID;
    private String optionItem;

    public Option(int optionID, String optionItem) {
        this.optionID = optionID;
        this.optionItem = optionItem;
    }

    public int getOptionID() {
        return optionID;
    }

    public void setOptionID(int optionID) {
        this.optionID = optionID;
    }

    public String getOptionItem() {
        return optionItem;
    }

    public void setOptionItem(String optionItem) {
        this.optionItem = optionItem;
    }
}
