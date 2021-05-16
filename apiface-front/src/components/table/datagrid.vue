<!--
  封装datagrid  -带分页组件,滚动加载的表格
  作者：Daye Shing
-->
<template>
  <div class="datagrid">
    <!-- 表格头部 -->
    <div class="datagrid_header" ref="toolbar" v-if="config.title !== '' || $slots.toolbar">
      <div class="title" v-if="config.title !== ''">
        <span name="title">{{ config.title }}</span>
      </div>
      <div class="toolbar" v-if="$slots.toolbar">
        <slot name="toolbar"></slot>
        <div class="clear"></div>
      </div>
      <div class="clear"></div>
    </div>
    <!-- 表格内容 -->
    <div
      class="datagrid_content"
      :class="{ 'content-with-header': config.title !== '' || $slots.toolbar }"
    >
      <el-table
        ref="datagrid"
        :current-row-key="config.currentRowKey"
        :row-key="config.rowKey"
        :sum-text="config.sumText"
        :show-summary="config.showSummary"
        :row-class-name="config.rowClass"
        :header-row-class-name="config.headerClass"
        :row-style="config.rowStyle"
        :show-header="config.showHeader"
        class="data-table"
        :class="config.customClass"
        :height="datagridHeight"
        @current-change="handleDatagirdCurrentChange"
        @row-dblclick="rowDblClick"
        @row-click="rowClick"
        @select="onSelect"
        @header-dragend="headerDragend"
        @cell-dblclick="cellDblclick"
        @selection-change="selectionChange"
        @select-all="onSelectAll"
        v-loading="loading && !config.pagination.autoRoll"
        v-loadmore="loadMore"
        element-loading-spinner="fa fa-spinner fa-pulse fa-3x"
        :data="currentData"
        :empty-text="config.emptyText"
        :border="config.border"
        :highlight-current-row="config.highlightCurrentRow"
        :stripe="config.stripe"
      >
        <!-- 第一行复选框 -->
        <el-table-column
          align="center"
          label-class-name="no-padding"
          v-if="config.selection.showSelection"
          type="selection"
          :fixed="wigther"
          :label="config.selection.selectionLabel"
          :width="config.selection.selectionWidth"
        ></el-table-column>
        <!-- 第二行序号 -->
        <el-table-column
          :fixed="wigther"
          align="center"
          class-name="no-padding"
          v-if="config.rowNumber.showRowNumbers"
          type="index"
          :label="config.rowNumber.rowNumberLabel"
          :width="config.rowNumber.rowNumberWidth"
        ></el-table-column>
        <el-table-column
          type="expand"
          class-name="no-padding"
          label="扩展"
          width="45"
          v-if="wigther && config.expand.show && config.columns.length > 0"
        >
          <template slot-scope="scope">
            <div style="padding: 10px 0px 10px 66px; overflow: hidden">
              <ul class="information-list">
                <li v-for="(item, index) in config.columns" :key="item.prop">
                  <label>
                    <span>{{ index + 1 }}</span>
                    <span>.</span>
                    <span>{{ item.label }}</span>
                  </label>
                  <p>
                    <span class="information-list-end">:</span>
                    {{
                    item.formatter
                    ? item.formatter(scope.row, item.prop)
                    : scope.row[item.prop]
                    }}
                  </p>
                </li>
              </ul>
            </div>
          </template>
        </el-table-column>
        <!-- 首先插入的位置 -->
        <slot name="first"></slot>
        <!-- 显示数据 -->
        <el-table-column
          v-for="item in config.columns"
          :key="item.prop"
          :class-name="item.className"
          :label-class-name="item.labelClassName"
          :width="item.width"
          :min-width="item.minWidth"
          :prop="item.prop"
          :label="item.label"
          :sortable="item.sortable"
          :header-align="item.headerAlign"
          :align="item.align == undefined ? 'center' : item.align"
          :formatter="item.formatter"
          :fixed="item.fixed"
        ></el-table-column>
        <!-- 最后插入的位置 -->
        <slot name="last"></slot>
        <slot></slot>
      </el-table>

      <!-- 分页与表格脚部信息 -->
      <div
        class="datagrid-pagination-box"
        v-if="config.pagination.showPagination || config.pagination.showMore"
      >
        <div class="push-left" v-if="config.pagination.showMore">
          <el-checkbox
            :indeterminate="indeterminate"
            v-model="checkAll"
            @change="toggleAllSelection"
          >{{ info.allSelect }}</el-checkbox>
          <span>{{ info.selected }}{{ selectionValue.length }}{{ info.item }}</span>
        </div>
        <div class="push-left" v-if="$slots.footbar">
          <slot name="footbar"></slot>
        </div>

        <div class="push-right" v-if="config.pagination.showPagination">
          <div class="push-right" style="margin-right: 8px">
            <el-select
              v-model="config.pagination.autoRoll"
              size="mini"
              style="width: 95px"
              @change="loadEvent"
            >
              <el-option :label="info.pageLoad" :value="false"></el-option>
              <el-option :label="info.rollLoad" :value="true"></el-option>
            </el-select>
          </div>
          <div class="push-right" v-if="!config.pagination.autoRoll">
            <el-pagination
              :pager-count="config.pagination.pageCount"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="page.currentPage"
              :page-sizes="config.pagination.pageSizeList"
              :page-size="page.pageSize"
              :layout="config.pagination.layout"
              :total="page.total"
            ></el-pagination>
          </div>
          <div
            class="push-right"
            style="
              line-height: 38px;
              color: #898989;
              font-size: 13px;
              padding-right: 15px;
            "
            v-if="config.pagination.autoRoll"
          >
            <span v-if="loading">
              <i class="el-icon-loading" aria-hidden="true"></i>
              {{ info.loading }}
            </span>
            <span v-if="!hasNext">{{ info.loaded }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import "components/scrollBar.css";
import qs from "qs";
import { EleResize } from "components/EleResize";
import scrollBar from "components/scrollbar";
export default {
  name: "datagrid",
  components: {
    scrollBar
  },
  data() {
    return {
      info: {
        allSelect: "全选",
        item: "项",
        selected: "已选择",
        loading: "正在为您加载数据...",
        loaded: "已为您加载全部数据",
        pageLoad: "分页加载",
        rollLoad: "滚动加载"
      },
      datagridHeight: 200,
      tempSelectValue: [],
      loading: false,
      page: {
        currentPage: 1, //当前页
        pageSize: 10, //当前页的size
        total: 0
      },
      selectionValue: [],
      currentRow: {},
      config: {
        //表单配置对象
        title: "",
        customClass: "",
        rowClass: "",
        headerClass: "",
        highlightCurrentRow: false,
        rowNumber: {
          showRowNumbers: false,
          rowNumberWidth: 70,
          rowNumberLabel: "$"
        },
        expand: {
          show: false,
          expandWidth: 80,
          expandLabel: "##"
        },
        //是否显示checkbox
        selection: {
          showSelection: true,
          selectionWidth: 30,
          selectionLabel: "#"
        },
        sumText: "列合计",
        isResize: true,
        rowKey: "",
        showHeader: true,
        creatScrollBar: false,
        border: false,
        expand: true,
        emptyText: "暂无数据",
        columns: [], //表格的列
        method: "get",
        queryParams: {}, //查询参数
        url: "", //查询
        data: [],
        currentRowKey: "",
        stripe: true, //斑马纹
        lazy: false, //创建时候是否立刻加载
        //分页对象
        pagination: {
          showBytotal: true,
          showPagination: true,
          autoRoll: true,
          layout: "sizes, prev, pager, next, jumper,total", //分页布局
          pageSizeList: [10, 20, 30, 50, 100],
          pageSize: 20,
          pageCount: 7,
          showMore: true
        }
      },
      ttScroll: "",
      checkAll: false,
      indeterminate: false,
      higher: false,
      wigther: false,
      hasNext: true,
      intervalTimer: null
    };
  },
  props: {
    datagrid: Object, //配置对象
    data: {
      //从父组件传入的数据
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  computed: {
    currentData: function() {
      if (this.data.length !== 0 || this.config.url === "") {
        this.page.total = this.data.length;
        if (this.config.pagination.showBytotal) {
          if (this.page.total <= this.config.pagination.pageSize) {
            this.config.pagination.showPagination = false;
          } else {
            this.config.pagination.showPagination = true;
          }
        }
        this.$emit("onShowPage", this.config.pagination.showPagination);
        return this.data;
      } else {
        return this.config.data;
      }
    }
  },
  created() {
    this.setConfig();
    if (!this.config.lazy && !this.config.pagination.autoRoll) {
      this.loadData();
    }
    if (!this.config.lazy && this.config.pagination.autoRoll) {
      this.intervalTimer = setInterval(() => {
        this.initLoadMore();
      }, 500);
    }
  },
  mounted() {
    //计算外层div高度
    if (this.config.isResize) {
      EleResize.on(this.$el, this.handleResize);
      this.$nextTick(() => {
        this.handleResize();
      });
    }
    if (this.config.creatScrollBar) {
      this.$nextTick(() => {
        this.creatScrollBar();
      });
    }
    this.$nextTick(() => {
      if (this.data.length != 0) {
        this.handleResize();
        this.resizeScroll(this.ttScroll);
      }
    });

    this.$nextTick(() => {
      this.initWidth();
    });
  },
  /* watch: {  取消深度watch属性，优化性能
    datagrid: {
      handler(curVal, oldVal) {
        this.setConfig();
        this.loadData();
      },
      deep: true
    }
  }, */
  methods: {
    loadEvent(value) {
      this.clearData();
      this.page.roll = value;
      this.hasNext = value;
      this.higher = !value;
      if (value) {
        this.intervalTimer = setInterval(() => {
          this.initLoadMore();
        }, 500);
      } else {
        this.loadData();
      }
    },
    getStyle(obj, attr) {
      if (obj.currentStyle) {
        //IE
        return obj.currentStyle[attr];
      } else {
        //Firefox
        return getComputedStyle(obj, "伪类")[attr];
      }
    },
    loadMore() {
      if (this.config.pagination.autoRoll && this.hasNext && !this.loading) {
        this.loadData();
      }
    },
    initWidth() {
      // 计算宽度
      var $pEl = document.querySelector("div.el-table__body-wrapper");
      var $tableEl = document.querySelector("table.el-table__body");
      var tableWidth = parseInt(this.getStyle($tableEl, "width"));
      var pWidth = parseInt(this.getStyle($pEl, "width"));
      this.wigther = tableWidth > pWidth;
      this.$emit("onLonger", this.wigther);
      return this.wigther;
    },
    initHigher() {
      // 计算高度
      var $pEl = document.querySelector("div.el-table__body-wrapper");
      var $tableEl = document.querySelector("table.el-table__body");
      var tableHight = parseInt(this.getStyle($tableEl, "height"));
      var pHight = parseInt(this.getStyle($pEl, "height"));
      this.higher = tableHight - 10 > pHight;
      return this.higher;
    },
    initLoadMore() {
      // 初始化加载
      if (
        !this.higher &&
        this.config.pagination.autoRoll &&
        this.hasNext &&
        !this.loading
      ) {
        this.loadData();
      }
      if (this.higher && this.config.pagination.autoRoll) {
        clearInterval(this.intervalTimer);
        this.intervalTimer = null;
      }
    },
    getCurrentData() {
      return this.currentData;
    },
    toggleAllSelection() {
      this.$refs.datagrid.toggleAllSelection();
    },
    //设置datagrid对象
    setConfig: function() {
      for (var i in this.datagrid) {
        if (this.datagrid.hasOwnProperty(i)) {
          if (typeof this.datagrid[i] == "object" && i != "data") {
            for (var j in this.datagrid[i]) {
              this.config[i][j] = this.datagrid[i][j];
            }
          } else {
            this.config[i] = this.datagrid[i];
          }
        }
      }
      // this.initWidth();
      this.page.pageSize = this.config.pagination.pageSize;
      this.page.roll = this.config.pagination.autoRoll;
    },
    setColumns() {
      this.config.columns = this.datagrid.columns;
    },
    creatScrollBar() {
      if (this.ttScroll == "") {
        let grid = this.$refs.datagrid;
        let gridBody = grid.$refs.bodyWrapper;
        gridBody.style.overflow = "hidden";
        let target = gridBody.children[0];
        this.ttScroll = this.scrollBar(target, { isHorizontal: true });
        if (this.config.showHeader) {
          let scrollHandle = gridBody.getElementsByClassName(
            "scrollHandleH"
          )[0];
          scrollHandle.addEventListener("mousedown", this.leftChange);
          let $this = this;
          EleResize.on(target, function() {
            $this.$nextTick(() => {
              $this.scrollChange($this.ttScroll);
              let scrollContent = gridBody.getElementsByClassName(
                "scrollContent"
              )[0];
              let header = grid.$refs.headerWrapper.children[0];
              header.style.marginLeft = scrollContent.offsetLeft + "px";
            });
          });
        }
      }
    },
    leftChange() {
      let $this = this;
      document.onmousemove = function() {
        let grid = $this.$refs.datagrid;
        let scrollContent = grid.$refs.bodyWrapper.getElementsByClassName(
          "scrollContent"
        )[0];
        let header = grid.$refs.headerWrapper.children[0];
        header.style.marginLeft = scrollContent.offsetLeft + "px";
      };
      document.onmouseup = function() {
        document.onmousemove = null;
        document.onmouseup = null;
      };
    },
    //控制分页的size变化
    handleSizeChange: function(size) {
      this.page.pageSize = size;
      if (undefined != this._events["currentPageChange"]) {
        this.$emit("pagesizeChange", size);
        return;
      }
      this.loadData();
    },
    doLayout: function() {
      this.$refs.datagrid.doLayout();
      if (this.config.creatScrollBar) {
        if (this.ttScroll) {
          this.scrollChange(this.ttScroll);
        }
      }
    },
    //控制分页的当前页控制
    handleCurrentChange: function(currentPage) {
      this.page.currentPage = currentPage;
      if (undefined != this._events["pagesizeChange"]) {
        this.$emit("currentPageChange", currentPage);
        return;
      }
      this.loadData();
    },

    //当用户手动勾选数据行的 Checkbox 时触发的事件
    onSelect: function(selection, row) {
      this.$emit("onSelect", selection, row);
      return;
    },
    //当用户手动勾选全选 Checkbox 时触发的事件
    onSelectAll: function(selection) {
      this.$emit("onSelectAll", selection);
      return;
    },
    //当选择项发生变化时会触发该事件
    onSelectChange: function(selection) {
      this.$emit("onSelectChange", selection);
      return;
    },
    //当前行变化的时候回调
    handleDatagirdCurrentChange: function(currentRow) {
      this.currentRow = currentRow;
    },
    /**
     * 获取当前行
     */
    getCurrentRow: function() {
      return this.currentRow;
    },
    selectRow: function(row) {
      var index = this.config.data.indexOf(row);
      row.selected = true;
      this.$set(this.config.data, index, row);
    },
    unSelectRow: function(row) {
      var index = this.config.data.indexOf(row);
      row.selected = false;
      this.$set(this.config.data, index, row);
    },
    toggleSelection: function(row) {
      if (row) {
        this.$refs.datagrid.toggleRowSelection(row);
      } else {
        this.$refs.datagrid.clearSelection();
      }
    },
    toggleSelection1: function(key, value) {
      for (var i = 0; i < this.config.data.length; i++) {
        if (this.config.data[i][key] === value) {
          if (this.config.data[i].selected) {
            this.config.data[i].selected = false;
          } else {
            this.config.data[i].selected = true;
          }
          this.$refs.datagrid.toggleRowSelection(this.config.data[i], true);
          this.$set(this.config.data, i, this.config.data[i]);
          break;
        }
      }
    },
    clearSelection: function() {
      this.$refs.datagrid.clearSelection();
    },
    clearSelection1: function() {
      for (var i = 0; i < this.config.data.length; i++) {
        this.config.data[i].selected = false;
        this.$set(this.config.data, i, this.config.data[i]);
      }
    },
    //设置当前选择的行
    setCurrentRow: function(row) {
      this.$refs.datagrid.setCurrentRow(row);
      this.highlightCurrentRow(row);
    },

    /**高亮当前行 */
    highlightCurrentRow: function(row) {
      if (!this.config.highlightCurrentRow) return;
      const el = this.$el;
      if (!el) return;
      const data = this.currentData;
      const rows = el.querySelectorAll("tbody > tr.el-table__row");
      const newRow = rows[data.indexOf(row)];
      if (rows) {
        [].forEach.call(rows, row => this.removeClass(row, "current-row"));
      }
      if (newRow) {
        this.addClass(newRow, "current-row");
      }
    },
    //重置分页,用于搜索前
    resetPage: function() {
      this.page = {
        currentPage: 1, //当前页
        pageSize: 10, //当前页的size
        total: 0
      };
    },

    //加载数据,接收参数
    loadData: function(params, resetPage) {
      var $this = this;
      if ($this.config.url == "") {
        return;
      }
      if (params !== undefined) {
        for (var i in params) {
          if (params.hasOwnProperty(i)) {
            $this.config.queryParams[i] = params[i];
          }
        }
      }
      var queryParams = {};
      for (var i in $this.config.queryParams) {
        if ($this.config.queryParams.hasOwnProperty(i)) {
          queryParams[i] = $this.config.queryParams[i];
        }
      }

      if ($this.config.pagination.showPagination) {
        if (resetPage === true) {
          $this.clearData();
        }
        queryParams.page = $this.page.currentPage;
        queryParams.rows = $this.page.pageSize;
        queryParams.roll = $this.page.roll;
      }

      $this.loading = true;
      if ($this.config.method.toUpperCase() == "post".toUpperCase()) {
        $this
          .$fetch(
            $this.config.url,
            qs.stringify(queryParams, { arrayFormat: "repeat" })
          )
          .then(res => {
            if (!res.success) {
              $this.msg(res);
              $this.loading = false;
              $this.hasNext = false;
              return;
            }
            if (res.data.rows) {
              $this.config.data = res.data.rows;
              $this.page.total = res.data.total;
            } else {
              //滚动加载
              $this.pushData(res.data, res.next);
              if (!this.higher) {
                this.initHigher();
              }
            }
            $this.$nextTick(() => {
              $this.loading = false;
              $this.$emit("onShowPage", $this.config.pagination.showPagination);
              $this.$emit("onLoadSuccess", $this.currentData);
              if (this.config.creatScrollBar) {
                $this.scrollChange($this.ttScroll);
              }
            });
          })
          .catch(err => {
            $this.loading = false;
            if (this.config.creatScrollBar) {
              $this.scrollChange($this.ttScroll);
            }
          });
      } else {
        $this.$catch($this.config.url, { params: queryParams }).then(res => {
          if (!res.success) {
            $this.msg(res);
            $this.loading = false;
            $this.hasNext = false;
            return;
          }

          if (res.data.rows) {
            $this.config.data = res.data.rows;
            $this.page.total = res.data.total;
          } else {
            //滚动加载
            $this.pushData(res.data, res.next);
            if (!this.higher) {
              this.initHigher();
            }
          }

          $this.$nextTick(() => {
            $this.loading = false;
            $this.$emit("onShowPage", $this.config.pagination.showPagination);
            $this.$emit("onLoadSuccess", $this.currentData);
            if (this.config.creatScrollBar) {
              $this.scrollChange($this.ttScroll);
            }
          });
        });
      }
    },
    /**
     * 行点击事件
     */
    rowClick: function(row, event, column) {
      this.$emit("rowClick", row);
    },
    /**
     * 控制resize
     */
    handleResize: function() {
      if (this.$el.offsetHeight != 0) {
        var toolbarHeight =
          this.$refs.toolbar !== undefined
            ? this.$refs.toolbar.offsetHeight
            : 0;
        if (this.config.pagination.showPagination) {
          this.datagridHeight = this.$el.offsetHeight - 37 - toolbarHeight;
        } else {
          this.datagridHeight = this.$el.offsetHeight - toolbarHeight;
        }
        this.doLayout();
      }
    },

    /*
     * 改变头部
     */
    headerDragend(newWidth, oldWidth, column, event) {
      this.$emit("headerDragend", newWidth, oldWidth, column, event);
    },
    /*
     * 当某个单元格被双击
     */
    cellDblclick(row, column, cell, event) {
      this.$emit("cellDblclick", row, column, cell, event);
    },
    rowDblClick(row, event) {
      this.$emit("row-dblclick", row, event);
    },
    /**
     * 每次选择变化的时候
     */
    selectionChange: function(value) {
      this.selectionValue = value;
      if (value.length > 0) {
        if (value.length == this.currentData.length) {
          this.indeterminate = false;
          this.checkAll = true;
        } else {
          this.indeterminate = true;
          this.checkAll = false;
        }
      } else {
        this.indeterminate = false;
        this.checkAll = false;
      }
      this.$emit("selectionChange", value);
    },

    /**
     * 获取选择的值
     */
    getSelection: function() {
      return this.selectionValue;
    },
    /**
     * 获取选择的值
     */
    pushData: function(data = [], next = false) {
      if (data.length > 0) {
        this.config.data.push.apply(this.config.data, data);
      }
      if (next != undefined || next != null) {
        this.hasNext = next;
        if (next) {
          this.page.currentPage = 1 + this.page.currentPage;
        }
      }
    },
    clearData: function() {
      this.config.data = [];
      this.page.currentPage = 1;
      this.page.pageSize = this.config.pagination.pageSize;
      this.page.total = 0;
    },
    /**更新数据,主要是用于config中的data需要改变的时候 */
    updateData: function(data, index) {
      this.$set(this.config.data, index, data);
    },
    resizeScroll() {
      this.scrollChange(this.ttScroll);
    }
  }
};
</script>
<style lang="less">
// @import "../../../var.less";
*::-webkit-scrollbar {
  width: 6px;
  height: 6px;
  background-color: transparent;
} /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
*::-webkit-scrollbar-track {
  background-color: transparent;
} /*定义滚动条轨道 内阴影+圆角*/
*::-webkit-scrollbar-thumb {
  background: rgba(199, 199, 199, 0.5) !important;
  border-radius: 3px;
} /*定义滑块 内阴影+圆角*/
.scrollbarHide::-webkit-scrollbar {
  display: none;
}
.scrollbarShow::-webkit-scrollbar {
  display: block;
}
.datagrid {
  height: 100%;
  .table-btn-group {
    font-size: 0;
    line-height: 1;
    .u-button {
      padding: 0;
      margin-left: 8px;
      padding-left: 8px;
      border-left: 1px solid #cbcbcb;
      line-height: 16px;
      height: 16px;
      &:first-child {
        margin-left: 0;
        padding-left: 0;
        border-left: none;
      }
    }
  }
  .datagrid_header {
    .title {
      float: left;
      color: #333;
      font-size: 14px;
    }
    .toolbar {
      padding-bottom: 6px;
      width: 100%;
    }
  }
  .datagrid_content {
    height: 100%;
    .el-table .cell,
    .el-table th div {
      padding-right: 10px !important;
    }

    .el-table .cell,
    .el-table th div,
    .el-table--border td:first-child .cell,
    .el-table--border th:first-child .cell {
      padding-left: 10px !important;
    }
    &.content-with-header {
      height: calc(~"100% - 45px");
    }

    .infinitedLoadingBox {
      position: absolute;
      top: 0;
      left: 0;
      z-index: 99;
      background: #ffffff;
      width: 100%;
      height: 100%;
      .loadingicon {
        position: absolute;
        top: 50%;
        left: 50%;
        color: #409eff;
        transform: translate(-50%, -50%);
      }
    }
  }
  .datagrid-pagination-box {
    overflow: hidden;
    background-color: #fff;
    z-index: 99;
    width: 100%;
    height: 37px;
    line-height: 36px;
    // border-color: #dddddd;
    // border-style: solid;
    // border-width: 0 1px 1px 1px;
    text-align: left;
    padding-left: 10px;
    box-sizing: border-box;
    .el-pagination {
      text-align: right;
    }
    .el-scrollbar__wrap {
      padding: 0px !important;
    }
  }
  .data-table {
    border-width: 1px 1px 0 1px;
    border-style: solid;
    border-color: #dddddd;
  }
  .el-table th {
    height: 50px;
    padding: 3px 0;
    background-color: #e5e9ee;
  }
  .el-table th > .cell {
    color: #333333;
    font-size: 12px;
    font-weight: bold;
    overflow: hidden;
    word-break: keep-all;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .el-table td {
    padding: 3px 0;
    height: 37px;
    background-color: #f2f4f6;
    color: #666666;
    font-size: 12px;
  }
  .el-table--striped .el-table__body tr.el-table__row--striped td {
    background-color: #fcfcfc !important;
  }
  .el-table .ascending .sort-caret.ascending,
  .el-table .descending .sort-caret.descending {
    color: #4b71a4;
  }
  .el-table td,
  .el-table th.is-leaf {
    border-bottom: 1px solid #dddddd;
  }
  .el-table--border td,
  .el-table--border th,
  .el-table__body-wrapper
    .el-table--border.is-scrolling-left
    ~ .el-table__fixed {
    border-right: 1px solid #dddddd;
  }
  // .el-table--enable-row-hover .el-table__body tr:hover > td,
  // .data-table .el-table__body tr:hover > td {
  //   background-color: rgba(45, 63, 89, 0.4);
  // }
  .el-table-column--selection .cell {
    text-overflow: initial !important;
  }
}
.information-list {
  padding-top: 10px;
  margin-bottom: 5px;
  li {
    font-size: 10px;
    margin-bottom: 9px;
    overflow: hidden;
    &:last-child {
      margin-bottom: 0 !important;
    }
    label {
      min-width: 95px;
      color: #99a9bf;
      vertical-align: middle;
      float: left;

      span {
        display: inline-block;
        vertical-align: text-top;
        text-align: right;
        word-break: break-all;
      }
    }
    p {
      position: relative;
      color: #606266;
      font-weight: bold;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      vertical-align: middle;
      text-align: left;
      .information-list-end {
        display: inline-block;
        text-align: right;
        word-break: break-all;
        padding: 0 12px 0 0;
        color: #99a9bf;
      }
    }
  }
}
.el-table-column--selection .cell {
  padding-left: 10px !important;
  padding-right: 10px !important;
}
.no-padding {
  padding-left: 0 !important;
  padding-right: 0 !important;
}
.clear {
  clear: both;
}
</style>
