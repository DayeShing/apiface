/** * 集成页 * author:Daye Shing */
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
        <div
          class="arrow-icon"
          :class="{ shrink: !isShowLeft }"
          @click="toggleLeft"
        ></div>
        <div class="left-position-content">
          <!-- 菜单层 -->
          <menu-tree @selectMenu="loadData" ref="menus" />
        </div>
      </div>
      <!-- 内容层 -->
      <div class="view-main" :class="{ 'not-show-left-body': isShowLeft }">
        <index-view
          :scrollTop="scrollTop"
          :error="error"
          :feelbackId="feelbackId"
          ref="view"
          @submit="submit"
          @feelback="feelback"
        />
        <transition name="el-fade-in">
          <div class="page-up" @click="scrollToTop" v-show="toTopShow">
            <i class="el-icon-caret-top"></i>
          </div>
        </transition>
      </div>
      <!-- 工具箱 -->
      <toolbox
        :class="{
          boxtoTopShow: toTopShow,
        }"
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
import InfoDialog from "dialog/infoDialog";
import ConfigDialog from "dialog/configDialog";
import IndexView from "page/api/index";
import MenuTree from "components/menu";
import Toolbox from "components/toolbox/toolbox";
import { inter } from "api/interface";
import { permitted } from "api/permitted";
import Qs from "qs";
export default {
  data() {
    return {
      page: "layout.layout.",
      isShowLeft: true,
      disableLeft: false,
      menus: [],
      blank: false,
      toTopShow: false,
      scrollTop: 0,
      error: false,
      feelbackId: "",
      data: {
        swagger: "", //版本信息
        host: "", //主机
        basePath: "", //base
        info: {}, //其他
        protocol: "" //协议
      },
      addr: "",
      index: 0,
      heads: [],
      auth: {
        use: false,
        type: "none",
        header: "Authorization",
        token: "",
        user: "",
        password: ""
      },
      loading: null
    };
  },
  components: {
    Toolbox,
    MenuTree,
    IndexView,
    InfoDialog,
    ConfigDialog,
    Blank: resolve => require(["page/api/blank.vue"], resolve)
  },
  props: {
    mode: {
      type: Boolean,
      default() {
        return true;
      }
    }
  },
  mounted() {
    this.loadConfig();
    this.$nextTick(() => {
      window.addEventListener("scroll", this.handleScroll, true); // 取消事件冒泡，防止绑定失败
    });
  },
  destroyed() {
    window.removeEventListener("scroll", this.handleScroll, true); // 取消事件冒泡
  },
  watch: {
    mode(val, oldVal) {
      this.disableLeft = oldVal;
      this.isShowLeft = val;
      this.error = oldVal;
      this.$refs.view.resize();
    }
  },
  methods: {
    feelback() {
      this.$refs.view.feel(this.heads, "head");
      this.$refs.view.feel(this.auth, "auth");
    },
    scrollToTop() {
      let timer = null;
      let _this = this;
      cancelAnimationFrame(timer);
      timer = requestAnimationFrame(function fn() {
        if (_this.scrollTop > 5000) {
          _this.scrollTop -= 200;
          document.getElementsByClassName("view-main")[0].scrollTop =
            _this.scrollTop;
          timer = requestAnimationFrame(fn);
        } else if (_this.scrollTop > 1000 && _this.scrollTop <= 5000) {
          _this.scrollTop -= 100;
          document.getElementsByClassName("view-main")[0].scrollTop =
            _this.scrollTop;
          timer = requestAnimationFrame(fn);
        } else if (_this.scrollTop > 200 && _this.scrollTop <= 1000) {
          _this.scrollTop -= 20;
          document.getElementsByClassName("view-main")[0].scrollTop =
            _this.scrollTop;
          timer = requestAnimationFrame(fn);
        } else if (_this.scrollTop > 0 && _this.scrollTop <= 200) {
          _this.scrollTop -= 10;
          document.getElementsByClassName("view-main")[0].scrollTop =
            _this.scrollTop;
          timer = requestAnimationFrame(fn);
        } else {
          cancelAnimationFrame(timer);
          _this.toTopShow = false;
        }
      });
    },
    handleScroll() {
      let dom = document.getElementsByClassName("view-main")[0];
      this.scrollTop = dom.scrollTop;
      if (this.scrollTop > 200) {
        this.toTopShow = true;
      } else {
        this.toTopShow = false;
      }
    },
    loadConfig() {
      if (this.mode) {
        this.$getch(
          "conf.json",
          "get",
          {},
          { Pragma: "no-cache", "Cache-Control": "no-cache" }
        )
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
    loadFeelback(id) {
      var u = inter.back.document.replace(/\$\{resourceId\}/g, id);
      this.$fetch(u).then(res => {
        if (
          res &&
          res.data &&
          res.status &&
          res.response &&
          res.request &&
          res.demo
        ) {
          this.$refs.view.loadFeelback(
            res.data,
            res.status,
            res.demo,
            res.request,
            res.response
          );
          this.feelbackId = id;
          if (res.head) {
          }
          if (res.cookie) {
          }
          if (res.auth) {
          }
          return;
        }
        this.calcMenu(undefined);
      });
    },
    _fetch(url, method, heads, data, ret) {
      this.openLoad(this.$self("loading"));
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
      if (this.$store.state.app) {
        _url = inter.proxy;
        h["Self-Agent-Address"] = url;
      } else if (this.$store.state.proxy) {
        _url = this.$store.state.proxy;
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
          this.closeLoad();
          if (res) {
            console.log(res, "response info");
            this.$refs.view.fetch(res);
          }
        })
        .catch(err => {
          this.closeLoad();
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
        if (!this.mode || this.$store.state.mode != "single") {
          this.$message({ type: "warning", message: this.$self("tips") });
          return;
        }
        this.$message({ type: "warning", message: "持续开发中" });
      }
    },
    gatType(arr) {
      if (arr) {
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
        for (var i = 0; i < methons.length; i++) {
          for (var m in arr) {
            if (m.toUpperCase() === methons[i]) {
              return m;
            }
          }
        }
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
      this.$refs.view.load(data);
    },
    submit(meta, data) {
      this._fetch(meta.url, meta.method, meta.heads, data, meta.ret);
    },
    toggleLeft() {
      if (this.disableLeft) {
        return;
      }
      this.isShowLeft = !this.isShowLeft;
      this.$refs.view.resize();
    },
    /**
     * 计算菜单
     */
    calcMenu(info) {
      if (!(info && info.swagger && info.paths && info.host && info.basePath)) {
        this.blank = true;
        return;
      }
      this.blank = false;
      this.$store.commit(
        "setTitle",
        info.info && info.info.title ? info.info.title : ""
      );
      this.$store.commit("setModels", info.definitions);
      var menus = [];

      for (var j = 0; j < info.tags.length; j++) {
        menus.push({
          name: info.tags[j].name,
          key: j + "",
          desc: info.tags[j].description
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
        var label = [];
        var tags = undefined;
        // 获取内容和标题，请求类型信息 type
        for (var req in info.paths[path]) {
          var md = info.paths[path][req];
          if (!tags) {
            tags = md.tags;
          }
          var lb = true;
          for (var i = 0; i < label.length; i++) {
            if (label[i] == md.summary) {
              lb = false;
            }
          }
          if (lb) {
            label.push(md.summary);
          }
        }
        if (!tags) {
          continue;
        }
        // 获取父类数据
        var data = { type: info.paths[path], path: path };
        for (var i = 0; i < tags.length; i++) {
          for (var j = 0; j < menus.length; j++) {
            if (tags[i] == menus[j].name) {
              if (menus[j].child && menus[j].child.length > 0) {
                data.key = menus[j].child.length + "";
                menus[j].child.push(data);
              } else {
                data.key = "0";
                menus[j].child = [data];
              }
              break;
            }
          }
        }
      }
      var tmp = [];
      for (var j = 0; j < menus.length; j++) {
        if(menus[j].child && menus[j].child.length > 0){
          var m = {
            label: menus[j].name,
            key: j + "",
            child:[]
          }
          for (var k = 0; k < menus[j].child.length; k++) {
            var lbs = [];
            for (var req in menus[j].child[k].type) {
              lbs.push(menus[j].child[k].type[req].summary);
            }
            m.child.push({ label: lbs, key: "" + k,path: menus[j].child[k].path});
          }
          m.child.sort(function (obj1, obj2) {
            return obj1.path.localeCompare(obj2.path);
          });
          tmp.push(m);
        }
      }

      tmp.sort(function (obj1, obj2) {
        return obj1.label.localeCompare(obj2.label,'zh-CN');
    });

      this.menus = menus;
      this.$refs.menus.loadMenus(tmp[0].key ,tmp[0].child[0].key, tmp);
      this.data.total = total;
    },

    /**
     * 加载动画
     */
    openLoad(tips) {
      if (this.loading == null) {
        this.loading = this.$loading({
          lock: true,
          text: tips,
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.3)"
        });
      }
    },
    closeLoad() {
      if (this.loading != null) {
        this.loading.close();
        this.loading = null;
      }
    },
    getUserInfo() {
      this.$fetch(inter.auth.getCurrentUser).then(res => {
        if (res && res.success) {
          this.$store.commit("setUser", res.data);
        }
      });
    },
    getPermitted() {
      this.$fetch(
        inter.auth.hasPermissions,
        Qs.stringify(
          { module: inter.moduleNames.center, permissions: permitted },
          { arrayFormat: "repeat" }
        ),
        { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
      ).then(res => {
        if (res && res.success) {
          this.$store.commit("setPermitteds", res.data);
        } else {
          this.msg(res);
        }
      });
    },
    loadApi(load = true) {
      this.$fetch(inter.api.docs)
        .then(res => {
          if (res && res.success) {
            this.options(res.data, undefined, load);
            return;
          }
          this.$message.error(res.message);
          return;
        })
        .catch(err => {
          console.log(err);
        });
    },
    options(docs, addr = undefined, load = true) {
      if (docs && docs.length > 0) {
        if (addr == undefined && docs[0].options.length > 0) {
          addr = docs[0].options[0].addr;
          if (docs[0].options[0].docId) {
            addr = docs[0].options[0].mapping
              ? docs[0].options[0].addr
              : docs[0].options[0].docId;
          }
        }
        this.$emit("docs", docs, load ? addr : undefined);
        if (load) {
          if (addr) {
            this.getApis(addr);
            return;
          }
          this.calcMenu(undefined);
        }
      }
    },
    /**
     * 请求获取APIs 加载APIs内容
     */
    getApis(url) {
      this.openLoad(this.$self("loading"));
      var reg = new RegExp("^[0-9]*$");
      var idUrl = reg.test(url);
      var baseUrl = url.startsWith(inter.ContextPath);
      if (idUrl || baseUrl) {
        var u = url;
        if (idUrl) {
          this.$store.commit("setDocId", url);
          u = inter.api.document.replace(/\$\{resourceId\}/g, url);
        }
        this.$fetch(u)
          .then(res => {
            this.calcMenu(res);
            this.closeLoad();
            return;
          })
          .catch(err => {
            this.closeLoad();
            this.calcMenu(undefined);
            console.log(err);
          });
        return;
      }
      //验证Email地址：/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
      if (url.startsWith("http")) {
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
        // 单独应用
        if (this.$store.state.app) {
          _url = inter.proxy;
          h["Self-Agent-Address"] = url;
        } else if (this.$store.state.proxy != "") {
          _url = this.$store.state.proxy;
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
            this.closeLoad();
            if (res.code == 200) {
              this.calcMenu(res.result);
            } else {
              this.calcMenu(undefined);
            }
          })
          .catch(err => {
            console.log(err);
            this.closeLoad();
            this.$message({
              type: "error",
              message: this.$t("code.fetchError")
            });
            this.calcMenu(undefined);
          });
        return;
      }
    },
    /**
     * 初始化配置
     */
    init(conf) {
      if (conf) {
        this.$store.commit("setConfig", conf);
        if (this.$store.state.app) {
          this.getUserInfo();
          this.getPermitted();
          this.loadApi();
          return;
        }
        if (this.$store.state.mode == "single") {
          this.options(conf.docs);
          return;
        }
        var options = [];
        var addr = window.location.protocol + "//" + window.location.host;
        if (!conf.api.startsWith("/")) {
          addr += "/";
        }
        addr += conf.api;
        var options = {
          group: this.$self("vm"),
          options: [
            {
              name: this.$self("addr1"),
              addr: addr
            }
          ]
        };
        this.options(options, addr);
        return;
      }
      this.calcMenu(undefined);
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
.apiface:not(.container-body) {
  position: relative;
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
      .page-up {
        background-color: #008ead;
        position: fixed;
        right: 10px;
        bottom: 10px;
        width: 40px;
        height: 40px;
        border-radius: 20px;
        cursor: pointer;
        transition: 0.3s;
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.5);
        opacity: 0.5;
        z-index: 100;
        .el-icon-caret-top {
          color: #fff;
          display: block;
          line-height: 40px;
          text-align: center;
          font-size: 18px;
        }
        p {
          display: none;
          text-align: center;
          color: #fff;
        }
        &:hover {
          opacity: 1;
        }
      }
    }
  }
  .boxtoTopShow {
    bottom: 60px !important;
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
      background-image: url("../assets/icons/expand_arrow.png");
      &:hover {
        background-image: url("../assets/icons/expand_arrow_active.png");
      }
      &.shrink {
        background-image: url("../assets/icons/shrink_arrow.png");
        &:hover {
          background-image: url("../assets/icons/shrink_arrow_active.png");
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
      background-image: url("../assets/icons/expand_arrow_r.png");
      &:hover {
        background-image: url("../assets/icons/expand_arrow_active_r.png");
      }
      &.shrink {
        background-image: url("../assets/icons/shrink_arrow_r.png");
        &:hover {
          background-image: url("../assets/icons/shrink_arrow_active_r.png");
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
