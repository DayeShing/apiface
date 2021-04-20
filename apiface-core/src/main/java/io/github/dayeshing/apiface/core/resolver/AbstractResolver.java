package io.github.dayeshing.apiface.core.resolver;

import io.github.dayeshing.apiface.core.comment.Mark;
import io.github.dayeshing.apiface.core.util.ObjectUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: <p> 抽象解析 </p>
 * @ClassName: DocTagResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.DocTagResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 11:19
 */
public abstract class AbstractResolver<T extends Mark> implements Resolver<T>, FileResolver<T> {

    @Override
    public List<T> resolve(List<String> files) {
        List<T> marks = new LinkedList<T>();
        if (ObjectUtil.isNotEmpty(files)) {
            for (String file : files) {
                marks.addAll(resolve(file));
            }
        }
        return marks;
    }

    @Override
    public List<T> resolveResources(List<File> files) {
        List<T> marks = new LinkedList<T>();
        if (ObjectUtil.isNotEmpty(files)) {
            for (File file : files) {
                marks.addAll(resolve(file));
            }
        }
        return marks;
    }

    @Override
    public List<T> resolve(String file) {
        return resolve(new File(file));
    }

    @Override
    public List<T> resolve(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            if(file.getName().endsWith(".java")){
                CompilationUnit unit = JavaParser.parse(in);
                if (unit.getTypes().size() > 0) {
                    return resolver(unit);
                }
            }
            // 其他语言入口 todo
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ObjectUtil.close(in);
        }
        return new LinkedList<T>();
    }

}
