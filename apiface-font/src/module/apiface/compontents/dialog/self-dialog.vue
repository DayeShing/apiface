<template>
  <el-dialog
    :title="title"
    :top="top"
    :modal="config.model"
    :modal-append-to-body="config.modalAppendToBody"
    :lock-scroll="config.lockScroll"
    :custom-class="customClass"
    :append-to-body="config.appendToBody"
    :close-on-click-modal="config.closeOnClickModal"
    :close-on-press-escape="config.closeOnPressEscape"
    :show-close="config.showClose"
    :visible.sync="visible"
    :before-close="handleBeforeClose"
    @close="handleClose"
    @open="handleOpen"
    ref="dialog"
  >
    <slot></slot>
    <div slot="footer" v-if="$slots.footer">
      <slot name="footer"></slot>
    </div>
  </el-dialog>
</template>

<script type="es6">
import { drag } from "./drag";
export default {
  data() {
    return {
      top: "",
      config: {
        top: "2%", //Dialog CSS 中的 top 值
        width: "850px", //Dialog CSS 中的 width 值
        height: "96%", //Dialog CSS 中的 height 值
        modal: true, //是否需要遮罩层
        modalAppendToBody: true, //遮罩层是否插入至 body 元素上，若为 false，则遮罩层会插入至 Dialog 的父元素上
        appendToBody: false,
        lockScroll: true, //是否在 Dialog 出现时将 body 滚动锁定
        customClass: "", //Dialog 的自定义类名
        closeOnClickModal: false, //是否可以通过点击 modal 关闭 Dialog
        closeOnPressEscape: true, //是否可以通过按下 ESC 关闭 Dialog
        showClose: true, //是否显示关闭按钮
        isDrag: true, //是否可以移动弹窗
        isScroll: true, //是否加载滚动条
        filterId: "", //模糊选取的id
        isFilter: true, //是否开启遮罩模糊
        isHeaderClass: false, //是否给header添加类
        headerClass: "" //当isHeaderClass为true是给header添加的类名
      }
    };
  },
  components: {},
  props: {
    visible: {
      default: false
    },
    title: "",
    params: Object
  },
  computed: {
    customClass() {
      let customClass =
        this.config.customClass == ""
          ? "self-dialog"
          : "self-dialog " + this.config.customClass;
      if (!this.$slots.footer) {
        customClass = customClass + " self-dialog-nofooter";
      }
      return customClass;
    }
  },
  created() {
    this.setConfig(this.params);
  },
  mounted() {
    this.setSize(this.params);
    let dialogbox = this.$refs.dialog.$el;
    if (this.config.isDrag) {
      drag(dialogbox, "el-dialog", "el-dialog__header");
    }
    if (this.config.isHeaderClass) {
      let header = dialogbox.getElementsByClassName("el-dialog__header")[0];
      this.addClass(header, this.config.headerClass);
    }
  },
  methods: {
    init() {
      let $this = this;
      if ($this.visible) {
        if ($this.config.isFilter && $this.config.filterId != "") {
          document.getElementById($this.config.filterId).style.filter =
            "blur(1.5px)";
        }
        $this.$nextTick(() => {
          $this.setTop();
        });
      } else {
        if ($this.config.isFilter && $this.config.filterId != "") {
          document.getElementById($this.config.filterId).style.filter = "none";
        }
      }
    },
    setConfig(params) {
      if (params === undefined) return;
      for (let key in params) {
        if (params.hasOwnProperty(key)) {
          this.config[key] = params[key];
        }
      }
    },
    setSize(params) {
      if (params.width != undefined) {
        this.setWidth();
      }
      if (params.height != undefined) {
        this.setHeight();
      }
    },
    setWidth() {
      this.$refs.dialog.$refs.dialog.style.width = this.params.width;
    },
    setHeight() {
      this.$refs.dialog.$refs.dialog.style.height = this.params.height;
    },
    setTop() {
      if (this.params.top != undefined) {
        this.top = this.params.top;
      } else {
        let dialogH = this.$refs.dialog.$refs.dialog.offsetHeight;
        let wrapperH = this.$refs.dialog.$el.offsetHeight;
        let betweenH = (wrapperH - dialogH) * 0.5;
        this.top = betweenH + "px";
      }
    },
    handleBeforeClose(ev) {
      this.$emit("before-close", ev);
    },
    handleClose(ev) {
      this.$emit("close", ev);
    },
    handleOpen(ev) {
      this.$emit("open", ev);
    }
  },
  watch: {
    visible: "init"
  }
};
</script>

<style lang="less">
@import "../../css/var.less";
.v-modal {
  background: @dialog_modal_bg;
}
.el-dialog__wrapper {
  overflow: hidden;
}
.self-dialog {
  margin: 0 auto;
  height: 96%;
  max-height: 600px;
  background-color: #f2f4f6;
  border: 1px solid #dddddd;
  &.el-dialog--full {
    height: 100%;
    max-height: 100%;
  }
  .el-dialog__header {
    padding: 0 16px;
    height: 43px;
    line-height: 42px;
    border-bottom: 1px solid #cbcbcb;
    .el-dialog__title {
      font-size: 14px;
      color: #333333;
      font-weight: bold;
    }
    .el-dialog__headerbtn {
      font-size: 14px;
      top: 14px;
      color: #999999;
      &:hover {
        color: #1177e2;
      }
      .el-dialog__close {
        color: #9e9e9e;
        &:hover {
          color: #4b70a4;
        }
      }
    }
  }
  .el-dialog__body {
    width: 100%;
    padding: 4px 0px;
    height: -moz-calc(~"100% - 94px");
    height: -webkit-calc(~"100% - 94px");
    height: calc(~"100% - 94px");
    overflow: hidden;
    .el-scrollbar {
      height: -moz-calc(~"100% + 17px");
      height: -webkit-calc(~"100% + 17px");
      height: calc(~"100% + 17px");
    }
    .el-scrollbar__wrap {
      padding: 10px;
      height: 100%;
    }
    .el-scrollbar__bar {
      bottom: 17px;
    }
    .__rail-is-vertical {
      right: 4px !important;
    }
  }
  .el-dialog__footer {
    text-align: center;
    position: absolute;
    width: 100%;
    height: 52px;
    box-sizing: border-box;
    bottom: 0;
    left: 0;
    padding: 10px 20px;
    border-radius: 2px;
    border-top: 1px solid #cbcbcb;
    .self-button {
      min-width: 90px;
      height: 30px;
      line-height: 28px;
      margin-left: 14px;
    }
  }
}
.self-dialog-nofooter {
  .el-dialog__body {
    width: 100%;
    padding: 0px;
    height: -moz-calc(~"100% - 38px");
    height: -webkit-calc(~"100% - 38px");
    height: calc(~"100% - 38px");
  }
}
</style>
