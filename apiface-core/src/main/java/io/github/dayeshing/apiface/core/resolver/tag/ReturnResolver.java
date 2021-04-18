package io.github.dayeshing.apiface.core.resolver.tag;

import io.github.dayeshing.apiface.core.api.meta.Return;
import io.github.dayeshing.apiface.core.resolver.TagResolver;

import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: ReturnResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.tag.ReturnResolver
 * @Author： Daye Shing
 */
public class ReturnResolver implements TagResolver {

    @Override
    public Return resolver(List<String> contents) {
        String s = contents.get(0);
        if (s != null) {
            int i = s.indexOf("|");
            if (i > 0) {
                return new Return(s.substring(0, i).trim(), s.substring(++i).trim());
            }
        }
        return new Return(s);
    }

}
