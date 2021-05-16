<!--
  封装element-scrollbar
-->
<template>
  <el-scrollbar
    ref="scrollbar"
    class="u-scrollbar"
    :tag="tag"
    :wrapClass="wrapClass"
    :viewClass="viewClass"
    :noresize="noresize"
    :viewStyle="viewStyle"
    :wrapStyle="wrapStyle"
  >
    <slot></slot>
  </el-scrollbar>
</template>
<style></style>
<script>
import { EleResize } from "./EleResize";
export default {
  name: "u-scrollbar", // u-scrollbar
  data() {
    return {};
  },
  components: {},
  created() {},
  mounted() {
    EleResize.on(this.$el, this.doUpdate);
  },
  props: {
    wrapStyle: {},
    wrapClass: {},
    viewClass: {},
    viewStyle: {},
    noresize: false, // 如果 container 尺寸不会发生变化，最好设置它可以优化性能
    tag: {
      type: String,
      default: "div"
    }
  },
  methods: {
    doUpdate: function() {
      this.$refs.scrollbar.update();
    }
  }
};
</script>
