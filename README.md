skin使用介绍

第一步我们要在自定义的application中初始化  
SkinManager.init(this)

换肤本质就是替换我们的资源，例如color,img等。所以我们在xml中定义的两套资源id必须是一样的

view分为原生和自定义的，参考demo实现！


skinapk是我们的资源包，只存放需要替换的资源,再次强调下资源id必须一样
使用as自带的gradle一键打包

