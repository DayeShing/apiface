package com.daysh.apiface.gradle;

import java.io.File;

/**
 * ApiPlugin的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-03-04 19:19
 * @since 1.0
 */
public class ApiPlugin extends AbstractFacePlugin {

    public static final String MOJO = "api";

    @Override
    protected byte[] process() {
        System.err.println(mojo());
        return new byte[0];
    }

    @Override
    protected File dist() {
        return null;
    }

    @Override
    protected String mojo() {
        return MOJO;
    }
}
