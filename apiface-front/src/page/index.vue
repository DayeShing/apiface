/** * API内容 * author:Daye Shing */
<template>
  <div class="view-main-data">
    <!-- 标题工具 -->
    <div class="base-info-warp">
      <div class="base-info">
        <span style="line-height: 20px;">{{ $self("tips") }}</span>
        <el-select v-model="local" class="lang fr" @change="changeLang">
          <el-option label="English" value="en" />
          <el-option label="简体中文" value="zh_cn" />
          <el-option label="繁體中文" value="zh_hk" />
          <!-- <el-option label="日本語" value="ja" /> -->
        </el-select>
        <el-switch
          v-model="mode"
          :active-text="$self('active')"
          active-color="#13ce66"
          inactive-color="#409eff"
          :inactive-text="$self('inactive')"
          class="fr"
        ></el-switch>
        <el-button class="fr" type="text" @click="download"
          ><i class="el-icon-share"></i
        ></el-button>
      </div>
    </div>
    <!-- 操作模式 -->
    <div v-if="mode" class="view-main-data-info">
      <!-- 基本信息 -->
      <div class="article-header-box" v-if="data.type">
        <div class="article-header">
          <div class="article-title-box">
            <span
              class="title-article"
              :class="{
                'title-deprecated': data.type[data.defaultType].deprecated,
              }"
              >{{ data.type[data.defaultType].summary }}</span
            >
            <br />
            <span class="subtitle-article"
              >{{ data.parent }}({{ data.parentDesc }})</span
            >
            <br />
          </div>
          <div class="author-info" v-if="data.type[data.defaultType].author">
            <el-avatar :size="45" src="../assets/icons/api.png">
              <img src="../assets/icons/api.png" />
            </el-avatar>
            <span class="author-info-title">
              {{ data.type[data.defaultType].author.name }}
              <el-button round size="mini">{{ $self("focus") }}</el-button>
            </span>
            <br />
            <span>
              <i class="fa fa-h-square" aria-hidden="true"></i>
              <span
                class="author-info-item"
                v-if="data.type[data.defaultType].author.contact"
                >{{ $self("contact") }} :
                {{ data.type[data.defaultType].author.contact }}</span
              >
              <span
                class="author-info-item"
                v-if="data.type[data.defaultType].version"
                >{{ $self("version") }} :
                {{ data.type[data.defaultType].version }}</span
              >
              <span
                class="author-info-item"
                v-if="data.type[data.defaultType].date"
                >{{ $self("date") }} :
                {{ data.type[data.defaultType].date }}</span
              >
            </span>
          </div>
          <div class="article-info-box">
            <div class="article-bar-top">
              <i class="fa fa-info-circle article-type-icon"></i>
              <p>{{ data.type[data.defaultType].description }}</p>
            </div>
            <!-- 地址栏 -->
            <div class="blog-addr-box blog-tags-box">
              <div>
                <span class="label">{{ $self("addr") }}</span>
                <span class="path tag-link"
                  >{{ data.host }}{{ data.basePath }}{{ data.path }}</span
                >
                <span class="path tag-link"
                  >{{ data.basePath }}{{ data.path }}</span
                >
                <span class="path tag-link">{{ data.path }}</span>
              </div>
            </div>
            <!-- 标签栏 -->
            <div class="blog-tags-box">
              <div class="tags-box artic-tag-box">
                <span class="label">{{ $self("tags") }}</span>
                <span
                  class="tag-link"
                  v-for="(item, index) in data.type[data.defaultType].tags"
                  :key="index"
                  >{{ item }}</span
                >
              </div>
              <div class="tags-box artic-tag-box">
                <span class="label">{{ $self("methods") }}</span>
                <span
                  class="tag-link"
                  v-for="(val, item, index) in data.type"
                  :key="index"
                  :class="{ 'is-active': item == data.defaultType }"
                  @click="data.defaultType = item"
                  >{{ item }}</span
                >
              </div>
              <div class="tags-box artic-tag-box">
                <span class="label">{{ $self("respm") }}</span>
                <span
                  class="tag-link"
                  v-for="(item, index) in data.type[data.defaultType].produces"
                  :key="index"
                  >{{ item }}</span
                >
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 请求参数 -->
      <request
        :data="parameter"
        :doc="status"
        ref="request"
        @submit="submit"
      ></request>
      <!-- 测试用例 -->
      <demo :data="responseInfo"></demo>
      <!-- 响应文档 -->
      <response :data="response"></response>
    </div>

    <!-- 文档模式 -->
    <doc
      v-else
      :data="data"
      :path="data.path"
      :status="status"
      ref="doc"
      @download="downloadBlob"
    ></doc>

    <!-- 版权信息 -->
    <div class="tip-foot">
      <el-link
        href="http://self.daysh.top"
        type="info"
        :underline="false"
        target="_blank"
        >{{ $t("page.v.self") }}</el-link
      >
    </div>
  </div>
</template>

<script>
import Doc from "./doc.vue";
import Response from "page/response.vue";
import Demo from "page/case.vue";
import Request from "page/request.vue";
import LangStorage from "script/lang/lang";
import $cookie from "js-cookie";
import FileSaver from "file-saver";
export default {
  data() {
    return {
      page: "page.index.",
      local: "",
      status: {
        path: "",
      },
      data: {
        path: "",
        type: undefined,
        host: "",
        basePath: "",
        parent: "",
        parentDesc: "",
        defaultType: "",
      },
      cookie: {},
      // 模式 只读，操作
      mode: true,
      // 响应
      responseInfo: {
        requestTime: "",
        responseTime: "",
        diffTime: 0,
        result: {},
        show: false,
        code: 200,
        requestHeader: {},
        responseHeader: {},
      },
    };
  },
  components: {
    Response,
    Request,
    Doc,
    Demo,
  },
  computed: {
    parameter() {
      if (this.data.type) {
        var ret = this.parameters(
          this.deepClone(this.data.type[this.data.defaultType].parameters),
          this.data.path
        );
        this.status = ret.status;
        console.log(ret.parameters, "parameters");
        return ret.parameters;
      }
      return [];
    },
    response() {
      if (this.data.type) {
        var responses = this.responses(
          this.deepClone(this.data.type[this.data.defaultType].responses)
        );
        console.log(responses, "responses");
        return responses;
      }
      return {};
    },
  },
  mounted() {
    this.local = this.$i18n._vm.locale;
    setTimeout(() => {
      this.resize();
    }, 2000);
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    download() {
      if (this.mode) {
        var data = {
          status: this.status,
          data: this.data,
          responseInfo: this.responseInfo,
        };
        var blob = new Blob([JSON.stringify(data)], {
          type: "text/plain;charset=utf-8",
        });
        this.downloadBlob(blob, "api.json");
        return;
      }
      this.$refs.doc.download();
    },
    downloadBlob(blob, name) {
      FileSaver.saveAs(blob, name);
    },
    changeLang(lang) {
      LangStorage.setLang(lang);
      window.location.reload();
    },
    submit(data, meta) {
      var p = this.data.path;
      for (var uri in meta.path) {
        var tmp = "{" + uri + "}";
        var u = p.replace(tmp, meta.path[uri]);
        p = u;
      }
      var cn = [];
      for (var c in meta.cookie) {
        cn.push(c);
        $cookie.set(c, meta.cookie[c]);
      }
      // this.cookie[p].data = cn;
      var heads = [
        {
          name: "Content-Type",
          content:
            this.status.$in == "body"
              ? "application/json"
              : "application/x-www-form-urlencoded",
        },
        {
          name: "Pragma",
          content: "no-cache",
        },
        {
          name: "Cache-Control",
          content: "no-cache",
        },
      ];
      for (var h in meta.head) {
        heads.push({ name: h, content: meta.head[h] });
      }
      var metadata = {
        url: this.data.host + this.data.basePath + p,
        method: this.data.defaultType,
        heads: heads,
        ret: true,
      };
      this.$emit("submit", metadata, data);
    },
    resize() {
      this.$nextTick(() => {
        this.$refs.request.resize();
      });
    },
    /**
     * 获取响应数据
     */
    fetch(data) {
      // $cookie.set(c, meta.cookie[c]);
      //       }这里 删除cookie,但是要获取唯一请求
      //       this.cookie[p].data = cn;
      this.responseInfo = data;
    },
    /**
     * 页面接口数据
     */
    load(data) {
      console.log(data, "api info");
      this.responseInfo.show = false;
      this.data = data;
    },
  },
};
</script>

<style lang="less">
// .el-select-dropdown {
//   min-width: 100px !important;
// }
.view-main-data {
  height: 100%;
  min-width: 940px;
  .base-info-warp {
    padding: 10px;
    .base-info {
      padding: 8px 16px;
      background-color: #ecf8ff;
      border-radius: 4px;
      border-left: 5px solid #50bfff;
      .lang {
        width: 100px;
        position: relative;
        top: -3px;
        right: -8px;
      }
      .el-switch__label * {
        font-size: 12px !important;
      }
      .el-switch__label--left.is-active {
        color: #409eff !important;
      }
      .el-switch__label--right.is-active {
        color: #13ce66 !important;
      }
      .el-button--small,
      .el-button--small.is-round {
        line-height: 20px;
        padding: 0px !important;
        margin-right: 10px;
        i {
          font-size: 20px;
          font-weight: 600 !important;
        }
      }
    }
  }
  .view-main-data-info {
    min-height: calc(~"100% - 116px");
    .article-header-box {
      padding: 0px 10px 10px;
      .article-header {
        div.author-info {
          height: 50px;
          padding-left: 58px;
          position: relative;
          i {
            color: #f1917d;
          }
          .el-button--mini,
          .el-button--mini.is-round {
            margin-left: 15px;
            padding: 4px 15px !important;
          }
          .author-info-title {
            line-height: 24px;
            font-size: 14px;
            font-weight: 500;
            color: #f1917d;
          }
          .author-info-item {
            line-height: 22px;
            padding-right: 15px;
          }

          .el-avatar {
            position: absolute;
            left: 4px;
            top: 2px;
          }
        }
        div.article-title-box {
          margin-bottom: 8px;
          font-weight: 600;
          .title-article {
            font-size: 22px;
            color: #555;
          }
          .title-deprecated {
            text-decoration: line-through;
          }
          .subtitle-article {
            font-size: 14px;
            color: #999;
          }
        }
        div.article-info-box {
          position: relative;
          background: #f7f7fc;
          border-radius: 5px;

          div.blog-tags-box {
            padding-left: 35px;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            font-size: 13px;
            span.label {
              display: inline-block;
              color: #999aaa;
            }
            .tag-link {
              -webkit-box-sizing: border-box;
              box-sizing: border-box;
              margin-right: 3px;
              padding: 3px 10px;
              font-size: 12px;
              background-color: #fff;
              color: #5094d5;
              border: 1px solid #eaeaef;
              line-height: 17px;
              border-radius: 4px;
              display: inline-block;
              &.is-active {
                border: 1px solid #5094d5 !important;
                background-color: #5094d5 !important;
                color: #fff !important;
              }
              &:last-child {
                margin-right: 0px;
              }
            }
            .tags-box {
              line-height: 24px;
              margin: 5px 20px 5px 0px;
              &:last-child {
                margin: 5px 0px 5px 0px;
              }
            }
          }
          div.article-bar-top {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            .article-type-icon {
              padding: 0px 9px;
              font-size: 20px;
              color: #3184da;
              line-height: 25px;
            }
            p {
              color: #5893c2;
              line-height: 25px;
              font-size: 13px;
              font-weight: 600;
              margin: 0px;
            }
          }
        }
      }
    }

    .fieldset-box {
      margin-left: 8px;
      margin-right: 8px;
      margin-bottom: 8px;
      &:last-child {
        margin-bottom: 0px;
      }
    }
  }

  .tip-foot {
    padding-bottom: 20px;
    text-align: center;
    width: 100%;
    padding-top: 20px;
  }
}
</style>
