/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.util
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 23:14
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.util;

import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;

import javax.naming.Name;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * @Description: <p> TODO </p>
 * @ClassName: ImportUtil
 * @ClassAllName: io.github.dayeshing.apiface.core.util.ImportUtil
 * @Author： Daye Shing
 * @Date： 2020-08-15 23:14
 */
public class ImportUtil {

    private static Set<String> HIDDEN = new HashSet<String>();
    
    static {
        read(new InputStreamReader(ImportUtil.class.getClassLoader().getResourceAsStream("hidden")));
    }

    protected static void read(Reader in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(in);
            for (String line = ""; (line = reader.readLine()) != null; ) {
                HIDDEN.add(line);
            }
        } catch (Exception e) {
        } finally {
            ObjectUtil.close(reader);
            ObjectUtil.close(in);
        }
    }

    public static String fullSingleType(String type,List<ImportDeclaration> imports, PackageDeclaration pack){
        if(ObjectUtil.isEmpty(type)){
            return "Object";
        }
        // 这种一般是泛型，正常人不会创建一个只有一个字符的类名
        if(type.length() == 1){
            return type;
        }
        // 包含类全名
        if(type.contains(".")){
            return type;
        }
        for (ImportDeclaration anImport : imports) {
            String name = anImport.getName().toString();
            String end = name;
            int index = end.lastIndexOf(".");
            if(index > 0) {
                end = end.substring(++index);
            }
            // 在 import 里面存在
            if (end.equals(type)) {
                return name;
            }
        }
        // 隐式导入
        if(HIDDEN.contains(type)){
            return type;
        }
        // 同级目录
        return String.format("%s.%s",pack.getPackageName(),type);
    }
    
    public static String fullType(String type, List<ImportDeclaration> imports, PackageDeclaration pack){
        Set<String> target = new HashSet<String>();
        String[] replace = type.replace("<", ",").replace(">",",").split(",");
        for (String s : replace) {
            if(ObjectUtil.isNotEmpty(s)){
                target.add(s.trim());
            }
        }
        // 这里不存在泛型
        if(target.size() == 1){
            return fullSingleType(type,imports,pack);
        }
        //包含泛型
        for (String s : target) {
            //替换泛型中的类
            type = type.replace(s,fullSingleType(s, imports, pack));
        }
        return type;
    }

}
