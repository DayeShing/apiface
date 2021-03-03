/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.resolver.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 13:14
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.resolver.impl;

import com.daysh.apiface.core.comment.impl.ClassMark;
import com.daysh.apiface.core.enums.MarkEnum;
import com.daysh.apiface.core.enums.TagEnum;
import com.daysh.apiface.core.exception.ApiException;
import com.daysh.apiface.core.resolver.Resolver;
import com.daysh.apiface.core.rule.Rule;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: <p> 类转换 </p>
 * @ClassName: ClassResolver
 * @ClassAllName: com.daysh.apiface.core.resolver.impl.ClassResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 13:14
 */
public class ClassResolver implements Resolver<ClassMark> {

    private Rule rule;

    public ClassResolver(Rule rule) {
        this.rule = rule;
    }

    @Override
    public List<ClassMark> resolver(CompilationUnit unit) {
        List<ClassMark> marks = new LinkedList<>();
        if (unit.getTypes().size() > 0) {
            // 获取声明类型
            TypeDeclaration clazz = unit.getTypes().get(0);
            ClassMark classMark = new ClassMark(String.format("%s.%s", unit.getPackage().getPackageName(), clazz.getName()));
            Comment comment = clazz.getComment();
            if(comment != null){
                CommentResolver cr = new CommentResolver(comment.getContent());
                boolean model = TagEnum.isModel(cr.tags());
                if(rule != null && rule.ignore(cr.tags(), MarkEnum.CLASS)){
                    return marks;
                }
                classMark.setDocTags(cr.getDocTags());
                classMark.setDesc(cr.getDesc().toString());
                classMark.setMethods(new MethodResolver(rule).resolver(unit));
                if(model){
                    classMark.setFields(new FieldResolver(rule).resolver(unit));
                }
                marks.add(classMark);
            }
        }
        return marks;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

}
