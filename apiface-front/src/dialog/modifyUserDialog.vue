<!--
  用户编辑对话框
  作者：Daye Shing
-->
<template>
  <dialog-box
    class="modifyUserDialog"
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <el-form
      :model="user"
      :rules="userFormFules"
      ref="userForm"
      label-suffix=":"
      label-width="80px"
      class="user-form"
      :class="{ 'edit-password': params.pwd }"
    >
      <template v-if="!params.pwd">
        <el-form-item label>
          <div class="user-img">
            <img :src="iconUrl" v-if="user.userLogo" />
            <img src="../assets/icons/api.png" v-else />
          </div>
          <div class="icon-btn">
            <el-upload
              class="upload-demo"
              :action="uploadUrl"
              :before-upload="beforeAvatarUpload"
              :limit="1"
              :show-file-list="false"
              list-type="picture"
              :on-success="onIconUploadSuccess"
            >
              <u-button type="primary">上传头像</u-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-input
            v-model="user.account"
            :disabled="user.userId != ''"
            placeholder="请输入账号"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input
            v-model="user.userName"
            placeholder="请输入用户名"
            auto-complete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input
            type="email"
            v-model="user.email"
            placeholder="请输入电子邮箱"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="mobile">
          <el-input
            type="mobile"
            v-model="user.mobile"
            placeholder="请输入联系电话"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio class="radio" v-model="user.gender" label="1">男</el-radio>
          <el-radio class="radio" v-model="user.gender" label="0">女</el-radio>
          <el-radio class="radio" v-model="user.gender" label="2"
            >保密</el-radio
          >
          <el-radio class="radio" v-model="user.gender" label="3"
            >第3性</el-radio
          >
        </el-form-item>
      </template>
      <el-form-item label="旧密码" prop="oldPwd" v-if="params.pwd">
        <el-input
          type="password"
          v-model="user.oldPwd"
          placeholder="请输入密码"
          auto-complete="off"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item
        label="密码"
        prop="pwd"
        v-if="user.userId == '' || params.pwd"
      >
        <el-input
          type="password"
          v-model="user.pwd"
          placeholder="请输入密码"
          auto-complete="off"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item
        label="确认密码"
        prop="confirmPwd"
        v-if="user.userId == '' || params.pwd"
      >
        <el-input
          type="password"
          v-model="user.confirmPwd"
          placeholder="请确认密码"
          auto-complete="off"
          show-password
        ></el-input>
      </el-form-item>
    </el-form>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button class="btn-item" type="cancle" @click="closeDialog"
          >取消</u-button
        >
        <u-button
          class="btn-item"
          type="submit"
          @click="saveUser"
          :disabled="btnState"
          >保存</u-button
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
import Crypto from "crypto";
export default {
  components: {DialogBox,uButton},
  data () {
    return {
      params: {
        title: "注册用户",
        width: "400px",
        height: "520px",
        visible: false,
        pwd: true
      },
      uploadUrl: inter.io.upload,
      user: {
        userId: "",
        userName: "",
        account: "",

        oldPwd: "",
        pwd: "",
        confirmPwd: "",

        email: "",
        gender: "2",
        userLogo: "",
        mobile: ""
      },
      userFormFules: {
        userName: [
          { required: true, message: "请输入用户名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" }
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
          { required: true, message: "请输入用户邮箱", trigger: "blur" },
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
        gender: [{ required: true, message: "请选择性别", trigger: "change" }],
        oldPwd: [{ required: true, message: "请输入旧密码", trigger: "blur" }],
        mobile: [
          { required: true, message: "请输入您的手机号码", trigger: "blur" },
          { min: 11, max: 11, message: "长度为11个号码", trigger: "blur" }
        ],

      },
      btnState: false
    };
  },
  computed: {
    iconUrl () {
      let icon = "";
      if (this.user.userLogo)      {
        icon = this.user.userLogo;
      }
      return icon;
    }
  },
  methods: {
    saveUser () {
      this.btnState = true;
      this.$refs.userForm.validate(valid => {
        if (valid)        {
          this.submit();
        } else        {
          this.btnState = false;
          return false;
        }
      });
    },
    openDialog (userId, pwd = false) {
      this.params.pwd = pwd;
      if (pwd)      {
        this.user.userId = userId;
        this.params.title = "修改密码";
        this.params.visible = true;
        return;
      }
      if (userId)      {
        this.$fetch(
          inter.user.getUser,
          Qs.stringify({ userId: userId })
        ).then(res => {
          if (res.success)          {
            this.params.title = "修改信息";
            this.params.visible = true;
            this.$nextTick(() => {
              this.user = res.data;
            });
          } else          {
            this.$message.error(res.message);
            this.params.title = "注册用户";
            this.params.visible = true;
          }
        });
        return;
      }
      this.params.title = "注册用户";
      this.params.visible = true;
    },
    closeDialog () {
      this.clearData();
      // this.$refs.userForm.resetFields();
      this.params.visible = false;
    },
    clearData () {
      this.user = {
        userId: "",
        userName: "",
        account: "",
        oldPwd: "",
        pwd: "",
        confirmPwd: "",
        email: "",
        gender: "2",
        userLogo: "",
        mobile: ""
      };
    },
    submit () {
      var saveUrl = inter.auth.modifyUser;
      var json = this.user;
      if (this.params.pwd)      {
        saveUrl = inter.auth.cypher;
        json = {
          userId: json.userId,
          oldPwd: Crypto.createHash("sha256")
            .update(json.oldPwd.trim())
            .digest("hex"),
          confirmPwd: Crypto.createHash("sha256")
            .update(json.confirmPwd.trim())
            .digest("hex"),
          pwd: Crypto.createHash("sha256")
            .update(json.pwd.trim())
            .digest("hex")
        };
      }
      if (!this.user.userId)      {
        json.pwd = Crypto.createHash("sha256")
          .update(json.pwd.trim())
          .digest("hex");
      }
      this.$fetch(saveUrl, json, {
        headers: { "Content-Type": "application/json" }
      })
        .then(res => {
          this.btnState = false;
          if (res && res.success)          {
            if (
              this.user.userId &&
              this.user.userId != "-3" &&
              !this.params.pwd
            )            {
              setTimeout(() => {
                this.$emit("reload");
              }, 100);
            }
            if (this.params.pwd && res.data > 0)            {
              this.$message({ type: "success", message: "密码修改成功" });
              this.closeDialog();
            } else if (this.params.pwd)            {
              this.$message.error("密码修改失败");
            } else            {
              this.closeDialog();
            }
          } else          {
            if (this.params.pwd)            {
              this.$message.error("密码修改失败,原因:" + res.message);
            } else            {
              this.$message.error(res.message);
            }
          }
        })
        .catch(err => { });
    },
    /**
     * 控制图片格式
     */
    beforeAvatarUpload: function (file) {
      var $this = this;
      const isIMG =
        file.type === "image/jpeg" ||
        file.type === "image/png" ||
        file.type === "image/gif" ||
        file.type === "image/svg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isIMG)      {
        $this.$message.error("上传图片只能是 JPG/png/gif/svg 格式!");
      }
      if (!isLt2M)      {
        $this.$message.error("上传图片大小不能超过 2MB!");
      }
      return isIMG && isLt2M;
    },
    /**在图标上传成功的时候*/
    onIconUploadSuccess: function (res, file, fileList) {
      if (res.success)      {
        this.user.userLogo = res.data;
      } else      {
        this.$message.error(res.message);
      }
    }
  }
};
</script>

<style lang="less">
.modifyUserDialog {
  .edit-password {
    padding-top: 60px !important;
  }
  .user-form {
    padding: 6px 50px 0 40px;
    .el-select {
      width: 100%;
    }
    .user-img {
      float: left;
      width: 70px;
      height: 70px;
      border-radius: 50%;
      overflow: hidden;
      background: #ffffff;
      border: 1px solid #cbcbcb;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .icon-btn {
      float: left;
      margin: 23px 0;
      font-size: 0;
      margin-left: 10px;
      .u-button {
        margin-left: 10px;
      }
      .upload-demo {
        float: right;
      }
    }
    &:first-child {
      position: relative;
    }
  }
}
</style>
