/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.api.meta
 * @Author： Daye Shing
 * @CreateTime： 2020-08-19 21:15
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.api.meta;

import java.io.Serializable;

/**
 * @Description: <p> 作者信息 </p>
 * @ClassName: Auther
 * @ClassAllName: com.daysh.apiface.core.api.meta.Auther
 * @Author： Daye Shing
 * @Date： 2020-08-19 21:15
 */
public class Auther implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 作者
     */
    private String name;
    /**
     * 联系
     */
    private String contact;

    public Auther() {
    }

    /**
     * 方法描述
     * @param name | 参数的描述 | 必须true/false | 的默认值
     * @exclude 排除参数1,排除参数2
     * @return 值 | 描述
     * @author Daye Shing
     * @since 1.0
     */
    public Auther(String name) {

        this.name = name;
    }

    public Auther(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
