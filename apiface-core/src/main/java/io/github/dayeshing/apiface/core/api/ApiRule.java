/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.rule
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 19:30
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.api;

import io.github.dayeshing.apiface.core.enums.MarkEnum;
import io.github.dayeshing.apiface.core.enums.TagEnum;
import io.github.dayeshing.apiface.core.rule.Rule;

import java.util.List;

/**
 * @Description: <p> api 扫描规则 </p>
 * @ClassName: ApiRule
 * @ClassAllName: io.github.dayeshing.apiface.core.rule.ApiRule
 * @Author： Daye Shing
 * @Date： 2020-08-15 19:30
 */
public class ApiRule implements Rule {

    private boolean api;

    public ApiRule(boolean api) {
        this.api = api;
    }

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

    @Override
    public boolean api() {
        return api;
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
        if(api){
            if(TagEnum.isApi(tags)){
                return TagEnum.isIgnore(tags);
            }
            return true;
        }
        return TagEnum.isIgnore(tags);
    }

    public boolean methodMark(List<String> tags) {
        return TagEnum.isIgnore(tags);
    }

    public boolean fieldMark(List<String> tags) {
        return TagEnum.isIgnore(tags);
    }
}
