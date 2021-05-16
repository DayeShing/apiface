<!--
  用户编辑对话框
  作者：Daye Shing
-->
<template>
  <dialog-box
    class="dialogs"
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <div class="step-progress">
      <div
        class="progress-item"
        :class="{ active: currentStep >= index + 1 }"
        v-for="(item, index) in info.titles"
        :key="index"
      >
        <div class="square">
          <span v-if="currentStep <= index + 1">{{ index + 1 }}</span>
          <i v-else class="el-icon-check"></i>
        </div>
        <p class="step-name">{{ item }}</p>
      </div>
    </div>
    <div class="select-auth" v-if="currentStep === 2">
      <datagrid :datagrid="datagrid" ref="pdatagrid">
        <el-table-column
          v-for="item in columns[this.select]"
          :key="item.prop"
          :class-name="item.className"
          :label-class-name="item.labelClassName"
          :width="item.width"
          :min-width="item.minWidth"
          :prop="item.prop"
          :label="item.label"
          :sortable="item.sortable"
          :header-align="item.headerAlign"
          :align="item.align == undefined ? 'center' : item.align"
          :formatter="item.formatter"
          :fixed="item.fixed"
          slot="first"
        ></el-table-column>

        <div slot="footbar">
          <search-input v-model="keyword" @search="reload" @clear="reload"></search-input>
        </div>
      </datagrid>
    </div>

    <div class="select-auth table-auth" v-if="currentStep === 3">
      <el-table :data="result" stripe style="width: 100%;min-height: 100%;border:1px solid #dddddd">
        <el-table-column label="资源名称" prop="entryName" width="150" align="center"></el-table-column>
        <el-table-column prop="tag" label-class-name="data-tag" label="权限">
          <template slot-scope="scope">
            <el-checkbox
              v-model="item.checked"
              v-for="(item, index) in scope.row.items"
              :key="index"
            >{{ item.grantName }}</el-checkbox>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 步骤一 -->
    <div class="select-box" v-if="currentStep === 1">
      <div class="select-demo">
        <p>
          {{
          select == "user"
          ? "用户授权"
          : select == "role"
          ? "角色授权"
          : "机构授权"
          }}
        </p>
        <div class="select-group">
          <el-radio v-model="select" label="user">
            <i class="fa fa-user"></i>
            <br />用户权限
          </el-radio>
          <el-radio v-model="select" label="role">
            <i class="fa fa-users"></i>
            <br />角色权限
          </el-radio>
          <el-radio v-model="select" label="org">
            <i class="fa fa-university"></i>
            <br />机构权限
          </el-radio>
        </div>
      </div>
      <ul class="select-tips">
        <li v-for="(item, index) in info.upload.tips" :key="index">{{ index + 1 }} . {{ item }}</li>
      </ul>
    </div>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button class="btn-item" type="cancle" @click="closeDialog">取消</u-button>
        <u-button
          class="btn-item"
          type="cancle"
          @click="backStep"
          :disabled="btnState"
          v-if="currentStep > 1"
        >返回</u-button>
        <u-button
          class="btn-item"
          type="submit"
          @click="nextStep"
          :disabled="btnState"
          v-if="currentStep < 3"
        >下一步</u-button>
        <u-button
          class="btn-item"
          type="submit"
          :disabled="btnState"
          @click="submitClick"
          v-if="currentStep == 3"
        >保存</u-button>
      </div>
    </div>
  </dialog-box>
</template>

<script type="es6">
import SearchInput from "components/search-input.vue";
import DialogBox from "components/dialog/dialogbox";
import uButton from "components/button/index";
import Datagrid from "components/table/datagrid.vue";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "permissions",
  components: {
    Datagrid,
    DialogBox,
    uButton,
    SearchInput
  },
  data() {
    return {
      currentStep: 1,
      params: {
        title: "数据授权",
        width: "800px",
        height: "530px",
        visible: false
      },
      select: "user",
      keyword: "",
      result: [],
      datagrid2: {
        url: "",
        lazy: true,
        selection: {
          showSelection: false
        },
        pagination: {
          showPagination: false,
          showMore: false
        }
      },
      datagrid: {
        url: "",
        lazy: true,
        selection: {
          showSelection: true
        },
        pagination: {
          showPagination: true,
          showMore: false
        }
      },
      columns: {
        user: [
          { label: "账号", prop: "account", minWidth: "150" },
          { label: "用户名", prop: "userName", minWidth: "150" },
          {
            label: "性别",
            prop: "gender",
            minWidth: "150",
            align: "center",
            formatter(row) {
              if (row.gender === "1") return "男";
              else if (row.gender === "0") return "女";
              else if (row.gender === "2") return "保密";
            }
          }
        ],
        role: [
          { label: "角色名称", prop: "roleName", minWidth: "150" },
          {
            label: "超级管理员",
            prop: "admin",
            minWidth: "150",
            align: "center",
            formatter(row) {
              if (row.admin) return "超级管理员";
              else return "普通管理员";
            }
          }
        ],
        org: [
          { label: "机构名称", prop: "orgName", minWidth: "150" },
          { label: "上级目录", prop: "upperName", minWidth: "150" },
          {
            label: "机构类型",
            prop: "orgType",
            minWidth: "150",
            align: "center",
            formatter(row) {
              if (row.orgType == "1") return "集团/顶级机构";
              else if (row.orgType == "2") return "机构";
              else return "职位";
            }
          }
        ]
      },
      conf: {
        user: inter.permitted.user,
        role: inter.permitted.role,
        org: inter.permitted.org,
        save: inter.permitted.save,
        list: inter.permitted.list
      },
      type: "",
      data: [],
      permission: [],
      info: {
        titles: ["授权方式", "权限关联", "资源选择"],
        upload: {
          selectFile: "选择文件",
          tips: [
            "用户授权--该用户将会有该类数据权限",
            "角色授权--角色对应的用户将会有该类数据权限",
            "机构授权--机构对应的用户将会有该类数据权限，并能看到直系上级以及所有下级的资源"
          ]
        },
        template: "导入模板下载",
        selectTips: "选择需要导入的信息",
        importTips: ["正在导入数据", "导入数据成功", "导入数据失败"]
      },
      btnState: false
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
    submitClick() {
      this.btnState = true;
      var req = [];
      var grantType =
        this.type +
        (this.select == "user" ? "/3" : this.select == "role" ? "/1" : "/2");
      for (var i = 0; i < this.result.length; i++) {
        var grantIds = [];
        for (var j = 0; j < this.result[i].items.length; j++) {
          if (this.result[i].items[j].checked) {
            grantIds.push(this.result[i].items[j].grantId);
          }
        }
        req.push({
          entryId: this.result[i].entryId,
          grantIds: grantIds
        });
      }
      this.$fetch(this.conf.save.replace(/\$\{type\}/g, grantType), req, {
        headers: { "Content-Type": "application/json" }
      })
        .then(res => {
          this.btnState = false;
          if (res && res.success) {
            this.$message({
              message: "成功保存",
              type: "success"
            });
          } else {
            this.$message({
              message: res.message,
              type: "error"
            });
          }
        })
        .catch(err => {
          this.btnState = false;
        });
    },
    reload() {
      this.$refs.pdatagrid.loadData({ keyword: this.keyword }, true);
    },
    backStep() {
      this.currentStep -= 1;
      if (this.currentStep == 2) {
        this.$nextTick(() => {
          this.reload();
        });
        return;
      }
    },
    nextStep() {
      if (this.currentStep == 1) {
        this.currentStep += 1;
        this.$nextTick(() => {
          this.datagrid.url = this.conf[this.select];
          this.$refs.pdatagrid.setConfig();
          this.reload();
        });
        return;
      }
      if (this.currentStep == 2) {
        var permission = this.getSelect();
        if (!permission || permission.length == 0) {
          this.$message({
            message: "请勾选授权对象",
            type: "warning"
          });
          return;
        }
        this.permitted(this.data, this.permission);
        return;
      }
    },
    permitted(data, permission) {
      var boo = [];
      for (var i = 0; i < data.length; i++) {
        for (var j = 0; j < permission.length; j++) {
          boo.push({
            grantType: permission[j].grantType,
            grantId: permission[j].grantId,
            grantName: permission[j].grantName,
            entryId: data[i].entryId,
            entryName: data[i].entryName
          });
        }
      }
      this.btnState = true;
      this.$fetch(this.conf.list.replace(/\$\{type\}/g, this.type), boo, {
        headers: { "Content-Type": "application/json" }
      })
        .then(res => {
          if (res && res.success) {
            this.btnState = false;
            this.currentStep += 1;
            this.$nextTick(() => {
              this.result = res.data;
            });
          } else {
            this.$message({
              message: res.message,
              type: "error"
            });
          }
        })
        .catch(err => {
          this.btnState = false;
        });
    },
    getSelect() {
      this.permission = [];
      let selectlist = this.$refs.pdatagrid.getSelection();
      if (selectlist.length > 0) {
        for (var i = 0; i < selectlist.length; i++) {
          if (this.select == "user") {
            this.permission.push({
              grantId: selectlist[i].userId,
              grantName: selectlist[i].userName,
              grantType: "3"
            });
          }
          if (this.select == "role") {
            this.permission.push({
              grantId: selectlist[i].roleId,
              grantName: selectlist[i].roleName,
              grantType: "1"
            });
          }
          if (this.select == "org") {
            this.permission.push({
              grantId: selectlist[i].orgId,
              grantName: selectlist[i].orgName,
              grantType: "2"
            });
          }
        }
      }
      return this.permission;
    },
    openDialog(type, data) {
      if (!data || data.length == 0) {
        this.$message({
          message: "请勾选授权数据",
          type: "warning"
        });
        return;
      }
      this.type = type;
      this.data = data;
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.currentStep = 1;
      this.type = "";
      this.data = [];
      this.permission = [];
    }
  }
};
</script>

<style lang="less">
.step-progress {
  padding: 15px 0px 20px 0px;
  text-align: center;
  .progress-item {
    display: inline-block;
    font-size: 0;
    margin-left: 160px;
    position: relative;
    .square {
      background-color: #ababab;
      width: 24px;
      height: 24px;
      margin: 0 auto;
      border-radius: 2px;
      span {
        font-size: 14px;
        line-height: 24px;
        font-weight: bold;
        color: #f2f2f2;
      }
      i {
        font-size: 14px;
        color: #f2f2f2;
        font-weight: bold;
        line-height: 24px;
      }
    }
    .step-name {
      color: #333333;
      font-weight: bold;
      font-size: 12px;
      margin-top: 10px;
      max-width: 48px;
      white-space: nowrap;
      position: relative;
    }
    &.active {
      .square {
        background-color: #1177e2;
        &:before {
          border-color: #1177e2;
        }
      }
    }
    &:before {
      content: "";
      position: absolute;
      left: -162px;
      top: 12px;
      width: 160px;
      height: 0;
      border-bottom: 1px dashed #cbcbcb;
    }
    &:first-child {
      margin-left: 0;
      // .step-name {
      //   left: -11px;
      // }
      &:before {
        display: none;
      }
    }
  }
}
.table-auth {
  overflow-y: auto;
  .data-tag {
    text-align: center;
  }
  .el-table th {
    height: 50px;
    padding: 3px 0;
    background-color: #e5e9ee;
  }
  .el-table th > .cell {
    color: #333333;
    font-size: 12px;
    font-weight: bold;
    overflow: hidden;
    word-break: keep-all;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .el-table td {
    padding: 3px 0;
    height: 37px;
    background-color: #f2f4f6;
    color: #666666;
    font-size: 12px;
  }

  .el-table .ascending .sort-caret.ascending,
  .el-table .descending .sort-caret.descending {
    color: #4b71a4;
  }
  .el-table td,
  .el-table th.is-leaf {
    border-bottom: 1px solid #dddddd;
  }
  .el-table--border td,
  .el-table--border th,
  .el-table__body-wrapper
    .el-table--border.is-scrolling-left
    ~ .el-table__fixed {
    border-right: 1px solid #dddddd;
  }
  .el-table--striped .el-table__body tr.el-table__row--striped td {
    background-color: #fcfcfc !important;
  }
  .el-table-column--selection .cell {
    text-overflow: initial !important;
    padding-left: 14px !important;
  }
}

.select-auth {
  height: 330px;
  padding: 0px 55px;
  .datagrid {
    .u-search-input {
      padding: 5px;
    }
  }
}
.select-box {
  padding: 0 50px 0px;
  position: relative;
  .select-demo {
    background-color: #fafafa;
    border: 1px solid #dddddd;
    border-radius: 2px;
    height: 220px;
    position: relative;
    & > p {
      text-align: center;
      padding-top: 20px;
    }
    .select-group {
      padding: 30px 60px 0px;
      .el-radio__input {
        display: none;
      }
      .el-radio__label {
        display: block;
        width: 186px;
        padding-left: 0px;
        text-align: center;
        i {
          font-size: 50px;
        }
      }
      .el-radio {
        line-height: 30px;
        margin-right: 0px;
      }
    }
  }
  .select-tips {
    padding-top: 15px;
    position: relative;
    li {
      font-size: 12px;
      color: #898989;
      line-height: 20px;
    }
  }
}
</style>
