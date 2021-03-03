/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.meta
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 17:04
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.api.meta;

import java.io.Serializable;

/**
 * @Description: <p> 接口返回类 </p>
 * @ClassName: Return
 * @ClassAllName: com.daysh.apiface.core.meta.Return
 * @Author： Daye Shing
 * @Date： 2020-08-15 17:04
 */
public class Return implements Meta, Serializable {

    private static final long serialVersionUID = -112L;

    /**
     * 返回值类型-可能是一个复杂类型
     */
    private String name;

    /**
     * 返回值描述
     */
    private String desc;

    /**
     * 返回类
     */
    private String type;

    /**
     * 返回格式处理
     */
    private String format;

    /**
     * 返回关联类，返回复杂对象时有效
     */
    private String ref;

    /**
     * 默认值
     */
    private String example;

    /**
     * 是否基本类型
     */
    private boolean base;

    public Return() {
    }

    public Return(String name) {
        this.name = name;
    }

    public Return(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isDeprecated() {
        return false;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }
}
