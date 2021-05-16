<!--
  封装默认的搜索input输入框,适用于单个搜索框
-->
<template>
  <div class="u-search-input">
    <el-input
      v-model="tempValue"
      :placeholder="placeholder"
      :size="size"
      @keyup.enter.native="keyEnterEvent"
    >
    </el-input>
    <i
      class="el-icon-delete el-input__icon clear"
      @click="clear"
      v-show="tempValue != ''"
    ></i>
    <i class="el-icon-search el-input__icon" @click="search"></i>
  </div>
</template>
<style lang="less">
.u-search-input {
  position: relative;
  .el-input__icon {
    top: -2px;
    width: 30px;
    position: absolute;
    line-height: 30px;
    color: #4b71a4;
    font-size: 16px;
  }
  .el-icon-search {
    right: 0px;
  }
  .el-icon-delete {
    right: 30px;
  }
  .el-input__icon:hover {
    background-color: none;
    color: #69c8ff;
    cursor: pointer;
    border-radius: 0px;
  }
  .clear {
    right: 30px;
  }
  .el-input__inner {
    padding-right: 60px;
  }
}
</style>
<script>
export default {
  data() {
    return {
      // tempValue: "",
      size: "small",
    };
  },
  model: {
    prop: "value",
    event: "input",
  },
  computed: {
    tempValue: {
      get() {
        return this.value;
      },
      set(value) {
        var temp = this.value;
        this.$emit("input", value);
        if (value === "" && temp !== "") {
          this.$emit("clear");
        }
      },
    },
  },
  props: {
    value: { required: true },
    placeholder: {
      type: String,
      default: "请输入关键词",
    },
  },
  // watch: {
  //   tempValue(value, oldValue) {
  //     this.$emit("input", value);
  //     if (value === "" && oldValue !== "") {
  //       this.$emit("clear");
  //     }
  //   }
  // },
  methods: {
    clear: function() {
      this.tempValue = "";
    },
    search: function() {
      this.$emit("search");
    },
    keyEnterEvent(e) {
      this.search();
    },
  },
};
</script>
