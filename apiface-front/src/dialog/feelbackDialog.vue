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
    class="feelback-dialog"
  >
    <el-form ref="reform" :model="mod" :rules="formRules" label-width="75px" label-suffix=":">
      <el-form-item label="错误描述" prop="description">
        <el-input
          :maxlength="1000"
          placeholder="错误描述"
          v-model="mod.description"
          type="textarea"
          :rows="7"
        ></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="contact">
        <el-input
          :maxlength="300"
          :disabled="mod.mode != '0'"
          placeholder="联系方式"
          v-model="mod.contact"
          auto-complete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="通知方式">
        <el-radio class="radio" v-model="mod.mode" label="0">邮箱</el-radio>
        <el-radio class="radio" v-model="mod.mode" label="1">开发者(系统)</el-radio>
        <el-radio class="radio" v-model="mod.mode" label="2">管理者(系统)</el-radio>
        <el-tooltip effect="light" :content="'系统默认能通知到具体开发者:' + contact" placement="top-start">
          <i class="fa fa-question-circle" v-show="mod.mode == '1'"></i>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="内容" prop="data">
        <el-tag type="success" v-for="(val, key, index) in mod.data" :key="index">{{ info[key] }}</el-tag>
      </el-form-item>
    </el-form>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button class="btn-item" type="cancle" @click="closeDialog">{{ $self("cancle") }}</u-button>
        <u-button
          class="btn-item"
          type="submit"
          @click="saveData"
          :disabled="btnState"
        >{{ $self("save") }}</u-button>
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
  name: "feelbackDialog",
  components: {
    DialogBox,
    uButton
  },
  data() {
    return {
      page: "dialog.conf.",
      params: {
        title: "错误反馈",
        width: "550px",
        height: "380px",
        visible: false
      },
      index: null,
      info: {
        data: "内容",
        status: "状态",
        request: "请求参数",
        response: "响应参数",
        demo: "调试参数",
        head: "请求头",
        cookie: "COOKIE",
        auth: "权限"
      },
      contact: "",
      mod: {
        docId: "",
        path: "",
        summary: "",
        description: "",
        contact: "",
        mode: "1", //联系方式，0 邮箱，1 系统-开发者，2 系统-管理员
        data: {}
      },
      btnState: false,
      formRules: {
        description: [
          { required: true, message: "请输入错误描述", trigger: "blur" },
          {
            min: 30,
            max: 1000,
            message: "长度为 30 到 1000 个字符",
            trigger: "blur"
          }
        ],
        contact: [
          { required: true, message: "请输入联系方式", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    feel(data, type) {
      this.mod.data[type] = data;
    },
    saveData() {
      this.$refs.reform.validate(validate => {
        if (validate) {
          this.submit();
        } else {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      var data = {
        docId: this.mod.docId,
        path: this.mod.path,
        summary: this.mod.summary,
        description: this.mod.description,
        contact: this.mod.contact,
        mode: this.mod.mode
      };
      if (this.mod.mode != "0") {
        data.contact = this.contact;
      }
      data.buffer = this.stringToByte(JSON.stringify(this.mod.data));
      this.$fetch(inter.back.save, data, {
        headers: { "Content-Type": "application/json" }
      }).then(res => {
        this.btnState = false;
        if (res && res.success) {
          this.closeDialog();
        } else {
          this.$message({ type: "error", message: res.message });
        }
      });
    },
    stringToByte(str) {
      var bytes = new Array();
      var len, c;
      len = str.length;
      for (var i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if (c >= 0x010000 && c <= 0x10ffff) {
          bytes.push(((c >> 18) & 0x07) | 0xf0);
          bytes.push(((c >> 12) & 0x3f) | 0x80);
          bytes.push(((c >> 6) & 0x3f) | 0x80);
          bytes.push((c & 0x3f) | 0x80);
        } else if (c >= 0x000800 && c <= 0x00ffff) {
          bytes.push(((c >> 12) & 0x0f) | 0xe0);
          bytes.push(((c >> 6) & 0x3f) | 0x80);
          bytes.push((c & 0x3f) | 0x80);
        } else if (c >= 0x000080 && c <= 0x0007ff) {
          bytes.push(((c >> 6) & 0x1f) | 0xc0);
          bytes.push((c & 0x3f) | 0x80);
        } else {
          bytes.push(c & 0xff);
        }
      }
      return bytes;
    },
    openDialog(contact, path, summary, docId) {
      this.mod.contact = contact;
      this.mod.path = path;
      this.mod.summary = summary;
      this.mod.docId = docId;
      this.contact = contact;
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.mod = {
        docId: "",
        path: "",
        summary: "",
        description: "",
        contact: "",
        mode: "1", //联系方式，0 邮箱，1 系统-开发者，2 系统-管理员
        data: {}
      };
    }
  }
};
</script>

<style lang="less">
.el-dialog__body {
  padding: 4px 6px !important;
  .el-form-item__content {
    .el-tag {
      margin-right: 3px;
    }
  }
}
</style>
