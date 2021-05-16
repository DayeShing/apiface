<template>
  <div class="apiface-role">
    <div class="block-left operation-role">
      <div class="operation-content">
        <div class="el-collapse-item__header operation-role-tilte">
          角色名称
          <span class="header-icon">
            <i
              class="fa fa-plus-square-o"
              @click.stop="editRole()"
              v-if="$store.state.permitteds[secretkey + 'add']"
            ></i>
          </span>
        </div>
        <el-collapse accordion>
          <el-collapse-item v-for="(role,key) in roleList" :key="key">
            <template slot="title">
              <span class="header-title">
                {{role.roleName}}
                <el-tooltip placement="right" effect="light" v-if="role.roleId== '-3'">
                  <div slot="content">
                    <p>系统的保留角色，不可删除，不可改变其管理员状态,具有访客权限</p>
                  </div>
                  <i class="el-icon-info"></i>
                </el-tooltip>
                <el-tooltip placement="right" effect="light" v-if="role.roleId== '-2'">
                  <div slot="content">
                    <p>系统的保留角色，不可删除，不可改变其管理员状态,具有授权权限</p>
                  </div>
                  <i class="el-icon-info"></i>
                </el-tooltip>
                <el-tooltip placement="right" effect="light" v-if="role.roleId== '-1'">
                  <div slot="content">
                    <p>系统的保留角色，不可删除，由具有角色权限的超级管理员用户创建超级管理员，具有所有访问权限</p>
                  </div>
                  <i class="el-icon-info"></i>
                </el-tooltip>
              </span>
              <span class="header-icon">
                <i
                  class="fa fa-trash-o"
                  @click.stop="deleteRole(role,key)"
                  v-if="$store.state.permitteds[secretkey + 'edit'] && role.roleId!= '-1'&& role.roleId!= '-2' && role.roleId!= '-3'"
                ></i>
                <i
                  class="fa fa-pencil-square-o"
                  @click.stop="editRole(role,key)"
                  v-if="$store.state.permitteds[secretkey + 'delete'] && role.roleId!= '-1' &&role.roleId!= '-2' && role.roleId!= '-3'"
                ></i>
                <i
                  class="fa fa-share-alt-square"
                  @click.stop="authorize(role,'role')"
                  v-if="$store.state.permitteds['btn:sys:project:authorize']"
                ></i>
              </span>
            </template>
            <p class="role-desc">{{role.descript}}</p>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div class="title-box">
        项目组
        <el-tooltip placement="top" effect="light">
          <div slot="content">
            <p>角色能控制数据的可见性</p>
            <p>对于系统保留的角色是不能删除的</p>
          </div>
          <i class="fa fa-question-circle"></i>
        </el-tooltip>
      </div>
    </div>
    <div class="block-left operation-org">
      <div class="operation-content">
        <org @authorize="authorize" />
      </div>
      <div class="title-box">
        组织架构
        <el-tooltip placement="top" effect="light">
          <div slot="content">
            <p>组织架构同样能控制数据的可见性</p>
            <p>子机构能看到直系上属和所有下属的数据</p>
          </div>
          <i class="fa fa-question-circle"></i>
        </el-tooltip>
      </div>
    </div>
    <permissions ref="permissions"></permissions>
    <editRoleDialog ref="editDialog" @reload="modify"></editRoleDialog>
  </div>
</template>
<script>
import Permissions from "components/permissions.vue";
import { inter } from "api/interface";
import EditRoleDialog from "dialog/editRoleDialog";
import Qs from "qs";
import Org from "./org.vue";
export default {
  name: "apiface-role",
  components: { Permissions, EditRoleDialog, Org },
  data() {
    return {
      roleList: [],
      secretkey: "btn:sys:role:"
    };
  },
  computed: {},
  created() {},
  mounted() {
    this.loadRole();
  },
  methods: {
    authorize(data, type) {
      if (type == "role") {
        data = [{ entryId: data.roleId, entryName: data.roleName }];
      }
      if (data.length == 0) {
        this.$message({ message: "请选择授权对象", type: "warning" });
        return;
      }
      this.$refs.permissions.openDialog(type, data);
    },
    modify(role, index) {
      if (index || index == 0) {
        this.roleList.splice(index, 1, role);
        return;
      }
      this.roleList.push(role);
    },
    deleteRole(role, index) {
      this.$confirm("是否删除角色", "删除数据", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$fetch(
          inter.role.delete,
          Qs.stringify({ roleIds: [role.roleId] }, { arrayFormat: "repeat" })
        )
          .then(res => {
            if (res && res.success) {
              this.roleList.splice(index, 1);
            } else {
              this.$message({ type: "error", message: res.message });
            }
          })
          .catch(err => {
            console.log(err);
          });
      });
    },
    editRole(role, index) {
      this.$refs.editDialog.openDialog(
        role,
        this.$store.state.user.admin,
        index
      );
    },
    loadRole() {
      this.roleList = [];
      this.$fetch(inter.role.list)
        .then(res => {
          if (res && res.success) {
            this.roleList = res.data;
          } else {
            this.$message({ type: "error", message: res.message });
          }
        })
        .catch(err => {
          console.log(err);
        });
    }
  },
  watch: {}
};
</script>
<style lang="less">
.apiface-role {
  height: 100;
  padding: 6px;
  .operation-role {
    width: 250px;
    .operation-content {
      padding: 6px;
      overflow-y: auto;
      .role-desc {
        padding: 6px;
      }
      .operation-role-tilte {
        border-top: 1px solid #ebeef5;
        border-bottom: none !important;
        .header-icon {
          display: inline !important;
        }
      }
      .el-collapse-item__content {
        padding-bottom: 15px !important;
      }
      .el-collapse-item__header {
        height: 36px !important;
        line-height: 36px !important;
        font-size: 12px !important;
        position: relative;
        padding-left: 17px;
        &.is-active .header-title {
          color: #5fa8c4;
        }
        &:hover {
          .header-title {
            color: #74a5d8 !important;
          }
          .header-icon {
            display: inline !important;
          }
        }
        .el-collapse-item__arrow {
          position: absolute;
          left: 3px;
          font-weight: 800 !important;
        }
        .header-icon {
          display: none;
          padding-right: 10px;
          position: absolute;
          right: 0px;
          i {
            padding-left: 10px;
            &:hover {
              color: #74a5d8 !important;
            }
          }
        }
      }
    }
  }
  .operation-org {
    width: calc(~"100% - 256px");
  }
  .block-left {
    float: left;
    height: 100%;
    .title-box {
      background-color: #008ead;
      color: #fff;
      font-size: 15px;
      font-weight: bold;
      height: 30px;
      line-height: 30px;
      text-align: center;
      border: 1px solid #008ead;
    }
    .operation-content {
      width: 100%;
      background-color: #eff3f4;
      height: calc(~"100% - 31px");
      border: 1px solid #008ead;
      border-bottom: none;
    }
    &:first-child {
      margin-right: 6px;
    }
  }
}
</style>
