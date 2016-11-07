package com.gzz100.Z100_HuiYi.data;

/**
 * Created by XieQXiong on 2016/11/6.
 */

public class VoteResult {
    private String optionItem;//选项名称
    private int voteNum;//选项获得的票数

    public VoteResult(String optionItem, int voteNum) {
        this.optionItem = optionItem;
        this.voteNum = voteNum;
    }

    public String getOptionItem() {
        return optionItem;
    }

    public void setOptionItem(String optionItem) {
        this.optionItem = optionItem;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
