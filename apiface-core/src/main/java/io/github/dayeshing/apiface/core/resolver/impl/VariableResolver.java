/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.resolver.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-16 09:14
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.resolver.impl;

import com.github.javaparser.ast.PackageDeclaration;
import io.github.dayeshing.apiface.core.enums.VariableEnum;
import io.github.dayeshing.apiface.core.util.ImportUtil;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: <p> 变量解析 </p>
 * @ClassName: VariableResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.impl.VariableResolver
 * @Author： Daye Shing
 * @Date： 2020-08-16 09:14
 */
public class VariableResolver {

    /**
     * 默认值
     */
    private String example;

    /**
     * 类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    public VariableResolver(List<ImportDeclaration> imports, PackageDeclaration pack, VariableDeclarator variable, Type type) {
        resolver(imports,pack, variable, type);
    }

    public String example(Expression line) {
        if(line != null){
            return line.toString().replace("\"","").replace("\'","");
        }
        return "";
    }

    private void resolver(List<ImportDeclaration> imports,PackageDeclaration pack, VariableDeclarator variable,Type type) {
        this.type = ImportUtil.fullType(type.toString(), imports, pack);
        this.name = variable.getId().toString();
        this.example = VariableEnum.of(this.type) !=null?example(variable.getInit()):"";
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
