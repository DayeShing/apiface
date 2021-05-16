/** * 空白数据页 * author:Daye Shing */
<template>
  <div class="postman-params">
    <p class="postman-params-title" v-if="title != ''">{{ title }}</p>
    <div class="postman-params-toolbar">
      <span class="opt-tip" v-if="mode">key:value</span>
      <span v-else>
        <div class="bulk-select fl"></div>
        <div class="bulk-key bulk-bordor-left fl">Key</div>
        <div class="bulk-value bulk-bordor-left fl">Value</div>
      </span>
      <span class="opt-btn fr" @click="changeMode">
        {{ mode ? "Key-Value Edit" : "Bulk Edit" }}
      </span>
    </div>
    <div
      class="postman-params-content"
      :class="{ 'content-has-title': title != '' }"
    >
      <textarea
        v-model="content"
        v-if="mode"
        @blur="blurEvent"
        placeholder="Rows are separated by new lines Keys and values are separated by :  Prepend // to any row you want to ass but keep disabled"
      ></textarea>
      <div class="postman-params-content-bulk">
        <div class="bulk-item" v-for="(bulk, index) in bulks" :key="index">
          <div class="bulk-select fl">
            <el-checkbox v-model="bulk.enble" @change="submit"></el-checkbox>
          </div>
          <div class="bulk-key bulk-bordor-left fl">
            <input
              type="text"
              v-model="bulk.key"
              placeholder="key"
              @change="changeBulk(index)"
            />
          </div>
          <div class="bulk-value bulk-bordor-left fl">
            <input
              type="text"
              v-model="bulk.value"
              placeholder="value"
              @change="changeBulk(index)"
            />
          </div>
          <span class="opt-del-btn fl">
            <i class="el-icon-close" />
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default() {
        return "";
      },
    },
  },
  data() {
    return {
      bulks: [],
      content: "",
      mode: true,
    };
  },
  mounted() {
    this.addBulks();
  },
  methods: {
    addBulks() {
      this.bulks.push({ enble: false, key: "", value: "", init: true });
    },
    blurEvent() {
      this.toBulks();
      this.submit();
    },
    changeBulk(index) {
      var len = this.bulks.length - 1;
      var key = this.bulks[index].key;
      var value = this.bulks[index].value;
      var flag = key != "" || value != "";
      if (flag) {
        if (this.bulks[index].init) {
          this.bulks[index].init = false;
          this.bulks[index].enble = true;
        }
        if (index == len) {
          this.addBulks();
        }
      }
      this.submit();
    },
    submit() {
      var data = [];
      for (var i = 0; i < this.bulks.length; i++) {
        if (this.bulks[i].init) {
          continue;
        }
        if (
          this.bulks[i].enble &&
          this.bulks[i].key != "" &&
          this.bulks[i].value != ""
        ) {
          data.push({ key: this.bulks[i].key, value: this.bulks[i].value });
        }
      }
      if (data.length > 0) {
        this.$emit("submit", data);
      }
    },
    toBulks() {
      this.bulks = [];
      if (this.content != "") {
        var arr = this.content.split(/[(\r\n)\r\n]+/);
        if (arr && arr.length > 0) {
          for (var i = 0; i < arr.length; i++) {
            var index = arr[i].indexOf(":");
            var value = "";
            if (index > -1) {
              value = arr[i].substring(index + 1);
            }
            var key = arr[i].substring(0, index);
            var enble = true;
            if (key.startsWith("//")) {
              key = key.substring(2);
              enble = false;
            }
            this.bulks.push({
              enble: enble,
              key: key,
              value: value,
              init: false,
            });
          }
        }
      }
      this.addBulks();
    },
    changeMode() {
      if (this.mode) {
        this.toBulks();
      } else {
        var arr = [];
        for (var i = 0; i < this.bulks.length; i++) {
          var v = "";
          if (this.bulks[i].init) {
            continue;
          }
          if (!this.bulks[i].enble) {
            v = "//";
          }
          v += this.bulks[i].key;
          v += ":";
          v += this.bulks[i].value;
          arr.push(v);
        }
        this.content = arr.join("\r\n");
      }
      this.mode = !this.mode;
    },
  },
};
</script>

<style lang="less">
.postman-params {
  width: 100%;
  height: 100%;
  &-title {
    font-size: 12px;
    line-height: 25px;
    font-weight: 600;
    padding: 0px 10px 0px 10px;
  }
  &-toolbar {
    line-height: 33px;
    height: 35px;
    font-size: 13px;
    font-weight: 500;

    border-top: 1px solid #e4e7ed;
    border-bottom: 1px solid #e4e7ed;
    .opt-tip {
      padding-left: 10px;
    }
    .bulk-value {
      width: 60px !important;
    }
    .bulk-bordor-left {
      line-height: 25px;
    }

    .opt-btn {
      padding-right: 10px;
      color: #f2ad10;
      cursor: pointer;
      &:hover {
        color: #f0b839;
      }
    }
  }
  .bulk-select {
    width: 45px;
    padding: 7px 16px;
  }
  .bulk-key {
    width: 30%;
  }
  .bulk-value {
    width: calc(~"70% - 70px");
  }
  .bulk-bordor-left {
    height: 100%;
    border-left: 1px solid #e4e7ed;
    padding: 4px;
    input {
      width: 99%;
      outline: none;
      height: 24px;
      border: 1px solid transparent;
      background: transparent;
      &:focus {
        border: 1px solid #e4e7ed;
      }
    }
  }
  .bulk-item {
    height: 34px;
    border-bottom: 1px solid #e4e7ed;
    &:hover {
      .opt-del-btn {
        display: inline;
      }
    }
  }
  .content-has-title {
    height: calc(~"100% - 60px");
  }
  .postman-params-content:not(.content-has-title) {
    height: calc(~"100% - 35px");
  }
  .postman-params-content {
    width: 100%;
    &-bulk {
      width: 100%;
      height: 100%;
      overflow-y: auto;
      .opt-del-btn {
        line-height: 34px;
        font-size: 14px;
        display: none;
        &:hover {
          color: red;
        }
      }
    }
    textarea {
      width: 100%;
      height: 100%;
      border: none;
      background: transparent;
      outline: none;
      resize: none;
    }
  }
}
</style>
