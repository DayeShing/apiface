package com.daysh.apiface.controller;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * FocusUser的包装类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @model 关注用户
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
public class FocusUser implements Serializable {

    private static final long serialVersionUID = -1832376890165664L;

    public static final String TABLE_NAME = "FOCUS_USER";
    public static final String FIELD_OPEN_ID = "userId";
    public static final String FIELD_FOCUS_ID = "focusId";

    /**
     * 关注用户id，该登陆用户唯一的值
     */
    private String userId;

    /**
     * 被关注用户id，该登陆用户唯一的值(必须)
     * @required
     */
    @NotBlank
    private String focusId;

    /**
     * 只读-被别人关注数量
     */
    private int focus;

    /**
     * 只读-关注别人的数量
     */
    private int toFocus;

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getFocusId() {
        return focusId;
    }

    public void setFocusId(String focusId) {
        this.focusId = focusId;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public int getToFocus() {
        return toFocus;
    }

    public void setToFocus(int toFocus) {
        this.toFocus = toFocus;
    }
}
