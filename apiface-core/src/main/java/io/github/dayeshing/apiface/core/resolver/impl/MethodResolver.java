/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.resolver.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 13:14
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.resolver.impl;

import io.github.dayeshing.apiface.core.comment.impl.MethodMark;
import io.github.dayeshing.apiface.core.comment.impl.ParamMark;
import io.github.dayeshing.apiface.core.enums.MarkEnum;
import io.github.dayeshing.apiface.core.resolver.Resolver;
import io.github.dayeshing.apiface.core.rule.Rule;
import io.github.dayeshing.apiface.core.util.ImportUtil;
import io.github.dayeshing.apiface.core.util.ObjectUtil;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: <p> 方法解析 </p>
 * @ClassName: MethodResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.impl.MethodResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 13:14
 */
public class MethodResolver extends VoidVisitorAdapter<Void> implements Resolver<MethodMark> {

    private Rule rule;
    private List<ImportDeclaration> imports;
    private List<MethodMark> marks;

    public MethodResolver(Rule rule) {
        this.rule = rule;
    }

    @Override
    public List<MethodMark> resolver(CompilationUnit unit) {
        imports = unit.getImports();
        marks = new LinkedList<MethodMark>();
        visit(unit,null);
        return marks;
    }

    @Override
    public void visit(MethodDeclaration m, Void args) {
        MethodMark methodMark = new MethodMark(m.getName());
        // 设置标注
        Comment comment = m.getComment();
        if (comment != null) {
            CommentResolver cr = new CommentResolver(comment.getContent());
            if(rule != null && rule.ignore(cr.tags(), MarkEnum.METHOD)){
                return;
            }
            methodMark.setDesc(cr.getDesc().toString());
            methodMark.setDocTags(cr.getDocTags());


        List<Parameter> parameters = m.getParameters();
        // 设置参数
        if(ObjectUtil.isNotEmpty(parameters)){
            List<ParamMark> param = new LinkedList<ParamMark>();
            for (Parameter parameter : parameters) {
                param.add(new ParamMark(ImportUtil.fullType(parameter.getType().toString(),imports),parameter.getName()));
            }
            methodMark.setParams(param);
        }
        // 设置返回值
        marks.add(methodMark);
        }
    }

}
