<!--
首页 * author:Daye Shing
-->
<template>
  <div class="container-view">
    <div class="header">
      <div class="header-wrap fl">
        <img height="34" width="34" src="../assets/icons/api.png" />
      </div>
      <div class="header-wrap fl">
        <a class="title" href="http://apiface.daysh.top">{{
          $store.state.title
        }}</a>
        <span
          class="subtitle"
          v-if="!$store.state.app && $store.state.subtitle != ''"
          >({{ $store.state.subtitle }})</span
        >
      </div>

      <el-menu
        class="fl"
        v-if="$store.state.app"
        :default-active="activeIndex"
        background-color="#008ead"
        text-color="#fff"
        active-text-color="#fff"
        mode="horizontal"
        @select="menuSelect"
      >
        <el-menu-item
          :index="m.index"
          :key="key"
          v-for="(m, key) in menu"
          v-show="$store.state.permitteds[secretkey + m.index]"
          >{{ m.lable }}</el-menu-item
        >
      </el-menu>

      <div class="more-info">
        <i class="fa fa-ellipsis-v" @click="more = true"></i>
      </div>
      <div class="api-select">
        <el-select
          v-model="addr"
          filterable
          :placeholder="$self('addr')"
          clearable
          remote
          reserve-keyword
          :loading="loading"
          @change="searchApi"
          :remote-method="selfFilter"
          v-show="!$store.state.app || activeIndex == 'apis'"
        >
          <el-option-group
            v-for="(group, key) in options"
            :key="key"
            :label="group.text"
          >
            <el-option
              v-for="(item, index) in group.options"
              :key="index"
              :label="item.text"
              :value="
                item.docId ? (item.mapping ? item.addr : item.docId) : item.addr
              "
              class="addr-select-item"
            >
              <el-tooltip placement="left-start" effect="light">
                <div slot="content">
                  <span>{{ group.text }}</span>
                  :
                  <span>{{ item.text }}</span>
                  <br />
                  <span>{{ item.addr }}</span>
                  <br />
                </div>
                <span class="item__title">{{ item.text }}</span>
              </el-tooltip>
              <span class="item__addr">{{ item.addr }}</span>
            </el-option>
          </el-option-group>
        </el-select>
      </div>

      <div class="fr icon-list" v-if="$store.state.app">
        <el-badge :is-dot="messageWait != 0" class="item">
          <i class="fa fa-bell-o" @click="notifies = true"></i>
        </el-badge>
      </div>
      <el-popover placement="bottom" trigger="hover" v-if="$store.state.app">
        <div class="user-info">
          <i class="desc-arrow"></i>
          <div class="desc-layer">
            <el-avatar class="fl" :size="50" :src="circleUrl">
              <img src="../assets/icons/api.png" />
            </el-avatar>
            <div class="fl">
              <div class="user-name">{{ $store.state.user.userName }}</div>
              <div>
                <svg class="self-icon" aria-hidden="true">
                  <use
                    xlink:href="#icon-chuangxiangqingtongvip"
                    v-if="$store.state.user.userId == '-3'"
                  />
                  <use
                    xlink:href="#icon-chuangxianghuangjinvip"
                    v-else-if="$store.state.user.admin"
                  />
                  <use xlink:href="#icon-chuangxiangbaiyinvip" v-else />
                </svg>
              </div>
            </div>
          </div>
          <div class="user-role">
            <el-tag
              :type="
                role.admin
                  ? 'success'
                  : role.roleId == '-3'
                  ? 'warning'
                  : 'info'
              "
              v-for="(role, index) in $store.state.user.roles"
              :key="index"
              >{{ role.roleName }}</el-tag
            >
          </div>
          <div class="dropdown-divider"></div>
          <ul>
            <li class="dropdown-item" @click="openDialog(true)">修改密码</li>
            <li class="dropdown-item" @click="openDialog(false)">修改信息</li>
            <li class="dropdown-item">上传头像</li>
            <div class="dropdown-divider"></div>
            <li class="dropdown-item" @click="authEvent('regist')">注册</li>
            <li
              class="dropdown-item"
              @click="authEvent('login')"
              v-if="$store.state.user.userId == '-3'"
            >
              登录
            </li>
            <li class="dropdown-item" @click="authEvent('logout')" v-else>
              退出登录
            </li>
          </ul>
        </div>
        <div class="fr user-info" slot="reference">
          <span class="fr">
            <i class="fa fa-chevron-down" aria-hidden="true"></i>
          </span>
          <span class="fr user-name">{{ $store.state.user.userName }}</span>
          <el-avatar class="fr" :size="34" :src="circleUrl">
            <img src="../assets/icons/api.png" />
          </el-avatar>
        </div>
      </el-popover>
    </div>
    <transition @enter="goLayout" name="el-zoom-in-top">
      <layout
        :mode="layoutMode"
        ref="layout"
        class="container-body"
        @docs="docs"
        v-show="!$store.state.app || activeIndex == 'apis'"
      />
    </transition>
    <home
      class="container-body"
      v-if="$store.state.app && activeIndex == 'home'"
    ></home>
    <doc-manager
      class="container-body"
      v-if="$store.state.app && activeIndex == 'doc'"
    />
    <feelback
      class="container-body"
      v-if="$store.state.app && activeIndex == 'error'"
    />
    <role
      class="container-body"
      v-if="$store.state.app && activeIndex == 'project'"
    />
    <el-drawer :visible.sync="more" :with-header="false" size="360px">
      <version v-if="more"></version>
    </el-drawer>
    <el-drawer
      :visible.sync="notifies"
      :with-header="false"
      size="400px"
      @open="messageWait = 0"
    >
      <notice type="notice" :activities="messages" v-if="notifies"></notice>
    </el-drawer>
    <ModifyUserDialog
      ref="dialog"
      @reload="authEvent('logout')"
    ></ModifyUserDialog>
  </div>
</template>

<script>
import Layout from "./layout.vue";
import Home from "page/home.vue";
import Version from "page/version.vue";
import Notice from "page/notice.vue";
import DocManager from "page/views/doc.vue";
import Feelback from "page/views/feelback.vue";
import Role from "page/views/role.vue";
import ModifyUserDialog from "../dialog/modifyUserDialog";
import { inter } from "api/interface";
export default {
  data() {
    return {
      page: "layout.index.",
      secretkey: "page:sys:",
      activeIndex: "home",
      index: 1,
      loading: false,
      searchTimeout: null,
      addr: "",
      more: false,
      notifies: false,
      init: false,
      id: null,
      type: null,
      //类型枚举
      types: {
        api: "预览文档",
        feelback: "反馈文档",
      },
      layoutMode: true,
      menu: [
        { index: "home", lable: "首页" },
        { index: "doc", lable: "文档管理" },
        { index: "project", lable: "项目组" },
        { index: "error", lable: "错误反馈" },
        { index: "apis", lable: "APIs" },
      ],
      options: [],
      messageWait: 0,
      messages: [],
      heart: null,
      counter: null,
      ws: null,
      reconnent: true,
    };
  },
  components: {
    Role,
    Home,
    Notice,
    DocManager,
    Layout,
    Version,
    Feelback,
    ModifyUserDialog,
  },
  computed: {
    circleUrl() {
      return "";
    },
    userId() {
      return this.$store.state.user.userId;
    },
  },
  watch: {
    userId: {
      handler(val, oldVal) {
        // 菜单发生变化则重新加载描述和表单
        this.clean(true);
        this.connent();
      },
    },
    /**
     * 当id 值确定 id ! null 时,监控init 变化加载
     */
    init(val, oldVal) {
      if (val && this.id && this.$store.state.app) {
        this.toAPIPage(this.id);
      }
    },
    /**
     * 当layout加载完成 init true时,监控id 变化加载
     */
    id(val, oldVal) {
      if (val && this.init && this.$store.state.app) {
        this.toAPIPage(val);
      }
    },

    $route() {
      // 带路由参数变化
      this.type = this.$route.params.type;
      this.changeId(this.$route.params.id);
    },
  },
  created() {
    // 带路由参数
    this.type = this.$route.params.type;
    this.changeId(this.$route.params.id);
  },
  mounted() {},
  methods: {
    openDialog(pwd) {
      if (this.$store.state.user.userId == "-3") {
        this.$router.push({ name: "auth", params: { type: "login" } });
        return;
      }
      this.$refs.dialog.openDialog(this.$store.state.user.userId, pwd);
    },
    authEvent(type) {
      if (type == "logout") {
        this.$fetch(inter.auth.signOut).then((res) => {
          if (res && res.success) {
            this.$router.push({ name: "auth", params: { type: type } });
          }
        });
      }
      this.$router.push({ name: "auth", params: { type: type } });
    },
    /**
     * 改变页面id
     */
    changeId(id) {
      if (id != null) {
        this.id = id;
      }
    },
    goLayout() {
      if (this.$store.state.app) {
        this.options = [];
        this.addOptions();
        this.$refs.layout.loadApi(false);
      }
    },
    /**
     * 改变页面
     */
    toAPIPage(id) {
      if (this.$store.state.app) {
        if (id) {
          if (this.type == "api") {
            this.searchApi(id);
          } else if (this.type == "feelback") {
            this.loadFeelback(id);
          }
          this.activeIndex = "apis";
        }
        return;
      }
    },
    addOptions() {
      if (this.id) {
        this.addr = this.type + this.id;
        var title = this.types[this.type];
        setTimeout(() => {
          this.options.push({
            text: title,
            options: [
              {
                text: title + "(" + this.id + ")",
                addr: window.location.href,
                docId: this.addr,
              },
            ],
          });
        }, 500);
      }
    },
    loadFeelback(id) {
      this.layoutMode = false;
      this.$refs.layout.loadFeelback(id);
    },
    menuSelect(key, keyPath) {
      if (this.activeIndex != key) {
        this.activeIndex = key;
        this.id = null;
        this.type = null;
        this.$router.push({ name: "index" });
      }
    },
    /**
     * 提交文档接口集合
     */
    docs(docs, addr) {
      this.init = true;
      if (docs) {
        for (var i = 0; i < docs.length; i++) {
          this.options.push(docs[i]);
        }
      }
      if (addr) {
        this.addr = addr;
      }
    },
    $self(k) {
      return this.$t(this.page + k);
    },
    searchApi(params) {
      // 加载APIs
      if (params !== "") {
        var f = params.startsWith("feelback");
        if (f) {
          params = params.replace("feelback", "");
          this.loadFeelback(params);
          return;
        }
        f = params.startsWith("api");
        params = params.replace("api", "");
        this.layoutMode = true;
        this.$refs.layout.getApis(params);
      }
    },
    /**
     * 自定义输入APIs-docs
     */
    selfFilter(params) {
      // 这里校验
      if (params !== "" && this.isUrl(params)) {
        if (this.loading) {
          clearInterval(this.searchTimeout);
          this.searchTimeout = null;
        }
        this.loading = true;
        this.searchTimeout = setTimeout(() => {
          this.loading = false;
          var inp = this.$self("input");
          var his = this.$self("his");
          if (this.options && this.options.length > 0) {
            if (this.options[0].id == 1) {
              var data = this.options[0];
              var exists = false;
              for (var i = 0; i < data.options.length; i++) {
                if (data.options[i].addr == params) {
                  return;
                }
              }
              if (!exists) {
                data.options.unshift({
                  text: inp + this.index,
                  addr: params,
                });
                this.options.splice(0, 1, data);
              }
            } else {
              this.options.unshift({
                text: his,
                id: 1,
                options: [
                  {
                    text: inp + this.index,
                    addr: params,
                  },
                ],
              });
            }
          } else {
            this.options.push({
              text: his,
              id: 1,
              options: [
                {
                  text: inp + this.index,
                  addr: params,
                },
              ],
            });
          }
          this.index += 1;
        }, 3000);
      } else {
        this.$message({ type: "warning", message: this.$self("tips") });
      }
    },
    isUrl(url) {
      return true;
      var strRegex =
        "^((https|http|ftp|rtsp|mms)?://)" +
        "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" + //ftp的user@
        "(([0-9]{1,3}.){3}[0-9]{1,3}" + // IP形式的URL- 199.194.52.184
        "|" + // 允许IP和DOMAIN（域名）
        "([0-9a-z_!~*'()-]+.)*" + // 域名- www.
        "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]." + // 二级域名
        "[a-z]{2,6})" + // first level domain- .com or .museum
        "(:[0-9]{1,4})?" + // 端口- :80
        "((/?)|" + // a slash isn't required if there is no file name
        "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
      var re = new RegExp(strRegex);
      if (re.test(url)) {
        return true;
      } else {
        return false;
      }
    },
    //ws================================
    message(res) {
      console.log(res);
    },
    heartBeat() {
      this.heart = setInterval(() => {
        let data = JSON.stringify({ type: 1 });
        if (this.ws && 1 == this.ws.readyState) {
          this.ws.send(data);
        }
      }, 120 * 1000);
    },
    clean(flag) {
      clearInterval(this.heart);
      this.heart = null;
      if (flag) {
        clearInterval(this.counter);
        this.counter = null;
      }
    },
    showNotice: async function(data) {
      await this.$notify({
        title: data.subject,
        message: data.content,
        type: "success",
        offset: 36,
      });
    },
    connent() {
      let $this = this;
      var port = inter.port;
      console.log(port);
      let url = this.address(
        "/center.io?secretkey=" + this.userId,
        port ? port : window.location.port,
        window.location.protocol == "https:" ? "wss" : "ws"
      );
      // let url = "ws://127.0.0.1:28085/ws/msg/" + this.userId;
      try {
        $this.ws = new WebSocket(url);
        ($this.ws.onopen = function() {
          $this.clean(true);
          $this.heartBeat();
        }),
          ($this.ws.onmessage = function(res) {
            // 消息消费
            if (res.data) {
              let result = JSON.parse(res.data);
              if (result && result.success && result.type == 5) {
                var item = {
                  subject: result.subject,
                  content: result.data,
                  timestamp: $this.datetimeFormat(
                    new Date(),
                    "yyyy-MM-dd hh:mm:ss"
                  ),
                };
                $this.messages.unshift(item);
                $this.messageWait += 1;
                $this.showNotice(item);
                return;
              }
              $this.message(JSON.parse(res.data));
            }
          }),
          ($this.ws.onclose = function() {
            // 清空心跳
            $this.clean(true);
            console.log("和服务器断开连接！");
            if ($this.reconnent) {
              $this.counter = setInterval(() => {
                $this.connent();
              }, 100000);
            }
          });
      } catch (a) {
        console.log("ws的地址错误,请重新输入！");
      }
    },
    address(
      uri = "",
      port = window.location.port,
      protocol = window.location.protocol,
      host = window.location.host
    ) {
      var index = host.lastIndexOf(window.location.port);
      if (index != -1) {
        host = host.substring(0, index);
      }
      if (
        uri != "" &&
        uri.substring(0, 1) != "/" &&
        uri.substring(0, 1) != "?"
      ) {
        uri = "/" + uri;
      }
      if (protocol.lastIndexOf(":") == -1) {
        protocol += ":";
      }
      return protocol + "//" + host + port + uri;
    },
  },
  beforeDestroy() {
    this.clean(true);
  },
};
</script>

<style lang="less">
.container-view {
  height: 100%;
  min-width: 1000px;
  overflow: hidden;
  .container-body {
    height: calc(~"100% - 50px");
    // padding-top: 50px;
    // z-index: 1;
  }
  .header {
    width: 100%;
    position: relative;
    z-index: 2;
    top: 0;
    left: 0;
    background-color: #008ead;
    height: 50px;
    padding: 5px;
    .more-info {
      float: right;
      line-height: 40px;
      color: #fff;
      font-size: 16px;
      padding: 0px 8px 0px 8px;
      cursor: pointer;
      i:hover {
        color: #a6deef;
      }
    }
    .icon-list {
      &::before {
        content: "";
        position: absolute;
        width: 0px;
        height: 18px;
        top: 18px;
        background-color: #fff;
      }
      line-height: 38px;
      color: #fff;
      font-size: 16px;

      i {
        margin-left: 8px;
        padding-right: 8px;
        cursor: pointer;
      }
      i:hover {
        color: #a6deef;
      }
      .el-badge__content {
        border: none !important;
      }
      .el-badge__content.is-fixed {
        top: 8px !important;
        transform: none !important;
      }
      .el-badge__content.is-dot {
        height: 6px !important;
        width: 6px !important;
      }
    }

    .header-wrap {
      .header-wrap-icon {
        padding: 0px 3px;
        line-height: 40px;
        font-size: 24px;
        color: #fff;
        font-weight: bolder;
      }
      img {
        padding: 5px 5px;
      }
      .subtitle {
        line-height: 40px;
        color: #fff;
        font-size: 14px;
        font-weight: 600;
      }
      .title {
        line-height: 40px;
        color: #fff;
        font-size: 22px;
        font-weight: 700;
        cursor: pointer;
      }
    }
    .user-info {
      line-height: 40px;
      color: #fff;
      &:hover {
        .fa-chevron-down::before {
          content: "\F077" !important;
        }
      }
      .fa-chevron-down {
        padding-right: 12px;
        padding-left: 6px;
      }

      .user-name {
        padding: 0px 0px 0px 5px;
        font-size: 14px;
      }
      .el-avatar {
        margin: 5px 0px;
      }
    }
    .api-select {
      float: right;
      padding: 5px 0px 5px 5px;
      .el-select {
        width: 380px;
        .el-input--small,
        .el-input--small .el-input__inner {
          height: 30px !important;
        }
      }
    }
    .el-menu.el-menu--horizontal {
      border-bottom: none !important;
      padding-top: 2px;
      padding-left: 50px;
      .el-menu-item {
        height: 36px !important;
        line-height: 36px !important;
      }
      .el-menu-item.is-active {
        border-bottom: 3px solid #fff !important;
      }
      .el-menu-item {
        padding: 0px !important;
        margin: 0 8px;
      }
      .el-menu-item:not(.is-active):focus,
      .el-menu-item:not(.is-active):hover,
      .el-menu-item:not(.is-disabled):focus,
      .el-menu-item:not(.is-disabled):hover {
        color: #ebebeb !important;
        background: #008ead !important;
      }
    }
  }
}

.addr-select-item {
  .item__title {
    float: left;
    width: 95px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .item__addr {
    float: right;
    color: rgb(132, 146, 166);
    font-size: 8px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    width: 240px;
    text-align: right;
  }
}
.el-scrollbar {
  .el-select-dropdown__item {
    padding: 0px 6px 0px 6px !important;
  }
  .el-select-group__title {
    padding-left: 6px !important;
    font-size: 10px !important;
    line-height: 28px !important;
  }
  .el-select-group__wrap:not(:last-of-type) {
    padding-bottom: 8px;
  }
  .el-select-group__wrap:not(:last-of-type)::after {
    content: "";
    position: absolute;
    display: block;
    left: 6px;
    right: 6px;
    bottom: 3px;
    height: 1px;
    background: #e4e7ed;
  }
}
.el-tooltip__popper.is-light {
  border: 1px solid #d3d4d8 !important;
}
.el-tooltip__popper.is-light[x-placement^="left"] .popper__arrow {
  border-left-color: #999999;
}

.el-popover {
  background: transparent !important;
  border: none !important;
  padding: 0px !important;
  min-width: 30px !important;
  .user-info {
    background: #fff;
    position: relative;
    width: 170px;
    height: 260px;
    padding-top: 62px;
    box-shadow: 0 2px 2px 2px rgba(0, 0, 0, 0.1);
    border-radius: 5px 5px 5px 5px;
    .user-role {
      height: 50px;
      padding: 5px;
      overflow: hidden;
      .el-tag {
        margin-right: 5px;
      }
    }
    .dropdown-divider {
      display: block;
      height: 0;
      margin: 4px 0;
      border-top: 1px solid #e1e4e8;
    }
    ul {
      display: inline-block;
      text-align: left;
      position: relative;
      margin: 0;
      padding-left: 0;
      width: 100%;
      .dropdown-item {
        height: 26px;
        line-height: 26px;
        font-size: 14px;
        color: #303133;
        padding: 0 20px;
        list-style: none;
        cursor: pointer;
        position: relative;
        transition: border-color 0.3s, background-color 0.3s, color 0.3s;
        box-sizing: border-box;
        white-space: nowrap;
        &:hover {
          outline: none;
          background-color: #ecf5ff;
        }
      }
    }
    .desc-layer {
      background-image: url(../assets/images/user-level-bg-0.png);
      width: 170px;
      height: 65px;
      top: 0px;
      border-radius: 5px 5px 0 0;
      background-size: cover;
      background-position: center center;
      position: absolute;
      z-index: 2;
      color: #fff;
      .user-name {
        padding-top: 10px;
        text-align: center;
        font-size: 14px;
      }
      .el-avatar {
        margin: 5px 5px 0px 5px;
      }
      .self-icon {
        padding-left: 5px;
        width: 60px;
        height: 30px;
      }
    }
    .desc-arrow {
      width: 0;
      height: 0;
      background-size: cover;
      background-position: center center;
      position: absolute;
      left: 90px;
      top: -10px;
      z-index: 1;
      border-left: 10px solid transparent;
      border-right: 10px solid transparent;
      border-bottom: 10px solid #2fc7fe;
      *background-image: url(../assets/images/user-level-arrow-0.png);
    }
  }
}
</style>
