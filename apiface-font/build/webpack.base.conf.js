var path = require("path");
var config = require("../config");
var utils = require("./utils");
var projectRoot = path.resolve(__dirname, "../");
var glob = require("glob");
var entries = getEntry(["./src/module/*.js", "./src/module/*/*.js"]); // 获得入口js文件
var env = process.env.NODE_ENV;
var cssSourceMapDev = env === "development" && config.dev.cssSourceMap;
var cssSourceMapProd = env === "production" && config.build.productionSourceMap;
var useCssSourceMap = cssSourceMapDev || cssSourceMapProd;
module.exports = {
  entry: entries,
  output: {
    path: config.build.assetsRoot,
    publicPath:
      process.env.NODE_ENV === "production"
        ? config.build.assetsPublicPath
        : config.dev.assetsPublicPath,
    filename: "[name].js"
  },
  resolve: {
    extensions: ["", ".js", ".vue"],
    fallback: [path.join(__dirname, "../node_modules")],
    alias: {
      vue$: "vue/dist/vue",
      src: path.resolve(__dirname, "../src"),
      common: path.resolve(__dirname, "../src/common"),
      components: path.resolve(__dirname, "../src/components"),
      module: path.resolve(__dirname, "../src/module"),
      config: path.resolve(__dirname, "../src/config")
    }
  },
  resolveLoader: {
    fallback: [path.join(__dirname, "../node_modules")]
  },
  module: {
    loaders: [
      {
        test: /\.vue$/,
        loader: "vue-loader"
      },
      {
        test: /\.js$/,
        loader: "babel-loader",
        include: projectRoot,
        exclude: /node_modules/
      },
      {
        test: /\.json$/,
        loader: "json-loader"
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: "url-loader",
        query: {
          limit: 10,
          name: utils.assetsPath("img/[name].[ext]") //.[hash:7]
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: "url-loader",
        query: {
          limit: 10000,
          name: utils.assetsPath("fonts/[name].[ext]") //.[hash:7]
        }
      }
    ]
  },
  vue: {
    loaders: utils.cssLoaders({
      sourceMap: useCssSourceMap
    }),
    postcss: [
      require("autoprefixer")({
        browsers: ["last 2 versions"]
      })
    ]
  }
};
function getEntry(globPath) {
  var entries = {},
    basename,
    tmp,
    pathname;
  if (typeof globPath != "object") {
    globPath = [globPath];
  }
  globPath.forEach(itemPath => {
    glob.sync(itemPath).forEach(function(entry) {
      basename = path.basename(entry, path.extname(entry));
      entries[basename] = entry;
    });
  });
  return entries;
}
