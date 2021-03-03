package com.daysh.apiface.core.comment;

import com.daysh.apiface.core.bean.DocTag;
import com.daysh.apiface.core.comment.tag.GeneralTag;

import java.util.List;

/**
 * @Description: <p> 标注标记信息 </p>
 * @ClassName: Mark
 * @ClassAllName: com.daysh.apiface.core.comment.Mark
 * @Author： Daye Shing
 * @Date： 2020-08-14 11:00
 */
public interface Mark {

    String getName();

    void setName(String name);

    String getDesc();

    void setDesc(String desc);

    List<GeneralTag> getDocTags();

    void setDocTags(List<GeneralTag> docTags);
    
}
