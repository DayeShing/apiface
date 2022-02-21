/** * 空白数据页 * author:Daye Shing */
<template>
  <div class="postman-mian">
    <div class="base-toolbar">
      <div class="base-toolbar-url">
      <el-select v-model="method" placeholder="Select item">
        <el-option
          v-for="item in methodes"
          :key="item"
          :label="item"
          :value="item"
        ></el-option>
      </el-select>
      <el-input
        class="req-base-url"
        v-model="url"
        placeholder="Enter requset URL"
      ></el-input>
</div>
      <el-button type="primary" class="fr" :disabled="btnState" @click="submit"
        >Save</el-button
      >
      <el-button type="primary" class="fr">Send</el-button>
    </div>
    <div class="body-box">
      <request
        @params="paramsEvent"
        @bodies="bodyEvent"
        @cookies="cookieEvent"
        @headers="headerEvent"
        @auth="authEvent"
      ></request>
      <response ref="resp"></response>
    </div>
  </div>
</template>

<script>
import Request from "./request";
import Response from "./response";
import $cookie from "js-cookie";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "postman",
  components: {
    Request,
    Response,
  },
  data() {
    return {
      page: "dialog.conf.",
      methodes: [
        "GET",
        "POST",
        "PUT",
        "DELETE",
        "OPTIONS",
        "PATCH",
        "HEAD",
        "TRACE",
      ],
      method: "GET",
      url: "",
      btnState: false,
      headers: [],
      cookies: [],
      bodies: null,
      auth: null,
    };
  },
  methods: {
    submit() {
      if (this.cookies && this.cookies.length > 0) {
        for (var i = 0; i < this.cookies.length; i++) {
          $cookie.set(this.cookies[i].key, this.cookies[i].value);
        }
      }
      var h = {};
      if (this.headers && this.headers.length > 0) {
        for (var i = 0; i < this.headers.length; i++) {
          h[this.headers[i].key] = this.headers[i].value;
        }
      }
      var data = {};
      if (this.bodies) {
        if (this.bodies.type == 1 || this.bodies.type == 2) {
          h["Content-Type"] = "application/x-www-form-urlencoded";
          var tmp = {};
          if (this.bodies.data && this.bodies.data.length > 0) {
            for (var i = 0; i < this.bodies.data.length; i++) {
              tmp[this.bodies.data[i].key] = this.bodies.data[i].value;
            }
          }
          data = Qs.stringify(tmp, { arrayFormat: "repeat" });
        }
        if (this.bodies.type == 3) {
          h["Content-Type"] = "text/plain";
          if (this.bodies.data.type == "JSON") {
            h["Content-Type"] = "application/json";
          }
          if (this.bodies.data.type == "HTML") {
            h["Content-Type"] = "text/html";
          }
          if (this.bodies.data.type == "XML") {
            h["Content-Type"] = "application/xml";
          }
          if (this.bodies.data.type == "JScript") {
            h["Content-Type"] = "application/javascript";
          }
          data = this.bodies.data.content;
        }
      }
      var _url = this.url;
      if (this.$store.state.app) {
        _url = inter.proxy;
        h["Self-Agent-Address"] = this.url;
      } else if (this.$store.state.proxy) {
        _url = this.$store.state.proxy;
        h["Self-Agent-Address"] = this.url;
      }
      this.$req(_url, this.method.toLowerCase(), data, h, undefined, true)
        .then((res) => {
          this.$refs.resp.load(res);
        })
        .catch((err) => {});
    },
    bodyEvent(params) {
      this.bodies = params;
    },
    cookieEvent(params) {
      this.cookies = params;
    },
    headerEvent(params) {
      this.headers = params;
    },
    authEvent(params) {
      this.auth = params;
    },
    paramsEvent(params) {
      var index = this.url.indexOf("?");
      if (index == -1) {
        this.url += "?";
      } else {
        this.url = this.url.substring(0, index + 1);
      }
      for (var i = 0; i < params.length; i++) {
        if (i != 0) {
          this.url += "&";
        }
        this.url += params[i].key;
        this.url += "=";
        this.url += params[i].value;
      }
    },
  },
};
</script>

<style lang="less">
.postman-mian:not(postman-dialag) {
  padding: 6px;
}
.postman-mian {
  height: 100%;
  .body-box {
    width: 100%;
    height: calc(~"100% - 42px");
    overflow: hidden;
    border: 1px solid #e4e7ed;

    .el-tabs {
      border-bottom: none;
      box-shadow: none !important;
      height: 100%;
      .el-tabs__content {
        padding: 0px !important;
        // height: 405px !important;
        height: calc(~"100% - 40px") !important;
        .el-tab-pane {
          height: 100%;
        }
      }
      .el-tabs__header {
        margin: 0px !important;
        padding: 0 0 4px;
        border-bottom: 1px solid #e4e7ed;
        .el-tabs__nav-wrap {
          &::after {
            height: 0px !important;
          }
          margin-bottom: 0px !important;
          .el-tabs__item {
            padding: 0px 14px !important;
            height: 35px !important;
            line-height: 35px !important;
            font-size: 13px !important;
          }
        }
      }
    }
  }
  .base-toolbar {
    padding-top: 2px;
    padding-bottom: 5px;
    .base-toolbar-url{
      width: calc(~"100% - 132px");
      float: left;
          border: 1px solid #DCDCDC;
          background-color: #F0F0F0;
    }
    .req-base-url {
      width: calc(~"100% - 110px");
    }
    .el-button + .el-button {
      margin-right: 6px;
      margin-left: 0px;
    }
    .el-select {
      width: 110px;
      .el-input__inner {
        background-color: #dadcdf;
        padding-left: 20px;
        color: #666;
        font-weight: 700;
      }
    }
    
    .el-input {
      height: 33px;
    }
    .el-input__suffix {
      line-height: 33px;
      
    }
    .is-focus{
      .el-input__inner {
      background-color: #FAFAFA;
      }
    }
    .el-input__inner {
          background-color: transparent !important;
      height: 33px;
      line-height: 33px;
      border-radius: 0px !important;
      border: none !important;
    }
      
  }
}
</style>
