var path = require("path");
var config = require("../config");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

exports.assetsPath = function(_path) {
  var assetsSubDirectory =
    process.env.NODE_ENV === "production"
      ? config.build.assetsSubDirectory
      : config.dev.assetsSubDirectory;
  return path.posix.join(assetsSubDirectory, _path);
};

exports.cssLoaders = function(options) {
  options = options || {};
  function generateLoaders(loaders) {
    var sourceLoader = loaders
      .map(function(loader) {
        var extraParamChar;
        if (/\?/.test(loader)) {
          loader = loader.replace(/\?/, "-loader?");
          extraParamChar = "&";
        } else {
          loader = loader + "-loader";
          extraParamChar = "?";
        }
        return loader + (options.sourceMap ? extraParamChar + "sourceMap" : "");
      })
      .join("!");
    if (options.extract) {
      return ExtractTextPlugin.extract("vue-style-loader", sourceLoader, {
        publicPath: "../../"
      });
    } else {
      return ["vue-style-loader", sourceLoader].join("!");
    }
  }
  return {
    css: generateLoaders(["css"]),
    postcss: generateLoaders(["css"]),
    less: generateLoaders(["css", "less"]),
    sass: generateLoaders(["css", "sass?indentedSyntax"]),
    scss: generateLoaders(["css", "sass"]),
    stylus: generateLoaders(["css", "stylus"]),
    styl: generateLoaders(["css", "stylus"])
  };
};
exports.styleLoaders = function(options) {
  var output = [];
  var loaders = exports.cssLoaders(options);
  for (var extension in loaders) {
    var loader = loaders[extension];
    output.push({
      test: new RegExp("\\." + extension + "$"),
      loader: loader
    });
  }
  return output;
};
