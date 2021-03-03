/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.swagger.v2
 * @Author： Daye Shing
 * @CreateTime： 2021-02-27 19:30
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import com.daysh.apiface.core.enums.VariableEnum;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Propertie
 * @ClassAllName: com.daysh.apiface.core.swagger.v2.Propertie
 * @Author： Daye Shing
 */
public class Propertie implements JsonApi {

    private String name;

    private String type;

    private String format;

    private String ref;

    /**
     * 描述
     */
    private String description;
    /**
     * 默认值
     */
    private String example;

    /**
     * 是否需要
     */
    private boolean required;

    /**
     * 忽视
     */
    private boolean ignore;

    /**
     * 是否过期，通过计算获取
     */
    private boolean deprecated;

    /**
     * 只读
     */
    private boolean onlyRead;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    //    private String enum;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public boolean isOnlyRead() {
        return onlyRead;
    }

    public void setOnlyRead(boolean onlyRead) {
        this.onlyRead = onlyRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Propertie propertie = (Propertie) o;
        return getName().equals(propertie.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("format", format);

        json.put("description", description);
        json.put("example", example);
        json.put("required", required);
        json.put("ignore", ignore);
        json.put("deprecated", deprecated);
        json.put("onlyRead", onlyRead);

        if (StringUtils.isNotEmpty(ref)) {
            if (VariableEnum.ARRAY.getType().equals(type)) {
                JSONObject items = new JSONObject();
                items.put("$ref", ref);
                json.put("items", items);
            }
            if (VariableEnum.OBJECT.getType().equals(type)) {
                json.put("$ref", ref);
            }
        }
        return json;
    }
}
