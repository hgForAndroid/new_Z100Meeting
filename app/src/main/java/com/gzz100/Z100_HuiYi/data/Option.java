package com.gzz100.Z100_HuiYi.data;

/**
 * Created by XieQXiong on 2016/11/2.
 */

public class Option extends BaseBean{
    private int optionId;
    private String optionItem;

    public Option(int optionId, String optionItem) {
        this.optionId = optionId;
        this.optionItem = optionItem;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionItem() {
        return optionItem;
    }

    public void setOptionItem(String optionItem) {
        this.optionItem = optionItem;
    }
}
