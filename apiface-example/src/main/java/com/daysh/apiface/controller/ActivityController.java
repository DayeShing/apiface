package com.daysh.apiface.controller;

import com.daysh.apiface.springmvc.controller.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ActivityController的接口类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action 活动
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
@RestController
@RequestMapping("activities")
public class ActivityController extends BaseController {

    /**
     * 保存活动(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param activities|com.daysh.apiface.controller.Activity|参数描述
     * @summary 保存活动
     * @return com.daysh.apiface.controller.Activity|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @PostMapping(value = "save")
    public Object save(@RequestBody @Validated Activity activities) {
        return GridReturn.success(activities);
    }

    /**
     * 删除活动(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param uuids |String|活动ids-数组
     * @summary 删除活动
     * @method post,get
     * @return int|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("delete")
    public Object delete(@RequestParam(value = "uuids") List<String> uuids) {
        return GridReturn.success(Integer.valueOf(1));
    }

    /**
     * 一条很长很长发描述
     * 开始活动-机器触发(从swagger换成了javadoc整个人就像如厕后，身心通泰)
     * (我们这样做不是因为我们想改变你，而是我们可以这么做)
     * (我们这样做不是因为我们想改变你，而是我们可以这么做)
     * (我们这样做不是因为我们想改变你，而是我们可以这么做)
     * (我们这样做不是因为我们想改变你，而是我们可以这么做)
     * (我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param uuid |String|活动id
     * @summary 开始活动-机器触发
     * @return void|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("start")
    public Object start(@RequestParam String uuid){
        return GridReturn.success();
    }

    /**
     * 结束活动-机器触发(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param uuid |String|活动id
     * @summary 结束活动-机器触发
     * @return void|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("finish")
    public Object finish(@RequestParam String uuid){
        return GridReturn.success();
    }

    /**
     * 获取活动列表,切记滚动加载和分页加载返回的数据不一样(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param rows |int|必须|10|一次分页加载记录条数(这个不是无用参数，在包装类里)
     * @param page |int|必须|1|当前页
     * @param keyword |String|搜索
     * @param roll |boolean|非必须|false|分页是否滚动加载(这个不是无用参数，在包装类里)
     * @summary 获取活动列表
     * @return com.daysh.apiface.controller.Activity|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("auth/list")
    public Object list(String keyword, Page page){
        return GridReturn.grid(new Activity(), page);
    }

    /**
     * 获取用户抽奖-参与用户列表,切记滚动加载和分页加载返回的数据不一样(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param rows |int|必须|10|一次分页加载记录条数(这个不是无用参数，在包装类里)
     * @param page |int|必须|1|当前页
     * @param uuid |String|活动id
     * @param roll |boolean|非必须|false|分页是否滚动加载(这个不是无用参数，在包装类里)
     * @summary 获取参与用户列表
     * @return com.daysh.apiface.controller.Activity|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("auth/partner")
    public Object partner(@RequestParam String uuid, Page page){
        return GridReturn.grid(new Activity(), page);
    }

    /**
     * 获取用户详情,切记滚动加载和分页加载返回的数据不一样(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param rows |int|必须|10|一次分页加载记录条数(这个不是无用参数，在包装类里)
     * @param page |int|必须|1|当前页
     * @param uuid |String|活动id
     * @param roll |boolean|非必须|false|分页是否滚动加载(这个不是无用参数，在包装类里)
     * @summary 获取用户详情
     * @return com.daysh.apiface.controller.TicketVo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("auth/userTicketDetails")
    public Object userTicketDetails(@RequestParam String uuid, Page page){
        return GridReturn.grid(new TicketVo(), page);
    }

    /**
     * 获取参与用户统计信息(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param uuid |String|活动id
     * @summary 获取参与用户统计信息
     * @return com.daysh.apiface.controller.TicketVo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("auth/userTicket")
    public Object userTicket(@RequestParam String uuid){
        return GridReturn.success(new TicketVo());
    }

    /**
     * 添加用户票(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param uuid |String|活动id
     * @summary 添加用户票
     * @return com.daysh.apiface.controller.TicketVo|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping("auth/saveTicket")
    public Object saveTicket(@RequestParam String uuid){
        return GridReturn.success(new TicketVo());
    }

}
