import el_ja from 'element-ui/lib/locale/lang/ja'
var ja = {
  input: "请输入内容",
  enable: "启用",
  cancle: "取消",
  save: "保存",
  del: "删除",
  opt: "操作",
  layout: {
    index: {
      title: "オンラインインターフェースドキュメント",
      addr: "请输入接口文档解析地址",
      his: "查询历史",
      input: "自定义输入",
      tips: "请输入合法url地址",
      vm: "集成环境",
      addr1: "接口文档地址"

    },
    layout: {
      portfolio: '请求配置',
      dribbble: '文档配置',
      linkedin: '信息',
      tips: "页面集成模式才可配置"
    }
  },
  page: {
    blank: "无任何数据",
    case: {
      label: "测试用例",
      reqTime: "请求时间：",
      resTime: "响应时间：",
      diff: "请求耗时：",
      code: "响应码：",
      reqHead: "请求头 / COOKIE / 认证",
      respHead: "响应头",
      body: "响应体",
    },
    index: {
      tips: "暖かいヒント : 文書モードに入ると、テストケースのテストができなくなります",
      active: "操作モード",
      inactive: "ドキュメントモード",
      addr: "インターフェースアドレス：",
      tags: "専属ラベル：",
      methods: "要求方式：",
      respm: "応答のタイプ：",

      label: "パラメータ",
      enable: "启用",
      details: "詳細",
      required: "必要とする",
      req: "必要",
      noreq: "非必要",
      param: "パラメータ名",
      value: "パラメータ値",
      desc: "パラメータの意味/説明",
      self: "カスタムパラメータ",
      type: "データの種類",
      s: "字符串",
      n: "小数",
      i: "整数",
      b: "布尔",
      format: "参数类型",
      sel: "全选",
      has: "已选择",
      item: "项",
      add: "追加",
      fill: "塗りつぶし",
      ty: "試してみる",
      warn: "これらのパラメータは必ず有効にしてください",
      label2: "応答内容",
      serial: "フォーマット変換",
      nodata: "オブジェクトを関連付けることができませんでした。空のオブジェクトに戻る予定です",
      base: "基本データタイプまたはその包装類",
      nometa: "メタデータの説明が見つかりませんでした",
      show: "すべてを展開",
      hidden: "部分を回収する",
      noparam: "* パラメータなし",
      respcode: "#####  ステータスコード:",
      pt: "#####  パラメータ形式：",
      json: "JSON(application/json)",
      form: "FORM(application/x-www-form-urlencoded)",
    },
    v: {
      info: "版本信息",
      teams: "开发团队",
      v: "当前版本 : snow white(白雪公主)",
      u: "前端框架 : vue2.x/elementUI-2.13",
      s: "主要特色 : 极简 / 易操作 / 单页面",
      a: "接口文档 : swagger2.0的定制页面",
      feel: "欢迎点评",
      submit: "立即评论",
      name: "昵称",
      suggest: "建议",
      namep: "使用匿名-请输入",
      suggestp: "请输入您的评论意见",
      score: "评分",
      self: "Copyright ©2020 土豆联盟",
      notEmpty: "请填写评论建议",
      min: "请填写 15 到 300 个字符"
    }
  },
  dialog: {
    info: {
      title: "版本信息",
      addr: "接口地址",
      info: "接口信息",
      t: "标题",
      v: "版本",
      d: "描述",
      author: "作者信息",
      a: "作者",
      e: "邮箱",
      p: "主页",
      tm: "团队",
      lecuce: "许可信息",
      l: "许可",
      la: "地址",
      total: "共计",
      item: "个接口",
    },
    conf: {
      title: "请求配置",
      enable: "启用",
      opt: "操作",
      input: "请输入内容",
      del: "删除",
      tab1: "请求头",
      headname: "头名称",
      headc: "头内容",
      tab2: "COOKIES",
      val: "值",
      tab3: "身份认证",
      type: "携帯モード",
      active: "URLに含まれていますか",
      auth: "認証の種類",
      cancle: "キャンセル",
      save: "保存",
    }
  },
  menu: {
    nodata: "記録が見つかりませんでした",
    search: "クエリーメニュー",
  },
  code: {
    req: "項目、パラメータが必要です",
    error: "項目、JSONフォーマットが間違っています。"
  }
};
ja.el = el_ja.el;
ja.language = '日本語';
export {
  ja
};