package com.daysh.apiface.controller;

/**
 * Page的描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @since 1.0
 */
public class Page {

    private int rows = 10;
    private int page = 1;
    private boolean roll;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isRoll() {
        return roll;
    }

    public void setRoll(boolean roll) {
        this.roll = roll;
    }
}
