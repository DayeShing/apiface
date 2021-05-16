<!--
  用户编辑对话框
  作者：Daye Shing
-->
<template>
  <dialog-box
    class="editOrgDialog"
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <el-form
      :model="org"
      :rules="formFules"
      ref="orgForm"
      label-suffix=":"
      label-width="80px"
      class="user-form"
    >
      <el-form-item label="机构名称" prop="orgName" v-if="org.orgType == '2'">
        <el-input
          :maxlength="30"
          placeholder="请输入集团/机构/部门名称"
          v-model="org.orgName"
          auto-complete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="职位名称" prop="orgName" v-else>
        <el-input
          :maxlength="30"
          placeholder="请输入职位名称"
          v-model="org.orgName"
          auto-complete="off"
        ></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="类型">
            <el-radio
              class="radio"
              v-model="org.orgType"
              v-if="org.orgType == '1'"
              label="1"
              >集团</el-radio
            >
            <el-radio class="radio" v-model="org.orgType" label="2" v-else
              >集团/机构/部门</el-radio
            >
            <el-radio class="radio" v-model="org.orgType" label="3"
              >职位</el-radio
            >
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="父机构" prop="upperId" style="width:100%">
            <select-tree
              :selectTree="selectTree"
              ref="selectTree"
              v-model="pOrg"
              @change="selectChange"
            ></select-tree>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="机构编码" prop="orgCode" v-if="org.orgType != '3'">
        <el-input
          :maxlength="50"
          placeholder="请输入机构编码"
          v-model="org.orgCode"
          auto-complete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="descript">
        <el-input
          :maxlength="300"
          placeholder="请输入描述"
          type="textarea"
          v-model="org.descript"
          :rows="4"
          auto-complete="off"
        ></el-input>
      </el-form-item>
    </el-form>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button class="btn-item" type="cancle" @click="closeDialog"
          >取消</u-button
        >
        <u-button class="btn-item" type="submit" @click="saveData"
          >保存</u-button
        >
      </div>
    </div>
  </dialog-box>
</template>

<script type="es6">

import SelectTree from "components/select/select-tree";
import DialogBox from "components/dialog/dialogbox";
import uButton from "components/button/index";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  components: {
    uButton,
    DialogBox,
    SelectTree
  },
  data () {
    return {
      info: {},
      params: {
        title: "新增机构",
        width: "530px",
        height: "350px",
        visible: false
      },
      pOrg: {

      },
      selectTree: {
        url: inter.org.tree,
        selectNodeType: "index",
        leafType: "child",
        headerNode: [{ entryId: "0", text: "全部机构", type: "index" }],
        placeholderText: "请选择父机构",
        includeRootNode: true,
        loadNodeAfterSearch: true
      },
      org: {
        orgId: "",
        orgType: "2",
        orgCode: "",
        orgName: "",
        descript: "",
        upperId: "",
      },
      formFules: {
        userName: [
          { required: true, message: "请输入用户名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" }
        ],
        code: [
          { required: true, message: "请选择密码方案", trigger: "change" }
        ],
        account: [
          { required: true, message: "请输入账户", trigger: "blur" },
          {
            min: 3,
            max: 30,
            message: "长度为 3 到 30 个字符",
            trigger: "blur"
          },
          {
            validator: (rule, value, callback) => {
              if (
                new RegExp(
                  /[`~\!@#\$%\^&*\+=<>?:"\{\}\|,\/;'\\·~！@#￥%&\*\+=|《》？：“”、；‘’\，。、…… ]/gim
                ).test(value)
              )              {
                callback(new Error("不能含有非法特殊字符"));
              } else              {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        mobile: [
          { min: 11, max: 11, message: "长度为11个号码", trigger: "blur" }
        ],
        pwd: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 8,
            max: 20,
            message: "长度为  8 到 20 个字符",
            trigger: "blur"
          }
        ],
        confirmPwd: [
          { required: true, message: "请输入确认密码", trigger: "blur" },
          {
            min: 8,
            max: 20,
            message: "长度为  8 到 20 个字符",
            trigger: "blur"
          },
          {
            validator: (rule, value, callback) => {
              if (value !== this.user.pwd)              {
                callback(new Error("两次输入密码不一致!"));
              } else              {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: "blur,change"
          },
          {
            validator: (rule, value, callback) => {
              var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/; //匹配邮箱
              var isok = reg.test(value);
              isok = value ? isok : true;
              if (!isok)              {
                callback(new Error("邮箱格式不正确！"));
              } else              {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        status: [
          { required: true, message: "请选择账户状态", trigger: "change" }
        ]
      }
    };
  },
  props: {
    dialog: {
      type: Object,
      default () {
        return {};
      }
    }
  },
  methods: {
    saveData () {
      this.$refs.orgForm.validate(valid => {
        if (valid)        {
          this.submit();
        } else        {
          return false;
        }
      });
    },
    setSelectTree (value, text) {
      if (!value)      {
        value = "0";
      }
      if (!text)      {
        text = "全部机构"
      }
      this.$nextTick(() => {
        this.$refs.selectTree.refreshTree();
        this.$refs.selectTree.setValue({ value: value, text: text })
      });
    },
    openDialog (org, node) {
      if (org)      {
        this.org = org;
        this.params.title = "编辑机构";
        this.params.visible = true;
        this.setSelectTree(org.upperId, org.upperName);
        return;
      }
      this.params.title = "新增机构";
      this.params.visible = true;
      this.org.upperId = node.entryId;
      this.setSelectTree(node.entryId, node.text);
    },
    closeDialog () {
      this.clearData();
      this.$refs.orgForm.resetFields();
      this.params.visible = false;
    },
    clearData () {
      this.org = {
        orgId: "",
        orgType: "2",
        orgCode: "",
        orgName: "",
        descript: "",
        upperId: ''
      };
      this.$refs.selectTree.clearValue();
    },
    selectChange (node) {
      if (node.entryId)      {
        this.org.upperId = node.entryId;
      }
    },
    submit () {
      this.btnState = true;
      this.$fetch(inter.org.save, this.org, {
        headers: { "Content-Type": "application/json" }
      }).then(res => {
        this.btnState = false;
        if (res && res.success)        {
          this.$emit("reload");
          this.closeDialog();
        } else        {
          this.$message({ type: "error", message: res.message });
        }
      }).catch(err => { });
    },
  }
};
</script>

<style lang="less">
// .editOrgDialog {

// }
</style>
