/**
 * 集成页
 * author:Daye Shing
 */
<template>
  <div class="apiface">
    <blank v-show="blank" />
    <div class="normal" v-show="!blank">
      <!-- 左侧面板层 -->
      <div
        class="left-position-box left-right-position-box"
        :class="{ 'not-show-left': !isShowLeft, shrink: !isShowLeft }"
        ref="leftPanel"
      >
        <div class="left-dash-panel dash-panel" ref="leftDashPanel"></div>
        <div class="arrow-icon" :class="{ shrink: !isShowLeft }" @click="toggleLeft"></div>
        <div class="left-position-content">
          <!-- 菜单层 -->
          <self-menu @selectMenu="loadData" ref="menus" />
        </div>
      </div>
      <!-- 内容层 -->
      <div class="view-main" :class="{ 'not-show-left-body': isShowLeft }">
        <!-- 具体页面 -->
        <self-view ref="view" @submit="submit" />
      </div>
      <!-- 工具箱 -->
      <toolbox
        :info="{
          portfolio: $self('portfolio'),
          dribbble: $self('dribbble'),
          linkedin: $self('linkedin'),
        }"
        @selectMenu="clickBox"
      />
      <info-dialog ref="infoDialog" />
      <config-dialog ref="configDialog" @commit="commit" />
    </div>
  </div>
</template>

<script type="cs6">
import InfoDialog from "../dialog/infoDialog";
import ConfigDialog from "../dialog/configDialog";
import Index from "../page/index";
import menu from "../compontents/menu";
import toolbox from "../compontents/toolbox/toolbox";
export default {
  data() {
    return {
      page: "layout.layout.",
      isShowLeft: true,
      menus: [],
      blank: false,
      data: {
        swagger: "", //版本信息
        host: "", //主机
        basePath: "", //base
        info: {}, //其他
        protocol: "" //协议
      },
      models: {},
      addr: "",
      options: [],
      single: false,
      index: 0,
      heads: [],
      proxy: "",
      auth: {
        use: false,
        type: "none",
        header: "Authorization",
        token: "",
        user: "",
        password: ""
      }
    };
  },
  props: {
    mode: {
      type: Boolean,
      default() {
        return true;
      }
    }
  },
  components: {
    "self-view": Index,
    "self-menu": menu,
    "info-dialog": InfoDialog,
    "config-dialog": ConfigDialog,
    toolbox: toolbox,
    blank: resolve => require(["../page/blank.vue"], resolve)
  },
  mounted() {
    this.getConf();
  },
  methods: {
    getConf() {
      if (this.mode) {
        this.$fetch("static/conf.json", "get")
          .then(res => {
            this.init(res);
          })
          .catch(err => {});
      }
    },
    commit(heads, auth) {
      this.auth = auth;
      this.heads = heads;
    },
    _fetch(url, method, heads, data, ret) {
      const loading = this.openLoad(this.$self("loading"));
      var h = {};
      for (var i = 0; i < this.heads.length; i++) {
        h[this.heads[i].name] = this.heads[i].content;
      }
      for (var i = 0; i < heads.length; i++) {
        h[heads[i].name] = heads[i].content;
      }

      if (this.auth.type == "bearer" && this.auth.use) {
        var token = this.auth.header + "=" + this.auth.token;
        if (url.indexOf("?") != -1) {
          url += "&" + token;
        } else {
          url += "?" + token;
        }
      } else if (this.auth.type == "bearer" && !this.auth.use) {
        h[this.auth.header] = this.auth.token;
      }

      var isGet = method.toLowerCase() == "get";
      if (isGet && data.length > 0) {
        if (url.indexOf("?") != -1) {
          url += "&" + data;
        } else {
          url += "?" + data;
        }
      }
      var _url = url;
      if (this.proxy) {
        _url = this.proxy;
        h["Self-Agent-Address"] = url;
      }
      this.$req(
        _url,
        method,
        isGet ? {} : data,
        h,
        this.auth.type == "basic"
          ? {
              username: this.auth.user,
              password: this.auth.password
            }
          : undefined,
        ret
      )
        .then(res => {
          loading.close();
          if (res) {
            console.log(res, "response info");
            this.$refs.view.fetch(res);
          }
        })
        .catch(err => {
          loading.close();
          this.$message({
            type: "error",
            message: this.$t("code.fetchError")
          });
        });
    },
    $self(k) {
      return this.$t(this.page + k);
    },
    clickBox(value) {
      if (value == "linkedin") {
        this.$refs.infoDialog.openDialog(this.data);
        return;
      }
      if (value == "portfolio") {
        this.$refs.configDialog.openDialog();
        return;
      }
      if (value == "dribbble") {
        if (!this.mode || !this.single) {
          this.$message({ type: "warning", message: this.$self("tips") });
          return;
        }
        this.$message({ type: "warning", message: "持续开发中" });
      }
    },
    gatType(arr) {
      if (arr && arr.length > 0) {
        var methons = [
          "POST",
          "GET",
          "PUT",
          "DELETE",
          "OPTIONS",
          "PATCH",
          "HEAD",
          "TRACE",
          "CONNECT"
        ];
        var type = undefined;
        for (var i = 0; i < methons.length; i++) {
          if (type) {
            return type;
          }
          for (var j = 0; j < arr.length; j++) {
            if (arr[j].toUpperCase() === methons[i]) {
              type = arr[j];
              break;
            }
          }
        }
        return arr[0];
      }
      return "get";
    },
    loadData(menu, child) {
      var m = this.menus[parseInt(menu)];
      var data = this.deepClone(m.child[parseInt(child)]);
      data.host = this.data.protocol + this.data.host;
      data.basePath = this.data.basePath;
      data.parent = m.name;
      data.parentDesc = m.desc;
      data.defaultType = this.gatType(data.type);
      this.$refs.view.load(this.resps(this.params(data)));
    },
    resps(data) {
      for (var key in data.content.responses) {
        var item = data.content.responses[key];
        if (item.schema && item.schema.$ref) {
          this.index = 0;
          data.content.responses[key].schema.data = this.transf(
            this.models,
            item.schema.$ref
          );
          data.content.responses[key].schema.demo = JSON.stringify(
            this.toJSONObject(data.content.responses[key].schema.data),
            undefined,
            2
          );
        }
      }
      return data;
    },
    params(data) {
      var isBody = false;
      //表单
      var $in = "form";
      // 半选
      var checkAll = false;
      var indeterminate = true;
      var checkNum = 0;
      if (data.content.parameters) {
        for (var i = 0; i < data.content.parameters.length; i++) {
          this.index = 0;
          var tmp = data.content.parameters[i];
          var value = "";
          var example = null;
          var enable = tmp.required;
          if (tmp["in"] == "body") {
            $in = "body";
          }
          if (tmp.schema) {
            if (!isBody) {
              isBody = true;
            }
            // 这里设置body参数
            example = "";
            value = "";
            data.content.parameters[i].schema.data = this.transf(
              this.models,
              tmp.schema.$ref
            );
            data.content.parameters[i].type = "json";
          } else {
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
          data.content.parameters[i].example = example;
          data.content.parameters[i].value = value;
          data.content.parameters[i].enable = enable;
          data.content.parameters[i].self = false;
        }
      } else {
        data.content.parameters = [];
      }
      data.content.status = {
        checkAll: checkNum > 0 && indeterminate,
        indeterminate: checkAll && !indeterminate,
        checkNum: checkNum,
        isBody: isBody,
        path: data.path,
        $in: $in
      };
      return data;
    },
    submit(meta, data) {
      // console.log(meta, "request meta info");
      this._fetch(meta.url, meta.method, meta.heads, data, meta.ret);
    },
    toggleLeft() {
      this.isShowLeft = !this.isShowLeft;
      if (this.isShowLeft) {
        this.$refs.view.resize();
      }
    },
    calcMenu(info) {
      if (!(info && info.swagger && info.paths && info.host && info.basePath)) {
        this.blank = true;
        return;
      }
      this.blank = false;
      this.models = info.definitions;
      var menus = [];
      var tmp = [];
      for (var j = 0; j < info.tags.length; j++) {
        menus.push({
          name: info.tags[j].name,
          key: j + "",
          desc: info.tags[j].description
        });
        tmp.push({
          label: info.tags[j].name,
          key: j + ""
        });
      }
      var protocol = info.protocol;
      if (!protocol) {
        var index = this.addr.indexOf("://");
        if (index != -1) {
          protocol = this.addr.substring(0, index + 3);
        } else {
          protocol = "http://";
        }
      }
      this.data = {
        swagger: info.swagger,
        host: info.host,
        basePath: info.basePath,
        info: info.info,
        protocol: protocol
      };
      var total = 0;
      for (var path in info.paths) {
        total++;
        var content = undefined;
        var type = [];
        var label = "";
        var tags = [];
        // 获取内容和标题，请求类型信息
        for (var req in info.paths[path]) {
          if (!content) {
            content = info.paths[path][req];
            label = content.summary;
            tags = content.tags;
          }
          type.push(req);
        }
        // 获取父类数据
        var data = { label: label, path: path, type: type, content: content };
        for (var i = 0; i < tags.length; i++) {
          for (var j = 0; j < menus.length; j++) {
            if (tags[i] == menus[j].name) {
              if (menus[j].child && menus[j].child.length > 0) {
                data.key = menus[j].child.length + "";
                menus[j].child.push(data);
                tmp[j].child.push({ label: data.label, key: data.key });
              } else {
                data.key = "0";
                menus[j].child = [data];
                tmp[j].child = [{ label: data.label, key: data.key }];
              }
              break;
            }
          }
        }
      }
      this.menus = menus;
      this.loadData(menus[0].key, menus[0].child[0].key);
      this.$refs.menus.loadMenus(tmp[0].key + "-" + tmp[0].child[0].key, tmp);
      this.data.total = total;
    },
    config(single, addr, options, proxy) {
      this.options = options;
      this.addr = addr;
      this.single = single;
      this.proxy = proxy;
      this.getApis(addr);
    },
    openLoad(tips) {
      return this.$loading({
        lock: true,
        text: tips,
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.3)"
      });
    },
    getApis(url) {
      const loading = this.openLoad(this.$self("loading"));
      var h = {
        Pragma: "no-cache",
        "Cache-Control": "no-cache"
      };
      for (var i = 0; i < this.heads.length; i++) {
        h[this.heads[i].name] = this.heads[i].content;
      }
      if (this.auth.type == "bearer" && this.auth.use) {
        var token = this.auth.header + "=" + this.auth.token;
        if (url.indexOf("?") != -1) {
          url += "&" + token;
        } else {
          url += "?" + token;
        }
      } else if (this.auth.type == "bearer" && !this.auth.use) {
        h[this.auth.header] = this.auth.token;
      }
      var _url = url;
      if (this.proxy) {
        _url = this.proxy;
        h["Self-Agent-Address"] = url;
      }
      this.$req(
        _url,
        "get",
        {},
        h,
        this.auth.type == "basic"
          ? {
              username: this.auth.user,
              password: this.auth.password
            }
          : undefined,
        false
      )
        .then(res => {
          loading.close();
          if (res.code == 200) {
            this.calcMenu(res.result);
          } else {
            this.calcMenu(undefined);
          }
        })
        .catch(err => {
          loading.close();
          this.$message({
            type: "error",
            message: this.$t("code.fetchError")
          });
          this.calcMenu(undefined);
        });
    },
    init(conf) {
      // 直接访问
      if (conf) {
        var single = conf.mode == "single";
        var addr = "";
        var options = [];
        var proxy = conf.proxy;
        if (single && conf.docs && conf.docs.length > 0) {
          addr = conf.docs[0].options[0].addr;
          options = conf.docs;
        } else {
          addr = window.location.href;
          var index = addr.indexOf("/apiface.html#/");
          if (index != -1) {
            addr = addr.substring(0, index);
          }
          addr = addr + conf.api;
          options = [
            {
              group: "集成环境",
              options: [
                {
                  name: "接口文档地址",
                  addr: this.addr
                }
              ]
            }
          ];
        }
        this.config(single, addr, options, proxy);
      }
    }
  }
};
</script>

<style lang="less">
.el-loading-mask.is-fullscreen {
  .el-loading-spinner {
    i {
      color: #ffffff;
      font-size: 50px;
    }
    .el-loading-text {
      color: #ffffff;
      margin: 8px 0;
      font-size: 22px;
    }
  }
}
.apiface {
  height: 100%;
  .normal {
    height: 100%;
    position: relative;
    .view-main:not(.not-show-left-body) {
      padding-left: 5px !important;
    }
    .view-main {
      overflow: auto;
      padding-left: 283px;
      height: 100%;
    }
  }
}

.left-right-position-box {
  .dash-panel {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    z-index: -99;
  }
  .arrow-icon {
    cursor: pointer;
    width: 12px;
    height: 40px;
    position: absolute;
    top: 50%;
    margin-top: -20px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
  }
  &.left-position-box {
    .arrow-icon {
      display: none;
      right: -12px;
      margin-top: -20px;
      background-image: url("../icons/expand_arrow.png");
      &:hover {
        background-image: url("../icons/expand_arrow_active.png");
      }
      &.shrink {
        background-image: url("../icons/shrink_arrow.png");
        &:hover {
          background-image: url("../icons/shrink_arrow_active.png");
        }
      }
    }
    &.not-show-left {
      .arrow-icon {
        display: block;
      }
    }
    &:hover {
      .arrow-icon {
        display: block;
      }
    }
  }
  &.right-position-box {
    .arrow-icon {
      display: none;
      left: -12px;
      background-image: url("../icons/expand_arrow_r.png");
      &:hover {
        background-image: url("../icons/expand_arrow_active_r.png");
      }
      &.shrink {
        background-image: url("../icons/shrink_arrow_r.png");
        &:hover {
          background-image: url("../icons/shrink_arrow_active_r.png");
        }
      }
    }
    &:hover {
      .arrow-icon {
        display: block;
      }
    }
  }
}
.left-position-box {
  width: 280px;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 99;
  transition: left 0.2s linear;
  &.not-show-left {
    left: -278px;
  }
  .left-position-content {
    width: 100%;
    height: 100%;
    border-right: 1px solid #e6e6e6;
    position: relative;
  }
}
</style>
