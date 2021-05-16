var ContextPath = "/apiface";
var port = "10090";
port = "";
var inter = {
  buildVersion: "版本：apiface 1.2.4 PRO",
  user: {
    getUser: ContextPath + "/user/get",
  },
  auth: {
    discuss: {
      save: ContextPath + "/auth/discuss/save", // 保存评论
      list: ContextPath + "/auth/discuss/list", //获取评论
    },
    validCode: ContextPath + "/auth/validCode", // 激活验证码
    validUser: ContextPath + "/auth/validUser", // 验证唯一性

    code: ContextPath + "/auth/login/code", // 验证码
    getPlugin: ContextPath + "/auth/menu/plugin", // 插件
    signIn: ContextPath + "/auth/signIn", // 登录OK password userName rememberMe
    signOut: ContextPath + "/auth/signOut", // 登出OK
    getCurrentUser: ContextPath + "/auth/getCurrentUser", // 获取登录用户信息
    modifyUser: ContextPath + "/auth/modifyUser", //编辑用户(本地保存用户编辑)json
    cypher: ContextPath + "/auth/cypher", //修改用户密码(本地用户编辑)json
    hasPermissions: ContextPath + "/auth/hasPermissions" //是否有权限
  },
  permitted: {
    user: ContextPath + "/user/list",
    role: ContextPath + "/role/list",
    org: ContextPath + "/org/list",
    list: ContextPath + "/permitted/${type}/list",
    save: ContextPath + "/permitted/${type}/save",
  },
  back: {
    get: ContextPath + "/back/get",
    save: ContextPath + "/back/save",
    list: ContextPath + "/back/list",
    status: ContextPath + "/back/status",
    delete: ContextPath + "/back/delete",
    document: ContextPath + "/back/${resourceId}/document",
    progress: {
      save: ContextPath + "/back/progress/save",
      list: ContextPath + "/back/progress/list",
    }
  },
  org: {
    save: ContextPath + "/org/save", //权限保存json
    list: ContextPath + "/org/list", //权限列表 pId(默认'0') keyword  分页 row size count
    delete: ContextPath + "/org/delete", //权限删除 funcIds arr(功能权限id)
    get: ContextPath + "/org/get", //功能权限 funcId base(true[默认]/false())
    tree: ContextPath + "/org/tree" //功能树 id keyword
  },
  role: {
    list: ContextPath + "/role/list", // 角色列表
    save: ContextPath + "/role/save", //权限保存json
    delete: ContextPath + "/role/delete", //权限删除 roleIds arr(功能权限id)
  },
  analyze: {
    apiStat: ContextPath + "/analyze/log/apiStat", // 概览
  },
  api: {
    docs: ContextPath + "/api/docs",
    document: ContextPath + "/api/${resourceId}/document",
    tree: ContextPath + "/api/tree",
    doc: {
      save: ContextPath + "/api/doc/save",
      delete: ContextPath + "/api/doc/delete",
      list: ContextPath + "/api/doc/list",
      valid: ContextPath + "/api/doc/valid",
      status: ContextPath + "/api/doc/status",
    },
    group: {
      delete: ContextPath + "/api/group/delete",
      save: ContextPath + "/api/group/save",
    },
  },
  resource: {
    merge: ContextPath + "/resource/merge",
    chunk: ContextPath + "/resource/chunk",
    cancel: ContextPath + "/resource/cancel",
    upload: ContextPath + "/resource/upload",
    upload2: ContextPath + "/resource/upload/v2",
  },
  io: {
    import: ContextPath + "/io/import", //导入模板数据-参数MultipartFile file
    upload: ContextPath + "/io/upload", //上传文件到资源文件夹-参数MultipartFile file
    downlaod: ContextPath + "/io/downlaod", //下载资源文件夹的文件-参数String path
    template: ContextPath + "/io/template", //下载模板文件-参数String name
  },
  moduleNames: {
    center: "apiface",
  },
  ContextPath: ContextPath,
  port: port,
  feelback: "",
  proxy: ContextPath + "/agent/api",
};

export {
  inter
};