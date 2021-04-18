/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.comment
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 10:55
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.comment.impl;

import io.github.dayeshing.apiface.core.comment.Mark;
import io.github.dayeshing.apiface.core.bean.DocTag;
import io.github.dayeshing.apiface.core.comment.tag.GeneralTag;

import java.util.List;

/**
 * @Description: <p> 方法的标记 </p>
 * @ClassName: MethodMark
 * @ClassAllName: io.github.dayeshing.apiface.core.comment.impl.MethodMark
 * @Author： Daye Shing
 * @Date： 2020-08-14 10:55
 */
public class MethodMark implements Mark {

    /**
     * 方法名
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
     * 方法参数
     */
    private List<ParamMark> params;

    public MethodMark() {
    }

    public MethodMark(String name) {
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

    public List<ParamMark> getParams() {
        return params;
    }

    public void setParams(List<ParamMark> params) {
        this.params = params;
    }
}
