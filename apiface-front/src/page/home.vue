<template>
  <div class="module-home">
    <el-row class="version-info">
      <el-col class="version-info-longer">
        <el-row class="base-analyz">
          <el-col :span="6" v-for="(item, index) in baseCard" :key="index">
            <el-card shadow="hover">
              <div slot="header" class="clearfix">
                <span>{{ item.title }}</span>
                <span
                  class="title-badge"
                  :class="{
                    'title-badge-blue': index == 0,
                    'title-badge-green': index == 1,
                    'title-badge-yellow': index == 2,
                    'title-badge-red': index == 3,
                  }"
                  >{{ item.unit }}</span
                >
              </div>
              <p class="content-info">
                {{ item.content }}
              </p>
              <span class="content-span"><i class="fa fa-bullhorn"></i></span>
              <span class="content-span">{{ item.ano }}</span>
            </el-card>
          </el-col>
        </el-row>
        <div class="version-info-detail">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>迭代记录</span>
            </div>
            <div class="echart-main" id="version-id"></div>
          </el-card>
        </div>
      </el-col>
      <el-col class="version-info-small">
        <div style="height:266px">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>版本信息</span>
              <el-button
                type="text"
                style="float: right; padding: 3px 0;line-height: 35px;"
                @click="onClick(3)"
                >更新日志</el-button
              >
            </div>
            <table class="version-table">
              <colgroup>
                <col width="100" />
                <col />
              </colgroup>
              <tbody>
                <tr v-for="(item, key) in versionInfo" :key="key">
                  <td>{{ item.title }}</td>
                  <td>{{ item.text }}</td>
                </tr>
                <tr>
                  <td>获取渠道</td>
                  <td style="padding: 0px;">
                    <el-button
                      type="danger"
                      size="mini"
                      style="margin: 0px 0px 0px 15px;"
                      @click="onClick(1)"
                      >获取文档</el-button
                    >
                    <el-button type="success" size="mini" @click="onClick(2)"
                      >立即下载</el-button
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </el-card>
        </div>
        <div style="height:323px;padding-top:6px;">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>开发周期</span>
            </div>
            <div class="echart-main" id="create-id"></div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <el-row class="base-info">
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>用户留言</span>
          </div>
          <div
            class="user-suggest"
            v-infinite-scroll="loadMore"
            infinite-scroll-disabled="loading"
            infinite-scroll-distance="20"
          >
            <ul>
              <li v-for="(item, key) in activities" :key="key">
                <span class="user-title">{{
                  item.nickname ? item.nickname : item.updateUserName
                }}</span>
                <p>
                  {{ item.descripter }}
                </p>
                <el-rate
                  v-model="item.score"
                  disabled
                  :show-text="false"
                  text-color="#ff9900"
                ></el-rate>
                <span class="user-time">{{
                  item.updateTime.substring(0, 16)
                }}</span>
              </li>
            </ul>
            <el-divider v-if="loading">努力加载中...</el-divider>
            <el-divider v-if="noMore">没有更多了</el-divider>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>错误反馈</span>
          </div>
          <feelback :mode="false" />
        </el-card>
      </el-col>
    </el-row>
    <!-- 版权信息 -->
    <div class="tip-foot">
      <el-link
        href="http://self.daysh.top"
        type="info"
        :underline="false"
        target="_blank"
        >{{ $t("page.v.self") }}</el-link
      >
    </div>
  </div>
</template>

<script>
import FileSaver from "file-saver";
import echarts from "echarts";
import "echarts/map/js/china.js";
import Feelback from "page/views/feelback.vue";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "home",
  data() {
    return {
      baseCard: [
        {
          title: "累计访问量",
          content: "9999",
          unit: "-/次",
          ano: "系统接受的全部访问次数",
        },
        {
          title: "成功访问量",
          content: "9000",
          unit: "-/次",
          ano: "成功访问系统的次数",
        },
        {
          title: "成功拦截量",
          content: "9999",
          unit: "-/次",
          ano: "系统拦截越权访问的次数",
        },
        {
          title: "访问拦截率",
          content: "11.11",
          unit: "-%",
          ano: "越权访问与成功访问之比",
        },
      ],
      versionInfo: [
        { title: "当前版本", text: "snow white(白雪公主)" },
        { title: "后端框架", text: "springboot2.2.5" },
        { title: "前端框架", text: "vue2.x/elementUI-2.13" },
        { title: "主要特色", text: "极简 / 易操作 / 单页面" },
        { title: "开发团队", text: "土豆联盟" },
      ],
      versions: [
        "修改Swagger前端页面",
        "修改Swagger后端返回内容",
        "重构Swagger前端UI界面",
        "解决axios动态路径访问问题",
        "添加代理解决访问不同后端跨域",
        "发布第一个版本，后端还是Swagger",
        "封装各种前端组件",
        "基本实现页面中的所有操作流程",
        "添加前端非编译动态配置",
        "发布第二个版本，后端也还是Swagger",
        "无法容忍Swagger变态的编写文档方式",
        "自定义编写javadoc生成APIs文档",
        "实现源码扫描文档注释",
        "自定义文档结构，并生成接口文档",
        "添加swagger文档转换器，转换文档结构",
        "前端添加JSON显示支持",
        "发布第三个版本，换掉swagger后端",
        "编写maven插件，让插件帮忙生成文档",
        "新增接口文档范例模块",
        "添加springMvc支持",
        "升级大版本",
        "添加protobuf压缩文档依据",
        "前端更换vuecli",
        "前后端更新所有依赖到较新版本",
        "添加后端注释文档对数组的支持",
        "修改前端的部分显示问题",
        "源码扫描添加gradle插件支持",
        "自定义文档更多描述注释",
        "前端添加应用模式对接apiface后端",
        "编写文档管理后端应用apiface",
        "添加文档管理页面，错误反馈页面等",
        "换掉原来的前端计算后端返回的文档结构",
        "添加邮件支持",
        "添加WEBSOCKET支持",
        "添加消息机制",
        "发布第一个版本",
        "添加扫描无注释文档的模型属性",
        "不改变源码的情况下添加旧代码对文档描述的支持",
        "添加接口调试工具",
        "完成新版本的编写",
      ],
      activities: [],
      loading: false,
      noMore: false,
      page: 1,
      size: 10,
    };
  },
  props: {
    currentUser: {
      type: Object,
      default() {
        return {};
      },
    },
    permitty: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  components: {
    Feelback,
  },
  mounted() {
    this.versionId();
    this.createId();
    this.overview();
  },
  methods: {
    onClick(index) {
      if (index == 1) {
        this.$router.push({ name: "face", params: { type: "api", id: "1" } });
        return;
      }
      if (index == 2) {
        var a = document.createElement("a");
        a.href = "https://github.com/DayeShing/apiface";
        a.target = "_blank";
        a.click();
        return;
      }
      var blob = new Blob([JSON.stringify(this.versions)], {
        type: "text/plain;charset=utf-8",
      });
      FileSaver.saveAs(blob, "version.json");
    },
    overview() {
      this.$fetch(inter.analyze.apiStat).then((res) => {
        if (res && res.success) {
          this.baseCard[0].content = res.data.successfully + res.data.failure;
          this.baseCard[1].content = res.data.successfully;
          this.baseCard[2].content = res.data.failure;
          this.baseCard[3].content =
            Math.round((res.data.failure * 1000000) / res.data.successfully) /
            10000;
        } else {
          this.msg(res);
        }
      });
    },
    loadMore() {
      var url = inter.auth.discuss.list;
      var pro = !this.$store.state.app && this.$store.state.proxy != "";
      if (pro) {
        url = "http://apiface.daysh.top" + url;
      }
      let $this = this;
      if (!$this.noMore) {
        $this.loading = true;
        $this
          .$fetch(
            pro ? this.$store.state.proxy : url,
            Qs.stringify({
              page: $this.page,
              rows: $this.size,
              roll: true,
            }),
            { headers: { "Self-Agent-Address": url } }
          )
          .then((res) => {
            if (res.success) {
              if (res.data) {
                for (let i = 0; i < res.data.length; i++) {
                  $this.activities.push(res.data[i]);
                }
              }
              if (res.next) {
                $this.page += 1;
              }
              $this.noMore = !res.next;
            } else {
              $this.noMore = true;
            }
            $this.loading = false;
          });
      }
    },
    versionId() {
      var versionTitle = this.versions;
      var data = [];
      for (var i = 0; i < versionTitle.length; i++) {
        data.push([i]);
      }
      var position = [
        [
          [4.4, "添加代理解决访问不同后端跨域", "2019-11-10", "1.1.0-snap"],
          [21.4, "添加protobuf压缩文档依据", "2020-5-01", "2.0.0-snap"],
          [29.4, "编写文档管理后端应用apiface", "2020-5-16", "2.1.0-snap"],
        ],
        [
          [3.4, "封装各种前端组件", "2019-11-05", "1.1.0-snap"],
          [6.4, "封装各种前端组件", "2019-11-20", "1.2.0-snap"],
          [7.4, "基本实现页面中的所有操作流程", "2019-11-21", "1.2.0-snap"],
          [19.4, "添加springMvc支持", "2020-04-70", "1.4.0-snap"],
          [25.4, "修改前端的部分显示问题", "2020-05-04", "2.0.0-snap"],
          [26.4, "源码扫描添加gradle插件支持", "2020-05-05", "2.0.0-snap"],
        ],
        [
          [0.4, "修改Swagger前端页面", "2019-10-05", "1.0.0-snap"],
          [5.4, "发布第一个版本，后端还是Swagger", "2019-11-15", "1.2.0-snap"],
          [
            9.4,
            "发布第二个版本，后端也还是Swagger",
            "2019-11-30",
            "1.3.0-snap",
          ],
          [16.4, "发布第三个版本，换掉swagger后端", "2020-02-30", "1.4.0-snap"],
          [20.4, "升级大版本", "2020-04-30", "2.0.0-snap"],
          [27.4, "修改版本号", "2020-05-13", "2.1.0-snap"],
          [35.4, "发布第一个版本，后端是文档管理", "2021-04-24", "2.2.0-snap"],
        ],
        [
          [11.9, "实现源码扫描文档注释", "2020-01-05", "1.3.0-snap"],
          [14.9, "前端添加JSON显示支持", "2020-01-10", "1.3.0-snap"],
        ],
        [
          [31.9, "添加邮件支持", "2021-04-22", "2.1.0-snap"],
          [32.9, "添加WEBSOCKET支持", "2021-04-23", "2.1.0-snap"],
        ],
        [
          [
            30.9,
            "换掉原来的前端计算后端返回的文档结构",
            "2021-04-18",
            "2.1.0-snap",
          ],
        ],
      ];

      var colors = ["#666", "rgba(0,191,183,1)"];
      var icons = [
        "path://M31.604 4.203c-3.461-2.623-8.787-4.189-14.247-4.189-6.754 0-12.257 2.358-15.099 6.469-1.335 1.931-2.073 4.217-2.194 6.796-0.108 2.296 0.278 4.835 1.146 7.567 2.965-8.887 11.244-15.847 20.79-15.847 0 0-8.932 2.351-14.548 9.631-0.003 0.004-0.078 0.097-0.207 0.272-1.128 1.509-2.111 3.224-2.846 5.166-1.246 2.963-2.4 7.030-2.4 11.931h4c0 0-0.607-3.819 0.449-8.212 1.747 0.236 3.308 0.353 4.714 0.353 3.677 0 6.293-0.796 8.231-2.504 1.736-1.531 2.694-3.587 3.707-5.764 1.548-3.325 3.302-7.094 8.395-10.005 0.292-0.167 0.48-0.468 0.502-0.804s-0.126-0.659-0.394-0.862z", //树叶
        "path://M0 32c4-12 14.469-32 32-32-8.219 6.594-12 22-18 22s-6 0-6 0l-6 10h-2z", //硬笔
        "path://M54.227,12.611c-0.338-0.336-0.736-0.505-1.196-0.505c-0.229,0-0.712,0.188-1.446,0.559  c-0.735,0.372-1.515,0.786-2.336,1.248c-0.823,0.459-1.797,0.875-2.921,1.247c-1.123,0.371-2.163,0.559-3.12,0.559  c-0.884,0-1.664-0.168-2.336-0.505c-2.229-1.044-4.168-1.823-5.814-2.337c-1.646-0.513-3.416-0.771-5.311-0.771  c-3.272,0-6.999,1.064-11.177,3.188c-0.862,0.43-1.48,0.763-1.88,1.007l-0.397-2.911c0.897-0.779,1.476-1.914,1.476-3.195  c0-2.347-1.902-4.249-4.249-4.249c-2.347,0-4.249,1.902-4.249,4.249c0,1.531,0.818,2.862,2.032,3.61l5.74,42.09  c0.171,1.253,1.243,2.162,2.474,2.162c0.112,0,0.226-0.007,0.341-0.022c1.368-0.188,2.326-1.447,2.139-2.815L19.69,38.303  c4.186-2.077,7.807-3.124,10.853-3.124c1.293,0,2.554,0.193,3.783,0.583c1.23,0.391,2.253,0.815,3.067,1.274  c0.814,0.46,1.775,0.886,2.88,1.274c1.107,0.39,2.2,0.585,3.279,0.585c2.726,0,5.991-1.027,9.796-3.08  c0.478-0.248,0.828-0.492,1.049-0.731c0.221-0.239,0.332-0.579,0.332-1.021V13.806C54.729,13.347,54.562,12.948,54.227,12.611z", //旗子
        "path://M31.342 25.559l-14.392-12.336c0.67-1.259 1.051-2.696 1.051-4.222 0-4.971-4.029-9-9-9-0.909 0-1.787 0.135-2.614 0.386l5.2 5.2c0.778 0.778 0.778 2.051 0 2.828l-3.172 3.172c-0.778 0.778-2.051 0.778-2.828 0l-5.2-5.2c-0.251 0.827-0.386 1.705-0.386 2.614 0 4.971 4.029 9 9 9 1.526 0 2.963-0.38 4.222-1.051l12.336 14.392c0.716 0.835 1.938 0.882 2.716 0.104l3.172-3.172c0.778-0.778 0.731-2-0.104-2.716z", //扳手
        "path://M29 4h-26c-1.65 0-3 1.35-3 3v20c0 1.65 1.35 3 3 3h26c1.65 0 3-1.35 3-3v-20c0-1.65-1.35-3-3-3zM12.461 17.199l-8.461 6.59v-15.676l8.461 9.086zM5.512 8h20.976l-10.488 7.875-10.488-7.875zM12.79 17.553l3.21 3.447 3.21-3.447 6.58 8.447h-19.579l6.58-8.447zM19.539 17.199l8.461-9.086v15.676l-8.461-6.59z", //邮件
        "path://M0 26h32v4h-32zM4 18h4v6h-4zM10 10h4v14h-4zM16 16h4v8h-4zM22 4h4v20h-4z", //柱图
      ];

      var series = [];
      for (var i = 0; i < position.length; i++) {
        series.push({
          singleAxisIndex: 0,
          coordinateSystem: "singleAxis",
          type: "scatter",
          symbol: icons[i],
          symbolSize: [20, 18],
          symbolOffset: [0, "-40%"],
          data: position[i],
        });
      }
      series.push({
        singleAxisIndex: 1,
        coordinateSystem: "singleAxis",
        type: "scatter",
        symbolSize: 0,
        data: data,
      });
      var option = {
        color: [
          "#80cc1e",
          "#1ecc8d",
          "#1e7ccc",
          "#4f1ecc",
          "#9d1ecc",
          "#cc1e78",
        ],
        grid: {
          containLabel: true,
        },
        tooltip: {
          show: true,
          formatter: function(params) {
            return (
              "描述: " +
              params.value[1] +
              "<br/>" +
              "时间: " +
              params.value[2] +
              "<br/>" +
              "版本: " +
              params.value[3] +
              "<br/>"
            );
          },
        },
        singleAxis: [
          {
            type: "value",
            top: "40",
            height: "20",
            splitLine: { show: false },
            interval: 1,
            axisLine: {
              // symbol: ['none', 'arrow']
            },
            axisLabel: {
              show: true,
              margin: 13,
              textStyle: { fontSize: 12 },
              formatter: function(value, index) {
                return value + 1;
              },
            },
            min: 0,
            max: versionTitle.length - 1,
          },
          {
            type: "category",
            boundaryGap: false,
            top: "60",
            height: "20",
            splitLine: { show: false },
            axisLabel: {
              interval: 0,
              textStyle: {
                color: "#455A74",
                fontSize: 12,
              },
              formatter: function(value, index) {
                return value ? value.split("").join("\n") : ""; //竖排文字
              },
            },
            axisTick: { show: false },
            axisLine: { show: false },
            data: versionTitle,
          },
        ],
        series: series,
      };
      let tmp = echarts.init(document.getElementById("version-id"));
      tmp.setOption(option);
    },
    //
    createId() {
      var option = {
        title: {
          text: "",
          left: "center",
          top: "49%",
          textStyle: {
            fontSize: 16,
            color: "#3C4353",
            fontStyle: "normal",
            fontWeight: "400",
            fontFamily: "PingFangSC-Regular,PingFang SC;",
          },
        },
        color: [
          "#7eacea",
          "#e15777",
          "#95ea71",
          "#ea9b4f",
          "#7577df",
          "#be72d8",
          "#fff",
        ],
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
        series: [
          {
            name: "统计",
            type: "pie",
            radius: [30, 110],
            center: ["50%", "50%"],
            roseType: "radius",
            label: {
              show: true,
              formatter: "{d}%",
            },
            emphasis: {
              label: {
                show: true,
              },
            },
            data: [
              {
                value: 25,
                name: "apiface前端",
              },
              {
                value: 22,
                name: "apiface源码扫描",
              },
              {
                value: 18,
                name: "文档管理",
              },
              {
                value: 9,
                name: "gradle插件",
              },
              {
                value: 7,
                name: "maven插件",
              },
            ],
          },
          {
            name: "占位",
            type: "pie",
            silent: true,
            center: ["50%", "50%"],
            radius: 30,
            hoverAnimation: false,
            label: {
              show: false,
              position: "center",
            },
            data: [
              {
                value: 1,
                name: "",
              },
            ],
            itemStyle: {
              normal: {
                color: "#fff",
                shadowColor: "rgba(0, 0, 0, 0.5)",
                shadowBlur: 10,
              },
            },
          },
        ],
      };
      let tmp = echarts.init(document.getElementById("create-id"));
      tmp.setOption(option);
    },
  },
};
</script>

<style lang="less">
.module-home {
  height: 100%;
  padding: 10px;
  overflow-y: auto;
  .tip-foot {
    padding-bottom: 20px;
    text-align: center;
    width: 100%;
    padding-top: 20px;
  }
  .base-analyz {
    height: 150px;
    .title-badge {
      float: right;
      padding: 0px 9px;
      line-height: 25px;
      margin-top: 8px;
      color: #fff;
      font-size: 12px;
      border-radius: 2px;
    }
    .title-badge-blue {
      background-color: #1e9fff;
    }
    .title-badge-green {
      background-color: #009688;
    }
    .title-badge-yellow {
      background-color: #ffb800;
    }
    .title-badge-red {
      background-color: #ff5722;
    }
    .content-info {
      font-size: 36px;
      color: #666;
      line-height: 50px;
      padding: 5px 10px 10px;
      span {
        float: right;
        font-size: 30px;
      }
    }
    .content-span {
      font-size: 13px;
      line-height: 34px;
      color: #777;
      padding: 0px 8px;
      &:last-child {
        float: right;
      }
      i {
        font-size: 16px;
      }
    }
  }

  .echart-main {
    height: 100%;
  }
  .base-info {
    height: 80%;
    .feelback-view {
      padding: 0px !important;
    }
    .user-suggest {
      width: 100%;
      height: 100%;
      padding: 0px 15px;
      overflow-y: auto;
      color: #666;
      ul {
        padding: 0px 10px;
        li {
          position: relative;
          padding: 12px 0px 26px 0px;
          border-bottom: 1px solid #eee;
          .user-title {
            padding-bottom: 4px;
            line-height: 21px;
            font-size: 16px;
            font-weight: 700;
          }
          .el-rate {
            width: 160px !important;
            float: left;
          }
          .user-time {
            line-height: 21px;
            font-size: 14px;
            float: right;
          }
          p {
            line-height: 24px;
            font-size: 14px;
            padding-bottom: 8px;
          }
          &:last-child {
            border-bottom: none;
          }
        }
      }
    }
  }
  .version-info {
    height: 588px;
    .version-info-longer {
      width: calc(~"100% - 400px");
    }
    .version-info-small {
      width: 400px;
    }
    .version-info-detail {
      height: 432px;
    }
    .version-table {
      width: 100%;
      color: #666;
      tr {
        display: table-row;
        vertical-align: inherit;
        border-color: inherit;
      }
      td {
        border-width: 1px;
        border-style: solid;
        border-color: #e6e6e6;
        padding: 8px 12px;
        min-height: 19px;
        line-height: 19px;
        font-size: 13px;
      }
    }
  }
  .el-card {
    height: 100%;

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
  .el-row {
    margin-bottom: 6px;
    .el-col {
      height: 100%;
      padding-right: 6px;
      &:last-child {
        padding-right: 0px;
      }
    }
  }
}
</style>
