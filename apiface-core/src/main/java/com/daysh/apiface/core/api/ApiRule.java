/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.rule
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 19:30
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.api;

import com.daysh.apiface.core.enums.MarkEnum;
import com.daysh.apiface.core.enums.TagEnum;
import com.daysh.apiface.core.rule.Rule;

import java.util.List;

/**
 * @Description: <p> api 扫描规则 </p>
 * @ClassName: ApiRule
 * @ClassAllName: com.daysh.apiface.core.rule.ApiRule
 * @Author： Daye Shing
 * @Date： 2020-08-15 19:30
 */
public class ApiRule implements Rule {

    @Override
    public boolean ignore(List<String> tags, MarkEnum mark) {
        boolean ret = false;
        switch (mark) {
            case CLASS:
                ret = classMark(tags);
                break;
            case METHOD:
                ret = methodMark(tags);
                break;
            case FIELD:
                ret = fieldMark(tags);
                break;
        }
        return ret;
    }

    /**
     * 是否是一个文档解析对象
     * @param tags | 参数的描述 | 必须true/false | 的默认值
     * @exclude 排除参数1,排除参数2
     * @return boolean|描述
     * @author Daye Shing
     * @since 1.0
     */
    public boolean classMark(List<String> tags) {
        return TagEnum.isIgnore(tags);
    }

    public boolean methodMark(List<String> tags) {
        return TagEnum.isIgnore(tags);
    }

    public boolean fieldMark(List<String> tags) {
        return TagEnum.isIgnore(tags);
    }
}
