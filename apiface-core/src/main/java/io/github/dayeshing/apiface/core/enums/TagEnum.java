package io.github.dayeshing.apiface.core.enums;

import io.github.dayeshing.apiface.core.resolver.TagResolver;
import io.github.dayeshing.apiface.core.resolver.tag.*;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.util.List;
import java.util.Map;

/**
 * @Description: <p>标记枚举类 </p>
 * @ClassName: TagEnum
 * @ClassAllName: io.github.dayeshing.apiface.core.enums.TagEnum
 * @Author： Daye Shing
 */
public enum TagEnum {

    /**
     * 接口
     *
     */
    ACTION("@action", new GroupResolver()),
    /**
     * 参数
     */
    PARAM("@param", new ParamResoler()),
    /**
     * 返回值
     */
    RETURN("@return", new ReturnResolver()),
    /**
     * 排除参数
     */
    EXCLUDE("@exclude", new GroupResolver()),
    /**
     * 作者
     */
    AUTHOR("@author", new AuthorResolver()),
    /**
     * 错误描述类
     */
    ERROR("@error",null),
    /**
     * 时间
     */
    DATE("@date",null),
    /**
     * 版本
     */
    SINCE("@since", null),
    /**
     * 统一包装返回类
     */
    PACK("@pack", null),
    /**
     * 统一包装返回类
     */
    UNPACK("@unpack", null),
    /**
     * 摘要
     */
    SUMMARY("@summary", null),

    /**
     * 本地uri处理
     */
    URI("@uri", new GroupResolver()),
    /**
     * 响应类行 doc,json,xml,...
     */
    CONSUME("@consume", new GroupResolver()),
    /**
     * 请求类型 doc,json,xml,...
     */
    PRODUCE("@produce", new GroupResolver()),
    /**
     * 请求方法 get,post
     */
    METHOD("@method", new MethodGroupResolver()),
    /**
     * 模型
     */
    MODEL("@model", null),
    /**
     * 要求
     */
    REQUIRED("@required", new GroupResolver()),
    /**
     * 参数分组 和 @required(dd,ff) 配合使用
     */
    GROUP("@group", new GroupResolver()),
    /**
     * 忽略
     */
    IGNORE("@ignore", null),
    /**
     * 格式化
     */
    FORMAT("@format", null),
    /**
     * 默认值
     */
    EXAMPLE("@example", null),
    /**
     * 隐藏
     */
    HIDDEN("@hidden", null),
    UNDEFINED("@undefined", null),
    ;

    /**
     * 标记名称
     */
    private String tagName;

    /**
     * 处理类
     */
    private TagResolver resolver;

    TagEnum(String tagName, TagResolver resolver) {
        this.tagName = tagName;
        this.resolver = resolver;
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
    public static boolean isApi(List<String> tags) {
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

    public TagResolver getResolver() {
        return resolver;
    }

}
