<!--
  用户编辑对话框
  作者：Daye Shing
-->
<template>
  <self-dialog
    :params="params"
    :title="$self('title')"
    :visible="params.visible"
    class="config-dialog"
    @before-close="closeDialog"
  >
    <el-tabs v-model="activeName" editable @edit="createData">
      <el-tab-pane :label="$self('tab1')" name="1">
        <datagrid :data="data.heads" style="width: 100%;">
          <el-table-column prop="enable" :label="$self('enable')" width="60" align="center">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.enable"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column prop="name" :label="$self('headname')" width="150" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.name" :placeholder="$self('input')"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="content" :label="$self('headc')" minWidth="120" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.content" :placeholder="$self('input')"></el-input>
            </template>
          </el-table-column>
          <el-table-column :label="$self('opt')" width="80" align="center">
            <template slot-scope="scope">
              <el-button
                @click.native.prevent="remove(scope.$index)"
                type="text"
                size="small"
              >{{ $self("del") }}</el-button>
            </template>
          </el-table-column>
        </datagrid>
      </el-tab-pane>
      <el-tab-pane :label="$self('tab2')" name="2">
        <datagrid :data="data.cookies" style="width: 100%;">
          <el-table-column prop="enable" :label="$self('enable')" width="60" align="center">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.enable"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="cookie" width="150" align="center">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.name"
                :disabled="scope.row.disabled"
                :placeholder="$self('input')"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="content" :label="$self('val')" minWidth="120" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.content" :placeholder="$self('input')"></el-input>
            </template>
          </el-table-column>
          <el-table-column :label="$self('opt')" width="80" align="center">
            <template slot-scope="scope">
              <el-button
                @click.native.prevent="remove(scope.$index)"
                type="text"
                size="small"
              >{{ $self("del") }}</el-button>
            </template>
          </el-table-column>
        </datagrid>
      </el-tab-pane>
      <el-tab-pane :label="$self('tab3')" name="3">
        <el-form label-width="80px" :model="data.auth" style="padding-top: 10px;">
          <el-form-item :label="$self('auth')">
            <el-select v-model="data.auth.type">
              <el-option label="None" value="none"></el-option>
              <el-option label="Basic" value="basic"></el-option>
              <el-option label="Bearer Basic" value="bearer"></el-option>
            </el-select>
          </el-form-item>
          <template v-if="data.auth.type == 'basic'">
            <el-form-item label="User">
              <el-input v-model="data.auth.user" :placeholder="$self('input')"></el-input>
            </el-form-item>
            <el-form-item label="Password">
              <el-input v-model="data.auth.password" :placeholder="$self('input')"></el-input>
            </el-form-item>
          </template>
          <el-form-item label="Header" v-if="data.auth.type == 'bearer'">
            <el-input v-model="data.auth.header" :placeholder="$self('input')"></el-input>
          </el-form-item>
          <el-form-item label="Token" v-if="data.auth.type == 'bearer'">
            <el-input v-model="data.auth.user" :placeholder="$self('input')"></el-input>
          </el-form-item>
          <el-form-item :label="$self('type')" v-if="data.auth.type == 'bearer'">
            <el-switch v-model="data.auth.use" :active-text="$self('active')"></el-switch>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <self-button class="btn-item" type="cancle" @click="closeDialog">
          {{
          $self("cancle")
          }}
        </self-button>
        <self-button
          class="btn-item"
          type="submit"
          @click="saveData"
          :disabled="btnState"
        >{{ $self("save") }}</self-button>
      </div>
    </div>
  </self-dialog>
</template>

<script type="es6">
import datagrid from "../compontents/datagrid";
import selfDialog from "../compontents/dialog/self-dialog";
import selfButton from "../compontents/button/self-button";
import $cookie from "js-cookie";
export default {
  name: "configDialog",
  components: {
    datagrid,
    "self-dialog": selfDialog,
    "self-button": selfButton
  },
  data() {
    return {
      page: "dialog.conf.",
      activeName: "1",
      params: {
        title: "",
        width: "700px",
        height: "460px",
        visible: false
      },
      data: {
        heads: [],
        cookies: [],
        auth: {
          use: false,
          type: "none",
          header: "Authorization",
          token: "",
          user: "",
          password: ""
        }
      },
      btnState: false
    };
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    createData() {
      if (this.activeName == "1") {
        this.data.heads.push({ enable: true, name: "", content: "" });
        return;
      }
      if (this.activeName == "2") {
        this.data.cookies.push({
          enable: true,
          name: "",
          content: "",
          disabled: false
        });
      }
    },
    remove(index) {
      if (this.activeName == "1") {
        this.data.heads.splice(index, 1);
        return;
      }
      if (this.activeName == "2") {
        // 删除cookie
        if (this.data.cookies[index].disabled) {
          $cookie.remove(this.data.cookies[index].name);
        }
        this.data.cookies.splice(index, 1);
      }
    },
    saveData() {
      for (var i = 0; i < this.data.cookies.length; i++) {
        if (this.data.cookies[i].enable && this.data.cookies[i].name != "") {
          this.data.cookies[i].disabled = true;
          $cookie.set(this.data.cookies[i].name, this.data.cookies[i].content);
        }
      }
      var heads = [];
      for (var i = 0; i < this.data.heads.length; i++) {
        if (this.data.heads[i].enable) {
          heads.push({
            name: this.data.heads[i].name,
            content: this.data.heads[i].content
          });
        }
      }
      this.$emit("commit", heads, this.deepClone(this.data.auth));
      this.closeDialog();
    },
    openDialog() {
      this.params.visible = true;
    },
    closeDialog() {
      // this.clearData();
      this.params.visible = false;
    },
    clearData() {},
    submit() {
      this.btnState = true;
      this.btnState = false;
    }
  }
};
</script>

<style lang="less">
.config-dialog {
  .el-dialog__body {
    padding: 4px 6px !important;
  }
  .datagrid {
    .el-table__body-wrapper {
      overflow: auto !important;
      height: 283px;
    }
  }
  .el-scrollbar__view {
    height: 98%;
  }
  .self-dialog .el-dialog__body .el-scrollbar__wrap {
    padding: 0px 10px !important;
  }
  .el-tabs {
    height: 100%;
    .el-tabs__header {
      margin: 0px !important;
      border-bottom: 1px solid #ebeef5;
      .el-tabs__new-tab {
        margin: 7px !important;
        border: 1px solid #1499e7 !important;
      }
      .el-icon-close {
        display: none;
      }
      .el-tabs__item {
        padding: 0 6px !important;
        height: 36px !important;
        line-height: 36px !important;
      }
    }
    .el-tabs__content {
      height: calc(~"100% - 45px");
      overflow: visible !important;
      .el-tab-pane {
        height: 100%;
      }
    }
    .el-tabs__nav-wrap::after {
      background-color: #f2f4f6 !important;
    }
  }
}
</style>
