<!--
  用户编辑对话框
  作者：Daye Shing
-->
<template>
  <dialog-box
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
    class="postman-dialog"
  >
    <post-man></post-man>
  </dialog-box>
</template>

<script type="es6">
const PostMan = () => import('page/postman/index.vue')
import DialogBox from "components/dialog/dialogbox";

export default {
  name: "postmanDialog",
  components: {
    DialogBox,PostMan
  },
  data() {
    return {
      page: "dialog.conf.",
      params: {
        title: "接口调试工具",
        width: "915px",
        height: "575px",
        visible: false
      }
    };
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    submit() {},
    openDialog() {
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {}
  }
};
</script>

<style lang="less">
.postman-dialog {
  .el-dialog__body {
    padding: 4px 6px !important;
  }
}
</style>
