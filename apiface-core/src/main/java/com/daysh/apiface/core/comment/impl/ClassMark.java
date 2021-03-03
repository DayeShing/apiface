/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.comment
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 10:49
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.comment.impl;

import com.daysh.apiface.core.comment.Mark;
import com.daysh.apiface.core.comment.tag.GeneralTag;

import java.util.List;

/**
 * @Description: <p> 类标记 </p>
 * @ClassName: ClassMark
 * @ClassAllName: com.daysh.apiface.core.comment.impl.ClassMark
 * @Author： Daye Shing
 * @Date： 2020-08-14 10:49
 */
public class ClassMark implements Mark {

    /**
     * 类全名
     */
    private String name;

    /**
     * 注释上的描述信息
     */
    private String desc;

    /**
     * 上面的标注标签信息
     */
    private List<GeneralTag> docTags;

    /**
     * 属性
     */
    private List<FieldMark> fields;

    /**
     * 方法
     */
    private List<MethodMark> methods;

    public ClassMark() {
    }

    public ClassMark(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public List<GeneralTag> getDocTags() {
        return docTags;
    }

    @Override
    public void setDocTags(List<GeneralTag> docTags) {
        this.docTags = docTags;
    }

    public List<FieldMark> getFields() {
        return fields;
    }

    public void setFields(List<FieldMark> fields) {
        this.fields = fields;
    }

    public List<MethodMark> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodMark> methods) {
        this.methods = methods;
    }
}
