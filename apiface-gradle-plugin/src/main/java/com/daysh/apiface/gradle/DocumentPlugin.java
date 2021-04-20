package com.daysh.apiface.gradle;

import java.io.File;

/**
 * DocumentPlugin的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-03-04 19:20
 * @since 1.0
 */
public class DocumentPlugin extends AbstractFacePlugin {

    public static final String MOJO = "doc";

    @Override
    protected byte[] process() {
        System.err.println(mojo());
        return new byte[0];
    }

    @Override
    protected File dist() {
        File write = new File(project.getBuildDir(), DIRECTORY);
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
