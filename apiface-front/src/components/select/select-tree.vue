<!--
  封装下拉树 
-->
<template>
  <div class="select-tree u-select-common">
    <el-popover
      ref="popover"
      transition="el-zoom-in-top"
      popper-class="bdaPopver el-select-dropdown popperClass"
      :visible-arrow="false"
      :width="popoverWidth"
      trigger="click"
      @show="showPopper"
      @hide="hidePopper"
      :disabled="isDisabled"
    >
      <tree
        ref="tree"
        :tree="config"
        @onNodeClick="nodeClick"
        style="height:300px"
      ></tree>
    </el-popover>
    <el-popover
      ref="popover1"
      popper-class="el-select-dropdown hoverPopver"
      :visible-arrow="false"
      :width="popoverWidth"
      trigger="hover"
      v-if="config.multiple"
      :disabled="isDisabled"
      @show="showHoverPopper"
      v-model="hoverPopoverStatus"
    >
      <transition-group>
        <el-tag
          v-for="(value, index) in tempValues"
          :key="value.entryId"
          size="small"
          :closable="true"
          :type="value.type"
          @close="closeTag(index)"
        >
          <span class="el-select__tags-text"> {{ value.text }}</span>
        </el-tag>
      </transition-group>
    </el-popover>
    <div
      :class="{ 'el-select': config.multiple }"
      v-popover:popover
      class="u-selet-container"
    >
      <div
        v-popover:popover1
        class="el-select__tags treeHover"
        ref="tags"
        v-if="config.multiple"
        :disabled="isDisabled"
      >
        <!-- <transition-group> @after-leave="resetInputHeight
              <el-tag v-for="(value,index) in tempValues" :key="value.entryId" :closable="!config.isDisabled" :type="value.type" @close="closeTag(index)">
               {{value.text}}
              </el-tag>
            </transition-group> -->
      </div>
      <div :class="'u-' + config.size">
        <el-input
          :size="config.size"
          readonly
          :value="tempValueText"
          ref="reference"
          :class="{ 'input-padding-right': showClearIcon }"
          :placeholder="tempValues.length == 0 ? config.placeholderText : ''"
          :autosize="config.multiple"
          :disabled="isDisabled"
        >
        </el-input>
        <i
          v-show="showClearIcon"
          class="el-icon-close el-input__icon clear-icon"
          :class="{ 'u-select-disabled': isDisabled }"
          @click.stop="clearValue"
        ></i>
        <i
          :class="[
            popperState && !isDisabled ? 'is-reverse' : '',
            'el-icon-arrow-up',
            'el-input__icon',
            'el-select__caret',
            'u-select-icon',
          ]"
        ></i>
      </div>
    </div>
  </div>
</template>

<script>
import qs from "qs";
import { EleResize } from "components/EleResize";
import "./select.less";
import Tree from "components/tree/tree";
const sizeMap = {
  medium: 36,
  small: 30,
  mini: 22,
};
export default {
  name: "selectTree",
  data() {
    return {
      tempValue: {
        value: "",
        text: "",
      },
      popperState: false, //弹出框的状态
      hoverPopoverStatus: false, //hover弹出框的状态
      tempValues: [],
      inputIcon: "el-icon-caret-bottom",
      popoverWidth: 150,
      config: {
        //这部分参数继承了tree的参数
        url: "", //selecttree的url
        size: "small", // input 尺寸 medium为36px高，small为30px,mini为22px
        leafType: "", //selectTree 叶子节点类型
        method: "post", //接收方式
        loadNodeAfterSearch: true, //是否在搜索后继续搜索
        selectNodeType: "", //
        isLoad: true,
        showCheckbox: false,
        multiple: false, //开启多选
        highlightCurrent: false,
        backgroud: "white",
        defaultValue: {
          value: "",
          text: "",
        }, //当设置了这个值的时候，为空的时候会设置为这个值
        placeholderText: "请选择",
        clearable: false,
        queryParams: {},
      },
    };
  },
  computed: {
    showClearIcon() {
      if (this.isDisabled) {
        return false;
      }
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
    tempValueText() {
      var temp = "";
      if (this.config.multiple) {
        var text = [];
        this.tempValues.forEach((item) => {
          text.push(item.text);
        });
        temp = text.join(",");
      } else {
        temp = this.tempValue.text;
      }
      return temp;
    },
  },
  props: {
    value: {},
    selectTree: {
      type: Object,
      default: function() {
        return {};
      },
    },
    isDisabled: Boolean,
  },
  model: {
    prop: "value",
    event: "change",
  },
  created() {
    this.setConfig(this.selectTree);
  },
  mounted() {
    //计算el的宽度，控制input的宽度
    this.popoverWidth = this.$el.offsetWidth;
    // let $this = this;
    // $this.$nextTick(() => {
    // this.popoverWidth = this.$el.offsetWidth;
    // });
    // EleResize.on(this.$el, this.handleResize);
  },
  watch: {
    tempValue(val) {
      // deep: true,    去掉深度watch
      // handler(val) {
      if (
        (val.text === "" || val.text === null) &&
        this.config.defaultValue.text !== ""
      ) {
        this.tempValue = JSON.parse(JSON.stringify(this.config.defaultValue));
        return;
      }
      this.$emit("change", val);
      // }
    },
    tempValues(val) {
      this.$emit("change", val);
    },

    // selectTree: {   去掉watch 直接调方法设置
    //   deep: true,
    //   handler(curVal, oldVal) {
    //     this.setConfig();
    //   }
    // }
  },
  components: {
    Tree,
  },
  methods: {
    /**
     * 设置配置项
     */
    setConfig: function(params) {
      for (var i in params) {
        if (params.hasOwnProperty(i)) {
          this.config[i] = params[i];
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
      if (!this.isDisabled) {
        if (this.config.multiple) {
          if (this.config.defaultValue instanceof Array) {
            this.tempValues = JSON.parse(
              JSON.stringify(this.config.defaultValue)
            );
          } else {
            this.tempValues = [];
          }
        } else {
          this.tempValue = JSON.parse(JSON.stringify(this.config.defaultValue));
        }
      }
    },

    /**
     * 设置model值
     */
    setValue: function(value) {
      if (this.config.multiple) {
        this.tempValues = JSON.parse(JSON.stringify(value));
      } else {
        this.tempValue = JSON.parse(JSON.stringify(value));
      }
    },
    // /**
    //    * 单选时候设置文本框显示值
    //    */
    // setText: function(text) {
    //   if (!this.config.multiple) {
    //     this.tempValue.text = text;
    //   }
    // },
    /**
     * 树点击的事件
     */
    nodeClick: function(node) {
      if (
        this.config.selectNodeType === undefined ||
        this.config.selectNodeType === ""
      ) {
        if (this.config.multiple) {
          if (!(this.config.defaultValue instanceof Array)) {
            this.config.defaultValue = [];
          }
          var tempNode = JSON.parse(JSON.stringify(node));
          if (this.getValueIndex(this.tempValues, tempNode) === -1)
            this.tempValues.push(tempNode);
          // this.resetInputHeight();
          return;
        }
        var temp = JSON.parse(JSON.stringify(node));
        temp.value = node.entryId;
        temp.text = node.text;
        this.tempValue = temp;
        this.$refs.popover.doClose();
        return;
      }

      if (node.type === this.config.selectNodeType) {
        if (this.config.multiple) {
          //判断是否已选择
          var tempNode = node;
          if (this.getValueIndex(this.tempValues, tempNode) === -1) {
            if (!this.config.defaultValue instanceof Array)
              this.config.defaultValue = [];
            this.tempValues.push(tempNode);
            return;
          }
          return;
        }
        var temp = JSON.parse(JSON.stringify(node));
        temp.value = node.entryId;
        temp.text = node.text;
        this.tempValue = temp;
        this.$refs.popover.doClose();
      }
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
          if (element["id"] == value["id"]) {
            return i;
          }
        }
        return index;
      }
    },
    /**
     * 显示弹窗的事件
     */
    showPopper: function() {
      this.popperState = true;
    },
    /**
     * 隐藏弹窗的事件
     */
    hidePopper: function() {
      this.popperState = false;
    },
    /*
     * 显示hover弹窗的事件
     */
    showHoverPopper() {
      if (this.tempValues.length > 0) {
        this.hoverPopoverStatus = true;
      } else {
        this.hoverPopoverStatus = false;
      }
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
        input.style.height =
          this.tempValues.length === 0 && this.config.size === "mini"
            ? sizeMap[this.config.size]
            : Math.max(
                tags ? tags.clientHeight + 10 : 0,
                sizeMap[this.config.size] || 36
              ) + "px";
        this.$refs.popover.updatePopper();
      });
    },
    /**
     * 多选时候清除某个值
     */
    closeTag: function(index) {
      this.tempValues.splice(index, 1);
      this.$refs.popover.doClose();
    },
    /**
     * 控制resize
     */
    handleResize: function() {
      if (this.config.multiple) this.resetInputHeight();
    },
    /**
     * 刷新所有节点
     */
    refreshTree: function(param) {
      this.$nextTick(() => {
        this.popoverWidth = this.$el.offsetWidth;
      });
      this.$refs.tree.setConfig(this.config);
      this.$refs.tree.reloadEvent(param);
    },
    /**设置查询的参数 */
    setQueryParams: function(param) {
      this.$refs.tree.setQueryParams(param);
    },
  },
};
</script>
