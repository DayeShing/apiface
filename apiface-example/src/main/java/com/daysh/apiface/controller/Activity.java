package com.daysh.apiface.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Activity的包装类描述
 * @model 活动
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
public class Activity implements Serializable {


    private static final long serialVersionUID = -189632589016599664L;

    /**
     * 活动id
     */
    private String uuid;

    /**
     * 活动名称
     */
    @NotBlank
    private String activityName;

    /**
     * 开始时间
     */
    @NotNull
    private Date beginTime;

    /**
     * 结束时间
     */
    @NotNull
    private Date finishTime;

    /**
     * 图片
     */
    @NotBlank
    private String images;


    /**
     * 活动状态-枚举，2：待进行  1：进行中 0：抽奖中  3：已开奖   4：暂停  5.错误
     * @example 2
     */
    private String activityMode = "2";

    private String beginId;

    private String finishId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getActivityMode() {
        return activityMode;
    }

    public void setActivityMode(String activityMode) {
        this.activityMode = activityMode;
    }

    public String getBeginId() {
        return beginId;
    }

    public void setBeginId(String beginId) {
        this.beginId = beginId;
    }

    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }
}
