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
        <span class="title">{{ $store.state.title }} </span>
        <span
          class="subtitle"
          v-if="$store.state.subtitle && $store.state.subtitle != ''"
          >({{ $store.state.subtitle }})</span
        >
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
          :disabled="$store.state.mode != 'single'"
          :loading="loading"
          @change="searchApi"
          :remote-method="selfFilter"
        >
          <el-option-group
            v-for="group in options"
            :key="group.group"
            :label="group.group"
          >
            <el-option
              v-for="(item, index) in group.options"
              :key="index"
              :label="item.name"
              :value="item.addr"
              class="addr-select-item"
            >
              <el-tooltip
                :content="item.addr"
                placement="left-start"
                effect="light"
              >
                <div slot="content">
                  <span>{{ group.group }}</span>
                  :
                  <span>{{ item.name }}</span>
                  <br />
                  <span>{{ item.addr }}</span>
                </div>
                <span class="item__title">{{ item.name }}</span>
              </el-tooltip>
              <span class="item__addr">{{ item.addr }}</span>
            </el-option>
          </el-option-group>
        </el-select>
      </div>
    </div>
    <layout ref="layout" class="container-body" @docs="docs" />
    <el-drawer :visible.sync="more" :with-header="false" size="360px">
      <version v-if="more"></version>
    </el-drawer>
  </div>
</template>

<script>
import Layout from "./layout.vue";
import Version from "../page/version.vue";
export default {
  data() {
    return {
      page: "layout.index.",
      index: 1,
      loading: false,
      searchTimeout: null,
      addr: "",
      more: false,

      options: [],
      proxy: "",
      single: false,
    };
  },
  components: {
    Layout,
    Version,
  },
  mounted() {},
  methods: {
    /**
     * 提交文档接口集合
     */
    docs(docs, addr) {
      this.options = docs || [];
      this.addr = addr;
    },
    $self(k) {
      return this.$t(this.page + k);
    },
    searchApi(params) {
      // 加载APIs
      if (params !== "") {
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
                  name: inp + this.index,
                  addr: params,
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
                    addr: params,
                  },
                ],
              });
            }
          } else {
            this.options.push({
              group: his,
              id: 1,
              options: [
                {
                  name: inp + this.index,
                  addr: params,
                },
              ],
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
  },
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
    padding-top: 50px;
    z-index: 1;
  }
  .header {
    width: 100%;
    position: absolute;
    z-index: 2;
    top: 0;
    left: 0;
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
      }
    }
  }
}
</style>
