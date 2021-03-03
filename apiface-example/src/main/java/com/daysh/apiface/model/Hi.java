package com.daysh.apiface.model;

/**
 * Hi的包装类描述
 * @model 唯一显示名称
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 15:06
 */
public class Hi {

    /**
     * id
     * @required
     */
    private String id;
    /**
     * 名称
     * @example aa
     */
    private String name = "aa";

    public Hi() {
    }

    public Hi(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
