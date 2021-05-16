<!--
  下拉table选择
-->
<template>
  <div class="select-table u-select-common">
    <el-popover
      ref="popover"
      placement="bottom"
      transition="el-zoom-in-top"
      popper-class="bdaPopver el-select-dropdown popperClass"
      :visible-arrow="false"
      :width="popoverWidth"
      trigger="click"
      @show="showPopper"
      @hide="hidePopper"
    >
      <datagrid
        ref="datagrid"
        :datagrid="config"
        @rowClick="nodeClick"
        @onLoadSuccess="onLoadSuccess"
      >
        <el-table-column v-if="config.multiple" label=" " width="60">
          <template scope="scope">
            <i class="el-icon-circle-check" v-show="scope.row.selected"></i>
          </template>
        </el-table-column>
        <div slot="toolbar">
          <slot name="toolbar"></slot>
        </div>
      </datagrid>
    </el-popover>
    <div
      :class="{ 'el-select': config.multiple }"
      v-popover:popover
      class="u-selet-container"
    >
      <div
        :class="{ 'padding-right': showClearIcon }"
        class="el-select__tags"
        ref="tags"
        v-if="config.multiple"
        v-show="tempValues.length !== 0"
      >
        <transition-group @after-leave="resetInputHeight">
          <el-tag
            v-for="(value, index) in tempValues"
            :key="value.value"
            :closable="true"
            :type="value.value"
            @close="closeTag(index)"
          >
            <span class="el-select__tags-text"> {{ value.text }}</span>
          </el-tag>
        </transition-group>
      </div>
      <el-input
        readonly
        v-model="tempValue.text"
        ref="reference"
        :size="config.size"
        :class="{ 'input-padding-right': showClearIcon }"
        :placeholder="tempValues.length === 0 ? '--请选择--' : ''"
        :autosize="config.multiple"
      >
      </el-input>
      <i
        v-show="showClearIcon"
        class="el-icon-close el-input__icon clear-icon"
        @click.stop="clearValue"
      ></i>
      <i
        :class="[
          popperState ? 'is-reverse' : '',
          'el-icon-arrow-up',
          'el-input__icon',
          'el-select__caret',
          'u-select-icon',
        ]"
      ></i>
    </div>
  </div>
</template>
<style></style>
<script>
import "./select.less";
import qs from "qs";
import datagrid from "components/datagrid/datagrid.vue";
import { EleResize } from "components/EleResize";
const sizeMap = {
  medium: 36,
  small: 30,
  mini: 22,
};
export default {
  name: "selectTable",
  data() {
    return {
      tempValue: {
        value: "",
        text: "",
      },
      tempValues: [],
      tempRow: {},
      tempRows: [],
      rows: [], //保存一份临时的变量用于checkbox
      popperState: false, //弹出框的状态
      inputIcon: "el-icon-caret-bottom",
      popoverWidth: 150,
      config: {
        //着部分参数继承datagrid的参数
        multiple: false, //开启多选
        id: "", //id的字段
        size: "small",
        text: "", //显示文本的字段
        defaultValue: {},
        popoverWidth: null,
        creatScrollBar: true,
        rowNumber: {
          //行序号
          showRowNumbers: false, //是否显示序号
          rowNumberWidth: 30, //行宽度
          rowNumberLabel: " ", //行的高度
        },
        border: false, //是否显示边框
        emptyText: "暂无数据", //无数据的显示
        columns: [], //表格的列
        method: "post", //默认方法post
        queryParams: {}, //查询参数
        url: "", //查询地址
        clearable: false,
        pagination: {
          showPagination: false, //是否显示分页
          layout: "sizes, prev, pager,next,total", //分页布局
          pageSizeList: [10, 30, 50, 100],
        },
        selection: {
          showSelection: false, //是否显示checkbox
        },
      },
    };
  },
  computed: {
    showClearIcon() {
      if (this.config.clearable) {
        if (!this.config.multiple)
          return (
            this.tempValue.value != "" &&
            this.tempValue.value !== this.config.defaultValue.value
          );
        else
          return (
            this.tempValues.length !== undefined && this.tempValues.length > 0
          );
      }
    },
  },
  props: {
    value: {},
    selectTable: {
      type: Object,
      default: function() {
        return {};
      },
    },
  },
  model: {
    prop: "value",
    event: "change",
  },
  components: {
    datagrid,
  },
  created() {
    this.setConfig();
  },
  mounted() {
    //计算el的宽度，控制input的宽度
    // this.popoverWidth =
    //   this.config.popoverWidth !== null
    //     ? this.config.popoverWidth
    //     : this.$el.offsetWidth;
    // debugger;
    // this.$refs.datagrid.doLayout();
    EleResize.on(this.$el, this.handleResize);
  },
  watch: {
    tempValue: {
      deep: true,
      handler(val) {
        if (
          (val.text === "" || val.text === null) &&
          this.config.defaultValue.text !== ""
        ) {
          this.tempValue = JSON.parse(JSON.stringify(this.config.defaultValue));
          return;
        }
        this.$emit("change", val);
      },
    },
    tempValues(val) {
      this.$emit("change", val);
    },
    // selectTable: {
    //   deep: true,
    //   handler(curVal, oldVal) {
    //     this.setConfig();
    //   }
    // }
  },
  methods: {
    /**
     * 设置配置项
     */
    setConfig: function() {
      for (var i in this.selectTable) {
        if (this.selectTable.hasOwnProperty(i)) {
          if (typeof this.selectTable[i] === "object") {
            for (var j in this.selectTable[i]) {
              this.config[i][j] = this.selectTable[i][j];
            }
          }
          this.config[i] = this.selectTable[i];
        }
      }
      if (!this.config.multiple) {
        this.tempValue = JSON.parse(JSON.stringify(this.config.defaultValue));
      } else {
        if (this.config.defaultValue instanceof Array)
          this.tempValues = JSON.parse(
            JSON.stringify(this.config.defaultValue)
          );
      }
    },
    /**
     * 清除值，如果有默认值的设置为默认值
     */
    clearValue: function() {
      if (this.config.multiple) {
        if (this.config.defaultValue instanceof Array) {
          this.tempValues = JSON.parse(
            JSON.stringify(this.config.defaultValue)
          );
        } else {
          this.tempValues = [];
          this.$refs.datagrid.clearSelection1();
        }
      } else {
        this.tempValue = JSON.parse(JSON.stringify(this.config.defaultValue));
      }
    },
    /**
     * 设置model值
     */
    setValue: function(value) {
      if (this.config.multiple) {
        this.tempValues = JSON.parse(JSON.stringify(value));
        //设置回显
        if (value.length > 0) {
          for (var i = 0; i < value.length; i++) {
            this.$refs.datagrid.toggleSelection1(
              this.config.id,
              value[i].value
            );
          }
        } else {
          this.$refs.datagrid.clearSelection1();
        }
      } else {
        this.tempValue = JSON.parse(JSON.stringify(value));
      }
      this.resetInputHeight();
    },
    /**
     * 单选时候设置文本框显示值
     */
    setText: function(text) {
      if (!this.config.multiple) {
        this.tempValue.text = text;
      }
    },
    /**
     * 事件点击回调
     */
    nodeClick: function(row) {
      var tempNode = {};
      tempNode.value = row[this.config.id];
      tempNode.text = row[this.config.text];
      if (this.config.multiple) {
        var index = this.getValueIndex(this.tempValues, tempNode);
        if (this.tempValues.length === 0 || index === -1) {
          this.tempValues.push(tempNode);
          this.$refs.datagrid.selectRow(row);
          this.resetInputHeight();
        } else {
          this.$refs.datagrid.unSelectRow(row);
          this.tempValues.splice(index, 1);
        }
        return;
      }
      this.tempValue = JSON.parse(JSON.stringify(tempNode));
      this.tempRow = JSON.parse(JSON.stringify(row));
      this.$refs.popover.doClose();
      return;
    },
    /**
     * 获取在数组中的位置
     */
    getValueIndex(arr, value) {
      const isObject =
        Object.prototype.toString.call(value).toLowerCase() ===
        "[object object]";
      if (!isObject) {
        return arr.indexOf(value);
      } else {
        var index = -1;
        for (var i = 0; i < arr.length; i++) {
          var element = arr[i];
          if (element.value === value.value) {
            return i;
          }
        }
        return index;
      }
    },
    /**
     * 获得当前已选择的行
     */
    getSelectRows: function() {
      return this.tempRow;
    },
    /**
     * 显示弹窗的事件
     */
    showPopper: function() {
      this.popoverWidth =
        this.config.popoverWidth !== null
          ? this.config.popoverWidth
          : this.$el.offsetWidth;
      this.$nextTick(() => {
        this.$refs.datagrid.doLayout();
      });
      this.popperState = true;
    },
    /**
     * 隐藏弹窗的事件
     */
    hidePopper: function() {
      this.popperState = false;
    },
    //更新input的高度
    resetInputHeight: function() {
      this.$nextTick(() => {
        if (!this.$refs.reference) return;
        let inputChildNodes = this.$refs.reference.$el.childNodes;
        let input = [].filter.call(
          inputChildNodes,
          (item) => item.tagName === "INPUT"
        )[0];
        const tags = this.$refs.tags;
        if (tags != undefined) {
          this.$refs.reference.$el.style.height = input.style.height =
            this.tempValues.length === 0 && this.config.size === "mini"
              ? sizeMap[this.config.size]
              : Math.max(
                  tags ? tags.clientHeight : 0,
                  sizeMap[this.config.size] || 30
                ) + "px";
          this.$refs.popover.updatePopper();
        }
      });
    },
    /**
     * 多选下删除选项
     */
    closeTag: function(index) {
      this.$refs.datagrid.toggleSelection1(
        this.config.id,
        this.tempValues[index].value
      );
      this.tempValues.splice(index, 1);
      this.$refs.popover.doClose();
    },
    /**
     * 控制重新resize
     */
    handleResize: function() {
      if (this.config.multiple) this.resetInputHeight();
    },
    /**
     * 重新加载数据
     */
    reload: function(param) {
      if (this.$refs.datagrid) this.$refs.datagrid.loadData(param);
    },
    /**
     * 用于搜索后设置checkbox
     */
    onLoadSuccess: function(rows) {
      if (this.config.multiple) {
        for (var i = 0; i < rows.length; i++) {
          for (var j = 0; j < this.tempValues.length; j++) {
            if (rows[i][this.config.id] === this.tempValues[j].value) {
              this.$refs.datagrid.selectRow(rows[i]);
              break;
            }
          }
        }
      }
    },
  },
};
</script>
