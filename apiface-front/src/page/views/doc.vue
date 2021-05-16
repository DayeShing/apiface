<template>
  <div class="doc-manager">
    <div class="doc-manager-left">
      <tree @onNodeClick="onClickTree" :tree="tree" ref="tree"></tree>
    </div>
    <div class="doc-manager-content">
      <div class="content-toolbar">
        <div class="fl btn-opt">
          <u-button
            type="build"
            @click="addGroup()"
            v-if="$store.state.permitteds[secretkey + 'group:add']"
            >新建分组</u-button
          >
          <u-button
            type="build"
            @click="editGroup()"
            v-if="$store.state.permitteds[secretkey + 'group:edit']"
            >编辑分组</u-button
          >
          <u-button
            type="delete"
            @click="removeGroup()"
            v-if="$store.state.permitteds[secretkey + 'group:delete']"
            >删除分组</u-button
          >
          <u-button
            type="build"
            @click="editDoc()"
            v-if="$store.state.permitteds[secretkey + 'add']"
            >新建文档</u-button
          >
          <u-button
            type="build"
            @click="permitted(true)"
            v-if="$store.state.permitteds[secretkey + 'authorize']"
            >授权</u-button
          >

          <el-upload
            class="fl"
            v-if="
              $store.state.permitteds[secretkey + 'uplaod'] && view == 'table'
            "
            :data="uploadParams"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <u-button type="build">点击上传</u-button>
          </el-upload>
        </div>

        <span class="fr view-btn">
          <i
            class="iconfont icon-iconfontsvgclassifyselect"
            v-if="view == 'table'"
            @click="view = 'view'"
          ></i>
          <i
            class="iconfont icon-list"
            v-if="view == 'view'"
            @click="view = 'table'"
          ></i>
        </span>
        <search-input
          class="fr"
          v-model="keyword"
          @search="reload"
          @clear="reload"
        ></search-input>
      </div>
      <transition name="el-fade-in" @enter="goIn">
        <datagrid
          class="content-main"
          :datagrid="datagrid"
          ref="datagrid"
          v-if="view == 'table'"
        >
          <el-table-column label="操作" width="200" slot="last" align="center">
            <template scope="scope">
              <div class="table-btn-group">
                <u-button
                  shape="text"
                  v-if="
                    $store.state.permitteds[secretkey + 'down'] &&
                      scope.row.status == '1'
                  "
                  @click="
                    publish(scope.row.docId, scope.$index, scope.row.status)
                  "
                  >下线</u-button
                >
                <u-button
                  shape="text"
                  v-if="
                    $store.state.permitteds[secretkey + 'publish'] &&
                      scope.row.status != '1'
                  "
                  @click="
                    publish(scope.row.docId, scope.$index, scope.row.status)
                  "
                  >发布</u-button
                >
                <u-button
                  shape="text"
                  @click="editDoc(scope.row, scope.$index)"
                  v-if="
                    $store.state.permitteds[secretkey + 'edit'] &&
                      scope.row.status != '1'
                  "
                  >编辑</u-button
                >
                <u-button
                  shape="text"
                  @click="deleteDoc(scope.row, scope.$index)"
                  v-if="
                    $store.state.permitteds[secretkey + 'delete'] &&
                      scope.row.status != '1'
                  "
                  >删除</u-button
                >
                <u-button shape="text" @click="openApi(scope.row.docId)"
                  >查看文档</u-button
                >
              </div>
            </template>
          </el-table-column>
          <div slot="footbar">
            <el-button
              type="text"
              size="mini"
              @click="deleteDoc()"
              style="color: #F56C6C;"
              v-if="$store.state.permitteds[secretkey + 'delete']"
              >删除文档</el-button
            >
          </div>
        </datagrid>
      </transition>
      <transition name="el-fade-in" @enter="goIn">
        <div
          v-if="view == 'view'"
          class="content-main"
          v-infinite-scroll="load"
          infinite-scroll-disabled="loading"
          infinite-scroll-distance="20"
        >
          <el-card
            shadow="hover"
            class="fl"
            v-for="(item, index) in docs"
            :key="index"
          >
            <div slot="header">
              <div
                class="title-view"
                :class="{
                  'title-green': item.status == '1',
                  'title-yellow': item.status == '0',
                  'title-red': item.status == '2',
                }"
                @click="openApi(item.docId)"
              >
                {{ item.text }}
              </div>
              <span
                title="发布"
                @click="publish(item.docId, index, item.status)"
                class="title-badge"
                v-if="
                  $store.state.permitteds[secretkey + 'publish'] &&
                    item.status != '1'
                "
                :class="
                  item.status == '1'
                    ? 'title-badge-yellow'
                    : 'title-badge-green'
                "
                >发布</span
              >
              <span
                title="下线"
                v-if="
                  $store.state.permitteds[secretkey + 'down'] &&
                    item.status == '1'
                "
                @click="publish(item.docId, index, item.status)"
                class="title-badge"
                :class="
                  item.status == '1'
                    ? 'title-badge-yellow'
                    : 'title-badge-green'
                "
                >下线</span
              >
            </div>
            <div class="content-info">
              <span class="content-info-title">访问地址</span>
              <span
                class="content-info-item content-info-addr"
                :title="item.addr"
                @click="openUrl(item.addr)"
                >{{ item.addr }}</span
              >
              <br />
              <span class="content-info-title">文档类型</span>
              <span class="content-info-item">
                {{ item.docType == "0" ? "swagger" : "apiface" }}
              </span>
              <br />
              <span class="content-info-title">开发团队</span>
              <span
                class="content-info-item content-info-addr"
                :title="item.termsUrl"
                @click="openUrl(item.termsUrl)"
                >{{ item.terms }}</span
              >
              <br />
              <span class="content-info-title">创建时间</span>
              <span class="content-info-item">{{ item.createTime }}</span>
            </div>
            <span class="content-foot">
              <i class="fa fa-code-fork"></i>
            </span>
            <span class="content-foot content-foot-gax">{{
              item.version
            }}</span>

            <span class="content-foot content-foot-gax fr">
              <i
                class="fa fa-trash-o"
                @click="deleteDoc(item, index)"
                v-if="
                  $store.state.permitteds[secretkey + 'delete'] &&
                    item.status != '1'
                "
              ></i>
            </span>
            <span class="content-foot fr">
              <i
                v-if="
                  $store.state.permitteds[secretkey + 'edit'] &&
                    item.status != '1'
                "
                class="fa fa-pencil-square-o"
                @click="editDoc(item, index)"
              ></i>
            </span>
          </el-card>
          <el-upload
            drag
            :data="uploadParams"
            class="fl"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
            v-if="$store.state.permitteds[secretkey + 'uplaod']"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或
              <em>点击上传</em>
            </div>
          </el-upload>
        </div>
      </transition>
      <transition name="el-fade-in">
        <div
          class="load-up"
          v-show="view == 'view' && (loading || noMore)"
          :title="loading ? '努力加载中...' : '已加载全部数据'"
        >
          <i
            :class="{
              'el-icon-sunrise-1': noMore,
              'el-icon-loading': loading,
            }"
          ></i>
        </div>
      </transition>
      <permissions ref="permissions"></permissions>
      <doc-dialog ref="docdialog" @submit="saveDoc"></doc-dialog>
      <group-dialog ref="groupdialog" @submit="reloadTree"></group-dialog>
    </div>
  </div>
</template>
<script>
import Tree from "components/tree/tree";
import { inter } from "api/interface";
import Qs from "qs";
import Permissions from "components/permissions.vue";
import SearchInput from "components/search-input.vue";
import DocDialog from "dialog/docDialog.vue";
import GroupDialog from "dialog/groupDialog.vue";
import uButton from "components/button/index";
import Datagrid from "components/table/datagrid.vue";
export default {
  name: "doc-manager",
  components: {
    Datagrid,
    Tree,
    SearchInput,
    DocDialog,
    GroupDialog,
    uButton,
    Permissions,
  },
  data() {
    return {
      secretkey: "btn:sys:doc:",
      keyword: "",
      node: {
        id: "0",
        text: "",
        type: "",
      },
      uploadUrl: inter.resource.upload2,
      uploadParams: {},
      docs: [],
      loading: false,
      noMore: false,
      page: 1,
      view: "view",
      datagrid: {
        url: inter.api.doc.list,
        lazy: true,
        selection: {
          showSelection: true,
        },
        pagination: {
          showPagination: true,
          showMore: true,
        },
        columns: [
          { label: "文档名称", prop: "text", minWidth: 200 },
          {
            label: "状态",
            prop: "status",
            width: 160,
            formatter: function(row) {
              if (row.status == "0") {
                return "草稿";
              }
              if (row.status == "1") {
                return "发布";
              }
              return "下线";
            },
          },
          {
            label: "文档类型",
            prop: "docType",
            width: 80,
            formatter: function(row) {
              if (row.docType == "1") {
                return "apiface";
              }
              return "swagger";
            },
          },
          {
            label: "开发团队",
            prop: "terms",
            minWidth: 140,
          },
          {
            label: "文档版本",
            prop: "version",
            width: 160,
          },
          {
            label: "创建时间",
            prop: "createTime",
            width: 140,
          },
        ],
      },
      tree: {
        url: inter.api.tree,
        leftType: "child",
        includeRootNode: true,
        headerNode: [{ entryId: "0", text: "所有分组", type: "index" }],
        icon: { index: "fa-folder-o" },
        loadNodeAfterSearch: true,
      },
    };
  },
  computed: {},
  created() {},
  methods: {
    goIn() {
      if (this.view == "view") {
        this.reload();
        return;
      }
      // this.$refs.datagrid.handleResize();
      this.reloadTable();
    },
    reloadTable() {
      this.$refs.datagrid.loadData(
        {
          keyword: this.keyword,
          pId: this.node.id,
        },
        true
      );
    },
    permitted(tr) {
      var type = "doc";
      var permitteds = [];
      if (this.view == "table") {
        let selectlist = this.$refs.datagrid.getSelection();
        if (selectlist.length > 0) {
          for (var i = 0; i < selectlist.length; i++) {
            permitteds.push({
              entryId: selectlist[i].docId,
              entryName: selectlist[i].text,
            });
          }
        }
      }
      if (permitteds.length == 0) {
        if (this.node.id != "0") {
          if (this.node.type == "index") {
            type = "groupdoc";
          }
          permitteds.push({ entryId: this.node.id, entryName: this.node.text });
        }
      }
      if (permitteds.length == 0) {
        this.$message({ message: "请选择授权对象", type: "warning" });
        return;
      }

      this.$refs.permissions.openDialog(type, permitteds);
    },
    addGroup() {
      this.$refs.groupdialog.openDialog(undefined);
    },
    editGroup() {
      if (this.node.id == "0" || this.node.type == "child") {
        this.$message({ message: "请选择分组", type: "warning" });
        return;
      }
      this.$refs.groupdialog.openDialog({
        groupId: this.node.id,
        text: this.node.text,
      });
    },
    removeGroup() {
      if (this.node.id == "0" || this.node.type == "child") {
        this.$message({ message: "请选择分组", type: "warning" });
        return;
      }
      this.$confirm("是否删除所选分组", "删除数据", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$fetch(
          inter.api.group.delete,
          Qs.stringify(
            {
              groupIds: [this.node.id],
            },
            { arrayFormat: "repeat" }
          )
        )
          .then((res) => {
            if (res && res.success) {
              this.reloadTree();
              return;
            }
            this.$message.error(res.message);
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    openApi(id) {
      this.$router.push({ name: "face", params: { type: "api", id: id } });
      // var addr = window.location.origin;
      // addr += window.location.pathname;
      // addr += "#/api/";
      // addr += id;
      // this.openUrl(addr);
    },
    publish(docId, index, status) {
      if (status != "1") {
        status = "1";
      } else {
        status = "2";
      }
      this.$fetch(
        inter.api.doc.status,
        Qs.stringify(
          { docId: docId, status: status },
          { arrayFormat: "repeat" }
        )
      )
        .then((res) => {
          if (res && res.success === true) {
            if (index != undefined) {
              if (this.view == "view") {
                this.docs.splice(index, 1, res.data);
                return;
              }
              this.$refs.datagrid.getCurrentData().splice(index, 1, res.data);
              return;
            }
            return;
          }
          this.$message.error(res.message);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    openUrl(url) {
      var a = document.createElement("a");
      a.href = url;
      a.target = "_blank";
      a.click();
    },
    editDoc(item = undefined, index = undefined) {
      this.$refs.docdialog.openDialog(item, index, this.node.id);
    },
    deleteDoc(item, index) {
      var ids = [];
      if (item == undefined) {
        let selectlist = this.$refs.datagrid.getSelection();
        if (selectlist.length > 0) {
          for (var i = 0; i < selectlist.length; i++) {
            if (selectlist[i].status != "1") {
              ids.push(selectlist[i].docId);
            }
          }
        }
        index = undefined;
      } else {
        ids.push(item.docId);
      }
      if (!ids || ids.length == 0) {
        this.$message({ message: "请勾选删除对象", type: "warning" });
        return;
      }
      this.$confirm("是否删除所选中对象", "删除数据", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.remove(ids, index);
      });
    },
    remove(ids, index = undefined) {
      this.$fetch(
        inter.api.doc.delete,
        Qs.stringify({ docIds: ids }, { arrayFormat: "repeat" })
      )
        .then((res) => {
          if (res && res.success === true) {
            if (index != undefined) {
              if (this.view == "view") {
                this.docs.splice(index, 1);
                return;
              }
              this.$refs.datagrid.getCurrentData().splice(index, 1);
              return;
            }
            if (this.view == "view") {
              this.reload();
              return;
            }
            this.reloadTable();
            return;
          }
          this.$message.error(res.message);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    reloadTree() {
      this.$refs.tree.reloadEvent();
    },
    load() {
      if (!this.noMore) {
        this.loading = true;
        this.$fetch(
          inter.api.doc.list,
          Qs.stringify(
            {
              page: this.page,
              size: 10,
              roll: true,
              pId: this.node.id,
              keyword: this.keyword,
            },
            { arrayFormat: "repeat" }
          )
        )
          .then((res) => {
            this.loading = false;
            if (res && res.success) {
              for (let i = 0; i < res.data.length; i++) {
                this.docs.push(res.data[i]);
              }

              if (res.next) {
                this.page += 1;
              }
              this.noMore = !res.next;
              return;
            }
            this.noMore = true;
            this.$message.error(res.message);
          })
          .catch((err) => {
            this.noMore = true;
            console.log(err);
          });
      }
    },
    reload() {
      this.page = 1;
      this.noMore = false;
      this.docs = [];
      this.load();
    },
    onClickTree(data) {
      this.node = {
        id: data.entryId,
        text: data.text,
        type: data.type,
      };
      if (data.type == "index") {
        if (this.view == "view") {
          this.reload();
          return;
        }
        this.reloadTable();
      }
    },
    saveDoc(doc, index) {
      if (index != undefined) {
        if (this.view == "view") {
          this.docs.splice(index, 1, doc);
          return;
        }
        this.$refs.datagrid.getCurrentData().splice(index, 1, doc);
        return;
      }
      if (this.view == "view") {
        this.docs.push(doc);
        return;
      }
      this.$refs.datagrid.pushData([doc], undefined);
    },
    handleUploadSuccess(res, file) {
      if (res && res.success) {
        this.$fetch(
          inter.api.doc.valid,
          Qs.stringify(
            { resourceId: res.data, groupId: this.node.id },
            { arrayFormat: "repeat" }
          )
        )
          .then((res) => {
            if (res && res.success) {
              if (this.view == "view") {
                this.docs.push(res.data);
                return;
              }
              this.reloadTable();

              return;
            }
            this.$message.error(res.message);
            return;
          })
          .catch((err) => {
            console.log(err);
          });
      }
      // this.$message.error(res.message);
      console.log(res);
    },
    beforeUpload(file) {
      this.uploadParams = {
        type: file.type,
      };
      const isJPG = file.type === "application/json";
      const isLt2M = file.size / 1024 / 1024 < 20;

      if (!isJPG) {
        this.$message.error("上传文件只能是JSON");
      }
      if (!isLt2M) {
        this.$message.error("上传大小不能超过 20MB!");
      }
      return isJPG && isLt2M;
    },
  },
  watch: {},
};
</script>
<style lang="less">
.doc-manager {
  background-color: #f6f6f6;
  padding: 5px;
  &-left {
    width: 250px;
    float: left;
    height: 100%;
  }
  &-content {
    width: calc(~"100% - 258px");
    float: right;
    height: 100%;
    .load-up {
      background-color: #008ead;
      position: fixed;
      right: 10px;
      bottom: 10px;
      width: 40px;
      height: 40px;
      border-radius: 20px;
      cursor: pointer;
      transition: 0.3s;
      box-shadow: 0 3px 6px rgba(0, 0, 0, 0.5);
      opacity: 0.5;
      z-index: 100;
      i {
        color: #fff;
        display: block;
        line-height: 40px;
        text-align: center;
        font-size: 20px;
      }
      &:hover {
        opacity: 1;
      }
    }
    .content-toolbar {
      height: 40px;
      padding-bottom: 8px;
      .view-btn {
        line-height: 34px;
        padding: 0px 8px;
        i {
          color: #999;
          &:hover {
            color: #666;
          }
        }
      }
      .btn-opt {
        padding-top: 4px;
        .u-btn-delete {
          background-color: #de1111 !important;
          border-color: #de1111 !important;
        }
        .u-button {
          margin-right: 8px;
        }
      }
      .u-search-input {
        width: 300px;
        .el-input--small,
        .el-input--small .el-input__inner {
          height: 32px !important;
          line-height: 32px !important;
        }
        .el-input__icon {
          line-height: 36px !important;
        }
        .el-input__inner {
          padding-right: 65px !important;
        }
        .el-icon-search {
          right: 5px;
          font-weight: 600;
        }
        .clear {
          right: 35px;
          font-weight: 600;
        }
        input {
          background-color: #eaecf1;
          border-radius: 15px;
          border: 0;
          outline: 0;
        }
      }
    }
    .content-main {
      height: calc(~"100% - 40px");
    }
    .content-main:not(.datagrid) {
      @width-box: 250px;
      @height-box: 180px;
      @margin-box: 8px;
      overflow-y: auto;
      .el-card {
        margin-right: @margin-box;
        margin-bottom: @margin-box;
        height: @height-box;
        width: @width-box;
        .el-card__header {
          height: 42px;
          line-height: 42px;
          padding: 0 10px;
          color: #333;
          font-size: 14px;
          -webkit-box-sizing: border-box;
          box-sizing: border-box;
          .title-view {
            float: left;
            padding-top: 10px;
            line-height: 20px;
            cursor: pointer;
            width: 180px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            &:hover {
              color: #1e9fff;
              border-bottom: 1px solid #1e9fff;
            }
          }
          .title-yellow {
            color: #ffb800;
          }
          .title-green {
            color: #009688;
          }
          .title-red {
            color: #ff5722;
          }
          .title-badge {
            float: right;
            cursor: pointer;
            padding: 0px 9px;
            line-height: 25px;
            margin-top: 8px;
            color: #fff;
            font-size: 12px;
            border-radius: 2px;
          }
          .title-badge-yellow {
            background-color: #ffb800;
          }
          .title-badge-green {
            background-color: #009688;
          }
          .title-badge-red {
            background-color: #ff5722;
          }
        }
        .el-card__body {
          padding: 6px;
          height: calc(~"100% - 43px");
          .content-info {
            color: #666;
            height: 105px;
            line-height: 20px;
            &-title {
              width: 60px;
              display: inline-block;
              &::after {
                content: ":";
                margin-left: 4px;
              }
            }
            &-addr {
              cursor: pointer;
              &:hover {
                color: #1e9fff;
                position: relative;
                &::after {
                  content: "";
                  position: absolute;
                  left: 0;
                  right: 0;
                  height: 0;
                  bottom: 0;
                  border-bottom: 1px solid #409eff;
                }
              }
            }
            &-item {
              width: 176px;
              float: right;
              display: block;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
          }
          .content-foot-gax {
            padding-left: @margin-box;
          }
          .content-foot {
            font-size: 13px;
            line-height: 20px;
            color: #777;
            i {
              font-size: 16px;
            }
          }
        }
      }
      .el-upload {
        .el-upload-dragger {
          width: 248px;
          height: 178px;
          border-radius: 4px;
          border: 1px solid #ebeef5;
          .el-icon-upload {
            font-size: 67px;
            color: #c0c4cc;
            margin: 40px 0 16px;
            line-height: 50px;
          }
          .el-upload__text {
            color: #606266;
            font-size: 14px;
            text-align: center;
            em {
              color: #409eff;
              font-style: normal;
            }
          }
          &:hover {
            // box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
            border: 1px dashed #409eff;
          }
        }
      }
    }
  }
}
</style>
