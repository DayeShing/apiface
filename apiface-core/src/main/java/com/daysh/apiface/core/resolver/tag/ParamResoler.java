package com.daysh.apiface.core.resolver.tag;

import com.daysh.apiface.core.api.meta.Param;
import com.daysh.apiface.core.enums.ParamEnum;
import com.daysh.apiface.core.enums.VariableEnum;
import com.daysh.apiface.core.resolver.TagResolver;
import com.daysh.apiface.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: <p> 参数解析 </p>
 * @ClassName: ParamResoler
 * @ClassAllName: com.daysh.apiface.core.resolver.tag.ParamResoler
 * @Author： Daye Shing
 */
public class ParamResoler implements TagResolver {

    @Override
    public List<Param> resolver(List<String> contents) {
        List<Param> params = new ArrayList<>();
        if(ObjectUtil.isNotEmpty(contents)){
            for (String content : contents) {
                params.add(resolver(content));
            }
        }
        return params;
    }

    protected Param resolver(String content){
        // 格式
        // 1.@param 参数名
        // 2.@param 参数名|参数类型
        // 3.@param 参数名|参数类型|参数描述
        // 4.@param 参数名|参数类型|是否必须 unrequired,|参数描述
        // 5.@param 参数名|参数类型|是否必须|默认值|参数描述
        // 6.@param 参数名|参数类型|是否必须|默认值|参数类型 body/form/path|参数描述
        String[] split = content.split("\\|");
        int len = split.length;
        Param param = new Param();
        param.setName(split[0].trim());
        param.setForm(ParamEnum.FORM);
        if(len > 1){
            param.setType(split[1].trim());
            if(len > 2){
                param.setDesc(split[len-1].trim());
                if(len > 3){
                    param.setRequired("required".equalsIgnoreCase(split[2].trim()) || "必须".equals(split[2].trim()));
                    if(len > 4){
                        param.setExample(split[3].trim());
                        if(len > 5){
                            param.setForm(ParamEnum.of(split[4].trim()));
                        }
                    }
                }
            }
        } else {
            param.setType(VariableEnum.OBJECT.getType());
            param.setRef(VariableEnum.OBJECT.getClazz());
        }
        return param;
    }
}
