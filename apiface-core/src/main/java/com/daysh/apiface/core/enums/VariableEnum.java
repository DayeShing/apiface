package com.daysh.apiface.core.enums;

import java.util.Date;

/**
 * @Description: <p> 变量枚举 </p>
 * @ClassName: VariableEnum
 * @ClassAllName: com.daysh.apiface.core.enums.VariableEnum
 * @Author： Daye Shing
 * @Date： 2020-08-16 09:37
 */
public enum VariableEnum {

    //base
    VOID("void","Void","java.lang.Void","",true,""),
    BYTE("byte","Byte","java.lang.Byte","",true,"0"),
    SHORT("short","Short","java.lang.Short","",true,"0"),

    INT("int","Integer","java.lang.Integer","int32",true,"0"),
    LONG("long","Long","java.lang.Long","int64",true,"0"),
    CHAR("char","Character","java.lang.Character","char",true,"a"),

    FLOAT("float","Float","java.lang.Float","number",true,"0"),
    DOUBLE("double","Double","java.lang.Double","number",true,"0"),
    BOOLEAN("boolean","Boolean","java.lang.Boolean","boolean",true,"false"),

    // compelx
    STRING("string","String","java.lang.String","",true,""),
    DATE("date","Date","java.util.Date","date",true,""),

    //包装
    ARRAY("array","Array","","",false,"[]"),
    OBJECT("object","Object","java.lang.Object","",false,"{}");

    public static VariableEnum of(String type){
        VariableEnum[] values = values();
        for (VariableEnum value : values) {
            if(value.getType().equals(type)){
                return value;
            }
            if(value.getAlias().equals(type)){
                return value;
            }
            if(value.getClazz().equals(type)){
                return value;
            }
        }
        return OBJECT;
    }

    /**
     * 类型
     */
    private String type;
    /**
     * 别名
     */
    private String alias;
    /**
     * 真实类
     */
    private String clazz;
    /**
     * 格式
     */
    private String format;
    /**
     * 基本类型
     */
    private boolean base;
    /**
     * 默认值
     */
    private String example;

    VariableEnum(String type, String alias, String clazz, String format, boolean base, String example) {
        this.type = type;
        this.alias = alias;
        this.clazz = clazz;
        this.format = format;
        this.base = base;
        this.example = example;
    }

    VariableEnum(String type, String format) {
        this.type = type;
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public java.lang.String getFormat() {
        return format;
    }

    public void setFormat(java.lang.String format) {
        this.format = format;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
