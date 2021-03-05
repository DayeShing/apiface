/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.util
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 23:14
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.util;

import com.github.javaparser.ast.ImportDeclaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: <p> TODO </p>
 * @ClassName: ImportUtil
 * @ClassAllName: com.daysh.apiface.core.util.ImportUtil
 * @Author： Daye Shing
 * @Date： 2020-08-15 23:14
 */
public class ImportUtil {

    public static String fullType(String type, List<ImportDeclaration> imports){
        Set<String> target = new HashSet<>();
        String[] replace = type.replace("<", ",").replace(">",",").split(",");
        for (String s : replace) {
            if(ObjectUtil.isNotEmpty(s)){
                target.add(s.trim());
            }
        }
        if(ObjectUtil.isNotEmpty(imports)){
            for (String s : target) {
                for (ImportDeclaration anImport : imports) {
                    String name = anImport.getName().toString();
                    if (name.endsWith(s)) {
                        type = type.replace(s,name);
                    }
                }
            }
        }
        return type;
    }

}
