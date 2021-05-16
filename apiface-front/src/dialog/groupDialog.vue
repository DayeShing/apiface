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
  >
    <el-form
      ref="reform"
      :model="mod"
      :rules="formRules"
      label-width="75px"
      label-suffix=":"
    >
      <el-form-item label="显示名称" prop="text">
        <el-input
          :maxlength="30"
          placeholder="请输入显示名称"
          v-model="mod.text"
          auto-complete="off"
        ></el-input>
      </el-form-item>
    </el-form>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button class="btn-item" type="cancle" @click="closeDialog">
          {{ $self("cancle") }}
        </u-button>
        <u-button
          class="btn-item"
          type="submit"
          @click="saveData"
          :disabled="btnState"
          >{{ $self("save") }}</u-button
        >
      </div>
    </div>
  </dialog-box>
</template>

<script type="es6">
import DialogBox from "components/dialog/dialogbox";
import uButton from "components/button/index";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "groupDialog",
  components: {
    DialogBox,
    uButton
  },
  data() {
    return {
      page: "dialog.conf.",
      params: {
        title: "",
        width: "400px",
        height: "200px",
        visible: false
      },
      mod:{
        groupId: "",
        text: ""
      },
      btnState: false,
      formRules: {
        text: [
          { required: true, message: "请输入显示名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" },
        ]
      }
    };
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    saveData () {
      this.$refs.reform.validate(validate => {
        if (validate)        {
          this.submit();
        } else        {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      this.$fetch(inter.api.group.save, this.mod, { headers: { "Content-Type": "application/json" } }).then(res => {
        this.btnState = false;
        if (res && res.success)        {
          this.$emit("submit");
          this.closeDialog();
        } else        {
          this.$message({ type: "error", message: res.message });
        }
      });
    },
    openDialog(mod) {
      this.params.title = "新增分组";
      if(mod){
        this.mod = mod;
        this.params.title = "编辑分组";
      }
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.mod = {
        groupId: "",
        text: ""
      }
    }
  }
};
</script>

<style lang="less">
.el-dialog__body {
  padding: 4px 6px !important;
}
</style>
