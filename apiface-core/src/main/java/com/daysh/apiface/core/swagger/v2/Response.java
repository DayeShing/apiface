package com.daysh.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import com.daysh.apiface.core.util.ObjectUtil;

/**
 * @Description: <p> TODO </p>
 * @ClassName: response
 * @ClassAllName: com.daysh.apiface.core.swagger.v2.response
 * @Author： Daye Shing
 */
public class Response implements JsonApi{

    /**
     * 复杂对象
     */
    private String ref;
    /**
     * 类型
     */
    private String type;

    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("description",description);
        if(ObjectUtil.isNotEmpty(ref)){
            json.put("$ref",ref);
        }
        if(ObjectUtil.isNotEmpty(type)){
            json.put("type",type);
        }
        return json;
    }
}
