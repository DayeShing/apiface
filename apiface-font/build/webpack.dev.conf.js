var path = require("path");
var config = require("../config");
var webpack = require("webpack");
var merge = require("webpack-merge");
var utils = require("./utils");
var baseWebpackConfig = require("./webpack.base.conf");
var HtmlWebpackPlugin = require("html-webpack-plugin");
// var SkeletonWebpackPlugin = require('vue-skeleton-webpack-plugin');
var glob = require("glob");
Object.keys(baseWebpackConfig.entry).forEach(function (name) {
  baseWebpackConfig.entry[name] = ["./build/dev-client"].concat(
    baseWebpackConfig.entry[name]
  );
});
module.exports = merge(baseWebpackConfig, {
  module: {
    loaders: utils.styleLoaders({
      sourceMap: config.dev.cssSourceMap
    })
  },
  devtool: "#eval-source-map",
  plugins: [
    new webpack.DefinePlugin({
      "process.env": config.dev.env
    }),
    new webpack.optimize.OccurenceOrderPlugin(),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NoErrorsPlugin(),
    // new SkeletonWebpackPlugin({
    //   webpackConfig: require('./webpack.skeleton.conf'),
    //   quiet: true
    // }),
  ]
});

function getEntry(globPath) {
  var entries = {},
    basename,
    tmp,
    pathname;
  if (typeof globPath != "object") {
    globPath = [globPath];
  }
  globPath.forEach(itemPath => {
    glob.sync(itemPath).forEach(function (entry) {
      basename = path.basename(entry, path.extname(entry));
      entries[basename] = entry;
    });
  });
  return entries;
}
var pages = getEntry(["./src/module/*.html", "./src/module/**/*.html"]);
for (var pathname in pages) {
  var conf = {
    filename: pathname + ".html",
    template: pages[pathname], // 模板路径
    inject: true, // js插入位置
    chunksSortMode: "dependency"
  };
  if (pathname in module.exports.entry) {
    conf.chunks = ["manifest", "vendor", pathname];
    conf.hash = true;
  }
  module.exports.plugins.push(new HtmlWebpackPlugin(conf));
}
