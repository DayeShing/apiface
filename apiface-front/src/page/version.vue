/** * 版本 * author:Daye Shing */
<template>
  <div class="verison">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ $t("page.v.info") }}</span>
      </div>
      <div class="verison-main">
        <el-carousel :interval="5000" height="170px" indicator-position="none">
          <el-carousel-item v-for="item in demos" :key="item">
            <img :src="item" />
          </el-carousel-item>
        </el-carousel>
        <div class="vinfo">
          <p>{{ $t("page.v.v") }}</p>
          <p>{{ $t("page.v.u") }}</p>
          <p>{{ $t("page.v.a") }}</p>
          <p>{{ $t("page.v.s") }}</p>
          <p>
            {{ $t("page.v.teams") }} :
            <el-link type="primary" href="mailto:896379914@qq.com">
              {{ $t("page.v.self") }}
            </el-link>
          </p>
        </div>
        <div class="suggest">
          <el-divider content-position="left">
            {{ $t("page.v.feel") }}
          </el-divider>
          <el-form
            ref="form"
            :model="suggest"
            :rules="formFules"
            label-suffix=":"
            label-width="75px"
          >
            <el-form-item :label="$t('page.v.name')">
              <el-input
                v-model="suggest.nickname"
                :placeholder="$t('page.v.namep')"
                :maxlength="30"
              ></el-input>
            </el-form-item>
            <el-form-item :label="$t('page.v.suggest')" prop="descripter">
              <el-input
                type="textarea"
                :rows="7"
                :maxlength="300"
                :placeholder="$t('page.v.suggestp')"
                v-model="suggest.descripter"
              ></el-input>
            </el-form-item>
            <el-form-item :label="$t('page.v.score')" class="no-margin">
              <el-rate v-model="suggest.score" show-text></el-rate>
            </el-form-item>
            <div style="float: right;">
              <el-button
                type="primary"
                @click="saveData"
                :disabled="btnState"
                size="mini"
                >{{ $t("page.v.submit") }}</el-button
              >
            </div>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
export default {
  data() {
    return {
      btnState: false,
      suggest: {
        nickname: "",
        descripter: "",
        score: 5,
      },
      showViewer: false, // 显示查看器
      demos: [
        "demo/e1.png",
        "demo/e2.png",
        "demo/z1.png",
        "demo/z2.png",
        "demo/z3.png",
        "demo/z4.png",
        "demo/z5.png",
        "demo/z6.png",
        "demo/u1.png",
        "demo/u2.png",
        "demo/u3.png",
      ],
      formFules: {
        descripter: [
          {
            required: true,
            message: this.$t("page.v.notEmpty"),
            trigger: "blur",
          },
          {
            min: 15,
            max: 300,
            message: this.$t("page.v.min"),
            trigger: "blur",
          },
        ],
      },
    };
  },

  methods: {
    onPreview() {
      this.showViewer = true;
    },
    // 关闭查看器
    closeViewer() {
      this.showViewer = false;
    },
    saveData() {
      this.$refs.form.validate((validate) => {
        if (validate) {
          this.submit();
        } else {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      var u = "http://self.daysh.top:8102/app/auth/discuss/save";
      this.$fetch(
        this.$store.state.proxy != "" ? this.$store.state.proxy : u,
        "post",
        this.suggest,
        {
          "Content-Type": "application/json",
          "Self-Agent-Address": u,
        }
      )
        .then((res) => {
          this.btnState = false;
          this.$message({
            type: "success",
            message: this.$t("page.v.feel"),
          });
        })
        .catch((err) => {
          this.btnState = false;
          this.$message({
            type: "error",
            message: this.$t("code.fetchError"),
          });
        });
    },
  },
};
</script>
<style lang="less">
.verison {
  width: 100%;
  height: 100%;
  .verison-main {
    position: relative;
    width: 100%;
    height: 100%;
    img {
      height: 170px;
    }
    .vinfo {
      line-height: 25px;
      color: #333;
      font-size: 14px;
      padding: 20px 15px;
    }
    .suggest {
      height: 300px;
      width: 100%;
      position: absolute;
      bottom: 0px;
      z-index: 99;
      background-color: #fff;
      .el-form-item__label {
        font-weight: normal !important;
      }
      .no-margin {
        margin-bottom: 2px !important;
      }
      textarea {
        resize: none;
      }
    }
  }
  .el-card {
    height: 100%;
    border: none;
    .el-card__header {
      height: 42px;
      line-height: 42px;
      padding: 0 10px;
      color: #333;
      font-size: 14px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
    }
    .el-card__body {
      padding: 6px !important;
      height: calc(~"100% - 43px");
    }
  }
}
</style>
