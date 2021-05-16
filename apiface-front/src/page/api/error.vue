<template>
  <div class="feelback-error" v-if="error" :class="{'feelback-error-shadow':max}">
    <!-- 标题栏 -->
    <el-button type="danger" v-show="!max" @click="showMax()" class="fr">反馈内容</el-button>
    <transition name="el-fade-in" @enter="goIn">
      <div class="feelback-error-info" v-show="max">
        <!-- 标题栏 -->
        <div class="feelback-error-info-header">
          <span>文档错误反馈</span>
          <el-button type="danger" @click="showMax()" class="fr">收起内容</el-button>
        </div>

        <!-- 反馈流程 -->
        <div
          class="feelback-error-info-body"
          v-infinite-scroll="loadFeelbackProgress"
          infinite-scroll-disabled="loading"
          infinite-scroll-distance="30"
        >
          <p class="info-desc">{{feelback.description}}</p>
          <div class="info-node">
            <el-timeline :reverse="reverse">
              <el-timeline-item
                v-if="base"
                :type="base[0].type"
                :color="base[0].color"
                :size="base[0].size"
                :icon="base[0].icon"
                :timestamp="base[0].timestamp"
              >{{base[0].content}}</el-timeline-item>

              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :type="activity.type"
                :color="activity.color"
                :size="activity.size"
                :icon="activity.icon"
                :timestamp="activity.timestamp"
              >{{activity.content}}</el-timeline-item>

              <el-timeline-item
                v-if="base"
                :type="base[1].type"
                :color="base[1].color"
                :size="base[1].size"
                :icon="base[1].icon"
                :timestamp="base[1].timestamp"
              >{{base[1].content}}</el-timeline-item>
            </el-timeline>
          </div>

          <!-- 表单 -->
          <el-form
            ref="reform"
            :model="mod"
            v-if="feelback.status != '4' && !feelback.result"
            :rules="formRules"
          >
            <el-form-item prop="description">
              <el-input
                type="textarea"
                placeholder="请输入内容"
                v-model="mod.description"
                maxlength="400"
                :rows="6"
                show-word-limit
              ></el-input>
            </el-form-item>
            <el-form-item prop="status">
              <el-radio v-model="mod.status" label="1">参数错误</el-radio>
              <el-radio v-model="mod.status" label="2">路径错误</el-radio>
              <el-radio v-model="mod.status" label="3">操作错误</el-radio>
              <el-radio v-model="mod.status" label="4">其他错误</el-radio>
              <el-switch
                :width="36"
                v-model="mod.result"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="成功解决"
                inactive-text="未解决"
              ></el-switch>
              <el-button @click="saveData" type="primary" class="fr" :disabled="btnState">提交</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </transition>
  </div>
</template>
<script>
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "feelback-error",
  components: {},
  data() {
    return {
      max: false,
      loading: false,
      noMore: false,
      page: 1,
      isPage: false,
      btnState: false,
      mod: {
        description: "",
        status: "1",
        result: false
      },
      feelback: {
        feelbackId: "",
        description: "",
        status: "0",
        result: null
      },
      reverse: false,
      base: null,
      activities: [],
      formRules: {
        description: [
          { required: true, message: "请输入描述内容", trigger: "blur" },
          {
            min: 1,
            max: 300,
            message: "长度为 1 到 300 个字符",
            trigger: "blur"
          }
        ]
      }
    };
  },
  props: {
    error: {
      type: Boolean,
      default() {
        return false;
      }
    },
    feelbackId: {
      type: String,
      default() {
        return "";
      }
    }
  },
  watch: {
    feelbackId(val, oldVal) {
      console.log(val, "dsdjkjjjdjfdjffhjfkdfjhjdfhkhdkfjghkdjf");
      this.base = null;
      this.activities = [];
      this.goIn();
    }
  },
  computed: {},
  created() {},
  methods: {
    saveData() {
      this.$refs.reform.validate(validate => {
        if (validate) {
          this.submit();
        } else {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      this.mod.feelbackId = this.feelback.feelbackId;
      this.$fetch(inter.back.progress.save, this.mod, {
        headers: { "Content-Type": "application/json" }
      }).then(res => {
        this.btnState = false;
        if (res && res.success) {
          if (res.data.result) {
            this.feelback.result = res.data.result;
          }

          this.mod.description = "";
          this.activities.push({
            content: res.data.description,
            timestamp: res.data.updateTime,
            size: "large",
            type: "info"
          });
        } else {
          this.$message({ type: "error", message: res.message });
        }
      });
    },
    showMax() {
      this.max = !this.max;
    },
    goIn() {
      console.log(this.feelbackId, "dsdjkjjjdjfdjffhjfkdfjhjdfhkhdkfjghkdjf");
      if (this.feelbackId != "") {
        this.page = 1;
        this.$fetch(
          inter.back.get,
          Qs.stringify(
            { feelbackId: this.feelbackId },
            { arrayFormat: "repeat" }
          ),
          { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
        ).then(res => {
          if (res && res.success) {
            this.feelback = res.data;
            this.base = [
              {
                content: "开始创建",
                timestamp: res.data.createTime,
                size: "large",
                type: "info",
                icon: "el-icon-star-off"
              },
              {
                content: "当前处理流程",
                timestamp: this.datetimeFormat(
                  new Date(),
                  "yyyy-MM-dd hh:mm:ss"
                ),
                size: "large",
                type: "primary",
                icon: "el-icon-help"
              }
            ];
            if (!this.isPage) {
              this.loadFeelbackProgress();
            }
          } else {
            this.msg(res);
          }
        });
      }
    },
    loadFeelbackProgress() {
      if (this.feelback == "") {
        return;
      }
      if (!this.noMore) {
        this.loading = true;
        this.$fetch(
          inter.back.progress.list,
          Qs.stringify(
            {
              page: this.page,
              size: 10,
              isPage: this.isPage,
              roll: true,
              feelbackId: this.feelback.feelbackId
            },
            { arrayFormat: "repeat" }
          )
        )
          .then(res => {
            this.loading = false;
            if (res && res.success) {
              for (let i = 0; i < res.data.length; i++) {
                this.activities.push({
                  content: res.data[i].description,
                  timestamp: res.data[i].updateTime,
                  size: "large",
                  type: "info"
                });
              }
              if (res.next) {
                this.page += 1;
              }
              this.noMore = !res.next || false;
              return;
            }
            this.noMore = true;
            this.$message.error(res.message);
          })
          .catch(err => {
            this.noMore = true;
            console.log(err);
          });
      }
    }
  },
  watch: {}
};
</script>
<style lang="less">
.feelback-error-shadow {
  box-shadow: 0 4px 17px rgba(0, 0, 0, 0.3);
  width: 600px;
  height: 400px;
}
.feelback-error {
  transition: 0.3s;
  z-index: 9;
  border-radius: 3px;
  background: transparent;
  .el-button--small,
  .el-button--small.is-round {
    padding: 6px 10px;
    margin: 3px;
  }
  &-info {
    width: 100%;
    height: 100%;
    background-color: #fff;
    border-radius: 3px;
    &-header {
      line-height: 32px;
      padding-left: 10px;
      border-bottom: 1px solid #dcdfe6;
    }
    &-body {
      width: 100%;
      height: 367px;
      padding: 5px 8px;
      overflow-y: auto;
      .info-desc {
        background: #f7f7fc;
        border-radius: 5px;
        padding: 8px 12px;
      }
      .info-node {
        padding: 8px 12px 4px 12px;
        .el-timeline-item:last-child {
          padding-bottom: 0px !important;
        }
      }

      .el-form {
        padding: 4px 12px;
        .el-form-item__label {
          line-height: 32px !important;
        }

        .el-form-item--mini.el-form-item,
        .el-form-item--small.el-form-item {
          .el-switch__label * {
            font-size: 12px !important;
          }
          &:last-child {
            margin-bottom: 0px !important;
          }
        }
      }
    }
  }
}
</style>
