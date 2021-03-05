/**
 * 响应参数
 * author:Daye Shing
 */
<template>
  <selection class="blue" :label="$self('label2')">
    <div
      class="responses-info-doc"
      v-for="(val, key, index) in data"
      :key="index"
      v-show="more || key == '200'"
    >
      <span class="title" :class="'c' + key">{{ key }}({{ val.description }})</span>
      <div v-if="val.schema && val.schema.$ref" class="schema-data bg">
        <div class="code-view">
          <pre>{{ val.schema.demo }}</pre>
        </div>
        <datagrid :data="val.schema.data.child">
          <el-table-column prop="name" :label="$self('param')" width="150" align="center"></el-table-column>
          <el-table-column prop="description" :label="$self('desc')" align="center"></el-table-column>
          <el-table-column prop="type" :label="$self('type')" width="80" align="center"></el-table-column>
          <el-table-column prop="format" :label="$self('serial')" width="80" align="center"></el-table-column>
        </datagrid>
      </div>
      <div
        v-else-if="val.schema && val.schema.type == 'object'"
        class="no-data"
      >{{ $self("nodata") }} : { }/None/Null/null/undefined/Void/[ ]</div>

      <div v-else-if="val.schema && val.schema.type" class="simple-data bg">
        <span>{{ $self("base") }} :</span>
        <span>({{ val.schema.type }})</span>
        <span v-if="val.schema.format">-/- {{ val.schema.format }})</span>
      </div>
      <div v-else-if="val.error" class="simple-data bg">{{val.error}}</div>
      <div v-else class="no-data">{{ $self("nometa") }}</div>
    </div>

    <div class="responses-info-doc-more">
      <span @click="more = !more" class="doc-more-font">
        <span v-if="more">{{ $self("hidden") }}</span>
        <span v-else>{{ $self("show") }}</span>
        <i
          class="fa"
          :class="{
            'fa-chevron-down': !more,
            'fa-chevron-up': more,
          }"
        ></i>
      </span>
    </div>
  </selection>
</template>

<script>
import datagrid from "../compontents/datagrid";
import selection from "../compontents/section";
export default {
  data() {
    return {
      page: "page.index.",
      more: false
    };
  },
  components: {
    selection,
    datagrid
  },
  props: {
    data: {
      type: Object
    }
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    }
  }
};
</script>

<style lang="less">
.responses-info-doc {
  border-left: 1px solid #cfd9e5;
  border-right: 1px solid #cfd9e5;
  border-top: 1px solid #cfd9e5;
  &:first-child {
    border-radius: 5px 5px 0px 0px;
  }
  .bg {
    background: #f7f7fc;
  }
  span.title {
    line-height: 28px;
    font-size: 14px;
    font-weight: 700;
    border-bottom: 1px solid #eaeaef;
    padding: 5px 0px 5px 24px;
    display: flex;
    &.c200 {
      color: #67c23a;
    }
    &.c204 {
      color: #909399;
    }
    &.c401 {
      color: #f56c6c;
    }
    &.c50x {
      color: #f56c6c;
    }
    &.c403 {
      color: #e6a23c;
    }
  }
  .schema-data {
    padding: 10px;
    background: #f7f7fc;
    height: 390px;
    .code-view {
      overflow: auto;
      height: 100%;
      width: 25%;
      float: left;
      height: 100%;
      pre {
        margin-top: 0;
        margin-bottom: 0px;
        font-family: SFMono-Regular, Consolas, "Liberation Mono", Menlo, Courier,
          monospace;
        color: #d255c5;
      }
    }
    .datagrid {
      float: right;
      width: 74%;
      .el-table__body-wrapper {
        height: 333px;
        overflow-y: auto;
        overflow-x: hidden;
      }
    }
  }
  .simple-data {
    font-size: 13px;
    font-weight: 600;
    height: 66px;
    line-height: 66px;
    padding-left: 70px;
    color: #5893c2;
  }
  .no-data {
    color: #999;
    line-height: 66px;
    text-align: center;
  }
}
.responses-info-doc-more {
  border-radius: 0px 0px 5px 5px;
  border: 1px solid #cfd9e5;
  padding: 6px;
  text-align: center;
  color: #555;
  .doc-more-font:hover {
    color: #5893c2;
  }
  .doc-more-font {
    cursor: pointer;
  }
  i {
    padding-left: 3px;
  }
}
</style>
