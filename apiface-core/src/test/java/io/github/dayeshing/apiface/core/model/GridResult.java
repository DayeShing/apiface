package io.github.dayeshing.apiface.core.model;

/**
 * GridResult的包装类描述
 * @model 泛型结果
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/26 7:46
 */
public class GridResult<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 数据
     */
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
