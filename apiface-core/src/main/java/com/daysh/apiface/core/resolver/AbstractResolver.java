package com.daysh.apiface.core.resolver;

import com.daysh.apiface.core.comment.Mark;
import com.daysh.apiface.core.util.ObjectUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: <p> 抽象解析 </p>
 * @ClassName: DocTagResolver
 * @ClassAllName: com.daysh.apiface.core.resolver.DocTagResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 11:19
 */
public abstract class AbstractResolver<T extends Mark> implements Resolver<T>, FileResolver<T> {

    @Override
    public List<T> resolve(List<String> files) {
        List<T> marks = new LinkedList<>();
        if (ObjectUtil.isNotEmpty(files)) {
            for (String file : files) {
                marks.addAll(resolve(file));
            }
        }
        return marks;
    }

    @Override
    public List<T> resolveResources(List<File> files) {
        List<T> marks = new LinkedList<>();
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
        try (FileInputStream in = new FileInputStream(file)) {
            CompilationUnit unit = JavaParser.parse(in);
            if (unit.getTypes().size() > 0) {
                return resolver(unit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

}
