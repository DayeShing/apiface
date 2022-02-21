package io.github.dayeshing.apiface.core.resolver.tag;

import io.github.dayeshing.apiface.core.enums.RequestMethod;
import io.github.dayeshing.apiface.core.resolver.TagResolver;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: <p> TODO </p>
 * @ClassName: MethodGroupTag
 * @ClassAllName: io.github.dayeshing.apiface.core.comment.tag.serial.MethodGroupTag
 * @Author： Daye Shing
 */
public class MethodGroupResolver implements TagResolver {

    @Override
    public List<String> resolver(List<String> contents) {
//        List<String> ret = new ArrayList<String>();
//        for (RequestMethod value : RequestMethod.values()) {
//            for (String content : contents) {
//                boolean flag = false;
//                if (content == null || "".equals(content)) {
//                    continue;
//                }
//                String[] split = content.split(",");
//                for (String s : split) {
//                    if(value.name().equalsIgnoreCase(s)){
//                        ret.add(value.name());
//                        flag = true;
//                        break;
//                    }
//                }
//                if (flag) {
//                    break;
//                }
//            }
//        }
//        return ret;
        Set<String> ret = new HashSet<String>();
        for (String content : contents) {
            if(content != null && !"".equals(content)){
                String[] split = content.split(",");
                for (String s : split) {
                    ret.add(ObjectUtil.toUpperCase(s));
                }
            }
        }
        return new ArrayList<>(ret);
    }
}
