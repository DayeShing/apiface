package io.github.dayeshing.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import io.github.dayeshing.apiface.core.enums.VariableEnum;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

/**
 * @Description: <p> TODO </p>
 * @ClassName: response
 * @ClassAllName: io.github.dayeshing.apiface.core.swagger.v2.response
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

    private boolean array;

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

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("description",description);
        json.put("type",type);
        if(array){
            JSONObject items = new JSONObject();
            json.put("type", VariableEnum.ARRAY.getType());
            if(ObjectUtil.isNotEmpty(ref)){
                items.put("$ref",ref);
            }
            items.put("type",type);
            json.put("items",items);
        }else if(ObjectUtil.isNotEmpty(ref)){
            json.put("$ref",ref);
        }
        return json;
    }
}
