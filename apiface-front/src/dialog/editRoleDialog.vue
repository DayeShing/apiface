<!--
  用户编辑对话框
  作者：Daye Shing
-->
<template>
  <dialog-box
    class="editRoleDialog"
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <el-form ref="roleForm" :model="role" :rules="formRules" label-width="85px" label-suffix=":">
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          :maxlength="30"
          placeholder="请输入角色名称"
          v-model="role.roleName"
          :disabled="role.disabled"
          auto-complete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="角色描述" prop="description">
        <el-input
          :maxlength="300"
          placeholder="请输入角色描述"
          type="textarea"
          v-model="role.descript"
          auto-complete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="超管角色"
        v-if="role.roleId != '-1' && role.roleId != '-2' && role.roleId != '-3'"
      >
        <el-checkbox v-model="role.admin" :disabled="admin">是否超管</el-checkbox>
      </el-form-item>
      <el-form-item label="权限缓存" v-if="role.roleId != '-1' && role.roleId != '-3' && !admin">
        <el-checkbox v-model="role.cachedValue">是否缓存权限</el-checkbox>
      </el-form-item>
    </el-form>
    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button class="btn-item" type="cancle" @click="closeDialog">取消</u-button>
        <u-button class="btn-item" type="submit" @click="saveData" :disabled="btnState">保存</u-button>
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
  name: "editFuncDialog",
  components: {
    uButton,
    DialogBox
  },
  data() {
    return {
      params: {
        title: "新增角色",
        width: "530px",
        height: "350px",
        visible: false
      },
      admin: false,
      index: 0,
      role: {
        roleId: "",
        roleName: "",
        descript: "",
        admin: false,
        cachedValue: false
      },
      editFlag: false,
      btnState: false,
      formRules: {
        funcName: [
          { required: true, message: "请输入功能名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" }
        ],
        pFuncId: [{ required: true, message: "请选择父功能", trigger: "blur" }],
        visual: [{ required: true, message: "请选择权限", trigger: "blur" }]
      }
    };
  },
  props: {
    dialog: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  methods: {
    saveData() {
      this.$refs.roleForm.validate(validate => {
        if (validate) {
          this.submit();
        } else {
          return false;
        }
      });
    },
    openDialog(role = undefined, admin, index = undefined) {
      this.index = index;
      if (admin === true) {
        this.admin = false;
      } else {
        this.admin = true;
      }
      if (role) {
        this.role = this.deepClone(role);
        this.editFlag = true;
        this.params.title = "编辑角色";
        this.params.visible = true;
        return;
      }
      this.params.title = "新增角色";
      this.editFlag = false;
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.$refs.roleForm.resetFields();
      this.params.visible = false;
    },
    clearData() {
      this.admin = false;
      this.role = {
        roleId: "",
        roleName: "",
        descript: "",
        admin: false,
        cachedValue: false
      };
    },
    submit() {
      this.btnState = true;
      // 去掉空值提交
      this.$fetch(inter.role.save, this.role, {
        headers: { "Content-Type": "application/json" }
      }).then(res => {
        this.btnState = false;
        if (res && res.success) {
          this.$emit("reload", res.data, this.index);
          this.closeDialog();
        } else {
          this.$message({ type: "error", message: res.message });
        }
      });
    }
  }
};
</script>

<style lang="less"></style>
