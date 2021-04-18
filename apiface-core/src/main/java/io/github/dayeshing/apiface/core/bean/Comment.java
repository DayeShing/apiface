/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.vo
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 20:03
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.bean;

import java.util.List;

/**
 * @Description: <p> 注释封装 </p>
 * @ClassName: Comment
 * @ClassAllName: io.github.dayeshing.apiface.core.vo.Comment
 * @Author： Daye Shing
 * @Date： 2020-08-14 20:03
 */
public class Comment {

    /**
     * 注释上的描述信息
     */
    private String desc;

    /**
     * 上面的标注标签信息
     */
    private List<DocTag> docTags;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<DocTag> getDocTags() {
        return docTags;
    }

    public void setDocTags(List<DocTag> docTags) {
        this.docTags = docTags;
    }
}
