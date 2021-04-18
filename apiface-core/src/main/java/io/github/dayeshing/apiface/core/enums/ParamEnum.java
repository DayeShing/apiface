package io.github.dayeshing.apiface.core.enums;

/**
 * @Description: <p> 参数枚举类 </p>
 * @ClassName: ParamEnum
 * @ClassAllName: io.github.dayeshing.apiface.core.enums.ParamEnum
 * @Author： Daye Shing
 */
public enum ParamEnum {

    BODY,FORM,PATH,HEAD,COOKIE;

    public static ParamEnum of(String type){
        for (ParamEnum value : values()) {
            if(value.name().equalsIgnoreCase(type)){
                return value;
            }
        }
        return FORM;
    }

}
