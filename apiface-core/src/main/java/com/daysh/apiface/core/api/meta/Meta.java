package com.daysh.apiface.core.api.meta;

/**
 * @Description: <p> 接口 </p>
 * @ClassName: Api
 * @ClassAllName: com.daysh.apiface.core.meta.Api
 * @Author： Daye Shing
 * @Date： 2020-08-15 16:42
 */
public interface Meta {

    String getName();

    String getDesc();

    boolean isDeprecated();

    boolean isHidden();
}
