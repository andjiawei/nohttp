
#how to use

    1 添加 compile 'com.yolanda.nohttp:nohttp:1.0.3'
    
    2 自定义BaseApplication 更改清单文件name 且在oncreate中添加 NoHttp.initialize(this);
    
    3 复制nohttp包到项目中。
    
    4 BaseActivity中添加对话框的若干方法
    
    5 在需要的activity调用get put 等方法即可  
    
    6 退出时取消所有队列 enjoy it;