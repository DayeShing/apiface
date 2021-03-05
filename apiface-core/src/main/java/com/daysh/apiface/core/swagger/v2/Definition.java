package com.daysh.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import com.daysh.apiface.core.util.ObjectUtil;

import java.util.Set;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Definition
 * @ClassAllName: com.daysh.apiface.core.swagger.v2.Definition
 * @Author： Daye Shing
 */
public class Definition implements JsonApi{

    private String description;

    /**
     * 显示名称
     */
    private String tag;

    /**
     * 是否过期，通过计算获取
     */
    private boolean deprecated;

    private String name;

    private String type;

    private Set<Propertie> properties;

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

    public Set<Propertie> getProperties() {
        return properties;
    }

    public void setProperties(Set<Propertie> properties) {
        this.properties = properties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("tag", tag);
        json.put("deprecated", deprecated);
        json.put("description", description);
        JSONObject arr = new JSONObject();
        if(ObjectUtil.isNotEmpty(properties)){
            for (Propertie property : properties) {
                arr.put(property.getName(),property.toJSON());
            }
        }
        json.put("properties",arr);
        return json;
    }
}
