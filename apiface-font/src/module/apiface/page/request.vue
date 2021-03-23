/**
 * 请求参数配置
 * author:Daye Shing
 */
<template>
  <selection
    class="blue request-section"
    :label="$self('label')"
    :collapsed="collapsed"
    ref="requests"
  >
    <datagrid :data="data" rowkey="name">
      <el-table-column prop="enable" :label="$t('enable')" width="60" align="center">
        <template slot-scope="scope">
          <el-checkbox
            v-model="scope.row.enable"
            :disabled="scope.row.required"
            @change="checkChange(false)"
          ></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column
        type="expand"
        class-name="no-padding"
        :label="$self('details')"
        width="60"
        v-if="doc.isBody"
      >
        <template slot-scope="scope">
          <div class="el-table__inner-self" v-if="scope.row.type == 'json'">
            <datagrid :data="scope.row.schema.data.child">
              <el-table-column prop="name" label="#" width="160" align="center"></el-table-column>
              <el-table-column prop="value" label="#" width="290" align="center">
                <template slot-scope="scope">
                  <el-input
                    type="textarea"
                    :rows="1"
                    :placeholder="$t('input')"
                    v-model="scope.row.example"
                    v-if="scope.row.format != 'child'"
                  ></el-input>
                  <span v-else>- / -</span>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="#" align="center"></el-table-column>
              <el-table-column prop="type" label="#" width="120" align="center"></el-table-column>
              <el-table-column
                prop="required"
                label="#"
                width="80"
                :formatter="format"
                align="center"
              ></el-table-column>
              <el-table-column prop="format" label="#" width="80" align="center"></el-table-column>
            </datagrid>
          </div>
          <div
            class="el-table__inner-self"
            v-else
          >-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --</div>
        </template>
      </el-table-column>
      <el-table-column prop="name" :label="$self('param')" width="160" align="center">
        <template slot-scope="scope">
          <el-input
            type="textarea"
            :rows="1"
            v-if="scope.row.self"
            :placeholder="$t('input')"
            v-model="scope.row.name"
          ></el-input>
          <span v-else>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="value" :label="$self('value')" width="290" align="center">
        <template slot-scope="scope">
          <el-input
            type="textarea"
            :rows="scope.row.type == 'json' ? 10 : 1"
            :placeholder="$t('input')"
            v-model="scope.row.value"
          ></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="description" :label="$self('desc')" align="center">
        <template slot-scope="scope">
          <span v-if="doc.isBody">{{ scope.row.description }}</span>
          <span v-else-if="!doc.isBody && scope.row.self">
            {{ $self("self") }}
            <el-button
              type="text"
              @click.native.prevent="remove(scope.$index)"
              style="float: right; color: #f56c6c;"
            >{{ $t("del") }}</el-button>
          </span>
          <span v-else>{{ scope.row.description }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="type" :label="$self('type')" width="120" align="center">
        <template slot-scope="scope">
          <el-select v-model="scope.row.type" v-if="scope.row.self">
            <el-option :label="$self('s')" value="string"></el-option>
            <el-option :label="$self('n')" value="number"></el-option>
            <el-option :label="$self('i')" value="integer"></el-option>
            <el-option :label="$self('b')" value="boolean"></el-option>
          </el-select>
          <span v-else>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="required"
        :label="$self('required')"
        width="80"
        :formatter="format"
        align="center"
      ></el-table-column>
      <el-table-column prop="in" :label="$self('format')" width="80" align="center"></el-table-column>
    </datagrid>
    <div class="datagrid-pagination-box">
      <div class="push-left">
        <el-checkbox
          :indeterminate="doc.indeterminate"
          :disabled="data.length == 0"
          v-model="doc.checkAll"
          @change="checkChange(true)"
        >{{ $self("sel") }}</el-checkbox>
        <span>{{ $self("has") }}{{ doc.checkNum }}{{ $self("item") }}</span>
      </div>
      <div class="push-left array-format" v-if="doc.$in != 'body'">
        <span
          class="array-format-select"
          v-for="(valu, item, index) in arrayFormatItem"
          :key="index"
          :class="{'current-format':arrayFormat == item}"
          @click="arrayFormat = item"
        >{{item}}</span>
      </div>
      <div class="push-left array-format-tips" v-if="doc.$in != 'body'">
        <el-popover placement="bottom" width="260" trigger="hover">
          <p>{a : ['b', 'c', 'd']}</p>
          <p v-for="(val, key, f) in arrayFormatItem" :key="f">
            <span class="array-format-tips-title">{{key}} :</span>
            {{val}}
          </p>
          <i class="fa fa-question-circle" slot="reference"></i>
        </el-popover>
      </div>
      <div class="btn-group">
        <el-button type="text" @click="addRows" v-if="doc.$in != 'body'">
          {{
          $self("add")
          }}
        </el-button>
        <el-button type="text" @click="fillData">{{ $self("fill") }}</el-button>
        <el-button type="text" @click="submit">{{ $self("ty") }}</el-button>
      </div>
    </div>
  </selection>
</template>

<script>
import datagrid from "../compontents/datagrid";
import selection from "../compontents/section";
import Qs from "qs";
export default {
  data() {
    return {
      collapsed: false,
      page: "page.index.",
      arrayFormatItem: {
        repeat: "a=b&a=c&a=d",
        brackets: "a[]=b&a[]=c&a[]=d",
        indices: "a[0]=b&a[1]=c&a[2]=d",
        comma: "a=b,c,d"
      },
      arrayFormat: "repeat"
    };
  },
  components: {
    selection,
    datagrid
  },
  props: {
    doc: {
      type: Object,
      default: function() {
        return {
          checkAll: true,
          indeterminate: true,
          checkNum: 0,
          isBody: true,
          path: ""
        };
      }
    },
    data: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  mounted() {
    window.onresize = () => {
      return (() => {
        this.resize();
      })();
    };
  },
  computed: {
    pathcalc: function() {
      return this.doc.path;
    }
  },
  watch: {
    pathcalc: {
      handler(val, oldVal) {
        // 菜单发生变化则重新加载描述和表单
        if (this.doc.isBody) {
          this.fillData();
        }
      },
      deep: true
    }
  },
  methods: {
    format(row) {
      return row.required ? this.$self("req") : this.$self("noreq");
    },
    $self(k) {
      return this.$t(this.page + k);
    },
    checkChange(flag) {
      if (flag) {
        //改变状态并计算值
        // 状态1 全选  状态2 半选  状态3 不选
        // 证明旧数据是 不选 或者半选状态，这时需要选择全部数据
        var num = 0;
        if (this.doc.checkAll) {
          this.doc.indeterminate = false;
          for (var i = 0; i < this.data.length; i++) {
            this.data[i].enable = true;
            num++;
          }
        } else {
          // 证明旧数据是 全选状态，需要取消选择
          for (var i = 0; i < this.data.length; i++) {
            if (this.data[i].required) {
              this.doc.indeterminate = true;
              num++;
            } else {
              this.data[i].enable = false;
            }
          }
          if (num == this.data.length) {
            this.doc.checkAll = true;
            this.doc.indeterminate = false;
            this.$message({ type: "warning", message: this.$self("warn") });
          }
        }
        this.doc.checkNum = num;
      } else {
        // 仅仅计算值
        var checkAll = false;
        var indeterminate = true;
        var checkNum = 0;
        for (var i = 0; i < this.data.length; i++) {
          indeterminate = indeterminate && this.data[i].enable;
          // 设置选择状态
          if (this.data[i].enable) {
            checkNum++;
            checkAll = true;
          }
        }
        this.doc.checkNum = checkNum;
        this.doc.checkAll = indeterminate;
        this.doc.indeterminate = checkAll && !indeterminate;
      }
    },
    submit() {
      var data = this.toParams(this.data);
      console.log(data, "request data");
      if (data.success) {
        this.$emit("submit", this.formatParam(data.data), data.meta);
      } else {
        this.$message({
          type: "error",
          message: data.field + this.$t("code." + data.msg)
        });
      }
    },
    formatParam(data) {
      if (this.doc.$in == "body") {
        return data;
      }
      return Qs.stringify(data, { arrayFormat: this.arrayFormat });
    },
    remove(index) {
      this.data.splice(index, 1);
      this.checkChange(false);
    },
    addRows() {
      this.data.push({
        enable: false,
        self: true,
        value: "",
        example: "",
        type: "string",
        required: false,
        name: "",
        in: "self"
      });
      this.checkChange(false);
    },
    fillData() {
      for (var i = 0; i < this.data.length; i++) {
        if (this.data[i].self) {
          continue;
        }
        if (this.doc.isBody) {
          this.data[i].value = JSON.stringify(
            this.toJSONObject(this.data[i].schema.data),
            null,
            4
          );
          continue;
        }
        this.data[i].value = this.data[i].example;
      }
    },
    resize() {
      if (this.collapsed) {
        return;
      }
      this.collapsed = true;
      setTimeout(() => {
        this.collapsed = false;
      }, 10);
    }
  }
};
</script>

<style lang="less">
.array-format-tips-title {
  width: 68px;
  line-height: 18px;
  display: inline-flex;
  color: #4b71a4;
}
.request-section {
  width: calc(~"100% - 42px");
  overflow: hidden;
  .el-table__inner-self {
    padding: 0px 0px 0px 120px;
    .el-table__header-wrapper {
      display: none;
    }
  }
  .collapsible {
    width: 100%;
  }
  .el-table__header,
  el-table__body {
    width: 100% !important;
  }
  .cell {
    height: 100% !important;
    padding-bottom: 1px;
    padding-top: 1px;
    line-height: 31px;
    .el-table__expand-icon {
      padding-top: 30px;
    }
  }

  .el-textarea.el-input--small {
    height: 100% !important;
    textarea {
      resize: none;
      height: 100%;
      white-space: nowrap;
    }
  }
  .datagrid-pagination-box {
    overflow: hidden;
    background-color: #fff;
    z-index: 99;
    width: 100%;
    height: 37px;
    line-height: 36px;
    border-color: #dddddd;
    border-style: solid;
    border-width: 0 1px 1px 1px;
    text-align: left;
    padding-left: 22px;
    box-sizing: border-box;
    .array-format-tips {
      padding-left: 6px;
      i {
        font-size: 14px;
        color: #4b71a4;
      }
    }
    .array-format-select {
      padding: 0 6px;
      float: left;
      border: 1px solid #ced8e5;
      color: #4d4d4d;
      height: 23px;
      line-height: 23px;
      cursor: pointer;
      margin-left: -1px;
      &:first-child {
        margin-left: 0;
      }
      &.current-format {
        background-color: #4b71a4;
        color: #ffffff;
      }
    }
    .array-format {
      padding-left: 18px;
      padding-top: 6px;
    }
    .btn-group {
      font-size: 0;
      float: right;
      padding: 5px 10px;
      .el-button {
        padding: 0;
        margin-left: 8px;
        padding-left: 8px;
        border-left: 1px solid #cbcbcb;
        height: 17px;
        &:first-child {
          margin-left: 0;
          padding-left: 0;
          border-left: none;
        }
      }
    }
  }
}
</style>
