var path = require("path");
var config = require("../config");
var utils = require("./utils");
var webpack = require("webpack");
var merge = require("webpack-merge");
var baseWebpackConfig = require("./webpack.base.conf");
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var HtmlWebpackPlugin = require("html-webpack-plugin");
var CleanPlugin = require("clean-webpack-plugin"); //webpack插件，用于清除目录文件
// var SkeletonWebpackPlugin = require('vue-skeleton-webpack-plugin');
var glob = require("glob");
var env = config.build.env;
var webpackConfig = merge(baseWebpackConfig, {
  module: {
    loaders: utils.styleLoaders({
      sourceMap: config.build.productionSourceMap,
      extract: true
    })
  },
  devtool: config.build.productionSourceMap ? "#source-map" : false,
  output: {
    path: config.build.assetsRoot,
    filename: utils.assetsPath("js/[name][hash].js"), //.[chunkhash]
    chunkFilename: utils.assetsPath("js/[id][chunkhash].js") //.[chunkhash]
  },
  vue: {
    loaders: utils.cssLoaders({
      sourceMap: config.build.productionSourceMap,
      extract: true
    })
  },
  plugins: [
    // http://vuejs.github.io/vue-loader/workflow/production.html
    new webpack.DefinePlugin({
      "process.env": env
    }),
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false
      }
    }),
    new CleanPlugin(["../dist"]), //清空生成目录
    // new SkeletonWebpackPlugin({
    //   webpackConfig: require('./webpack.skeleton.conf'),
    //   quiet: true
    // }),
    new webpack.optimize.OccurenceOrderPlugin(),
    // extract css into its own file
    new ExtractTextPlugin(utils.assetsPath("css/[name][contenthash].css")), //.[contenthash]
    new webpack.optimize.CommonsChunkPlugin({
      name: "vendor",
      minChunks: function (module, count) {
        return (
          module.resource &&
          /\.js$/.test(module.resource) &&
          module.resource.indexOf(path.join(__dirname, "../node_modules")) === 0
        );
      }
    }),
    new webpack.optimize.CommonsChunkPlugin({
      name: "manifest",
      chunks: ["vendor"]
    })
  ]
});
if (config.build.productionGzip) {
  var CompressionWebpackPlugin = require("compression-webpack-plugin");
  webpackConfig.plugins.push(
    new CompressionWebpackPlugin({
      asset: "[path].gz[query]",
      algorithm: "gzip",
      test: new RegExp(
        "\\.(" + config.build.productionGzipExtensions.join("|") + ")$"
      ),
      threshold: 10240,
      minRatio: 0.8
    })
  );
}
module.exports = webpackConfig;

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
  // 配置生成的html文件，定义路径等
  var conf = {
    filename: pathname + ".html",
    template: pages[pathname], // 模板路径
    inject: true, // js插入位置
    chunksSortMode: "dependency"
  };
  if (pathname in module.exports.entry) {
    conf.chunks = ["manifest", "vendor", pathname];
  }
  module.exports.plugins.push(new HtmlWebpackPlugin(conf));
}
