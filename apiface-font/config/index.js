var path = require("path");
var inter = require("../src/config/proxy.js");
module.exports = {
  build: {
    env: require("./prod.env"),
    index: path.resolve(__dirname, "../dist/index.html"),
    assetsRoot: path.resolve(__dirname, "../dist"),
    assetsSubDirectory: "static",
    assetsPublicPath: "",
    productionSourceMap: true,
    productionGzip: false,
    productionGzipExtensions: ["js", "css"]
  },
  dev: {
    env: require("./dev.env"),
    port: 28093,
    assetsSubDirectory: "static",
    assetsPublicPath: "/",
    proxyTable: inter.proxy,
    cssSourceMap: false
  }
};
