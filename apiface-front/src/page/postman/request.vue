/** * 空白数据页 * author:Daye Shing */
<template>
  <div class="postman-request">
    <el-tabs>
      <el-tab-pane label="Params">
        <params title="Query Params" @submit="paramSubmit"></params>
      </el-tab-pane>
      <el-tab-pane label="Body">
        <el-radio-group v-model="radio" @change="changeEvent">
          <el-radio :label="0">none</el-radio>
          <el-radio :label="1">form-data</el-radio>
          <el-radio :label="2">form-urlencoded</el-radio>
          <el-radio :label="4">binary</el-radio>
          <el-radio :label="3">raw</el-radio>
          <el-select v-model="rawType" v-show="radio == 3">
            <el-option
              v-for="item in raws"
              :key="item"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-radio-group>
        <div class="postman-request-none" v-if="radio == 0">
          This request does not have a body
        </div>
        <div class="postman-request-form" v-if="radio == 3">
          <textarea v-model="content" @blur="bodySubmit"></textarea>
        </div>
        <params
          v-if="radio == 1 || radio == 2"
          class="postman-request-form"
          @submit="formSubmit"
          ref="form"
        ></params>
      </el-tab-pane>
      <el-tab-pane label="Headers">
        <params title="Headers" @submit="headerSubmit"></params>
      </el-tab-pane>
      <el-tab-pane label="Cookies">
        <params title="Cookies" @submit="cookieSubmit"></params>
      </el-tab-pane>
      <el-tab-pane label="Authorization"></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Params from "./params";
export default {
  components: {
    Params,
  },
  data() {
    return {
      radio: 0,
      rawType: "JSON",
      raws: ["TEXT", "JSON", "HTML", "XML", "JScript"],
      content: "",
    };
  },
  methods: {
    changeEvent(radio) {
      console.log(radio);
      if (radio == 1 || radio == 2) {
        this.$refs.form.submit();
        return;
      }
      if (radio == 3) {
        this.bodySubmit();
        return;
      }
      this.$emit("bodies", null);
    },
    bodySubmit() {
      this.$emit("bodies", {
        type: this.radio,
        data: {
          raw: this.rawType,
          content: this.content,
        },
      });
    },
    formSubmit(params) {
      this.$emit("bodies", { type: this.radio, data: params });
    },
    cookieSubmit(params) {
      this.$emit("cookies", params);
    },
    headerSubmit(params) {
      this.$emit("headers", params);
    },
    paramSubmit(params) {
      this.$emit("params", params);
    },
  },
};
</script>

<style lang="less">
.postman-request {
  width: 100%;
  height: calc(~"100% - 40px");
  .el-radio-group {
    padding-left: 14px;
    .el-input__inner {
      color: #eac459;
      width: 78px;
      border: 0px solid transparent !important;
    }
  }
  &-form {
    height: calc(~"100% - 27px") !important;
    textarea {
      width: 100%;
      height: 100%;
      border: none;
      background: transparent;
      outline: none;
      resize: none;
    }
  }
  &-none {
    text-align: center;
  }
}
</style>
