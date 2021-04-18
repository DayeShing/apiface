/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.comment.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 12:56
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.comment.impl;

/**
 * @Description: <p> 参数标记 </p>
 * @ClassName: ParamMark
 * @ClassAllName: io.github.dayeshing.apiface.core.comment.impl.ParamMark
 * @Author： Daye Shing
 * @Date： 2020-08-14 12:56
 */
public class ParamMark {

    /**
     * 参数名称
     */
    private String name;

    /*
    参数类型名称
     */
    private String type;

    public ParamMark() {
    }

    public ParamMark(String type,String name) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
