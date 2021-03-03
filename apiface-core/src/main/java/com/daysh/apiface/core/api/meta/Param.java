/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.meta
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 16:54
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.api.meta;

import com.daysh.apiface.core.enums.ParamEnum;

/**
 * @Description: <p> 接口参数类 </p>
 * @ClassName: Param
 * @ClassAllName: com.daysh.apiface.core.meta.Param
 * @Author： Daye Shing
 */
public class Param extends Field {

    private static final long serialVersionUID = -111L;

    /**
     * 是否表单
     */
    private ParamEnum form;



    public ParamEnum getForm() {
        return form;
    }

    public void setForm(ParamEnum form) {
        this.form = form;
    }


}
