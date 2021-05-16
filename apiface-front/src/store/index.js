import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
import {
  inter
} from "api/interface";
export default new Vuex.Store({
  // 全局状态
  state: {
    //标题
    title: "apiface",
    // 模式 三种 app single dev
    mode: "single",
    api: "/base/api/api-docs",
    proxy: "/proxy/agent/api",
    subtitle: "",
    app: false,
    models: {},
    user: {
      userId: "",
      userName: "游客",
      roles: []
    },
    permitteds: {},
    docId: "",
  },
  // 修改全局状态
  mutations: {
    setDocId(state, docId) {
      state.docId = docId;
    },
    setPermitteds(state, permitteds) {
      state.permitteds = permitteds;
    },
    setApp(state, app) {
      state.app = app;
    },
    setUser(state, user) {
      state.user = user;
    },
    setConfig(state, conf) {
      state.title = conf.title;
      state.api = conf.api;
      state.proxy = conf.proxy;
      if (conf.mode == 'app') {
        conf.mode = 'single'
        state.app = true;
        state.proxy = inter.proxy;
      }
      state.mode = conf.mode;
    },
    setModels(state, models) {
      state.models = models;
    },
    setTitle(state, subtitle) {
      state.subtitle = subtitle;
    }
  },
  getters: {},
  actions: {},
  modules: {}
})