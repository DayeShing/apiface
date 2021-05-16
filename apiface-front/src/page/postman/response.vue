/** * 空白数据页 * author:Daye Shing */
<template>
  <div
    class="postman-response"
    :class="{
      'postman-response-max': mode,
    }"
  >
    <el-tabs>
      <el-tab-pane label="Body">
        <json-viewer
          :value="data.result"
          :expand-depth="5"
          copyable
          boxed
          sort
        ></json-viewer>
      </el-tab-pane>
      <el-tab-pane label="Headers">
        <json-viewer
          :value="data.responseHeader"
          :expand-depth="5"
          copyable
          boxed
          sort
        ></json-viewer>
      </el-tab-pane>
    </el-tabs>
    <div class="btn-tool-opt">
      <span>
        Status:
      </span>
      <span>
        {{ data.code }}
      </span>

      <span>
        Time:
      </span>
      <span>
        {{ data.diffTime }}
      </span>
      <span @click="change">
        Response
        <i
          class="fa"
          :class="{
            'fa-chevron-down': mode,
            'fa-chevron-up': !mode,
          }"
        ></i>
      </span>
    </div>
  </div>
</template>

<script>
import JsonViewer from "vue-json-viewer";
export default {
  name: "response",
  components: { JsonViewer },
  data() {
    return {
      mode: false,
      data: {
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
  methods: {
    load(data) {
      this.data = data;
      this.mode = data.show;
    },
    change() {
      this.mode = !this.mode;
    },
  },
};
</script>

<style lang="less">
.postman-response-max {
  bottom: calc(~"100% - 79px");
}
.postman-response {
  width: 100%;
  height: calc(~"100% - 40px");
  border-top: 1px solid #e4e7ed;
  position: relative;
  background-color: aliceblue;

  .el-tab-pane {
    overflow-y: auto;
  }
  .btn-tool-opt {
    position: absolute;
    line-height: 40px;
    right: 6px;
    top: 0px;
    span {
      color: #666;
      cursor: pointer;
      &:hover {
        color: #333333;
      }
    }
  }
}
</style>
