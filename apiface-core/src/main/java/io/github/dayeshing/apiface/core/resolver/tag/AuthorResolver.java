package io.github.dayeshing.apiface.core.resolver.tag;

import io.github.dayeshing.apiface.core.api.meta.Author;
import io.github.dayeshing.apiface.core.resolver.TagResolver;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: AutherResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.tag.AutherResolver
 * @Author： Daye Shing
 */
public class AuthorResolver implements TagResolver {

    @Override
    public Author resolver(List<String> contents) {
        String s = contents.get(0);
        if (ObjectUtil.isNotEmpty(s)) {
            String[] split = s.split("\\|");
            if (split.length > 1) {
                return new Author(split[0].trim(),split[1].trim());
            }
        }
        return new Author(s);
    }

}
