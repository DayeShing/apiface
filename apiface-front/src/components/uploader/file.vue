<template>
  <div class="uploader-file" :status="status">
    <slot
      :file="file"
      :list="list"
      :status="status"
      :paused="paused"
      :error="error"
      :response="response"
      :average-speed="averageSpeed"
      :formated-average-speed="formatedAverageSpeed"
      :current-speed="currentSpeed"
      :is-complete="isComplete"
      :is-uploading="isUploading"
      :size="size"
      :formated-size="formatedSize"
      :uploaded-size="uploadedSize"
      :progress="progress"
      :progress-style="progressStyle"
      :progressing-class="progressingClass"
      :time-remaining="timeRemaining"
      :formated-time-remaining="formatedTimeRemaining"
      :type="type"
      :extension="extension"
      :file-category="fileCategory"
    >
      <div
        class="uploader-file-progress"
        :class="{
          progressingClass,
          'low-progress': progress <= 0.3,
          'middle-progress': progress > 0.3 && progress <= 0.6,
          'hight-progress': progress > 0.6 && progress < 1,
          'over-progress': progress == 1,
          validmdfine: mdstatus != '',
        }"
        :style="progressStyle"
      ></div>
      <div class="uploader-file-info">
        <div class="uploader-file-name">
          <svg class="self-icon" aria-hidden="true">
            <use v-if="fileCategory == 'folder'" xlink:href="#icon-file-b-2" />
            <use
              v-else-if="fileCategory == 'audio'"
              xlink:href="#icon-file-b-3"
            />
            <use
              v-else-if="fileCategory == 'video'"
              xlink:href="#icon-file-b-9"
            />
            <use
              v-else-if="fileCategory == 'image'"
              xlink:href="#icon-file-b-6"
            />
            <use
              v-else-if="fileCategory == 'document'"
              xlink:href="#icon-file-b-1"
            />
            <use v-else xlink:href="#icon-file-b-8" />
          </svg>
          {{ file.name }}
        </div>
        <div class="uploader-file-size">{{ formatedSize }}</div>
        <div class="uploader-file-status">
          <span v-show="status !== 'uploading'">{{ statusText }}</span>
          <span v-show="status === 'uploading'">
            <span>{{ progressStyle.progress }}</span>
            <em>{{ formatedAverageSpeed }}</em>
            <i>{{ formatedTimeRemaining }}</i>
          </span>
        </div>
        <div class="uploader-file-actions">
          <span class="uploader-file-pause" @click="pause"></span>
          <span class="uploader-file-resume" @click="resume">️</span>
          <span class="uploader-file-retry" @click="retry"></span>
          <span class="uploader-file-remove" @click="remove"></span>
        </div>
      </div>
    </slot>
  </div>
</template>

<script>
import Uploader from "simple-uploader.js";
import SparkMD5 from "./js/spark-md5.min.js";
const events = ["fileProgress", "fileSuccess", "fileComplete", "fileError"];
const fileStatusText = {
  success: "已上传",
  error: "上传错误",
  uploading: "上传中",
  paused: "暂停中",
  waiting: "等待中",
  valid: "校验文件",
  valided: "校验完成",
};
export default {
  name: "uploader-file",
  props: {
    file: {
      type: Object,
      default() {
        return {};
      },
    },
    list: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      response: null,
      paused: false,
      error: false,
      averageSpeed: 0,
      currentSpeed: 0,
      isComplete: false,
      isUploading: false,
      mdstatus: "",
      size: 0,
      formatedSize: "",
      uploadedSize: 0,
      progress: 0,
      timeRemaining: 0,
      type: "",
      extension: "",
      progressingClass: "",
    };
  },
  computed: {
    fileCategory() {
      const extension = this.extension;
      const isFolder = this.file.isFolder;
      let type = isFolder ? "folder" : "unknown";
      const categoryMap = this.file.uploader.opts.categoryMap;
      const typeMap = categoryMap || {
        image: ["gif", "jpg", "jpeg", "png", "bmp", "webp"],
        video: ["mp4", "m3u8", "rmvb", "avi", "swf", "3gp", "mkv", "flv"],
        audio: ["mp3", "wav", "wma", "ogg", "aac", "flac"],
        document: [
          "doc",
          "txt",
          "docx",
          "pdf",
          "csv",
          "xls",
          "xlsx",
          "ppt",
          "pptx",
        ],
      };
      Object.keys(typeMap).forEach((_type) => {
        const extensions = typeMap[_type];
        if (extensions.indexOf(extension) > -1) {
          type = _type;
        }
      });
      return type;
    },
    progressStyle() {
      const progress = Math.floor(this.progress * 100);
      const style = `translateX(${Math.floor(progress - 100)}%)`;
      return {
        progress: `${progress}%`,
        webkitTransform: style,
        mozTransform: style,
        msTransform: style,
        transform: style,
      };
    },
    formatedAverageSpeed() {
      return `${Uploader.utils.formatSize(this.averageSpeed)} / s`;
    },
    status() {
      const isUploading = this.isUploading;
      const isComplete = this.isComplete;
      const isError = this.error;
      const paused = this.paused;

      if (this.mdstatus != "") {
        return this.mdstatus;
      } else if (isComplete) {
        return "success";
      } else if (isError) {
        return "error";
      } else if (isUploading) {
        return "uploading";
      } else if (paused) {
        return "paused";
      } else {
        return "waiting";
      }
    },
    statusText() {
      const status = this.status;
      let txt = status;
      if (typeof fileStatusText === "function") {
        txt = fileStatusText(status, this.response);
      } else {
        txt = fileStatusText[status];
      }
      return txt || status;
    },
    formatedTimeRemaining() {
      const timeRemaining = this.timeRemaining;
      const file = this.file;
      if (timeRemaining === Number.POSITIVE_INFINITY || timeRemaining === 0) {
        return "";
      }
      let parsedTimeRemaining = this.secondsToStr(timeRemaining);
      const parseTimeRemaining = file.uploader.opts.parseTimeRemaining;
      if (parseTimeRemaining) {
        parsedTimeRemaining = parseTimeRemaining(
          timeRemaining,
          parsedTimeRemaining
        );
      }
      return parsedTimeRemaining;
    },
  },
  watch: {
    status(newStatus, oldStatus) {
      if (oldStatus && newStatus === "uploading" && oldStatus !== "uploading") {
        this.tid = setTimeout(() => {
          this.progressingClass = "uploader-file-progressing";
        }, 200);
      } else {
        clearTimeout(this.tid);
        this.progressingClass = "";
      }
    },
  },
  methods: {
    /**
     * 2
     */
    _actionCheck() {
      this.paused = this.file.paused;
      this.error = this.file.error;
      this.isUploading = this.file.isUploading();
    },
    pause() {
      if (this.mdstatus != "") {
        return;
      }
      this.file.pause();
      this._actionCheck();
      this._fileProgress();
    },
    resume() {
      this.file.resume();
      this._actionCheck();
    },
    remove() {
      this.file.cancel();
    },
    retry() {
      this.file.retry();
      this._actionCheck();
    },
    /**
     * 4
     */
    processResponse(message) {
      let res = message;
      try {
        res = JSON.parse(message);
      } catch (e) {}
      this.response = res;
    },
    fileEventsHandler(event, args) {
      const rootFile = args[0];
      const file = args[1];
      const target = this.list ? rootFile : file;
      if (this.file === target) {
        if (this.list && event === "fileSuccess") {
          this.processResponse(args[2]);
          return;
        }
        this[`_${event}`].apply(this, args);
      }
    },
    /**
     * 1
     */
    _fileProgress() {
      this.progress = this.file.progress();
      this.averageSpeed = this.file.averageSpeed;
      this.currentSpeed = this.file.currentSpeed;
      this.timeRemaining = this.file.timeRemaining();
      this.uploadedSize = this.file.sizeUploaded();
      this._actionCheck();
    },
    /**
     * 3
     */
    _fileSuccess(rootFile, file, message) {
      if (rootFile) {
        this.processResponse(message);
      }
      this._fileProgress();
      this.error = false;
      this.isComplete = true;
      this.isUploading = false;
    },
    /**
     * 5
     */
    _fileComplete() {
      this._fileSuccess();
    },
    _fileError(rootFile, file, message) {
      this._fileProgress();
      this.processResponse(message);
      this.error = true;
      this.isComplete = false;
      this.isUploading = false;
    },
    secondsToStr(temp) {
      const years = Math.floor(temp / 31536000);
      if (years) {
        return years + " year" + numberEnding(years);
      }
      const days = Math.floor((temp %= 31536000) / 86400);
      if (days) {
        return days + " day" + numberEnding(days);
      }
      const hours = Math.floor((temp %= 86400) / 3600);
      if (hours) {
        return hours + " hour" + numberEnding(hours);
      }
      const minutes = Math.floor((temp %= 3600) / 60);
      if (minutes) {
        return minutes + " minute" + numberEnding(minutes);
      }
      const seconds = temp % 60;
      return seconds + " second" + numberEnding(seconds);
      function numberEnding(number) {
        return number > 1 ? "s" : "";
      }
    },
    computeMD5(file) {
      let fileReader = new FileReader();
      let time = new Date().getTime();
      let blobSlice =
        File.prototype.slice ||
        File.prototype.mozSlice ||
        File.prototype.webkitSlice;
      let currentChunk = 0;
      const chunkSize = 1024 * 1024;
      let chunks = Math.ceil(file.size / chunkSize);
      let spark = new SparkMD5.ArrayBuffer();

      // 文件状态设为"计算MD5"
      file.pause();
      loadNext();
      // 设置状态

      fileReader.onload = (e) => {
        spark.append(e.target.result);

        if (currentChunk < chunks) {
          currentChunk++;
          loadNext();
          this.progress = Math.floor(currentChunk) / chunks;
          this.mdstatus = "valid";
        } else {
          let md5 = spark.end();
          this.progress = 0;
          this.mdstatus = "";
          Object.assign(file.uploader.opts, {
            query: {
              ...this.params,
            },
          });
          file.uniqueIdentifier = md5;
          file.resume();
        }
      };

      fileReader.onerror = function () {
        this.mdstatus = "valid";
        this.error = true;
      };

      function loadNext() {
        let start = currentChunk * chunkSize;
        let end =
          start + chunkSize >= file.size ? file.size : start + chunkSize;

        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end));
      }
      return file;
    },
    init() {
      this.file = this.computeMD5(this.file);
      const staticProps = ["paused", "error", "averageSpeed", "currentSpeed"];
      const fnProps = [
        "isComplete",
        "isUploading",
        {
          key: "size",
          fn: "getSize",
        },
        {
          key: "formatedSize",
          fn: "getFormatSize",
        },
        {
          key: "uploadedSize",
          fn: "sizeUploaded",
        },
        "progress",
        "timeRemaining",
        {
          key: "type",
          fn: "getType",
        },
        {
          key: "extension",
          fn: "getExtension",
        },
      ];
      staticProps.forEach((prop) => {
        this[prop] = this.file[prop];
      });
      fnProps.forEach((fnProp) => {
        if (typeof fnProp === "string") {
          this[fnProp] = this.file[fnProp]();
        } else {
          this[fnProp.key] = this.file[fnProp.fn]();
        }
      });
    },
    handler() {
      const handlers = (this._handlers = {});
      const eventHandler = (event) => {
        handlers[event] = (...args) => {
          this.fileEventsHandler(event, args);
        };
        return handlers[event];
      };
      events.forEach((event) => {
        this.file.uploader.on(event, eventHandler(event));
      });
    },
  },
  mounted() {
    this.init();
    this.handler();
  },
  destroyed() {
    events.forEach((event) => {
      this.file.uploader.off(event, this._handlers[event]);
    });
    this._handlers = null;
  },
};
</script>

<style lang="less">
.uploader-file {
  position: relative;
  height: 35px;
  line-height: 35px;
  overflow: hidden;
  border-bottom: 1px solid #cdcdcd;
}
.uploader-file[status="waiting"] .uploader-file-pause,
.uploader-file[status="uploading"] .uploader-file-pause {
  display: block;
}
.uploader-file[status="paused"] .uploader-file-resume {
  display: block;
}
.uploader-file[status="error"] .uploader-file-retry {
  display: block;
}
.uploader-file[status="success"] .uploader-file-remove {
  display: none;
}
.uploader-file[status="error"] .uploader-file-progress {
  background: #ffe0e0;
}
.uploader-file-progress {
  position: absolute;
  width: 100%;
  height: 100%;

  transform: translateX(-100%);
}
.low-progress:not(.validmdfine) {
  background: #f1fef4;
}
.low-progress {
  background: #fafee8;
}

.middle-progress:not(.validmdfine) {
  background: #defce5;
}
.middle-progress {
  background: #f8fedd;
}

.hight-progress:not(.validmdfine) {
  background: #cbf1d4;
}
.hight-progress {
  background: #eff6cf;
}
.over-progress {
  background: #b9ebc5;
}

.uploader-file-progressing {
  transition: all 0.4s linear;
}
.uploader-file-info {
  position: relative;
  z-index: 1;
  height: 100%;
  overflow: hidden;
}
.uploader-file-info:hover {
  background-color: rgba(240, 240, 240, 0.2);
}
.uploader-file-info i,
.uploader-file-info em {
  font-style: normal;
}
.uploader-file-name,
.uploader-file-size,
.uploader-file-status,
.uploader-file-actions {
  float: left;
  position: relative;
  height: 100%;
}
.uploader-file-name {
  width: 53%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  text-indent: 14px;
  .self-icon {
    width: 24px;
    height: 24px;
    margin-right: 6px;
    vertical-align: top;
    fill: currentColor;
    overflow: hidden;
    padding-top: 5px;
  }
}
.uploader-file-size {
  width: 13%;
  text-indent: 10px;
}
.uploader-file-status {
  width: 17%;
  text-indent: 20px;
}
.uploader-file-actions {
  width: 17%;
}
.uploader-file-actions > span {
  display: none;
  float: left;
  width: 16px;
  height: 16px;
  margin-top: 10px;
  margin-right: 8px;
  cursor: pointer;
  background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAABkCAYAAAD0ZHJ6AAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAAJcEhZcwAACxMAAAsTAQCanBgAAARkSURBVGje7ZnfS1NRHMAH4ptPkvQSuAdBkCxD8FUQJMEULUgzy1KyyPVQ4JMiiP4Bvg6EwUQQfMmwhwRDshwaKUjDVCgoSdDNHkzTJZ6+Z37Purve8+PeTb2TM/ggu+ew89l33x8H9BBCPG7GowXTJej3+wnDvEm0JuLC04+EYWftVAUv+fiCvDUdQR1BHUEdQR3BTIygvixoQS14XgTtthLVdpNWwXRLqvQ724LplFRtyrYF0yVpFLQrKRVMh6RZ0I6kkmCqklaCqpKZH0FX56Crq9jVfdDVk0RfFrSgFsxkQVmLcdKCVrKySCrryhPEyYShhzOcrFtG0EoilfHHk1CRU5rF6ZjNZhlVOW6RnMSVyyilKies4pO41diVy8wIujoHXV3FGdMHXTtJKLFYTLhZtq4vC1rwXApCZTIqgR6g1PBMCO9DL3bMMSqBHqDU8EyISDAHiGKvWwcCQG2KgjlAFCDAOhAAap0K5gKLphk8mqJgLrCIgoxRJ4J5wKpJ7gAoMkn5EBXBPGDVJHcAFJmkfIhQcAql1oBpTvTol9gG9pm4RHAKpdaAaU706JfYBvaZuJVgPQrt4sFlnOh5MC/p3lmJYD0K7eLBZZzoeTAv6d5ZnuAYHjpgEOnk5F0ufhG6v1ggOIaHDhhEOjl5l4tfhO4vthLcwAMrFNvLJO5vEwhu4IEViu1lEve3WQmyoihQFBzG/V0CQVYUBYqCw7i/SxTBcpsRbFeIYLnNCLZbCY5b5KAnxRwct8hBj9McZFVMW0ihRNBuFdMWUigRlFaxuQ9WWYjRMTiIe5z0wSoLMToGB3GPsA9aTZIJoB+nRgBnM1tzOkkmgH6cGgGczWzNpzqLx3n/aULJJgezeNw07oxQySbVywKjBOgFRnDs+VEsx8FlgVEC9AIjOPb8KJYjvSzoG7UW1IJaUAtqQS14toLNM5fN5APdwBJA8G83Pk/aK/rgzVvXzeQD3cASQPBvNz5P2ssTzAaGUIrHEO6zI5gNDKEUjyHcxxWkh4Ylcowwk1QQpIeGJXKMMJO0EgwqyjGCioJBJvDrxRMSuVOTJEXfbz1/bHwWtBL0yoQehK6RucgE+bGzanzulQh6E3IgQV+xpc8kcrfuSO7eTfJ3ZYmQw0Oy9azVKOk1C/bJ5D5F38YPeLfx0rjWJxHsS0SqsSYuxySjj5qO5Oj7xQWy2VBtFOwzCy6ryH3YfE3uh64Y1xckgstJPydEjkkeHv07Iy4Xaao15+KCWTBx6M/db+T9xivSErqaJDdzXI6yLRE8Vgg0coex/SPJvT0SbWu0KpZtbgSpCH3NRt7I5OxHkObc6heU+/M/J5vrpBFM5GBLqCQux14COXs5CNXK5OjPGm1tSMrJSOMNYQ4mVTGV/L6zTL7+DovkbFUxbSW0Wo05l8hJWsU+cRWfSh+Mt5Lb1ck/J1TvVsdDaR/MiEni+llsdZuZp62EViu+96bpNjNPWwmtVnzvFd5m9IVVC54x/wA7gNvqFG9vXQAAAABJRU5ErkJggg==")
    no-repeat 0 0;
  &:last-child {
    margin-right: 8px !important;
  }
}
.uploader-file-actions > span:hover {
  background-position-x: -21px;
}
.uploader-file-actions .uploader-file-pause {
  background-position-y: 0;
}
.uploader-file-actions .uploader-file-resume {
  background-position-y: -17px;
}
.uploader-file-actions .uploader-file-retry {
  background-position-y: -53px;
}
.uploader-file-actions .uploader-file-remove {
  display: block;
  background-position-y: -34px;
}
</style>
