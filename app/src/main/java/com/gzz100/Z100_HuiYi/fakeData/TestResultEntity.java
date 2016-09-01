package com.gzz100.Z100_HuiYi.fakeData;

/**
 * 回调信息统一封装类
 * Created by WZG on 2016/7/16.
 */
public class TestResultEntity<T> {
    //  判断标示
    private int count;
    private int start;
    private int total;
    //显示数据（用户需要关心的数据）
    private T books;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getBooks() {
        return books;
    }

    public void setBooks(T books) {
        this.books = books;
    }
}
