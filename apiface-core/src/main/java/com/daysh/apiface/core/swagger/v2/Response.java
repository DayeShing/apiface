/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.swagger.v2
 * @Author： Daye Shing
 * @CreateTime： 2021-02-27 14:19
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.List;

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
    /**
     * 状态
     */
    private Status status = Status.OK;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        if(StringUtils.isNotEmpty(ref)){
            json.put("$ref",ref);
        }
        if(StringUtils.isNotEmpty(type)){
            json.put("type",type);
        }
        return json;
    }
}
