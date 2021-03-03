/**
 * 首页
 * author:Daye Shing
 */
<template>
  <div class="container-view">
    <div class="header">
      <div class="header-wrap fl">
        <img height="34" width="34" src="../icons/api.png" />
      </div>
      <div class="header-wrap fl">
        <span class="title">{{ title }}</span>
      </div>
      <div class="more-info">
        <i class="fa fa-ellipsis-v" @click="more = true"></i>
      </div>
      <div class="header-wrap api-select">
        <el-select
          v-model="addr"
          filterable
          :placeholder="$self('addr')"
          clearable
          remote
          reserve-keyword
          :disabled="!single"
          :loading="loading"
          @change="searchApi"
          :remote-method="selfFilter"
        >
          <el-option-group v-for="group in options" :key="group.group" :label="group.group">
            <el-option
              v-for="(item, index) in group.options"
              :key="index"
              :label="item.name"
              :value="item.addr"
              class="addr-select-item"
            >
              <span class="item__title">{{ item.name }}</span>
              <span class="item__addr">{{ item.addr }}</span>
            </el-option>
          </el-option-group>
        </el-select>
      </div>
    </div>
    <layout :mode="false" ref="layout" class="container-body" />
    <el-drawer :visible.sync="more" :with-header="false" size="360px">
      <version v-if="more" @submit="submit"></version>
    </el-drawer>
  </div>
</template>

<script>
import Layout from "./layout.vue";
import Version from "../page/version.vue";
import $axios from "axios";
export default {
  data() {
    return {
      page: "layout.index.",
      title: "",
      index: 1,
      loading: false,
      searchTimeout: null,
      addr: "",
      options: [],
      proxy: "",
      single: false,
      more: false
    };
  },
  components: {
    layout: Layout,
    version: Version
  },
  mounted() {
    this.getConf();
  },
  methods: {
    getConf() {
      let $this = this;
      $axios.get("static/conf.json", { id: 1 }).then(response => {
        window.CONF = response.data;
        $this.init();
      });
    },
    $self(k) {
      return this.$t(this.page + k);
    },
    submit(meta, data) {
      this.$refs.layout.submit(meta, data);
    },
    searchApi(params) {
      if (params !== "") {
        this.$refs.layout.config(
          this.single,
          this.addr,
          this.options,
          this.proxy
        );
      }
    },
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
                  name: inp + this.index,
                  addr: params
                });
                this.options.splice(0, 1, data);
              }
            } else {
              this.options.unshift({
                group: his,
                id: 1,
                options: [
                  {
                    name: inp + this.index,
                    addr: params
                  }
                ]
              });
            }
          } else {
            this.options.push({
              group: his,
              id: 1,
              options: [
                {
                  name: inp + this.index,
                  addr: params
                }
              ]
            });
          }
          this.index += 1;
        }, 1000);
      } else {
        this.$message({ type: "warning", message: this.$self("tips") });
      }
    },
    isUrl(url) {
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
    init() {
      // 非直接访问
      if (window.CONF) {
        this.title = window.CONF.title;
        this.single = window.CONF.mode == "single";
        this.proxy = window.CONF.proxy;
        if (this.single && window.CONF.docs && window.CONF.docs.length > 0) {
          this.addr = window.CONF.docs[0].options[0].addr;
          this.options = window.CONF.docs;
        } else {
          var addr = window.location.href;
          var index = addr.indexOf("/apiface.html#/");
          if (index != -1) {
            addr = addr.substring(0, index);
          }
          this.addr = addr + window.CONF.api;
          this.options = [
            {
              group: this.$self("vm"),
              options: [
                {
                  name: this.$self("addr1"),
                  addr: this.addr
                }
              ]
            }
          ];
        }
      }
      if (!this.title) {
        this.title = this.$self("title");
      }
      this.$refs.layout.config(
        this.single,
        this.addr,
        this.options,
        this.proxy
      );
    }
  }
};
</script>

<style lang="less">
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
    width: 260px;
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
.container-view {
  height: 100%;
  .container-body {
    height: calc(~"100% - 50px");
  }
  .header {
    background-color: #008ead;
    height: 50px;
    padding: 5px;
    .fl {
      float: left;
    }
    .more-info {
      float: right;
      line-height: 40px;
      color: #fff;
      font-size: 16px;
      padding: 0px 10px 0px 10px;
      cursor: pointer;
      i:hover {
        color: #a6deef;
      }
    }
    .api-select {
      float: right;
      padding: 5px;

      .el-select {
        width: 380px;
        .el-input--small,
        .el-input--small .el-input__inner {
          height: 30px !important;
        }
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
      .title {
        line-height: 40px;
        color: #fff;
        font-size: 22px;
        font-weight: 700;
      }
    }
  }
}
</style>
