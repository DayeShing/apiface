<template>
  <div id="module-org">
    <div class="org-main-left">
      <tree @onNodeClick="onClickTree" :tree="treeData" ref="tree"></tree>
    </div>
    <div class="org-main-content">
      <datagrid :datagrid="datagrid" ref="orgDatagrid" @onLonger="longer">
        <div slot="toolbar">
          <div class="toolbar-btn push-left">
            <u-button
              type="build"
              @click="addOrg()"
              v-if="$store.state.permitteds[secretkey + 'add']"
            >新增机构</u-button>
            <u-button
              type="build"
              @click="authorize()"
              v-if="$store.state.permitteds['btn:sys:project:authorize']"
            >授权</u-button>
          </div>
          <div class="search push-right">
            <search-input v-model="keyword" @search="reload()" @clear="reload()"></search-input>
          </div>
        </div>
        <el-table-column
          label="操作"
          width="120"
          slot="last"
          align="center"
          :fixed="onLonger ? 'right' : onLonger"
        >
          <template scope="scope">
            <div class="table-btn-group">
              <u-button
                shape="text"
                @click="addOrg(scope.row)"
                v-if="$store.state.permitteds[secretkey + 'edit']"
              >修改</u-button>
              <u-button
                shape="text"
                @click="deleteOrg(scope.row)"
                v-if="$store.state.permitteds[secretkey + 'delete']"
              >删除</u-button>
            </div>
          </template>
        </el-table-column>
        <div slot="footbar">
          <el-button
            type="text"
            size="mini"
            @click="deleteOrg()"
            style="color: #F56C6C;"
            v-if="$store.state.permitteds[secretkey + 'delete']"
          >删除机构</el-button>
        </div>
      </datagrid>
    </div>
    <edit-org-dialog ref="editer" @reload="reload(true)"></edit-org-dialog>
  </div>
</template>
<script>
import Datagrid from "components/table/datagrid.vue";
import { inter } from "api/interface";
import Qs from "qs";
import SearchInput from "components/search-input.vue";
import Tree from "components/tree/tree";
import uButton from "components/button/index";
import EditOrgDialog from "dialog/editOrgDialog";
export default {
  name: "org",
  components: {
    Tree,
    SearchInput,
    uButton,
    Datagrid,
    EditOrgDialog
  },
  data() {
    return {
      keyword: "",
      pId: "0",
      text: "",
      ids: [],
      onLonger: false,
      secretkey: "btn:sys:org:",
      datagrid: {
        url: inter.org.list,
        selection: {
          showSelection: true
        },
        pagination: {
          showPagination: true,
          autoRoll: false,
          showMore: true
        },
        queryParams: { pId: "0" },
        columns: [
          { label: "名称", prop: "orgName", minWidth: 130 },
          { label: "描述", prop: "descript", minWidth: 200 },
          {
            label: "父机构",
            prop: "upperName",
            minWidth: 130,
            formatter: function(row) {
              return row.upperId == "0" ? "顶级机构" : row.upperName;
            }
          },
          {
            label: "机构类型",
            prop: "orgType",
            minWidth: 130,
            formatter: function(row) {
              if (row.orgType == "3") return "-";
              else if (row.orgType == "2") return "机构/部门";
              else return "集团";
            }
          },
          {
            label: "机构编码",
            prop: "orgCode",
            minWidth: 130,
            formatter: function(row) {
              if (row.orgType == "3") return "-";
              else return row.orgCode;
            }
          },
          { label: "更新用户", prop: "updateUserName", minWidth: 100 },
          {
            label: "更新时间",
            prop: "updateTime",
            minWidth: 140
          },
          {
            label: "创建时间",
            prop: "createTime",
            minWidth: 140
          }
        ]
      },
      treeData: {
        isLoad: false,
        url: inter.org.tree,
        leftType: "child",
        includeRootNode: true,
        headerNode: [{ entryId: "-2", text: "全部机构", type: "top" }],
        icon: { index: "fa fa-university", child: "fa fa-id-badge" },
        loadNodeAfterSearch: true
      },
      currentOrg: {}
    };
  },
  mounted() {
    this.$refs.tree.reloadEvent();
  },
  methods: {
    onClickTree(data) {
      if (data.type == "child") {
        return;
      }
      if (data.entryId == "-2") {
        this.pId = "0";
        this.text = "全部机构";
      } else {
        this.pId = data.entryId;
        this.text = data.text;
      }
      this.currentOrg = data;
      this.reload();
    },
    deleteOrg(org) {
      var orgIds = [];
      if (org == undefined) {
        orgIds = this.getSelectIds();
      } else {
        orgIds.push(org.orgId);
      }
      if (!orgIds || orgIds.length == 0) {
        this.$message({ message: "请勾选功能", type: "warning" });
        return;
      }
      this.$confirm("是否删除所选中的功能", "删除功能", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.removeOrg(orgIds);
      });
    },
    getSelectIds() {
      this.ids = [];
      let selectlist = this.$refs.orgDatagrid.getSelection();
      if (selectlist.length > 0) {
        for (var i = 0; i < selectlist.length; i++) {
          this.ids.push(selectlist[i].orgId);
        }
      }
      return this.ids;
    },
    authorize() {
      var auths = [];
      let selectlist = this.$refs.orgDatagrid.getSelection();
      if (selectlist.length > 0) {
        for (var i = 0; i < selectlist.length; i++) {
          auths.push({
            entryId: selectlist[i].orgId,
            entryName: selectlist[i].orgName
          });
        }
      }
      this.$emit("authorize", auths, "org");
    },
    removeOrg(orgIds) {
      this.$fetch(
        inter.org.delete,
        Qs.stringify({ orgIds: orgIds }, { arrayFormat: "repeat" })
      )
        .then(res => {
          if (res && res.success) {
            this.reload(true);
          } else {
            this.$message({ type: "error", message: res.message });
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    addOrg(org) {
      this.$refs.editer.openDialog(org, this.currentOrg);
    },
    longer(data) {
      this.onLonger = data;
    },
    reload(flag) {
      if (flag) {
        this.$refs.tree.reloadEvent();
      }
      this.$refs.orgDatagrid.loadData(
        { pId: this.pId, keyword: this.keyword },
        true
      );
    }
  }
};
</script>

<style lang="less">
#module-org {
  height: 100%;
  padding: 6px;
  overflow: hidden;
  .org-main-left {
    width: 220px;
    float: left;
    height: 100%;
  }
  .org-main-content {
    width: calc(~"100% - 225px");
    float: right;
    height: 100%;
    .toolbar-btn {
      padding-top: 4px;
      .u-button {
        margin-right: 5px;
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
}
</style>
