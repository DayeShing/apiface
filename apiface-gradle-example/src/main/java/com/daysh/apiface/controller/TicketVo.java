package com.daysh.apiface.controller;

/**
 * TicketVo的包装类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @model 用户统计
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
public class TicketVo {

    /**
     * 头像
     */
    private String userLogo;

    /**
     * 昵称
     */
    private String userName;

    /**
     * 累计获取票总数
     */
    private Integer total;

    /**
     * 标注信息-只读
     */
    private String remarks;

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
