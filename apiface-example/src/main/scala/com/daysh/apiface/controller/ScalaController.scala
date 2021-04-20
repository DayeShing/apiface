package com.daysh.apiface.controller

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * KotlinController的接口类描述
 * @action scala
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/17 6:56
 */
@RestController
@RequestMapping(Array("scala"))
class ScalaController {

  /**
   * json 数组
   * @param hots |String|hot
   * @summary 摘要
   * @error 错误描述
   * @return String|描述
   * @author Daye Shing | 896379914@qq.com
   * @since 1.0
   * @date 2021/4/17 6:54
   */
  @GetMapping(Array("hi"))
  def sayHis(hots:String):String = {
    return hots;
  }

}
