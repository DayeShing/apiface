<template>
  <div
    class="uploader-box"
    :class="{
      'uploader-box-hidden': display == 'hidden',
      'uploader-box-min': display == 'min',
      'uploader-box-max': display == 'max',
    }"
  >
    <div class="progress-status" v-if="display == 'min'">
      <div class="progress__title" @dblclick="display = 'max'">
        {{ displayName }}
        <span class="progress__text">{{ progress }}%</span>
      </div>
      <el-progress :percentage="progress"></el-progress>
    </div>

    <div class="header-nav">
      <h2>文件列表</h2>
      <div class="operate-btn">
        <button
          type="button"
          @click="display = 'min'"
          class="el-button el-button--text el-button--small"
        >
          <span><i class="el-icon-d-caret"></i></span>
        </button>
        <button
          @click="display = 'hidden'"
          type="button"
          class="el-button el-button--text el-button--small"
        >
          <span><i class="el-icon-close"></i></span>
        </button>
      </div>
    </div>
    <slot :files="files" :file-list="fileList" :started="started">
      <ul class="file-list">
        <li v-for="(file, index) in files" :key="index">
          <uploader-file :file="file" :list="true"></uploader-file>
        </li>
      </ul>
    </slot>
  </div>
</template>

<script>
import Uploader from "simple-uploader.js";
import UploaderFile from "./file.vue";
export default {
  data() {
    return {
      uploader: this,
      dropEle: null,
      started: false,
      files: [],
      fileList: [],
      displayName: "计算中...",
      progress: 0,
      // hidden,min,max
      display: "hidden",
    };
  },
  props: {
    options: {
      type: Object,
      default() {
        return {};
      },
    },
    autoStart: {
      type: Boolean,
      default: true,
    },
  },
  mounted() {},
  created() {
    this.options.initialPaused = !this.autoStart;
    this.uploader = new Uploader(this.options);
    this.uploader.on("catchAll", this.allEvent);
    this.uploader.on("fileAdded", this.fileAdded);
    this.uploader.on("filesAdded", this.filesAdded);
    this.uploader.on("fileRemoved", this.fileRemoved);
    this.uploader.on("filesSubmitted", this.filesSubmitted);
  },
  methods: {
    uploadStart() {
      this.started = true;
    },
    fileAdded(file) {
      this.$emit("fileAdded", file);
      this.display = "max";
      if (file.ignored) {
        return false;
      }
    },
    filesAdded(files, fileList) {
      this.$emit("filesAdded", files, fileList);
      if (files.ignored || fileList.ignored) {
        return false;
      }
    },
    fileRemoved(file) {
      this.files = this.uploader.files;
      this.fileList = this.uploader.fileList;
      this.$emit("cancel", file);
    },
    filesSubmitted(files, fileList) {
      this.files = this.uploader.files;
      this.fileList = this.uploader.fileList;
      if (this.autoStart) {
        this.uploader.upload();
      }
    },
    fileComplete() {
      this.display = "hidden";
    },
    fileProgress(rootFile, file, chunk) {
      if (this.display == "min") {
        this.displayName = file.name;
        this.progress = parseInt((file.progress() * 100).toFixed(0));
        if (this.progress == 100) {
          this.displayName = "计算中...";
          this.progress = 0;
        }
      }
    },
    allEvent(...args) {
      const name = args[0];
      const EVENTSMAP = {
        fileAdded: true,
        filesAdded: true,
        uploadStart: "uploadStart",
        fileProgress: "fileProgress",
        fileComplete: "fileComplete",
      };
      const handler = EVENTSMAP[name];
      if (handler) {
        if (handler === true) {
          return;
        }
        this[handler].apply(this, args.slice(1));
      }
      this.$emit.apply(this, args);
    },
    /**
     * 绑定打开文件选择器对象
     * @param ref 关联对象
     * @param folder 是否文件夹
     * @param attrs 筛选
     */
    assignBrowse(ref = undefined, folder = true, attrs = {}) {
      if (ref != undefined) {
        this.uploader.assignBrowse(ref, folder, false, attrs);
      }
    },
    onDragEnter() {
      this.$emit("onDragEnter");
    },
    onDragLeave() {
      this.$emit("onDragLeave");
    },
    onDrop() {
      this.$emit("onDrop");
    },
    assignDrop(ref = undefined) {
      if (ref != undefined) {
        this.dropEle = ref;
        this.uploader.assignDrop(this.dropEle);
        this.uploader.on("dragenter", this.onDragEnter);
        this.uploader.on("dragleave", this.onDragLeave);
        this.uploader.on("drop", this.onDrop);
      }
    },
  },
  beforeDestroy() {
    if (this.dropEle) {
      this.uploader.off("dragenter", this.onDragEnter);
      this.uploader.off("dragleave", this.onDragLeave);
      this.uploader.off("drop", this.onDrop);
      this.uploader.unAssignDrop(this.dropEle);
      this.dropEle = null;
    }
    this.uploader.off("catchAll", this.allEvent);
    this.uploader.off("fileAdded", this.fileAdded);
    this.uploader.off("filesAdded", this.filesAdded);
    this.uploader.off("fileRemoved", this.fileRemoved);
    this.uploader.off("filesSubmitted", this.filesSubmitted);
    this.uploader = null;
  },
  components: { UploaderFile },
};
</script>

<style lang="less">
.uploader-box {
  width: 520px;
  position: fixed;
  z-index: 20;
  right: 10px;
  bottom: 45px;

  .header-nav {
    display: none;
  }
  .file-list {
    display: none;
  }
}
.uploader-box-hidden {
  display: none;
}
.uploader-box-min {
  width: 250px !important;
  .progress-status {
    width: 100%;
    padding: 8px 8px 16px 8px;
    .el-progress {
      .el-progress-bar {
        padding-right: 0px !important;
      }
      .el-progress__text {
        display: none;
      }
    }
    .progress__title {
      text-align: left;
      .progress__text {
        display: block;
        text-align: right;
        float: right;
      }
    }
  }
}
.uploader-box-max {
  background-color: #fff;
  border: 1px solid #e2e2e2;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  .header-nav {
    display: -ms-flexbox !important;
    display: flex !important;
    height: 40px;
    line-height: 40px;
    padding: 0 15px;
    border-bottom: 1px solid #ddd;
    .operate-btn {
      -ms-flex: 1;
      flex: 1;
      text-align: right;
    }
  }
  .file-list {
    display: block !important;
    position: relative;
    height: 240px;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: #fff;
  }
}
</style>
