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
  >
    <el-form
      ref="reform"
      :model="doc"
      :rules="formRules"
      label-width="75px"
      label-suffix=":"
    >
      <el-form-item label="显示名称" prop="text">
        <el-input
          :maxlength="30"
          placeholder="请输入显示名称"
          v-model="doc.text"
          auto-complete="off"
        ></el-input>
      </el-form-item>

      <el-row>
        <el-col :span="12">
          <el-form-item label="文档版本" prop="version">
            <el-input
              :maxlength="100"
              placeholder="请输入接口文档版本"
              v-model="doc.version"
              auto-complete="off"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文档分组" prop="groupId" style="width:100%">
            <select-tree
              :selectTree="selectTree"
              ref="selectTree"
              v-model="node"
              @change="selectChange"
            ></select-tree>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="开发团队" prop="terms">
        <el-input
          :maxlength="300"
          placeholder="开发团队"
          v-model="doc.terms"
          auto-complete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="团队地址" prop="termsUrl">
        <el-input
          :maxlength="300"
          placeholder="开发团队地址"
          v-model="doc.termsUrl"
          auto-complete="off"
        ></el-input>
      </el-form-item>

      <el-form-item :label="doc.mapping ? '映射地址' : '接口地址'" prop="addr">
        <el-input
          :maxlength="300"
          :placeholder="
            doc.mapping ? '请输入文档映射地址' : '请输入文档接口地址'
          "
          v-model="doc.addr"
          auto-complete="off"
        ></el-input>
      </el-form-item>

      <el-row>
        <el-col :span="12">
          <el-form-item label="文档类型">
            <el-select v-model="doc.docType">
              <el-option label="apiface接口文档" value="1"></el-option>
              <el-option label="swagger接口文档" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-radio class="radio" v-model="doc.status" label="0"
              >草稿</el-radio
            >
            <el-radio class="radio" v-model="doc.status" label="1"
              >发布</el-radio
            >
            <el-radio class="radio" v-model="doc.status" label="2"
              >下线</el-radio
            >
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button class="btn-item" type="cancle" @click="closeDialog">
          {{ $self("cancle") }}
        </u-button>
        <u-button
          class="btn-item"
          type="submit"
          @click="saveData"
          :disabled="btnState"
          >{{ $self("save") }}</u-button
        >
      </div>
    </div>
  </dialog-box>
</template>

<script type="es6">
import DialogBox from "components/dialog/dialogbox";
import uButton from "components/button/index";
import SelectTree from "components/select/select-tree";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "docDialog",
  components: {
    SelectTree,
    DialogBox,
    uButton
  },
  data() {
    return {
      page: "dialog.conf.",
      selectTree: {
        url: inter.api.tree,
        selectNodeType: "index",
        headerNode: [{ entryId: "0", text: "全部分组", type: "index" }],
        placeholderText: "请选择分组",
        includeRootNode: true,
        loadNodeAfterSearch: true
      },
      node: {},
      params: {
        title: "",
        width: "500px",
        height: "380px",
        visible: false
      },
      index:null,
      doc:{
        addr: "",
        docId: "",
        docType: "0",
        groupId: "0",
        status: "0",
        mapping:true,
        termsUrl:"",
        terms: "",
        text: "",
        version: ""
      },
      btnState: false,
      formRules: {
        text: [
          { required: true, message: "请输入显示名称", trigger: "blur" },
          { min: 1, max: 30, message: "长度为 1 到 30 个字符", trigger: "blur" },
        ],
        version: [{ required: true, message: "请输入版本", trigger: "blur" }],
        addr: [{ required: true, message: "请输入地址", trigger: "blur" }],
      }
    };
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    selectChange(node){
      if (node.entryId)      {
        this.doc.groupId = node.entryId;
      }
    },
    saveData () {
      this.$refs.reform.validate(validate => {
        if (validate)        {
          this.submit();
        } else        {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      this.$fetch(inter.api.doc.save, this.doc, { headers: { "Content-Type": "application/json" } }).then(res => {
        this.btnState = false;
        if (res && res.success)        {
          this.$emit("submit",res.data,this.index);
          this.closeDialog();
        } else        {
          this.$message({ type: "error", message: res.message });
        }
      });
    },
    openDialog(doc,index,groupId) {
      this.params.title = "新增文档映射";
      if(doc){
        this.doc = this.deepClone(doc);
        this.params.title = "编辑文档映射";
      }else{
        this.doc.groupId = groupId;
      }
      this.index = index;
      this.setSelectTree(groupId,undefined);
      this.params.visible = true;
    },
    setSelectTree (value, text) {
      if (!value)      {
        value = "0";
      }
      if (!text)      {
        text = "全部分组"
      }
      this.$nextTick(() => {
        this.$refs.selectTree.refreshTree();
        this.$refs.selectTree.setValue({ value: value, text: text })
      });
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.index = 0;
      this.doc = {
        addr: "",
        docId: "",
        docType: "0",
        groupId: "0",
        status: "0",
        mapping:true,
        termsUrl:"",
        terms: "",
        text: "",
        version: ""
      }
    },
  }
};
</script>

<style lang="less">
.el-dialog__body {
  padding: 4px 6px !important;
}
</style>
