import axios from 'axios'
var util = {}
util.install = function (Vue, options) {

  //================ 工具类操作 - 开始 ================
  /**
   * 格式化时间输出
   * @param {*} date
   * @param {*} fmt
   */
  Vue.prototype.datetimeFormat = function (date, fmt) {
    var date = new Date(date);
    var o = {
      "M+": date.getMonth() + 1, //月份
      "d+": date.getDate(), //日
      "h+": date.getHours(), //小时
      "m+": date.getMinutes(), //分
      "s+": date.getSeconds(), //秒
      "q+": Math.floor((date.getMonth() + 3) / 3), //季度
      "S": date.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
      if (new RegExp("(" + k + ")").test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      }
    }
    return fmt;
  }
  /**
   *
   * @param {*} time1
   * @param {*} time2
   */
  Vue.prototype.diffTime = function (time1, time2) {
    let date1 = new Date(time1); //开始时间
    let date2 = new Date(time2); //结束时间
    let date3 = date2.getTime() - date1.getTime(); //时间差的毫秒数
    //计算出相差年数
    let years = Math.floor(date3 / (24 * 3600 * 1000 * 365));
    //计算出相差月数
    let leave1 = date3 % (24 * 3600 * 1000 * 365); //计算年数后剩余的毫秒数
    let mouths = Math.floor(leave1 / (24 * 3600 * 1000 * 30));
    //计算出相差天数
    let levave2 = leave1 % (24 * 3600 * 1000 * 30); //计算月数后剩余的毫秒数
    let days = Math.floor(levave2 / (24 * 3600 * 1000));
    //计算出小时数
    let leave3 = levave2 % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
    let hours = Math.floor(leave3 / (3600 * 1000));
    //计算相差分钟数
    let leave4 = leave3 % (3600 * 1000); //计算小时数后剩余的毫秒数
    let minutes = Math.floor(leave4 / (60 * 1000));
    //计算相差秒数
    let leave5 = leave4 % (60 * 1000); //计算分钟数后剩余的毫秒数
    let seconds = Math.round(leave5 / 1000);

    let format = '';
    if (years != 0) {
      format = years + '年' + mouths + '月' + days + '日' + hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (mouths != 0) {
      format = mouths + '月' + days + '日' + hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (days != 0) {
      format = days + '日' + hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (hours != 0) {
      format = hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (minutes != 0) {
      format = minutes + '分钟' + seconds + '秒';
    } else {
      format = seconds + '秒';
    }
    return format;
  }
  Vue.prototype.deepClone = function (obj) {
    var o;
    // 如果  他是对象object的话  , 因为null,object,array  也是'object';
    if (typeof obj === "object") {
      // 如果  他是空的话
      if (obj === null) {
        o = null;
      } else {
        // 如果  他是数组arr的话
        if (obj instanceof Array) {
          o = [];
          for (var i = 0, len = obj.length; i < len; i++) {
            o.push(this.deepClone(obj[i]));
          }
        }
        // 如果  他是对象object的话
        else {
          o = {};
          for (var j in obj) {
            o[j] = this.deepClone(obj[j]);
          }
        }
      }
    } else {
      o = obj;
    }
    return o;
  }
  //================ 工具类操作 - 结束 ================

  //================ 网络类操作 - 开始 ================
  /**
   * 设置cookie
   * @param {*} name
   * @param {*} value
   * @param {*} time
   */
  Vue.prototype.setCookie = function (name, value, time) {
    var strsec = this.getsec(time);
    var exp = new Date();
    exp.setTime(exp.getTime() + strsec * 1);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
  }
  /**
   *
   * @param {*} str
   */
  Vue.prototype.getsec = function (str) {
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    if (str2 == "s") {
      return str1 * 1000;
    } else if (str2 == "h") {
      return str1 * 60 * 60 * 1000;
    } else if (str2 == "d") {
      return str1 * 24 * 60 * 60 * 1000;
    }
  }
  Vue.prototype.getCookie = function (name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
      return unescape(arr[2]);
    else
      return null;
  }
  Vue.prototype.$fetch = function (
      $url,
      $method,
      $data,
      $headers,
    ) {
      return new Promise((resolve, reject) => {
        axios({
          url: $url,
          method: $method.toLowerCase(),
          data: $data,
          headers: $headers,
          timeout: 30000
        }).then(res => {
          resolve(res.data);
        }).catch((err) => {
          reject(err);
        })
      });
    },
    Vue.prototype.$req = function (
      $url,
      $method,
      $data,
      $headers,
      $auth,
      show = false
    ) {
      var startTime = new Date();
      return new Promise((resolve, reject) => {
        axios({
          url: $url,
          method: $method.toLowerCase(),
          data: $data,
          headers: $headers,
          timeout: 30000,
          auth: $auth
        }).then(res => {
          var endTime = new Date();
          var data = {
            requestTime: this.datetimeFormat(startTime, "yyyy-MM-dd hh:mm:ss"),
            responseTime: this.datetimeFormat(endTime, "yyyy-MM-dd hh:mm:ss"),
            diffTime: (endTime.getTime() - startTime.getTime()),
            result: res.data,
            show: show,
            code: res.status,
            requestHeader: $headers,
            responseHeader: res.headers,
          };
          resolve(data);
        }).catch((err) => {
          reject(err);
          console.log(err);
        })
      })
    }
  //================ 网络类操作 - 结束 ================

  //================ swagger操作 - 结束 ================
  /**
   * 获取样例数据
   * @param {*} type
   * @param {*} format
   */
  Vue.prototype.exampleValue = function (type, format) {
    if (type == "boolean") {
      return "false";
    } else if (
      type == "string" &&
      (format == "date" || format == "date-time")
    ) {
      return this.datetimeFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
    } else if (type == "string") {
      return "string";
    } else if (type == "integer") {
      return "0";
    } else if (type == "number") {
      return "0";
    }
    return null;
  }
  /**
   * 获取真值
   * @param {*} type
   * @param {*} format
   */
  Vue.prototype.realValue = function (type, example) {
    if (type == "boolean") {
      return example === 'true';
    } else if (type == "string") {
      return example || '';
    } else if (type == "integer" || type == "number") {
      return parseInt(example);
    }
    return example;
  }
  Vue.prototype.toParams = function (data) {
    var ret = {
      data: {},
      success: true,
      msg: "",
      field: "",
      meta: {}
    };
    var meta = {
      path: {},
      head: {},
      cookie: {}
    };
    if (data && data.length > 0) {
      var tmp = {};
      for (var i = 0; i < data.length; i++) {
        var v = "";
        if (data[i].value) {
          v = data[i].value.trim();
        }
        v = v.replace(/[\r\n]/g, "");
        var field = data[i].name;
        if (data[i].schema && (data[i].schema.$ref || (data[i].schema.items && data[i].schema.items.$ref))) {
          try {
            if (v == '') {
              return {
                data: {},
                success: false,
                msg: "req",
                field: field
              };
            }
            if (v.indexOf("{") == 0) {
              var p = JSON.parse(v);
              if (data[i].schema.items) {
                tmp = [p];
              } else {
                tmp = p;
              }
            } else {
              tmp = eval(v);
            }
          } catch (exception) {
            ret.success = false;
            ret.msg = "error";
            ret.field = field;
            return ret;
          }
        } else if (data[i].schema.items) {
          if (v == '' && data[i].required) {
            return {
              data: {},
              success: false,
              msg: "req",
              field: field
            };
          }
          if (data[i].enable) {
            tmp = [this.realValue(data[i].type, v)];
          }
        } else {
          if (v == '' && data[i].required) {
            return {
              data: {},
              success: false,
              msg: "req",
              field: field
            };
          }
          if (data[i].enable) {
            if (data[i].in == 'path') {
              meta.path[field] = v;
              continue;
            }
            if (data[i].in == 'head') {
              meta.head[field] = v;
              continue;
            }
            if (data[i].in == 'cookie') {
              meta.cookie[field] = v;
              continue;
            }
            var t = this.realValue(data[i].type, v);
            if (tmp[field] != undefined) {
              if (!(tmp[field] instanceof Array)) {
                var f = tmp[field];
                tmp[field] = [f];
              }
              tmp[field].push(t);
            } else {
              tmp[field] = t;
            }
          }
        }
      }
      ret.data = tmp;
      ret.meta = meta;
    }
    return ret;
  }
  Vue.prototype.toJSONObject = function (data) {
    var jt = true;
    if (data && data.child && data.child.length > 0) {
      if (data.type == 'array') {
        jt = false;
      }
      var ret = {};
      for (var i = 0; i < data.child.length; i++) {
        if (data.child[i].type == 'array' || (data.child[i].type == 'object' && data.child[i].format == 'child')) {
          ret[data.child[i].name] = this.toJSONObject(data.child[i]);
        } else {
          ret[data.child[i].name] = this.realValue(data.child[i].type, data.child[i].example);
        }
      }
      if (jt) {
        return ret;
      } else {
        return [ret];
      }
    } else {
      if (data.type == 'array' && data.format != 'child') {
        return [this.realValue(data.format, data.example)];
      }
      return undefined;
    }
  }
  Vue.prototype.parameters = function (parameters, path) {
    var isBody = false;
    //表单
    var $in = "form";
    //   // 半选
    var checkAll = false;
    var indeterminate = true;
    var checkNum = 0;
    if (parameters) {
      for (var i = 0; i < parameters.length; i++) {
        //       this.index = 0;
        var tmp = parameters[i];
        var value = "";
        var example = "";
        var enable = tmp.required;
        var arr = tmp.type == "array";
        if (tmp["in"] == "body") {
          $in = "body";
        }

        // 这里有问题，有毛病
        if (tmp.schema) {
          enable = true;
          if (!arr && tmp.schema.$ref) {
            if (!isBody) {
              isBody = true;
            }
            parameters[i].schema.data = this.transf(
              this.$store.state.models,
              tmp.schema.$ref
            );
            parameters[i].type = "json";
          } else if (arr && tmp.schema.items.$ref) {
            if (!isBody) {
              isBody = true;
            }
            parameters[i].schema.data = this.transf(
              this.$store.state.models,
              tmp.schema.items.$ref
            );
            parameters[i].type = "json";
          } else if (arr) {
            parameters[i].format = tmp.schema.items.type;
            parameters[i].type = tmp.type;
          } else {
            parameters[i].type = "object";
          }
        } else {
          parameters[i].schema = {
            data: [],
          };
          // 存在默认数据
          var d = tmp["default"];
          if (d != undefined) {
            if (d === true) {
              example = "true";
            } else if (d === false) {
              example = "false";
            } else {
              example = d + "";
            }
            enable = true;
            value = example;
          } else if (tmp.type == "ref") {
            example = "";
          } else {
            example = this.exampleValue(tmp.type, tmp.format);
          }
        }
        // 设置选择状态
        indeterminate = indeterminate && enable;
        if (enable) {
          checkNum++;
          checkAll = true;
        }
        parameters[i].example = example;
        parameters[i].value = value;
        parameters[i].enable = enable;
        parameters[i].self = false;
        parameters[i].key = parameters[i].name + i;
      }
    } else {
      parameters = [];
    }
    var ret = {
      parameters: parameters,
      status: {
        checkAll: checkNum > 0 && indeterminate,
        indeterminate: checkAll && !indeterminate,
        checkNum: checkNum,
        isBody: isBody,
        path: path,
        $in: $in,
      }
    }
    return ret;
  }
  Vue.prototype.responses = function (responses) {
    for (var key in responses) {
      var item = responses[key];
      if (item.schema && item.schema.$ref) {
        item.schema.data = this.transf(
          this.$store.state.models,
          item.schema.$ref
        );
        item.schema.demo = JSON.stringify(
          this.toJSONObject(item.schema.data),
          undefined,
          2
        );
        continue;
      }
      if (item.schema && item.schema.items && item.schema.items.$ref) {
        item.schema.data = this.transf(
          this.$store.state.models,
          item.schema.items.$ref
        );
        item.schema.demo = JSON.stringify(
          [this.toJSONObject(item.schema.data)],
          undefined,
          2
        );
        continue;
      }
    }
    return responses;
  }
  /**
   * swagger复杂类型转换成 table 可视数据
   * @param {*} ref
   * @param {*} data
   */
  Vue.prototype.transf = function (data, ref) {
    var key = "";
    if (ref) {
      key = ref.replace("#/definitions/", "");
    }
    if (data && data[key] && data[key].properties) {
      var ret = {
        description: data[key].description,
        type: data[key].type,
        name: key,
        example: "",
        format: data[key].format,
        required: true,
        key: key
      };
      var child = [];
      var requireds = data[key].required;
      var index = 0;
      for (var propertie in data[key].properties) {
        var p = data[key].properties[propertie];
        var description = p.description;
        var required = false;
        var format = p.format;
        var item = undefined;
        if (requireds) {
          for (var i = 0; i < requireds.length; i++) {
            if (propertie == requireds[i]) {
              required = true;
              break;
            }
          }
        }
        var example = this.exampleValue(p.type, p.format);
        if (p.type == "array" && p.items && p.items.$ref) {
          item = this.transf(data, p.items.$ref);
          item.format = "child";
        } else if (p.type == "object" && p.items && p.items.$ref) {
          item = this.transf(data, p.items.$ref);
          item.format = "child";
        } else if (p.type == "array" && p.items && p.items.type) {
          format = p.items.type;
          example = this.exampleValue(format, null);
        }
        if (item) {
          item.type = p.type;
          item.name = propertie;
          item.required = required;
        } else {
          item = {
            name: propertie,
            type: p.type,
            example: example,
            description: description,
            required: required,
            format: format
          };
        }
        item.key = key + (index++);
        child.push(item);
      }
      if (child.length > 0) {
        ret.child = child;
      }
      return ret;
    }
    return undefined;
  }
  //================ swagger操作 - 结束 ================
}
export default util