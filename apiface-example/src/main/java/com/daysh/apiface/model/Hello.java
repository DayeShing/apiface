package com.daysh.apiface.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Hello的 数组测试，文人多外号
 * @model Hello
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/13 5:20
 */
public class Hello {


    private List<String> names;

    /**
     * 哩视就
     */
    private List<Hi> His;

    private String[] nicknames;

    /**
     * 是否认证
     */
    private boolean auth;

    /**
     * 名称dd
     */
    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    /**
     * 外号fd
     */
    public String[] getNicknames() {
        return nicknames;
    }

    public void setNicknames(String[] nicknames) {
        this.nicknames = nicknames;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

//    List<String> stringList = new ArrayList<>();
//    List<Integer> integerList = new ArrayList<>();
//    List<Hi> hi = new ArrayList<>();
////
////    public static void main(String... args) throws Exception {
////        Field stringListField = Hello.class.getDeclaredField("stringList");
////        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
////        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
////        System.out.println(stringListClass); // class java.lang.String.
////
////        Field integerListField = Hello.class.getDeclaredField("integerList");
////        ParameterizedType integerListType = (ParameterizedType) integerListField.getGenericType();
////        Class<?> integerListClass = (Class<?>) integerListType.getActualTypeArguments()[0];
////        System.out.println(integerListClass); // class java.lang.Integer.
////
////        Field hi = Hello.class.getDeclaredField("hi");
////        ParameterizedType hiType = (ParameterizedType) hi.getGenericType();
////        Class<?> hiListClass = (Class<?>) hiType.getActualTypeArguments()[0];
////        System.out.println(hiListClass); // class java.lang.Integer.
////    }
//
//    public static void main(String[] args) throws NoSuchMethodException {
//        Collection<String> dd = new ArrayList<>();
//        Class<? extends Collection> aClass = dd.getClass();
//
//        Method toArray = aClass.getMethod("toArray");
//        System.err.println(toArray.getReturnType().getName());
////        Type[] realType = getRealType(dd.getClass());
////        for (Type type : realType) {
////            System.err.println(type.getTypeName());
////        }
//
//    }
//
//    public static Type[] getRealType(Class c){
//        // 获取当前new的对象的泛型的父类类型
//        ParameterizedType pt = (ParameterizedType) c.getGenericSuperclass();
//        // 获取第一个类型参数的真实类型
//        Type[] types = pt.getActualTypeArguments();
//        return types;
//    }


    public List<Hi> getHis() {
        return His;
    }

    public void setHis(List<Hi> his) {
        His = his;
    }
}
