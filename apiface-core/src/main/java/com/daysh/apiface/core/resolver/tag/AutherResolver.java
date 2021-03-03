/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.comment.tag.serial
 * @Author： Daye Shing
 * @CreateTime： 2021-02-26 15:52
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.resolver.tag;

import com.daysh.apiface.core.api.meta.Auther;
import com.daysh.apiface.core.resolver.TagResolver;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: AutherResolver
 * @ClassAllName: com.daysh.apiface.core.resolver.tag.AutherResolver
 * @Author： Daye Shing
 */
public class AutherResolver implements TagResolver {

    @Override
    public Auther resolver(List<String> contents) {
        String s = contents.get(0);
        if (StringUtils.isNotEmpty(s)) {
            String[] split = s.split("\\|");
            if (split.length > 1) {
                return new Auther(split[0].trim(),split[1].trim());
            }
        }
        return new Auther(s);
    }

}
