/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.meta
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 17:18
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.api.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @Description: <p> 字段类 </p>
 * @ClassName: FieldGroup
 * @ClassAllName: io.github.dayeshing.apiface.core.meta.FieldGroup
 * @Author： Daye Shing
 * @Date： 2020-08-15 17:18
 */
public class FieldGroup implements Meta, Serializable {

    private static final long serialVersionUID = -2L;

    private String name;
    private String desc;

    /**
     * 显示名称
     */
    private String tag;

    /**
     * 这里用于继承类-可以递归获取继承对象中的字段
     */
    private List<String> ref;

    /**
     * 字段
     */
    private List<Field> fields;

    /**
     * 是否过期，通过计算获取
     */
    private boolean deprecated;

    private boolean hidden;

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getRef() {
        return ref;
    }

    public void setRef(List<String> ref) {
        this.ref = ref;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldGroup that = (FieldGroup) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
