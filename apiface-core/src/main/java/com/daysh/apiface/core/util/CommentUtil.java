/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.util
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 11:53
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: <p> TODO </p>
 * @ClassName: CommentUtils
 * @ClassAllName: com.daysh.apiface.core.util.CommentUtils
 * @Author： Daye Shing
 * @Date： 2020-08-14 11:53
 */
public class CommentUtil {

    private static Pattern TAG_NAME_COMPILE = Pattern.compile("^@[\\w]+[\\t ]");

    /**
     * 获取注释的类型
     *
     * @param comment 注释文本
     * @return @see @param @resp @return等
     */
    public static String getTagType(String comment) {
        Matcher m = TAG_NAME_COMPILE.matcher(comment);
        if (m.find()) {
            return m.group().trim();
        } else {
            return null;
        }
    }

    /**
     * 解析基本的文本注释
     *
     * @param comment 注释文本
     */
    public static String parseCommentText(String comment) {
        List<String> comments = asCommentList(comment);
        for (String s : comments) {
            if (!s.startsWith("@")) {
                String[] split = s.split("\n");
                StringBuffer buf = new StringBuffer();
                for (String s1 : split) {
                    if(s1.startsWith("@")){
                        return buf.toString();
                    }
                    buf.append(s1.replace("\r",""));
                }
                return buf.toString();
            }
        }
        return "";
    }

    /**
     * 将注释转为多行文本
     *
     * @param comment 注释文本
     */
    public static List<String> asCommentList(String comment) {
        comment = comment.replaceAll("\\*", "").trim();
        String[] array = comment.split("\n");
        List<String> comments = new ArrayList(array.length);
        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (; index < array.length; index++) {
            String c = array[index].trim();

            if (StringUtils.isBlank(c)) {
                continue;
            }

            String tagType = CommentUtil.getTagType(c);
            if (StringUtils.isBlank(tagType)) {
                sb.append(c);
                sb.append("\n");
            } else {
                break;
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            comments.add(sb.toString());
        }

        for (int i = index; i < array.length; i++) {
            String c = array[i].trim();
            if (StringUtils.isNotBlank(c)) {
                comments.add(c);
            }
        }
        return comments;
    }
}
