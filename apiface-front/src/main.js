import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import "./styles/reset.css";
import "./styles/common.less";
import "font-awesome/css/font-awesome.css";
import "./font/iconfont/iconfont.css";
import "./font/iconfont/iconfont.js";

import util from "script/util";
Vue.use(util);

// 国际化 必须先引用再初始化
import VueI18n from "vue-i18n";
Vue.use(VueI18n);

// ElementUI 和 国际化
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import lang from "./script/lang/lang";
const i18n = lang.i18nInit();
Vue.use(ElementUI, {
  size: "small",
  i18n: (key, value) => i18n.t(key, value),
});

Vue.config.productionTip = false;

// import VueLazyLoad from "vue-lazyload";
// Vue.use(VueLazyLoad, {
//   loading: "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7",
// });

// 事件总线
Vue.prototype.$bus = new Vue();

// 滚动加载
import InfiniteScroll from "vue-infinite-scroll";
Vue.use(InfiniteScroll);
Vue.directive("loadmore", {
  bind(el, binding) {
    const selectWrap = el.querySelector(".el-table__body-wrapper");
    selectWrap.addEventListener("scroll", function () {
      let sign = 20;
      const scrollDistance =
        this.scrollHeight - this.scrollTop - this.clientHeight;
      if (scrollDistance <= sign) {
        binding.value();
      }
    });
  }
});

new Vue({
  router,
  store,
  i18n,
  render: (h) => h(App),
}).$mount("#app");

export default Vue;