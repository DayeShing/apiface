package com.daysh.apiface.controller;

import com.daysh.apiface.springmvc.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 个人信息(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action 个人信息
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
@RestController
@RequestMapping("user")
public class UserInfoController extends BaseController {

    /**
     * 获取登录用户信息(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @summary 登录用户信息
     * @return com.daysh.apiface.controller.UserInfo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("userInfo")
    public Object userInfo() {
        return GridReturn.success(new UserInfo());
    }

    /**
     * 签发证书返回单点登陆的 jwt(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param user |com.daysh.apiface.controller.UserInfo|用户信息
     * @summary 签发token证书
     * @return com.daysh.apiface.controller.UserInfo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @PostMapping("issue")
    public Object issue(@RequestBody @Validated UserInfo user) {
        return GridReturn.success(user);
    }

    /**
     * 保存用户基本信息(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param user |com.daysh.apiface.controller.UserInfo|用户信息
     * @summary 保存用户基本信息
     * @return com.daysh.apiface.controller.UserInfo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @PostMapping(value = "updateUser")
    public Object updateUser(@RequestBody @Validated UserInfo user) {
        return GridReturn.success(user);
    }

    /**
     * 获取我关注用户信息(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @summary 获取我关注用户信息
     * @return com.daysh.apiface.controller.UserInfo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping(value = "getFocus")
    public Object getFocus() {
        return GridReturn.success(new UserInfo());
    }

    /**
     * 关注用户(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param focusUser |com.daysh.apiface.controller.FocusUser|用户信息
     * @summary 关注用户
     * @return com.daysh.apiface.controller.FocusUser|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @PostMapping(value = "focus")
    public Object focus(@RequestBody @Validated FocusUser focusUser) {
        return GridReturn.success(focusUser);
    }

    /**
     * 取消关注用户(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param focusUser |com.daysh.apiface.controller.FocusUser|用户信息
     * @summary 取消关注用户
     * @return com.daysh.apiface.controller.FocusUser|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @PostMapping(value = "ignore")
    public Object ignore(@RequestBody @Validated FocusUser focusUser) {
        return GridReturn.success(focusUser);
    }

    /**
     * 获取被关注用户列表,切记滚动加载和分页加载返回的数据不一样(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param rows |int|必须|10|一次分页加载记录条数
     * @param page |int|必须|1|当前页
     * @param keyword |String|用户昵称搜索
     * @param roll |boolean|非必须|false|分页是否滚动加载
     * @param focus |boolean|非必须|true|是否仅仅查询被关注
     * @summary 获取被关注用户列表
     * @return com.daysh.apiface.controller.UserInfo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping(value = "listToFocusUser")
    public Object listToFocusUser(String keyword, @RequestParam(value = "focus",defaultValue = "true") boolean focus, Page page) {
        return GridReturn.grid(new UserInfo(),page);
    }

    /**
     * 获取用户列表,切记滚动加载和分页加载返回的数据不一样(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param rows |int|必须|10|一次分页加载记录条数
     * @param page |int|必须|1|当前页
     * @param keyword |String|用户昵称搜索
     * @param roll |boolean|非必须|false|分页是否滚动加载
     * @param focus |boolean|非必须|true|是否仅仅查询被关注
     * @summary 获取用户列表
     * @return com.daysh.apiface.controller.UserInfo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping(value = "listUser")
    public Object listUser(String keyword, @RequestParam(value = "focus",defaultValue = "true") boolean focus, Page page) {
        return GridReturn.grid(new UserInfo(),page);
    }
}
