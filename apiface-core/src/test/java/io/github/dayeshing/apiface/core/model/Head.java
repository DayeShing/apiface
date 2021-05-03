package io.github.dayeshing.apiface.core.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Head的包装类描述
 * @model 泛型测试
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/26 7:44
 */
public class Head extends GridResult<Hi>{

    /**
     * 泛型参数
     */
    private GridResult<String> name;

    public GridResult<String> getName() {
        return name;
    }

    public void setName(GridResult<String> name) {
        this.name = name;
    }

    public static void main(String[] args){
        Class<GridResult> result = GridResult.class;
//        System.err.println(result.get);
        System.err.println(result.getName());
        Type[] types = result.getGenericInterfaces();
        for (Type type : types) {
            System.err.println(type.getClass().getName());
        System.err.println(type instanceof ParameterizedType);
        }
//        System.err.println(types.getClass().getName());
//        System.err.println(types instanceof ParameterizedType);
        System.err.println("----------------------------");
        Class<Head> resultClass = Head.class;
        System.err.println(resultClass.getName());
        Type superclass = resultClass.getGenericSuperclass();
        System.err.println(superclass.getClass().getName());
        System.err.println(superclass instanceof ParameterizedType);
//        System.err.println(ParameterizedType.class.isAssignableFrom(superclass));
    }

}
