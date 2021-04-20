function drag(obj,dialog,head){
    var object = '';
    if (typeof obj == "string") {
        object = document.getElementById(obj);
    }else{
        object = obj;
    }
    var dialog = object.getElementsByClassName(dialog)[0];
    var header = object.getElementsByClassName(head)[0];
    header.onmousedown=function (e){
        //鼠标按下
        this.style.cursor="move";

        object.style.overflow = 'hidden';
        dialog.style.left = dialog.offsetLeft + 'px';
        dialog.style.top = dialog.offsetTop + 'px';
        dialog.style.marginTop = '0';
        dialog.style.position = 'absolute';
        var d=document;
        if(!e) e=window.event;
        //按下时创建一个事件
        var x=e.clientX-dialog.offsetLeft;
        //x=鼠标相对于网页的x坐标-网页被卷去的宽-待移动对象的左外边距
        var y=e.clientY-dialog.offsetTop;
        //y=鼠标相对于网页的y左边-网页被卷去的高-待移动对象的左上边距
        document.onmousemove=function(e){//鼠标移动
            if(!e) e=window.event;//移动时创建一个事件
            var dragX = e.clientX - x +'px';
            var dragY = e.clientY - y +'px';
            dialog.style.left = dragX;
            dialog.style.top = dragY;
            
        };
        document.onmouseup=function (){//鼠标放开
            dialog.style.cursor = "normal";//设置放开的样式
            document.onmousemove=null;
            document.onmouseup = null;
        }
    }
}

export { //很关键
  drag
}