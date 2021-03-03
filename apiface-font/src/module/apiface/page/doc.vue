/**
 * md页
 * author:Daye Shing
 */
<template>
  <mavon-editor
    class="view-main-data-info"
    :class="{ 'not-body': notBody }"
    :value="text"
    defaultOpen="preview"
    :subfield="false"
    :ishljs="true"
    :scrollStyle="false"
    :boxShadow="false"
    :toolbarsFlag="true"
    :editable="false"
    previewBackground="#fff"
  ></mavon-editor>
</template>

<script>
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
export default {
  data() {
    return {
      page: "page.index.",
      text: "",
      notBody: false,
      // md 工具栏
      toolbars: {
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        /* 1.4.2 */
        navigation: true // 导航目录
      }
    };
  },
  components: {
    "mavon-editor": mavonEditor
  },
  props: {
    data: {
      type: Object,
      default: function() {
        return {};
      }
    },
    path: {
      type: String,
      default: function() {
        return "";
      }
    }
  },
  watch: {
    path(val, oldVal) {
      this.read();
    }
  },
  mounted() {
    this.read();
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    read() {
      var arr = [];
      this.baseInfo(arr, this.data);
      this.reqInfo(arr, this.data);
      this.resInfo(arr, this.data);
      this.text = arr.join("\r\n");
    },
    /**
     * 基本信息
     */
    baseInfo(arr, data) {
      var label = data.label;
      if (data.content.deprecated) {
        label = "~~" + data.label + "~~";
      }
      arr.push("## " + label);
      arr.push("##### " + data.parent + "(" + data.parentDesc + ")");
      arr.push("> " + data.content.description);
      var addr = data.host + data.basePath + data.path;
      arr.push(
        "> ###### " + this.$self("addr") + " [" + addr + "](" + addr + ")"
      );
      var tags = this.$self("tags");
      for (var i = 0; i < data.content.tags.length; i++) {
        tags += " [";
        tags += data.content.tags[i] + "](#) ";
      }
      var rt = this.$self("methods");
      for (var i = 0; i < data.type.length; i++) {
        rt += " [";
        rt += data.type[i] + "](#) ";
      }
      var rp = this.$self("respm");
      for (var i = 0; i < data.content.produces.length; i++) {
        rp += " [";
        rp += data.content.produces[i] + "](#) ";
      }
      arr.push(
        "> ###### " +
          tags +
          "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
          rt +
          "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
          rp
      );
      arr.push("-------");
    },
    /**
     * 请求
     */
    reqInfo(arr, data) {
      arr.push("## " + this.$self("label"));
      arr.push(
        this.$self("pt") +
          (data.content.status.isBody ? this.$self("json") : this.$self("form"))
      );
      if (data.content.parameters && data.content.parameters.length > 0) {
        if (data.content.status.isBody) {
          arr.push("```bash");
          arr.push(
            JSON.stringify(
              this.toJSONObject(data.content.parameters[0].schema.data),
              null,
              4
            )
          );
          arr.push("```");
          arr.push(this.title(false));
          arr.push(":---:|:--:|:--:|:--:|:--:");
          this.reqFor(
            data.content.parameters[0].schema.data.child,
            arr,
            undefined
          );
        } else {
          arr.push(this.title(true));
          arr.push(":---:|:--:|:--:|:--:|:--:");
          this.reqFor(data.content.parameters, arr, undefined);
        }
      } else {
        arr.push(this.$self("noparam"));
      }
    },
    title(flag) {
      if (flag) {
        return (
          this.$self("param") +
          "|" +
          this.$self("desc") +
          "|" +
          this.$self("type") +
          "|" +
          this.$self("required") +
          "|" +
          this.$self("format")
        );
      }
      return (
        this.$self("param") +
        "|" +
        this.$self("desc") +
        "|" +
        this.$self("type") +
        "|" +
        this.$self("required") +
        "|" +
        this.$self("serial")
      );
    },
    /**
     * 循环遍历
     */
    reqFor(data, arr, args) {
      for (var i = 0; i < data.length; i++) {
        var item = data[i];
        var d = item.description ? item.description : "-/-";
        var r = item.required ? this.$self("req") : this.$self("noreq");
        var f = item.format ? item.format : "-/-";
        arr.push(
          (args != undefined ? "(" + args + ") -/- " + item.name : item.name) +
            "|" +
            (args != undefined ? "(" + args + ") -/- " + d : d) +
            "|" +
            item.type +
            "|" +
            r +
            "|" +
            f
        );
        if (item.child) {
          this.reqFor(item.child, arr, item.name);
        }
      }
    },
    /**
     * 响应项
     */
    resItemInfo(arr, name, desc, type, data) {
      arr.push("-------");
      arr.push("## " + this.$self("label2"));
      arr.push(this.$self("respcode") + name + "(" + desc + ")");
      if (type == "1") {
        arr.push("```bash");
        arr.push(data.demo);
        arr.push("```");
        arr.push(this.title(false));
        arr.push(":---:|:--:|:--:|:--:|:--:");
        this.reqFor(data.data.child, arr, undefined);
      } else if (type == "2") {
        arr.push(
          "* " +
            this.$self("base") +
            ":(" +
            data.type +
            "-/-" +
            data.format +
            ")"
        );
      } else if (type == "3") {
        arr.push(
          "* " +
            this.$self("nodata") +
            ": { }/None/Null/null/undefined/Void/[ ]"
        );
      } else {
        arr.push("* " + this.$self("nometa"));
      }
    },
    /**
     * 响应
     */
    resInfo(arr, data) {
      for (var key in data.content.responses) {
        var val = data.content.responses[key];
        var type = "";
        if (val.schema && val.schema.$ref) {
          type = "1";
        } else if (val.schema && val.schema.type == "object") {
          type = "3";
        } else if (val.schema && val.schema.type) {
          type = "2";
        }
        this.resItemInfo(arr, key, val.description, type, val.schema);
      }
    }
  }
};
</script>

<style lang="less">
.markdown-body {
  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    margin-top: 0px !important;
    margin-bottom: 0px !important;
    font-weight: 600 !important;
  }
  h2 {
    padding-bottom: 0px !important;
    line-height: normal !important;
    font-size: 22px !important;
    color: #555;
    border-bottom: none !important;
  }
  h5 {
    font-size: 14px !important;
    color: #999;
    margin-bottom: 8px !important;
    line-height: normal !important;
  }
  h6 {
    line-height: 25px !important;
  }
  blockquote {
    padding: 10px !important;
    color: #6a737d;
    border-left: 0.25em solid #50bfff !important;
    background: #f7f7fc;
    border-radius: 5px;
    margin-left: 6px !important;
    margin-bottom: 0px !important;
    p {
      font-size: 14px;
      color: #5893c2;
      font-weight: 600;
      margin-bottom: 5px !important;
    }
  }
  ul {
    padding-left: 0px !important;
    li {
      width: 100%;
      display: block;
      line-height: 100px;
      text-align: center;
      color: #777;
      font-size: 12px;
      background-color: rgba(27, 31, 35, 0.05);
      border-radius: 3px;
    }
  }
  hr {
    height: 1px !important;
    margin: 13px 0 !important;
  }
  table {
    display: block;
    margin-bottom: 0px !important;
    thead {
      color: #909399;
      font-weight: 500;
      width: 100%;
      display: inline-table;
    }
    tbody {
      width: 100%;
      display: inline-table;
    }
    tr {
      border-color: #ebf0f7 !important;
      &:hover td {
        background-color: #f3fdf9 !important;
      }
    }
    td {
      width: 20%;
      line-height: 31px !important;
      padding: 0px 6px !important;
      color: #666666 !important;
      font-size: 12px !important;
    }
    th {
      width: 20%;
      color: #4d4d4d !important;
      font-size: 12px !important;
      font-weight: bold !important;
      line-height: 31px !important;
      padding: 0px 6px !important;
      background-color: #e5e9ee !important;
    }
  }
  pre {
    margin-bottom: 0px !important;
    padding: 10px !important;
  }
}

.view-main-data-info {
  &.v-note-wrapper {
    border: none !important;
    height: auto !important;
    z-index: 0 !important;
    .v-note-panel .v-note-show .v-show-content,
    .v-note-panel .v-note-show .v-show-content-html {
      padding: 0px 10px 0px 10px !important;
      overflow-y: visible !important;
    }
    .v-note-panel {
      overflow: visible !important;
      .v-note-show {
        overflow-y: visible !important;
      }
    }
  }
}
</style>
