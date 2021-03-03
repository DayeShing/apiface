/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.comment.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 11:06
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.comment.impl;

import com.daysh.apiface.core.comment.Mark;
import com.daysh.apiface.core.bean.DocTag;
import com.daysh.apiface.core.comment.tag.GeneralTag;

import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: FieldMark
 * @ClassAllName: com.daysh.apiface.core.comment.impl.FieldMark
 * @Author： Daye Shing
 * @Date： 2020-08-14 11:06
 */
public class FieldMark implements Mark {

    /**
     * 字段名变量名
     */
    private String name;

    /**
     * 注释上的描述信息
     */
    private String desc;

    /**
     * 默认值
     */
    private String example;

    /**
     * 类型
     */
    private String type;

    /**
     *
     * 上面的标注标签信息
     */
    private List<GeneralTag> docTags;

    public FieldMark() {
    }

    public FieldMark(String type, String name, String example) {
        this.name = name;
        this.example = example;
        this.type = type;
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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
