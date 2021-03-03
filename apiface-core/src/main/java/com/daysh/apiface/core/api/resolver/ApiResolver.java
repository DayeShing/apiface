package com.daysh.apiface.core.api.resolver;

import com.daysh.apiface.core.comment.impl.ClassMark;
import com.daysh.apiface.core.api.meta.ActionGroup;
import com.daysh.apiface.core.api.meta.FieldGroup;

import java.util.List;
import java.util.Map;

/**
 * @Description: <p> api转换接口 </p>
 * @ClassName: ApiResolver
 * @ClassAllName: com.daysh.apiface.core.api.resolver.ApiResolver
 * @Author： Daye Shing
 * @Date： 2020-08-16 10:09
 */
public interface ApiResolver {


    /**
     * getActions
     * @description: <p> 获取访问 </p>
     * @methodName getActions
     * @param  -
     * @return java.util.List<com.daysh.apiface.core.api.meta.ActionGroup> -
     * @throws
     * @author Daye Shing
     * @date 2020/8/16 10:17
     * @since 1.0
     */

    Map<String,ActionGroup> getActions();

    /**
     * getFields
     * @description: <p> 获取模型字段类 </p>
     * @methodName getFields
     * @param  -
     * @return java.util.List<com.daysh.apiface.core.api.meta.FieldGroup> -
     * @throws
     * @author Daye Shing
     * @date 2020/8/16 10:16
     * @since 1.0
     */
    Map<String,FieldGroup> getFields();

    /**
     * resolver
     * @description: <p> 转换类 </p>
     * @methodName resolver
     * @param marks -
     * @return void -
     * @throws
     * @author Daye Shing
     * @date 2020/8/16 10:16
     * @since 1.0
     */
    void resolver(List<ClassMark> marks);

}
