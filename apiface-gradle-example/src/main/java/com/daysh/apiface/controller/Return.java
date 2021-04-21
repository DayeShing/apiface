package com.daysh.apiface.controller;

/**
 * Return的包装类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @model 统一返回
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
public class Return<T> {
    private int code;
    private T data;

    public Return(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
