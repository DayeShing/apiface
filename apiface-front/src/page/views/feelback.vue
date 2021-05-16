<template>
  <div class="feelback-view">
    <datagrid :datagrid="datagrid" ref="datagrid">
      <div slot="toolbar" v-if="mode">
        <div class="toolbar-btn push-left">
          <!-- <u-button type="build" icon="el-icon-plus" typeClass="build" @click="deleteRow()">删除</u-button> -->
        </div>
        <div class="search push-right">
          <search-input v-model="keyword" @search="reload()" @clear="reload()"></search-input>
        </div>
      </div>

      <el-table-column
        type="expand"
        class-name="no-padding"
        label="详情"
        slot="first"
        width="45"
        v-if="mode"
      >
        <template slot-scope="scope">
          <div style="padding: 10px 0px 10px 66px; overflow: hidden">
            <ul class="information-list">
              <li v-for="(item, index) in datagrid.columns" :key="item.prop">
                <label>
                  <span>{{ index + 1 }}</span>
                  <span>.</span>
                  <span>{{ item.label }}</span>
                </label>
                <p>
                  <span class="information-list-end">:</span>
                  {{
                  item.formatter
                  ? item.formatter(scope.row, item.prop)
                  : scope.row[item.prop]
                  }}
                </p>
              </li>
            </ul>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80" slot="last" align="center">
        <template scope="scope">
          <div class="table-btn-group">
            <u-button shape="text" @click="document(scope.row)">查看文档</u-button>
          </div>
        </template>
      </el-table-column>
    </datagrid>
  </div>
</template>
<script>
import Datagrid from "components/table/datagrid.vue";
import { inter } from "api/interface";
import uButton from "components/button/index";
import SearchInput from "components/search-input.vue";
import Qs from "qs";
export default {
  name: "feelback",
  components: { Datagrid, uButton, SearchInput },
  data() {
    return {
      keyword: "",
      datagrid: {
        url: inter.back.list,
        selection: {
          showSelection: this.mode
        },
        pagination: {
          showPagination: true,
          showMore: true
        },
        columns: [
          { label: "接口", prop: "path", width: 160 },
          { label: "简述", prop: "summary", width: 160 },
          { label: "错误描述", prop: "description", minWidth: 200 },
          {
            label: "发送给",
            prop: "contact",
            width: 160,
            formatter: function(row) {
              if (row.mode == "2") {
                return "文档管理员";
              }
              return row.contact;
            }
          },
          {
            label: "状态",
            prop: "status",
            width: 80,
            formatter: function(row) {
              if (row.status == "1") {
                return "反馈中";
              }
              if (row.status == "2") {
                return "处理中";
              }
              if (row.status == "3") {
                return "处理完成";
              }
              if (row.status == "4") {
                return "关闭";
              }
              if (row.status == "5") {
                return "反馈失败";
              }
              return "草稿";
            }
          },
          {
            label: "创建时间",
            prop: "createTime",
            width: 140
          }
        ]
      }
    };
  },
  props: {
    mode: {
      type: Boolean,
      default() {
        return true;
      }
    }
  },
  computed: {},
  created() {},
  methods: {
    reload() {
      this.$refs.datagrid.loadData(
        {
          keyword: this.keyword
        },
        true
      );
    },
    deleteRow(row) {},
    addRow(row) {},
    document(row) {
      this.$router.push({
        name: "face",
        params: { type: "feelback", id: row.feelbackId }
      });
    }
  },
  watch: {}
};
</script>
<style lang="less">
.feelback-view {
  height: 100%;
  padding: 5px;
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
</style>
