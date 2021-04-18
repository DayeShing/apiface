/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.resolver.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 21:11
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.resolver.impl;

import io.github.dayeshing.apiface.core.comment.tag.GeneralTag;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: <p> 注释文本 实现类-转换描述和注释标签 </p>
 * @ClassName: CommentResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.impl.CommentResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 21:11
 */
public class CommentResolver {

    private static Pattern TAG_NAME_COMPILE = Pattern.compile("^@[\\w]+");

    /**
     * 注释上的描述信息
     */
    private StringBuffer desc = new StringBuffer();

    /**
     * 上面的标注标签信息
     */
    private List<GeneralTag> docTags;

    /**
     * getTag
     * @description: <p> 根据注释生产标签实现类 </p>
     * @methodName getTag
     * @param line -
     * @return io.github.dayeshing.apiface.core.comment.tag.GeneralTag -
     * @throw
     * @author Daye Shing
     * @date 2020/8/16 9:34
     * @since 1.0
     */
    public GeneralTag getTag(String line) {
        Matcher m = TAG_NAME_COMPILE.matcher(line);
        if (m.find()) {
            String tag = m.group().trim();
            return new GeneralTag(tag, line.substring(tag.length()).trim());
        } else {
            return null;
        }
    }


    public CommentResolver(String text) {
        resolver(text);
    }

    /**
     * resolver
     *
     * @param text -
     * @return void -
     * @throws
     * @description: <p> 注释转换 </p>
     * @methodName resolver
     * @author Daye Shing
     * @date 2020/8/14 21:14
     * @since 1.0
     */
    private void resolver(String text) {
        // 去掉* 和换行
        String sp = "-!-";
        text = text.replaceAll("\\*", sp);
        text = text.replaceAll("\r\n", sp);
        text = text.replaceAll("\n", sp);
        text = text.replaceAll("\r", sp);
        String[] comments = text.split(sp);
        if (comments != null) {
            int index = 0;
            // 读取描述
            for (String s : comments) {
                String tmp = s.trim();
                if (tmp.startsWith("@")) {
                    break;
                } else if (ObjectUtil.isNotEmpty(tmp)) {
                    index++;
                    desc.append(tmp).append(" ");
                }
            }
            // 读取标记
            StringBuffer buf = null;
            docTags = new ArrayList<>();
            for (; index < comments.length; index++) {
                String tmp = comments[index].trim();
                if (tmp.startsWith("@")) {
                    // 获取标签起始
                    if (buf != null) {
                        docTags.add(getTag(buf.toString()));
                    }
                    buf = new StringBuffer(tmp);
                } else if (buf != null && ObjectUtil.isNotEmpty(tmp)) {
                    buf.append(tmp);
                }
            }
            if (buf != null) {
                docTags.add(getTag(buf.toString()));
            }
        }
    }

    public List<String> tags() {
        List<String> tags = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(docTags)) {
            for (GeneralTag docTag : docTags) {
                tags.add(docTag.getTagName());
            }
        }
        return tags;
    }

    public StringBuffer getDesc() {
        return desc;
    }

    public void setDesc(StringBuffer desc) {
        this.desc = desc;
    }

    public List<GeneralTag> getDocTags() {
        return docTags;
    }

    public void setDocTags(List<GeneralTag> docTags) {
        this.docTags = docTags;
    }
}
