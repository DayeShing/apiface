<template>
  <button
    class="self-button"
    ref="button"
    :class="[sizeClass,shapeClass,typeClass,loadingClass,disabledClass]"
    :disabled="disabled"
    @click.prevent="clickEvent"
  >
    <span v-if="loadingValue==''">
      <i class="self-icon-left" :class="icon" v-if="icon!=''&&iconSide=='left'"></i>
      <span>
        <slot></slot>
      </span>
      <i class="self-icon-right" :class="icon" v-if="icon!=''&&iconSide=='right'"></i>
    </span>
    <span v-else>
      <i class="self-icon-left" :class="loadingIcon"></i>
      {{loadingValue}}
    </span>
  </button>
</template>

<script type="es6">
import "./selfButton.less";
export default {
  data() {
    return {
      loadingIcon: "",
      loadingValue: ""
    };
  },
  props: {
    size: {
      //尺寸   medium/small/mini
      type: String,
      default: "small"
    },
    type: {
      //类型
      type: String,
      default: ""
    },
    shape: {
      //形状   primary/plain/text
      type: String,
      default: "primary"
    },
    loading: {
      //是否加载中状态
      type: Boolean,
      default: false
    },
    disabled: {
      //是否禁用状态
      type: Boolean,
      default: false
    },
    icon: {
      //图标类名
      type: String,
      default: ""
    },
    iconSide: {
      //图标位置  left/right
      type: String,
      default: "left"
    }
  },
  computed: {
    sizeClass() {
      let size = this.size == "small" ? "" : "self-button--" + this.size;
      return size;
    },
    typeClass() {
      let type = this.type == "" ? "" : "self-btn-" + this.type;
      return type;
    },
    shapeClass() {
      let shape = this.shape == "primary" ? "" : "self-is-" + this.shape;
      return shape;
    },
    loadingClass() {
      let loading = this.loading ? "self-is-loading" : "";
      return loading;
    },
    disabledClass() {
      let disabled = this.disabled ? "self-is-disabled" : "";
      return disabled;
    }
  },
  methods: {
    setConfig() {
      this.loadingEvent();
    },
    loadingEvent() {
      if (this.loading) {
        this.loadingIcon = "el-icon-loading";
        this.loadingValue = "加载中";
      } else {
        this.loadingIcon = "";
        this.loadingValue = "";
      }
    },
    clickEvent(ev) {
      this.$emit("click", ev);
    }
  },
  created() {
    this.setConfig();
  }
};
</script>

<style>
</style>