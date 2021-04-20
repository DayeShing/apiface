package com.daysh.apiface.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Hello的 数组测试，文人多外号
 * @model Hello
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/13 5:20
 */
public class Hello {


    private List<String> names;

    /**
     * 哩视就
     */
    private List<Hi> his;

    private String[] nicknames;

    /**
     * 是否认证
     */
    private boolean auth;

    /**
     * 名称dd
     */
    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    /**
     * 外号fd
     */
    public String[] getNicknames() {
        return nicknames;
    }

    public void setNicknames(String[] nicknames) {
        this.nicknames = nicknames;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public List<Hi> getHis() {
        return his;
    }

    public void setHis(List<Hi> his) {
        this.his = his;
    }
}
