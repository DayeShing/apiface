/** * 测试用例组件 * author:Daye Shing */
<template>
  <selection class="blue" :label="$self('label')" v-show="data && data.show">
    <el-row class="time-info">
      <el-col :span="2">{{ $self("reqTime") }}</el-col>
      <el-col :span="6" class="el-col-params">{{ data.requestTime }}</el-col>
      <el-col :span="2">{{ $self("resTime") }}</el-col>
      <el-col :span="6" class="el-col-params">{{ data.responseTime }}</el-col>
      <el-col :span="2">{{ $self("diff") }}</el-col>
      <el-col :span="2" class="el-col-params">{{ data.diffTime }}ms</el-col>
      <el-col :span="2">{{ $self("code") }}</el-col>
      <el-col :span="2" class="el-col-params">{{ data.code }}</el-col>
    </el-row>
    <div class="time-info">
      <div class="time-info-title">{{ $self("reqHead") }}</div>
      <json-viewer
        :value="data.requestHeader"
        :expand-depth="5"
        copyable
        boxed
        sort
      ></json-viewer>
    </div>
    <div class="time-info">
      <div class="time-info-title">{{ $self("respHead") }}</div>
      <json-viewer
        :value="data.responseHeader"
        :expand-depth="5"
        copyable
        boxed
        sort
      ></json-viewer>
    </div>
    <div class="time-info">
      <div class="time-info-title">{{ $self("body") }}</div>
      <json-viewer
        :value="data.result"
        :expand-depth="5"
        copyable
        boxed
        sort
      ></json-viewer>
    </div>
  </selection>
</template>

<script>
import Selection from "components/section";
import JsonViewer from "vue-json-viewer";
export default {
  data() {
    return {
      page: "page.case.",
    };
  },
  components: {
    Selection,
    JsonViewer,
  },
  props: {
    data: {
      type: Object,
      default() {
        return {
          requestTime: "2020-01-01 00.00.00.000",
          responseTime: "2020-01-01 00.00.00.000",
          diffTime: 0,
          result: {},
          show: true,
          code: 200,
          requestHeader: {},
          responseHeader: {},
        };
      },
    },
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
  },
};
</script>

<style lang="less">
.time-info {
  padding: 5px;
  border-left: 1px solid #cfd9e5;
  border-right: 1px solid #cfd9e5;
  border-top: 1px solid #cfd9e5;
  .time-info-title {
    padding: 8px 16px;
    background-color: #ecf8ff;
    border-radius: 4px;
    border-left: 5px solid #50bfff;
    margin-bottom: 5px;
    color: #87afcc;
    font-size: 14px;
    font-weight: 700;
  }
  &:first-child {
    border-radius: 5px 5px 0px 0px;
  }
  &:last-child {
    border-radius: 0px 0px 5px 5px;
    border-bottom: 1px solid #eaeaef;
  }
  .el-col {
    line-height: 25px;
    height: 25px;
    font-size: 14px;
    font-weight: 600;
    color: #5893c2;
  }
  .el-col:not(.el-col-params) {
    color: #666;
    text-align: right;
  }
}
</style>
