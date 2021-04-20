package io.github.dayeshing.apiface.core.resolver.tag;

import io.github.dayeshing.apiface.core.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: GroupTag
 * @ClassAllName: io.github.dayeshing.apiface.core.comment.tag.serial.GroupTag
 * @Author： Daye Shing
 */
public class GroupResolver implements TagResolver {

    @Override
    public List<String> resolver(List<String> contents) {
        List<String> ret = new ArrayList<String>();
        for (String content : contents) {
            if(content != null && !"".equals(content)){
                String[] split = content.split(",");
                for (String s : split) {
                    ret.add(s.trim());
                }
            }

        }
        return ret;
    }

}
