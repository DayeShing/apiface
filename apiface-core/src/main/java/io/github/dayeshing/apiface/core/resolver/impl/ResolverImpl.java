/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.resolver.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 11:24
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.resolver.impl;

import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.resolver.AbstractResolver;
import io.github.dayeshing.apiface.core.rule.Rule;
import com.github.javaparser.ast.CompilationUnit;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: <p> 转换实现类 </p>
 * @ClassName: DocTagResolverImpl
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.impl.DocTagResolverImpl
 * @Author： Daye Shing
 * @Date： 2020-08-14 11:24
 */
public class ResolverImpl extends AbstractResolver<ClassMark> {

    private Rule rule;

    public ResolverImpl(Rule rule) {
        this.rule = rule;
    }

    @Override
    public List<ClassMark> resolver(CompilationUnit unit) {
        return new ClassResolver(rule).resolver(unit);
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

}
