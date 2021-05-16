<!--登录页面-->
<template>
  <div id="module-login">
    <div class="login-bg login-bg-tl"></div>
    <div class="login-bg login-bg-tr"></div>
    <div class="login-bg login-bg-bt"></div>
    <div class="login-contaniter">
      <div class="login-contaniter-header">
        <div class="title-cn">文档管理系统</div>
        <div class="title-en">Document Management</div>
      </div>

      <div class="login-form">
        <el-form
          ref="loginForm"
          :model="loginInfo"
          :rules="loginFormRules"
          v-if="type == 'login'"
        >
          <el-form-item prop="account">
            <el-input
              size="medium"
              placeholder="请输入账号/邮箱/电话"
              v-model="loginInfo.account"
              icon="a fa fa-user-o"
            >
              <template slot="prepend"> 账&nbsp;&nbsp;&nbsp;号</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              size="medium"
              type="password"
              placeholder="请输入密码"
              v-model="loginInfo.password"
              show-password
            >
              <template slot="prepend"> 密&nbsp;&nbsp;&nbsp;码</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="vacode">
            <el-row class="valid-code">
              <el-col :span="14">
                <el-input
                  size="medium"
                  placeholder="请输入验证码"
                  v-model="loginInfo.vacode"
                >
                  <template slot="prepend">验证码</template>
                </el-input>
              </el-col>
              <el-col :span="10">
                <div class="valid-img">
                  <img :src="vacodeUrl" @click="getCodeUrl(111, 34)" />
                </div>
              </el-col>
            </el-row>
          </el-form-item>

          <span class="forget-pass">
            <el-button type="text" style="float:right;" @click="forgetPass"
              >忘记密码</el-button
            >
          </span>
          <el-form-item>
            <el-button type="login" class="login-btn" @click="handleLogin"
              >登录</el-button
            >
          </el-form-item>

          <div class="txt1">
            <span>第三方登录</span>
          </div>

          <div class="flex-c-m">
            <a
              href="javascript:void()"
              @click="forgetPass"
              class="login100-social-item bg1"
            >
              <i class="fa fa-wechat"></i>
            </a>
            <a
              href="javascript:void()"
              @click="forgetPass"
              class="login100-social-item bg2"
            >
              <i class="fa fa-qq"></i>
            </a>
            <a
              href="javascript:void()"
              @click="forgetPass"
              class="login100-social-item bg3"
            >
              <i class="fa fa-weibo"></i>
            </a>
          </div>

          <div class="txt2">
            <span @click="authEvent">立即注册</span>
          </div>
        </el-form>
        <!-- 注册 -->
        <el-form
          ref="form"
          v-else
          :model="form[activeStep]"
          :rules="formFules[activeStep]"
          label-width="0px"
        >
          <el-form-item>
            <h2>
              {{
                activeStep == 0
                  ? "验证账号"
                  : activeStep == 1
                  ? "填写信息"
                  : "填写密码"
              }}
            </h2>
          </el-form-item>
          <template v-if="activeStep == 0">
            <el-form-item prop="mobile" :error="form[activeStep].mobileError">
              <el-input
                placeholder="请输入联系电话"
                size="medium"
                v-model="form[activeStep].mobile"
              >
                <template slot="prepend">联系电话</template>
              </el-input>
            </el-form-item>
            <el-form-item prop="email" :error="form[activeStep].emailError">
              <el-input
                placeholder="请输入电子邮箱"
                size="medium"
                v-model="form[activeStep].email"
              >
                <template slot="prepend">电子邮箱</template>
              </el-input>
            </el-form-item>

            <el-form-item>
              <div
                class="form-item-getcode"
                @click="validCode"
                v-if="!isValied"
              >
                点击按钮进行验证
              </div>
              <el-input
                placeholder="请输入邮箱验证码"
                size="medium"
                v-if="isValied"
                v-model="form[activeStep].vscode"
              >
                <template slot="prepend"
                  >验&nbsp;&nbsp;证&nbsp;&nbsp;码</template
                >
              </el-input>
            </el-form-item>
          </template>
          <template v-if="activeStep == 1">
            <el-form-item label-width="74px" class="item-avatar">
              <div class="item-avatar-img">
                <el-avatar :size="50" :src="iconUrl">
                  <img src="../assets/icons/api.png" />
                </el-avatar>

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
                    <bda-button type="primary">上传头像</bda-button>
                  </el-upload>
                </div>
              </div>
            </el-form-item>
            <el-form-item prop="userName">
              <el-input
                v-model="form[activeStep].userName"
                placeholder="请输入用户名"
                size="medium"
                auto-complete="off"
              >
                <template slot="prepend"
                  >用&nbsp;&nbsp;户&nbsp;&nbsp;名</template
                >
              </el-input>
            </el-form-item>
            <el-form-item>
              <div class="el-input-group__prepend fl">
                性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别
              </div>
              <div class="radio_content fl">
                <el-radio
                  class="radio"
                  v-model="form[activeStep].gender"
                  label="1"
                  >男</el-radio
                >
                <el-radio
                  class="radio"
                  v-model="form[activeStep].gender"
                  label="0"
                  >女</el-radio
                >
                <el-radio
                  class="radio"
                  v-model="form[activeStep].gender"
                  label="2"
                  >保密</el-radio
                >
                <el-radio
                  class="radio"
                  v-model="form[activeStep].gender"
                  label="3"
                  >第3性</el-radio
                >
              </div>
            </el-form-item>
          </template>
          <template v-if="activeStep == 2">
            <el-form-item prop="pwd">
              <el-input
                placeholder="请输入密码"
                size="medium"
                type="password"
                show-password
                @blur="blurEvent"
                v-model="form[activeStep].pwd"
              >
                <template slot="prepend"
                  >密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</template
                >
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPwd">
              <el-input
                placeholder="请输入确认密码"
                size="medium"
                type="password"
                show-password
                @blur="blurEvent"
                v-model="form[activeStep].confirmPwd"
              >
                <template slot="prepend">确认密码</template>
              </el-input>
            </el-form-item>
          </template>
          <el-form-item>
            <el-button
              type="login"
              class="login-btn"
              :disabled="btnState && isValied"
              @click="handleNext(activeStep)"
              >{{ active < 2 ? "下一步" : "提 交" }}</el-button
            >
          </el-form-item>
          <div class="txt2">
            <span @click="authEvent">已有账号</span>
          </div>
        </el-form>
        <Vcode
          :show="isShow"
          :sliderSize="20"
          @close="closeValid"
          @success="onSuccess"
        >
        </Vcode>
      </div>
    </div>
  </div>
</template>

<script type="es6">
import { inter } from "api/interface";
import Qs from "qs";
import Crypto from 'crypto'
import Vcode from "vue-puzzle-vcode";
export default {
  name: "auth",
  components: {
    Vcode,
  },
  data () {
    return {
      showLogo: false,
      loginInfo: {
        account: '',
        password: '',
        vacode: ''
      },
      vacodeUrl: "",
      type:"login",
      loginFormRules: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" },
          // { validator: formRules.checkSpace, trigger: 'blur' }
        ],
        vacode: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          // { validator: formRules.checkSpace, trigger: 'blur' }
        ]
      },
      formFules: [
        {
          vscode: [{ required: true, message: "请输入邮箱验证码", trigger: "blur" }],
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
                if (!isok)                {
                  callback(new Error("邮箱格式不正确！"));
                } else                {
                  callback();
                }
              },
              trigger: "blur"
            }
          ],
          mobile: [
            { required: true, message: "请输入您的手机号码", trigger: "blur" },
            { min: 11, max: 11, message: "长度为11个号码", trigger: "blur" }
          ],
        },
        {
          userName: [
            { required: true, message: "请输入用户名称", trigger: "blur" },
            { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" }
          ]
        }, {
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
                if (value !== this.form[2].pwd)                {
                  callback(new Error("两次输入密码不一致!"));
                } else                {
                  callback();
                }
              },
              trigger: "blur"
            }
          ]
        }
      ],
      form: [
        {
          email: "",
          mobile: "",
          vscode: '',
          messageType: 'email',
          emailError: '',
          mobileError: '',
        }, {
          userName: "",
          gender: "2",
          userLogo: "https://www.17sucai.com/preview/1673365/2019-05-28/sj/images/resources/user-pic.png"
        },
        {
          pwd: "",
          confirmPwd: "",
        }
      ],
      active: 0,
      activeStep: 0,
      btnState: false,
      isValied: false,
      isShow: false,
      uploadUrl: inter.io.upload,
    };
  },
  computed: {
    iconUrl () {
      let icon = "";
      if (this.form[1].userLogo)      {
        icon = this.form[1].userLogo;
      }
      return icon;
    }
  },
  watch: {
    $route() {
      // 带路由参数变化
      this.changeType(this.$route.params.type);
    }
  },
  created() {
    // 带路由参数
    this.changeType(this.$route.params.type);
  },
  mounted () {
    this.getCodeUrl(111, 34);
  },
  methods: {
    authEvent(){
      if(this.type == 'regist'){
        this.type = 'login';
        return;
      }
      this.type = 'regist';
    },
    changeType(type){
      if(type == 'regist'){
        this.type = 'regist';
        return;
      }
      this.type = 'login';
    },
    getCodeUrl (width, height) {
      this.loginInfo.vacode = "";
      this.vacodeUrl = inter.auth.code + "?width=" + width + "&height=" + height + "&d=" + Math.random();
    },
    forgetPass () {
      this.$message({
        type: 'warning',
        message: '持续开发中'
      })
    },
    handleLogin: function () {
      var $this = this;
      this.$refs['loginForm'].validate((valid) => {
        if (valid)        {
          $this.$fetch(inter.auth.signIn, Qs.stringify({
            userName: $this.loginInfo.account.trim(),
            password: Crypto.createHash('sha256').update($this.loginInfo.password.trim()).digest('hex'),
            vacode: $this.loginInfo.vacode.trim(),
            realm: "realm"
          })).then(res => {
            if (res.success)            {
              $this.$router.push('/index');
            } else            {
              $this.getCodeUrl(111, 34);
              $this.$message({
                type: 'error',
                message: res.message
              })
            }
          })
        } else        {
          console.log('error submit!!');
          return false;
        }
      });
    },
    //==============注册
    blurEvent () {
      if (this.form[2].pwd == this.form[2].confirmPwd && this.form[2].pwd != '')      {
        this.active = 3;
      } else      {
        this.active = 2;
      }
    },
    handleNext (active) {
      this.btnState = true;
      this.$refs.form.validate(valid => {
        if (valid)        {
          //校验邮箱验证码
          if (this.activeStep == 0)          {
            if (!this.isValied)            {
              this.btnState = false;
              this.$message.error("请点击按钮进行验证");
              return;
            }
            if (this.form[0].vscode == '')            {
              this.btnState = false;
              this.$message.error("请点验证码");
              return;
            }
            this.$fetch(
              inter.auth.validCode,
              Qs.stringify({ vscode: this.form[0].vscode, email: this.form[0].email })
            ).then(res => {
              this.btnState = false;
              if (res.success)              {
                this.active++;
                this.activeStep++;
              } else              {
                this.$message.error(res.message);
              }
            });
            return true;
          }
          if (this.activeStep < 2)          {
            this.active++;
            this.activeStep++;
            this.btnState = false;
          } else          {
            Crypto.createHash("sha256").update(this.form[2].pwd.trim()).digest("hex")
            var json = {
              userId: '',
              userName: this.form[1].userName,
              account: this.form[0].mobile,
              pwd: Crypto.createHash("sha256").update(this.form[2].pwd.trim()).digest("hex"),
              confirmPwd: Crypto.createHash("sha256").update(this.form[2].confirmPwd.trim()).digest("hex"),
              email: this.form[0].email,
              gender: this.form[1].gender,
              userLogo: this.form[1].userLogo,
              mobile: this.form[0].mobile,
              vscode: this.form[0].vscode
            };
            this.$fetch(inter.auth.modifyUser, json, {
              headers: { "Content-Type": "application/json" }
            }).then(res => {
              this.btnState = false;
              if (res && res.success)              {
                this.type = 'login';
                // 跳转登陆
              } else              {
                this.$message.error(res.message);
              }
            })
              .catch(err => { this.btnState = false; });
          }
        } else        {
          this.$message({ type: "warning", message: "请完善信息" });
          return false;
        }
      });
    },
    closeValid () {
      // this.isShow = false;
    },
    validCode () {
      this.$refs.form.validate(valid => {
        if (valid)        {
          this.form[0].emailError = '';
          this.form[0].mobileError = '';
          this.isShow = true;
        } else        {
          this.$message({ type: "warning", message: "请完善信息" });
          return false;
        }
      });
    },
    onSuccess (msg) {
      var json = {
        email: this.form[0].email,
        mobile: this.form[0].mobile,
        messageType: 'email',
      };
      this.$fetch(inter.auth.validUser, json, {
        headers: { "Content-Type": "application/json" }
      }).then(res => {
        this.isShow = false;
        if (res && res.success)        {
          if (res.data.notExist)          {
            this.$message.error('账号已经存在');
            this.$nextTick(() => {
              this.form[0].emailError = res.data.emailError;
              this.form[0].mobileError = res.data.mobileError;
            });
          } else          {
            this.isValied = true;
          }
        } else        {
          this.$message.error(res.message);
        }
      }).catch(err => { this.isShow = false; });
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
#module-login {
  height: 100%;
  min-height: 600px;
  background-color: #1fc8db;
  background-image: linear-gradient(
    141deg,
    #9fb8ad 0%,
    #1fc8db 51%,
    #2cb5e8 75%
  );
  overflow: hidden;
  position: relative;
  .login-bg-bt {
    background-color: rgba(255, 255, 255, 0.1);
    left: 0%;
    width: 434px;
    height: 273px;
    bottom: -192px;
  }
  .login-bg-tl {
    background-color: rgba(255, 255, 255, 0.1);
    left: -349px;
    width: 434px;
    height: 339px;
  }
  .login-bg-tr {
    background-color: rgba(0, 0, 0, 0.03);
    right: -351px;
    width: 473px;
    top: 64px;
    height: 565px;
  }
  .login-bg {
    border-radius: 6px;
    position: absolute;
    -webkit-transform: rotate(338deg);
    -moz-transform: rotate(338deg);
    -o-transform: rotate(338deg);
    transform: rotate(338deg);
    z-index: 1;
  }
  .forget-pass {
    height: 0px;
    position: relative;
    display: flex;
    right: -218px;
    top: -25px;
    .el-button--text {
      line-height: 25px;
      padding: 0px !important;
    }
  }
  .el-input-group__prepend {
    width: 69px !important;
    padding: 0 10px !important;
    line-height: 34px;
    color: #909399;
    font-size: 12px;
  }
  .valid-code {
    .valid-img {
      height: 36px;
      border: 1px solid #cbcbcb;
      border-left: none;
      img {
        width: 100%;
        height: 34px;
        vertical-align: middle;
        cursor: pointer;
      }
    }
  }

  .login-logo {
    position: absolute;
    left: 30px;
    top: 30px;
    width: 200px;
    img {
      width: 100%;
    }
  }
  .login-contaniter {
    position: relative;
    top: 28%;
    margin: 0 auto;
    width: 350px;
    background: #fff;
    border-radius: 8px;
    z-index: 3;
    .login-contaniter-header {
      color: #fff;
      position: absolute;
      top: -46px;
      left: 50%;
      transform: translate(-50%, 0);
      text-align: center;
      .title-cn {
        font-size: 20px;
        font-weight: bold;
        line-height: 1;
        margin-bottom: 8px;
      }
      .title-en {
        font-size: 12px;
        line-height: 1;
      }
    }
    .item-avatar {
      .el-form-item__content {
        height: 36px;
        line-height: 36px;
        position: relative;

        .item-avatar-img {
          position: absolute;
          height: 50px;
          width: 50px;
          top: -7px;
          margin-left: 8px;
          .icon-btn {
            position: absolute;
            top: 12px;
            left: 65px;
            font-size: 0;
            .upload-demo {
              float: right;
            }
          }
        }
      }

      .el-form-item__label {
        color: #909399 !important;
        font-size: 12px !important;
        line-height: 36px !important;
        font-weight: 400 !important;
      }
    }
    .radio_content {
      width: 201px;
      height: 35px;
      border: 1px solid #dcdfe6;
      .el-radio {
        line-height: 34px !important;
        margin-right: 0px !important;
        margin-left: 8px;
      }
    }
    .login-form {
      padding: 20px;
      .el-form {
        padding: 10px 20px 0px 20px;
        .el-input__inner {
          background-color: #fafafa !important;
          border-color: #cbcbcb !important;
          color: #898989 !important;
          border-radius: 0px;
        }
        .el-form-item__error {
          padding-top: 5px !important;
          padding-left: 60px;
        }
      }
      .fa-lock {
        font-size: 18px;
      }
      .el-form-item {
        margin-bottom: 25px;
        h2 {
          text-align: center;
          color: #666;
        }
      }
      .login-btn {
        font-size: 16px;
        height: 40px;
        width: 100%;
        color: #f2f2f2 !important;
        background: #2cb5e8;
        border: none;
      }
      .form-item-getcode {
        line-height: 34px;
        text-align: center;
        font-size: 14px;
        color: #333;
        border: solid 1px #ddd;
        cursor: pointer;
        z-index: 2;
      }
      .form-item-getcode:hover {
        border-color: #666;
      }
    }
  }
}

.login100-social-item {
  font-size: 16px;
  color: #fff;

  display: -webkit-box;
  display: -webkit-flex;
  display: -moz-box;
  display: -ms-flexbox;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin: 0px 6px;
}
.flex-c-m {
  display: -webkit-box;
  display: -webkit-flex;
  display: -moz-box;
  display: -ms-flexbox;
  display: flex;
  justify-content: center;
  -ms-align-items: center;
  align-items: center;
}
.login100-social-item:hover {
  color: #fff;
  background-color: #333333;
}
.bg1 {
  background-color: #32c577;
}
.bg2 {
  background-color: #1da1f2;
}
.bg3 {
  background-color: #ea4335;
}
.txt1 {
  font-family: "Microsoft Yahei";
  font-size: 12px;
  line-height: 1.5;
  color: #666666;
  padding-bottom: 10px;
  text-align: center;
}

.txt2 {
  padding-top: 10px;
  font-family: "Microsoft Yahei";
  font-size: 12px;
  line-height: 1.5;
  color: #333333;
  text-align: center;
  text-transform: uppercase;
}
.txt2 > span:hover {
  cursor: pointer;
  color: #a64bf4;
}
</style>
