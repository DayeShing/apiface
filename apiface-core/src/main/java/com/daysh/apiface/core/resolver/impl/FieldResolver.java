/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.resolver.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 13:14
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.resolver.impl;

import com.daysh.apiface.core.comment.impl.FieldMark;
import com.daysh.apiface.core.enums.MarkEnum;
import com.daysh.apiface.core.resolver.Resolver;
import com.daysh.apiface.core.rule.Rule;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: <p> 方法解析 </p>
 * @ClassName: MethodResolver
 * @ClassAllName: com.daysh.apiface.core.resolver.impl.MethodResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 13:14
 */
public class FieldResolver extends VoidVisitorAdapter<Void> implements Resolver<FieldMark> {

    private Rule rule;
    private List<ImportDeclaration> imports;
    private List<FieldMark> marks;

    public FieldResolver(Rule rule) {
        this.rule = rule;
    }

    @Override
    public List<FieldMark> resolver(CompilationUnit unit) {
        imports = unit.getImports();
        marks = new LinkedList<>();
        visit(unit, null);
        return marks;
    }

    @Override
    public void visit(FieldDeclaration f, Void args) {
        Comment comment = f.getComment();
        CommentResolver cr = null;
        if (comment != null) {
            cr = new CommentResolver(comment.getContent());
            if (rule != null && rule.ignore(cr.tags(), MarkEnum.FIELD)) {
                return;
            }

            Type type = f.getType();
            List<VariableDeclarator> variables = f.getVariables();
            for (VariableDeclarator variable : variables) {
                VariableResolver v = new VariableResolver(imports, variable, type);
                FieldMark fieldMark = new FieldMark(v.getType(), v.getName(), v.getExample());
                if (cr != null) {
                    fieldMark.setDesc(cr.getDesc().toString());
                    fieldMark.setDocTags(cr.getDocTags());
                }
                marks.add(fieldMark);
            }
        }
    }
}
