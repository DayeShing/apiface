/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.comment.tag
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 10:53
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.bean;

/**
 * @Description: <p> 文档标注 </p>
 * @ClassName: DocTag
 * @ClassAllName: io.github.dayeshing.apiface.core.bean.DocTag
 * @Author： Daye Shing
 * @Date： 2020-08-14 10:53
 */
public abstract class DocTag<T> {

    /**
     * 标注名 like @auther xxxx
     */
    private String tagName;

    /**
     * 标注的内容（信息）
     */
    private T value;

    public DocTag() {
    }

    public DocTag(String tagName, T value) {
        this.tagName = tagName;
        this.value = value;
    }

    public DocTag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
