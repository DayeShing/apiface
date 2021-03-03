/**
 * 配置js,可根据不同环境具体配置
 * author:Daye Shing
 */
window.CONF = {
  docs: [{
    group: '用例示例',
    options: [{
      name: "本地示例(英文)",
      addr: "http://localhost:28093/static/docs.json"
    }, {
      name: "本地示例(中文)",
      addr: "http://localhost:28093/static/demo.json"
    }]
  }, {
    group: 'Swagger示例',
    options: [{
      name: "swagger接口",
      addr: "http://localhost:8102/app/apifaces/api-docs"
    }]
  }],
  mode: 'single', // single(单独部署) or dev集成部署
  api: "proxy/apifaces/api-docs", //集成部署 指向的接口文档的地址
  title: "", //自定义标题。优先等级高于默认
  proxy: "/proxy/agent/api", //代理地址,为空这代表不使用代理服务，使用代理服务真实地址将放入请求头Self-Agent-Address
};
