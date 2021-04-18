/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.comment.tag
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 22:12
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.comment.tag;

import io.github.dayeshing.apiface.core.bean.DocTag;

/**
 * @Description: <p> TODO </p>
 * @ClassName: GeneralTag
 * @ClassAllName: io.github.dayeshing.apiface.core.comment.tag.GeneralTag
 * @Author： Daye Shing
 * @Date： 2020-08-15 22:12
 */
public class GeneralTag extends DocTag<String> {

    public GeneralTag() {
    }

    public GeneralTag(String tagName, String value) {
        super(tagName, value);
    }

    public GeneralTag(String tagName) {
        super(tagName);
    }
}
