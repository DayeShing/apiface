var url = "http://127.0.0.1:8080"
module.exports = {
  proxy: {
    '/proxy': {
      target: url,
      changeOrigin: true,
      headers: {
        'x-real-ip': '192.168.3.48'
      },
      pathRewrite: {
        '^/proxy': '/proxy'
      }
    }
  }
};
