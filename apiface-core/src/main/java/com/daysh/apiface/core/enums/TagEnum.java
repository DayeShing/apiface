package com.daysh.apiface.core.enums;

import com.daysh.apiface.core.resolver.TagResolver;
import com.daysh.apiface.core.resolver.tag.*;
import com.daysh.apiface.core.util.ObjectUtil;

import java.util.List;
import java.util.Map;

/**
 * @Description: <p>标记枚举类 </p>
 * @ClassName: TagEnum
 * @ClassAllName: com.daysh.apiface.core.enums.TagEnum
 * @Author： Daye Shing
 */
public enum TagEnum {

    ACTION("@action", "接口", "group,group1", new GroupResolver()),
    PARAM("@param", "参数", "", new ParamResoler()),
    RETURN("@return", "返回值", "", new ReturnResolver()),
    EXCLUDE("@exclude", "排除参数", "param1,param2", new GroupResolver()),
    AUTHOR("@author", "作者", "author|contact", new AutherResolver()),
    SINCE("@since", "版本", "", null),
    PACK("@pack", "统一包装返回类", "", null),
    UNPACK("@unpack", "统一包装返回类", "", null),
    SUMMARY("@summary", "摘要", "", null),

    URI("@uri", "本地url处理", "/uri", null),
    CONSUME("@consume", "响应类行 doc,json,xml,...", "doc,json,xml", new GroupResolver()),
    PRODUCE("@produce", "请求类型 doc,json,xml,...", "doc,json,xml", new GroupResolver()),
    METHOD("@method", "请求方法 get,post", "get,post", new MethodGroupResolver()),

    MODEL("@model", "模型", "tag", null),
    REQUIRED("@required", "逻辑", "", null),
    IGNORE("@ignore", "忽略", "", null),
    FORMAT("@format", "格式化", "format", null),
    EXAMPLE("@example", "默认值", "example", null),

    HIDDEN("@hidden", "默认值", "", null),
    UNDEFINED("@undefined", "未定义", "undefined", null),
    ;

    /**
     * 标记名称
     */
    private String tagName;
    /**
     * 名称
     */
    private String desc;
    /**
     * 格式
     */
    private String format;

    /**
     * 处理类
     */
    private TagResolver resolver;

    TagEnum(String tagName, String desc, String format, TagResolver resolver) {
        this.tagName = tagName;
        this.desc = desc;
        this.format = format;
        this.resolver = resolver;
    }

    public static boolean isSpecialTag(List<String> tags) {
        if (ObjectUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                if (ACTION.getTagName().equalsIgnoreCase(tag)) {
                    return true;
                }
                if (MODEL.getTagName().equalsIgnoreCase(tag)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isIgnore(List<String> tags) {
        if (ObjectUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                if (IGNORE.getTagName().equalsIgnoreCase(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAction(List<String> tags) {
        if (ObjectUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                if (ACTION.getTagName().equalsIgnoreCase(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isModel(List<String> tags) {
        if (ObjectUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                if (MODEL.getTagName().equalsIgnoreCase(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAction(Map<String, List<String>> tags) {
        if (ObjectUtil.isNotEmpty(tags)) {
            for (Map.Entry<String, List<String>> entry : tags.entrySet()) {
                if (ACTION.getTagName().equalsIgnoreCase(entry.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isModel(Map<String, List<String>> tags) {
        if (ObjectUtil.isNotEmpty(tags)) {
            for (Map.Entry<String, List<String>> entry : tags.entrySet()) {
                if (MODEL.getTagName().equalsIgnoreCase(entry.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static TagEnum nameOf(String tag) {
        TagEnum[] values = values();
        for (TagEnum value : values) {
            if (value.getTagName().equalsIgnoreCase(tag)) {
                return value;
            }
        }
        return UNDEFINED;
    }

    public Object resolver(Map<String, List<String>> tags) {
        if (tags != null && tags.containsKey(getTagName())) {
            return resolver(tags.get(getTagName()));
        }
        return null;
    }

    public Object resolver(List<String> content) {
        return resolver == null ? content.get(0) : resolver.resolver(content);
    }

    public String getTagName() {
        return tagName;
    }

    public String getDesc() {
        return desc;
    }

    public String getFormat() {
        return format;
    }

    public TagResolver getResolver() {
        return resolver;
    }

}
