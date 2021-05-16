<template>
  <div class="notice">
    <div class="notice-attr">
      <i class="fa fa-bell-o" aria-hidden="true" v-show="type =='notice'"></i>
      <i class="fa fa-commenting-o" aria-hidden="true" v-show="type !='notice'"></i>
      {{type =='notice'?"通知":"消息"}}
    </div>
    <div class="notice-timeline" v-show="model == 'drawer'">
      <el-card class="box-card" shadow="hover" v-for="(activity, index) in activities" :key="index">
        <div slot="header" class="clearfix">
          <span class="header-title">{{activity.subject}}</span>
          <span class="fr header-time">{{activity.timestamp}}</span>
        </div>
        <pre class="text-item">{{activity.content}}</pre>
      </el-card>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  props: {
    activities: {
      type: Array,
      default: []
    },
    model: {
      type: String,
      default: "drawer"
    },
    type: {
      type: String,
      default: "notice"
    }
  },
  mounted() {},
  computed: {},
  created() {},
  methods: {}
};
</script>
<style lang = "less">
.notice {
  width: 100%;
  height: 100%;
  .notice-attr {
    width: 100%;
    line-height: 40px;
    padding-left: 20px;
    font-size: 15px;
    font-weight: 500;
  }
  .notice-timeline {
    padding: 8px;
    .el-card {
      margin-top: 8px;
    }
    .el-card__header {
      padding: 10px 12px !important;
      span {
        color: #999;
        .header-title {
          font-size: 13px;
        }
      }
    }
    .el-card__body {
      padding: 12px;
      pre {
        white-space: pre-wrap;
        word-wrap: break-word;
        margin: 0px !important;
        font-size: 15px;
        color: #666;
        font-weight: 600;
      }
    }
  }
}
</style>