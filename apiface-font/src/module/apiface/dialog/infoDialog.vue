<!--
  用户编辑对话框
  作者：Daye Shing
-->
<template>
  <self-dialog
    :params="params"
    :title="$self('title')"
    :visible="params.visible"
    @before-close="closeDialog"
    class="version"
  >
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="block-title-left"></span>
        <span>{{ $self("addr") }}</span>
      </div>
      <div>
        <el-link
          :href="data.protocol + data.host + data.basePath"
          type="primary"
          :underline="false"
          target="_blank"
          >{{ data.protocol + data.host + data.basePath }}</el-link
        >
        <span style="float: right; font-size: 12px; color: #999;"
          >{{ $self("total") }}{{ data.total }}{{ $self("item") }}</span
        >
      </div>
    </el-card>

    <el-card class="box-card" style="margin-top: 10px;">
      <div slot="header" class="clearfix">
        <span class="block-title-left"></span>
        <span>{{ $self("info") }}</span>
      </div>
      <el-row>
        <el-col :span="12">
          <span class="left-field">{{ $self("t") }}</span>
          {{ data.info.title }}
        </el-col>

        <el-col :span="12">
          <span class="left-field">{{ $self("v") }}</span>
          {{ data.swagger }}
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <span class="left-field">{{ $self("d") }}</span>
          {{ data.info.description }}
        </el-col>
      </el-row>
    </el-card>
    <el-card class="box-card" style="margin-top: 10px;">
      <div slot="header" class="clearfix">
        <span class="block-title-left"></span>
        <span>{{ $self("auther") }}</span>
      </div>
      <el-row>
        <el-col :span="12">
          <span class="left-field">{{ $self("a") }}</span>
          {{ data.info.contact.name }}
        </el-col>
        <el-col :span="12">
          <span class="left-field">{{ $self("e") }}</span>
          {{ data.info.contact.email }}
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <span class="left-field">{{ $self("p") }}</span>
          <a target="_blank" :href="contactUrl">{{ data.info.contact.url }}</a>
        </el-col>
        <el-col :span="12">
          <span class="left-field">{{ $self("tm") }}</span>
          <a target="_blank" :href="contactUrl">{{
            data.info.termsOfService
          }}</a>
        </el-col>
      </el-row>
    </el-card>
    <el-card class="box-card" style="margin-top: 10px;">
      <div slot="header" class="clearfix">
        <span class="block-title-left"></span>
        <span>{{ $self("lecuce") }}</span>
      </div>
      <el-row>
        <el-col :span="12">
          <span class="left-field">{{ $self("l") }}</span>
          {{ data.info.license.name }}
        </el-col>
        <el-col :span="12">
          <span class="left-field">{{ $self("la") }}</span>
          {{ data.info.license.url }}
        </el-col>
      </el-row>
    </el-card>
  </self-dialog>
</template>

<script type="es6">
import selfDialog from "../compontents/dialog/self-dialog";
export default {
  name: "infomationDialog",
  components: {
    "self-dialog": selfDialog
  },
  data() {
    return {
      page: "dialog.info.",
      params: {
        title: "",
        width: "600px",
        height: "540px",
        visible: false
      },
      data: {
        swagger: "",
        info: {
          description: "",
          version: "",
          title: "",
          termsOfService: "",
          contact: {
            name: "",
            url: "",
            email: ""
          },
          license: {
            name: "",
            url: ""
          }
        },
        host: "",
        basePath: "",
        protocol: "",
        total: 0
      }
    };
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    openDialog(data) {
      this.data = data;
      this.params.visible = true;
    },
    closeDialog() {
      this.params.visible = false;
    }
  }
};
</script>

<style lang="less">
.version {
  .el-dialog__body {
    padding: 4px 6px !important;
  }

  .el-card__header {
    padding: 6px 10px;
  }
  .el-card__body {
    padding: 12px;
    line-height: 26px;
  }
  .left-field {
    font-size: 14px;
    font-weight: 600;
    padding-right: 5px;
  }
  .block-title-left {
    width: 3px;
    height: 15px;
    margin-top: -2px;
    background: #1379d2;
    border-radius: 25%;
    display: inline-block;
    margin-right: 5px;
    vertical-align: middle;
  }
}
</style>
