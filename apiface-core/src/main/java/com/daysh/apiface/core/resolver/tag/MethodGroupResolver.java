/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.comment.tag.serial
 * @Author： Daye Shing
 * @CreateTime： 2021-02-26 01:44
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.resolver.tag;

import com.daysh.apiface.core.enums.RequestMethod;
import com.daysh.apiface.core.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: MethodGroupTag
 * @ClassAllName: com.daysh.apiface.core.comment.tag.serial.MethodGroupTag
 * @Author： Daye Shing
 */
public class MethodGroupResolver implements TagResolver {

    @Override
    public List<String> resolver(List<String> contents) {
        List<String> ret = new ArrayList<>();
        for (RequestMethod value : RequestMethod.values()) {
            for (String content : contents) {
                boolean flag = false;
                if (content == null || "".equals(content)) {
                    continue;
                }
                String[] split = content.split(",");
                for (String s : split) {
                    if(value.name().equalsIgnoreCase(s)){
                        ret.add(value.name());
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
        }
        return ret;
    }
}
