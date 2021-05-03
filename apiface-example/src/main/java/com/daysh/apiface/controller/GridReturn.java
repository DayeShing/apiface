package com.daysh.apiface.controller;

/**
 * GridReturn的描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @since 1.0
 */
public class GridReturn{


    public static Return grid(Object obj, Page page) {
        return new Return(200, obj);
    }

    public static Return success() {
        return success(null);
    }

    public static Return success(Object obj) {
        return new Return(200, obj);
    }
}
