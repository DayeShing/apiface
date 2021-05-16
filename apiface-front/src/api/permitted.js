// 权限列表                                           url接口
var permitted = [

  "page:sys:project", //功能页面
  "btn:sys:project:authorize", //角色授权按钮      /app/center/role/savePermitted

  "btn:sys:role:add", //角色添加按钮               /app/center/role/save
  "btn:sys:role:edit", //角色编辑按钮              /app/center/role/save
  "btn:sys:role:delete", //角色删除按钮            /app/center/role/delete
  "btn:sys:org:add", //功能添加按钮                /app/center/org/save,tree
  "btn:sys:org:edit", //功能编辑按钮               /app/center/org/save,get,tree
  "btn:sys:org:delete", //功能删除按钮             /app/center/org/delete

  "page:sys:doc", //功能页面
  "btn:sys:doc:authorize", //角色授权按钮      /app/center/role/savePermitted

  "btn:sys:doc:add", //角色添加按钮               /app/center/role/save
  "btn:sys:doc:edit", //角色编辑按钮              /app/center/role/save
  "btn:sys:doc:delete", //角色删除按钮            /app/center/role/delete
  "btn:sys:doc:publish", //功能添加按钮                /app/center/org/save,tree
  "btn:sys:doc:down", //功能编辑按钮               /app/center/org/save,get,tree
  "btn:sys:doc:uplaod", //功能删除按钮             /app/center/org/delete
  "btn:sys:doc:group:add", //功能添加按钮                /app/center/org/save,tree
  "btn:sys:doc:group:edit", //功能编辑按钮               /app/center/org/save,get,tree
  "btn:sys:doc:group:delete", //功能删除按钮             /app/center/org/delete

  "page:sys:error", //功能页面
  "page:sys:apis", //功能页面
  "page:sys:home", //功能页面
];
//v-if="permitty[secretkey + 'delete']"
//secretkey:"btn:sys:user:",
export {
  permitted
};