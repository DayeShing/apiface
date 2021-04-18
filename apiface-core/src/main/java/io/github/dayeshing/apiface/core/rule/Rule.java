/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.rule
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 19:28
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.rule;

import io.github.dayeshing.apiface.core.enums.MarkEnum;

import java.util.List;

/**
 * @Description: <p> 规则接口 </p>
 * @ClassName: Rule
 * @ClassAllName: io.github.dayeshing.apiface.core.rule.Rule
 * @Author： Daye Shing
 * @Date： 2020-08-15 19:28
 */
public interface Rule {

    /**
     * keep
     * @description: <p> 是否继续 </p>
     * @methodName keep
     * @param  tag - 标签
     * @return boolean -
     * @throws
     * @author Daye Shing
     * @date 2020/8/15 19:30
     * @since 1.0
     */
    boolean ignore(List<String> tag, MarkEnum mark);
}
