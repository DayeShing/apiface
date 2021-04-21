package io.github.dayeshing.apiface.gradle;

import com.alibaba.fastjson.JSON;
import io.github.dayeshing.apiface.conver.ToBuf;
import io.github.dayeshing.apiface.core.api.ApiRule;
import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.resolver.impl.ResolverImpl;
import io.github.dayeshing.apiface.core.util.GZIPUtil;
import io.github.dayeshing.apiface.core.util.ScanUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * DocTask的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-04-21 05:48
 * @since 1.0
 */
public class DocTask extends AbstractFaceTask {

    public static final String MOJO = "doc";
    @Override
    protected byte[] process() {
        String[] suffix = new String[]{"java","kt","groovy","scala"};
        List<ClassMark> marks = new ArrayList<ClassMark>(180);
        getLog(String.format("----------->> 源码扫描路径：%s <<-----------", sourseDirectory.getParent(),mojo()));
        marks.addAll(new ResolverImpl(new ApiRule()).resolveResources(ScanUtil.scanResources(sourseDirectory.getParentFile(), suffix , apiExtension.getRule())));
        if(marks.size() == 0){
            return null;
        }
        if(apiExtension.isGzip()){
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
