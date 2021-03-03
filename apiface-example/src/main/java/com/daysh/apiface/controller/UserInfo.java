package com.daysh.apiface.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * UserInfo的包装类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @model 用户信息
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
public class UserInfo {

    /**
     *用户id，该登陆用户唯一的值
     * @required
     */
    @NotBlank
    private String userId;
    /**
     * 用户昵称
     */
    @Size(max=30)
    private String nickname;
    /**
     * 性别 1男  2女 0 保密
     */
    private Integer gender;
    /**
     * 头像图片
     */
    private String avatarUrl;

    /**
     * 个性签明
     */
    @Size(max=300)
    private String signature;
    /**
     * 生日 eq:1995-12-12
     */
    private String birthday;

    /**
     * focusId
     * @hidden
     */
    @JsonIgnore
    private String focusId;

    /**
     * focusId
     * @hidden
     */
    @JsonIgnore
    private Boolean focus;

    /**
     * 关注状态-只读，true 相互关注 - false  已关注  -  null  未关注
     */
    public Boolean getStatus() {
        return null;
    }

    public UserInfo() {
    }
    public UserInfo(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getFocusId() {
        return focusId;
    }

    public void setFocusId(String focusId) {
        this.focusId = focusId;
    }

    public Boolean getFocus() {
        return focus;
    }

    public void setFocus(Boolean focus) {
        this.focus = focus;
    }

}