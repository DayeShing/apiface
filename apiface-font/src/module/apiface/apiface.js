import Vue from "vue";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import Apiface from "./apiface.vue";
import util from "./js/util";
import "font-awesome/css/font-awesome.css";
import "./css/reset.css";
import VueRouter from "vue-router";
import "./css/common.less";
import Router from "./router/router";
import VueI18n from 'vue-i18n'
import Lang from './js/lang/lang'

Vue.use(VueI18n);
const i18n = Lang.i18nInit();
Vue.use(ElementUI, {
  size: "small",
  i18n: (key, value) => i18n.t(key, value)
});
Vue.use(util);
Vue.use(VueRouter);
const routes = Router.routes;
const router = new VueRouter({
  routes
});
new Vue({
  el: "#main",
  i18n,
  router,
  template: "<Apiface/>",
  components: {
    Apiface
  }
});
