import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  // 全局状态
  state: {
    //标题
    title: "apiface",
    mode: "single",
    api: "/base/api/api-docs",
    proxy: "/proxy/agent/api",
    subtitle: "",
    models: {}
  },
  // 修改全局状态
  mutations: {
    setConfig(state, conf) {
      state.title = conf.title;
      state.mode = conf.mode;
      state.api = conf.api;
      state.proxy = conf.proxy;
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