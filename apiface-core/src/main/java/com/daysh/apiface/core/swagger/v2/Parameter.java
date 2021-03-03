
/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.swagger.v2
 * @Author： Daye Shing
 * @CreateTime： 2021-02-27 14:32
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import com.daysh.apiface.core.enums.ParamEnum;
import com.daysh.apiface.core.util.ObjectUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @Description: <p> TODO </p>
 * @ClassName: Parameter
 * @ClassAllName: com.daysh.apiface.core.swagger.v2.Parameter
 * @Author： Daye Shing
 */
public class Parameter implements JsonApi{

    private String name;
    private ParamEnum in = ParamEnum.FORM;
    private String description;
    private boolean required;
    private String type;

    //default
    private  String example;
    private  String format;
    private  String ref;
    private List<Parameter> parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParamEnum getIn() {
        return in;
    }

    public void setIn(ParamEnum in) {
        this.in = in;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("in",in.name().toLowerCase());
        json.put("description",description);
        json.put("required",required);
        json.put("type",type);
        json.put("default",example);
        json.put("format",format);
        if(StringUtils.isNotEmpty(ref) || ObjectUtil.isNotEmpty(parameters)){
            JSONObject schema = new JSONObject();
            if(StringUtils.isNotEmpty(ref)){
                schema.put("$ref",ref);
            }
            if(ObjectUtil.isNotEmpty(parameters)){
                List<JSONObject> ret = new ArrayList<>();
                for (Parameter parameter : parameters) {
                    ret.add(parameter.toJSON());
                }
                schema.put("parameters",ret);
            }
            json.put("schema",schema);
        }
        return json;
    }
}
