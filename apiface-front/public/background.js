//  预设窗口 index.html
var createWin = function(name,minWidth=1000,minHeight=700,frame='none',resizable=true,alwaysOnTop=false) {
  var win = window.chrome.app.window.get("music_view_" + name);
  if(win){
    return;
  }
  window.chrome.system.display.getInfo(function(info) {
    window.chrome.app.window.create(
      name + ".html",
      {
        id: "music_view_" + name,
        minWidth:minWidth,
        minHeight:minHeight,
        bounds: {
          width: minWidth,
          height: minHeight,
        },
        // frame:frame,
        resizable:resizable,
        alwaysOnTop:alwaysOnTop
      },
      function(win){
        console.log(win,"dddddd");
      }
    );
  });
};

// chrome api   
//生命周期
//加载窗口 onLaunched 事件将会在用户启动应用时产生,应用从执行器执行时产生。
window.chrome.app.runtime.onLaunched.addListener(function(launch) {
  console.log(launch,"chrome app 加载应用!");
  createWin('index');
});
//当 Chrome 浏览器启动时产生，用于 Chrome 浏览器上一次关闭时正在运行的应用
window.chrome.app.runtime.onRestarted.addListener(function (){

});
//应用通信 当消息从扩展程序进程或者内容脚本中发送时产生。
window.chrome.runtime.onMessage.addListener(function(msg) {
  console.log(msg,"chrome app 消息");
});
