module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  outputDir: 'dist',
  productionSourceMap: false, // 打包时不要map文件
  lintOnSave: true, // 是否在保存的时候检查
  configureWebpack: {
    resolve: {
      alias: {
        'components': '@/components',
        'assets': '@/assets',
        'page': '@/page',
        'font': '@/font',
        'layout': '@/layout',
        'script': '@/script',
        'api': '@/api',
        'dialog': '@/dialog'
      }
    },
  },
  devServer: {
    port: 9000,
    hot: true,
    host: "0.0.0.0",
    // 设置代理
    proxy: {
      '/proxy': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/proxy': '/proxy'
        }
      },
      '/apiface': {
        target: 'http://127.0.0.1:28085',
        changeOrigin: true,
        pathRewrite: {
          '^/apiface': '/apiface'
        }
      },
    },
    disableHostCheck: true
  },
  //配置多页面入口
  // pages: {
  //   index: {
  //     // page 的入口
  //     entry: 'src/main.js',
  //     // 模板来源
  //     template: 'public/index.html',
  //     // 在 dist/index.html 的输出
  //     filename: 'apiface.html',
  //     // 当使用 title 选项时，
  //     // template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
  //     title: 'apiface',
  //     // 在这个页面中包含的块，默认情况下会包含
  //     // 提取出来的通用 chunk 和 vendor chunk。
  //     chunks: ['chunk-vendors', 'chunk-common', 'index']
  //   }
  // },
}