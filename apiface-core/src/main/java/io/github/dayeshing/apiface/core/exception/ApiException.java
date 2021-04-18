/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.exception
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 07:04
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.exception;

/**
 * @Description: <p> 异常 </p>
 * @ClassName: ApiException
 * @ClassAllName: io.github.dayeshing.apiface.core.exception.ApiException
 * @Author： Daye Shing
 * @Date： 2020-08-15 07:04
 */
public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
