
package io.github.dayeshing.apiface.maven;

import com.alibaba.fastjson.JSON;
import io.github.dayeshing.apiface.conver.ToBuf;
import io.github.dayeshing.apiface.core.api.ApiRule;
import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.resolver.impl.ResolverImpl;
import io.github.dayeshing.apiface.core.util.GZIPUtil;
import io.github.dayeshing.apiface.core.util.ScanUtil;
import org.apache.maven.plugins.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: <p> 初级文档构建类 </p>
 * @ClassName: ApiMojo
 * @ClassAllName: io.github.dayeshing.apiface.maven.ApiMojo
 * @Author： Daye Shing
 * @Date： 2020-08-13 14:26
 */
@Mojo(
        name = DocumentMojo.MOJO,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        threadSafe = true
)
@Execute(phase = LifecyclePhase.PROCESS_CLASSES)
public class DocumentMojo extends AbstractFaceMojo {

    public static final String MOJO = "doc";


    /**
     * //com.daysh.([a-z]+.)+controller.[a-zA-Z]+
     */
    @Parameter(
            property = "rule",
            defaultValue = ""
    )
    private String rule;

    @Override
    protected byte[] process() {
        List<ClassMark> marks = new ArrayList<>(180);
        marks.addAll(new ResolverImpl(new ApiRule()).resolveResources(ScanUtil.scanResources(sourseDirectory, new String[]{"java"}, rule)));
        if(marks.size() == 0){
            return null;
        }
        if(gzip){
            return GZIPUtil.compress(ToBuf.toBuf(marks));
        }
        return JSON.toJSONString(marks).getBytes();
    }

    @Override
    protected File dist() {
        File write = new File(outputDirectory, DIRECTORY);
        if (!write.exists()) {
            write.mkdirs();
        }
        return new File(write, getFilename(mojo()));
    }

    @Override
    protected String mojo() {
        return MOJO;
    }

}