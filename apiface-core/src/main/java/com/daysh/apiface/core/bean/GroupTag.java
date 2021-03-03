/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.bean
 * @Author： Daye Shing
 * @CreateTime： 2020-08-16 10:29
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.bean;

import java.util.List;
import java.util.Objects;

/**
 * @Description: <p> 标签分组 </p>
 * @ClassName: GroupTag
 * @ClassAllName: com.daysh.apiface.core.bean.GroupTag
 * @Author： Daye Shing
 * @since 2020-08-16 10:29
 */
public class GroupTag extends DocTag<List<String>> {

    public GroupTag() {
    }

    public GroupTag(String tagName, List<String> value) {
        super(tagName, value);
    }

    public GroupTag(String tagName) {
        super(tagName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupTag groupTag = (GroupTag) o;
        return Objects.equals(getTagName(), groupTag.getTagName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagName());
    }
}
