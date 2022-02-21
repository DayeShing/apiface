/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.meta
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 17:19
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.api.meta;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: <p> 字段类 </p>
 * @ClassName: Field
 * @ClassAllName: io.github.dayeshing.apiface.core.meta.Field
 * @Author： Daye Shing
 * @Date： 2020-08-15 17:19
 */
public class Field implements Meta, Serializable {

    private static final long serialVersionUID = -21L;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 默认值
     */
    private String example;

    /**
     * 是否需要
     */
    private boolean required;

    /**
     * 需要组
     */
    private List<String> groups;

    /**
     * 关联对象 - 这里将指向一个FeildGroup
     */
    private String ref;
    /**
     * 参数类型
     */
    private String type;

    /**
     * 格式化
     */
    private String format;

    /**
     * 隐藏
     */
    private boolean ignore;

    /**
     * 是否过期，通过计算获取
     */
    private boolean deprecated;

    /**
     * 只读
     */
    private boolean onlyRead = true;

    /**
     * 是否基本类型
     */
    private boolean base;

    /**
     * 泛型，类型参数
     */
    private boolean generic;

    /**
     * 是否数组
     */
    private boolean array;

    private boolean exists;

    @Override
    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    @Override
    public boolean isHidden() {
        return ignore;
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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
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

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public boolean isScope() {
        if(type == null){
            return false;
        }
        return !type.equals(ref);
    }

    public boolean isOnlyRead() {
        return onlyRead;
    }

    public void setOnlyRead(boolean onlyRead) {
        this.onlyRead = onlyRead;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
