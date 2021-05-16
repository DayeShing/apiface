<!--
  封装datagrid  -带分页组件,滚动加载的表格
  作者：Daye Shing
-->
<template>
  <!--  -->
  <el-table
    class="datagrid"
    :data="data"
    :row-key="rowkey"
    :default-expand-all="expand"
    @expand-change="expandChange"
    :tree-props="{ children: 'child', hasChildren: 'isLeft' }"
    border
    stripe
  >
    <slot></slot>
  </el-table>
</template>
<script>
export default {
  name: "datagrid", // 标签库封装的datagrid
  components: {},
  data() {
    return {};
  },
  props: {
    expand: {
      type: Boolean,
      default: function() {
        return true;
      },
    },
    rowkey: {
      //从父组件传入的数据
      type: String,
      default: function() {
        return "id";
      },
    },
    data: {
      //从父组件传入的数据
      type: Array,
      default: function() {
        return [];
      },
    },
  },
  computed: {},
  created() {},
  mounted() {},
  methods: {
    expandChange(row, status) {
      this.$emit("expandRow", row, status);
    },
    // rowkey(row) {
    //   var key = row[this.rowkey];
    //   if (key) {
    //     return key;
    //   }
    //   return this.rowkey;
    // }
  },
};
</script>
<style lang="less">
.datagrid {
  // border-width: 1px 1px 0 1px;
  // border-style: solid;
  // border-color: #dddddd;
  .el-table__body {
    tr.hover-row {
      td {
        background: #ebf0f7 !important;
      }
    }
  }
  &.el-table tr {
    border-color: #ebf0f7;
  }

  &.el-table th {
    height: 30px;
    padding: 3px 0;
    background-color: #e5e9ee;
  }
  //   .el-table th>.cell {
  //   font-size: 14px;
  // }
  &.el-table th > .cell {
    color: #4d4d4d;
    font-size: 12px;
    font-weight: bold;
    overflow: hidden;
    word-break: keep-all;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  &.el-table td {
    padding: 3px 0;
    height: 37px;
    background-color: #f2f4f6;
    color: #666666;
    font-size: 12px;
  }
  &.el-table td,
  &.el-table th.is-leaf {
    border-bottom: 1px solid #dddddd;
  }
  .el-table__row--striped td {
    background-color: #fdfdfd !important;
  }
  .el-table__row:hover td {
    background-color: #f3fdf9 !important;
  }
  &.el-table .ascending .sort-caret.ascending,
  &.el-table .descending .sort-caret.descending {
    color: #4b71a4;
  }
  &.el-table td,
  &.el-table th.is-leaf {
    border-bottom: 1px solid #dddddd;
  }
  .el-table--border td,
  .el-table--border th,
  .el-table__body-wrapper
    .el-table--border.is-scrolling-left
    ~ .el-table__fixed {
    border-right: 1px solid #dddddd;
  }
  .el-table--border td,
  .el-table--border th,
  .el-table__body-wrapper
    .el-table--border.is-scrolling-left
    ~ .el-table__fixed {
    border-right: 1px solid #dddddd;
  }

  .el-table-column--selection .cell {
    text-overflow: initial !important;
  }
  &.el-table .cell,
  &.el-table th div,
  &.el-table--border td:first-child .cell,
  &.el-table--border th:first-child .cell {
    padding-left: 6px;
  }

  &.el-table .cell,
  &.el-table th div {
    padding-right: 6px;
  }
}
</style>
