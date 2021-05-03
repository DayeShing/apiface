![tag](https://img.shields.io/static/v1?label=&message=文档构建&color=brightgreen)
![tag](https://img.shields.io/static/v1?label=&message=APIs&color=green)
![tag](https://img.shields.io/static/v1?label=&message=javadoc&color=yellowgreen)
![tag](https://img.shields.io/static/v1?label=&message=swagger&color=yellow)
![tag](https://img.shields.io/static/v1?label=&message=java注释&color=orange)
![tag](https://img.shields.io/static/v1?label=&message=javadoc2APIs&color=red)
![tag](https://img.shields.io/static/v1?label=&message=注释文档&color=blue)
![tag](https://img.shields.io/static/v1?label=&message=注释生成APIs&color=blueviolet)
![tag](https://img.shields.io/static/v1?label=&message=测试用例&color=ff69b4)
![tag](https://img.shields.io/static/v1?label=&message=APIs构建&color=9cf)

# [apiface](http://daysh.top/)使用篇
### 单纯使用环境（apiface的使用门槛）
> * jdk6+ （支持java或类似kotlin等运行在jvm层的 “任意语言(开发中)” 使用 “任意框架” 编写的http协议的RESTFUL APIs接口）
> * maven 或 gradle 

* ### <a id="apifaceMavenPluginUsing">使用apiface-maven-plugin插件构建APIs文档</a>
* ####第一步引入maven插件
```xml
<!--
插件已发布至中央仓库，可以直接从中央仓库获取 插件以及插件依赖，  认准 1.4.5-RC 版本及其以上版本
-->
<plugin>
    <groupId>io.github.dayeshing</groupId>
    <artifactId>apiface-maven-plugin</artifactId>
    <version>1.4.5-RC</version>
    <configuration>
        <artifactId>apiface-example</artifactId>

        <host>127.0.0.1:28080</host>
        <basePath>/app</basePath>
        <license>9526</license>
        <licenseUrl>http://self.daysh.top</licenseUrl>
        <url>http://self.daysh.top</url>
        <developer>Daye Shing|xuandeyu14@gmail.com</developer>
        <description>apiface让您的接口文档更丰富、更优雅</description>
        <version>${project.version}</version>
        <title>接口文档 APIs</title>
    </configuration>
    <executions>
        <execution>
            <id>apiface</id>
            <phase>package</phase>
            <goals>
                <goal>doc</goal>
                <goal>api</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
> * host          打包生成的接口文档指向 该项目部署的测试服务器，用于用例测试
> * basePath      相当于 HttpServletRequest.getContextPath()，部署项目的实际baseUrl用于用例测试
> * license       
> * licenseUrl    
> * developer    开发团队|开发团队联系 ，用于显示开发者信息
> * url          开发团队主页
> * description  该接口平台的描述信息
> * version      该接口平台记录的项目的版本
> * title        该接口平台的标题
> * artifactId   生成接口文档的项目的 artifactId （必填，不填无法生成接口文档）


* ### <a id="apifaceGradlePluginUsing">使用apiface-gradle-plugin插件构建APIs文档</a>
* ####第一步引入gradle插件
```groovy
/**
 * 插件已发布至中央仓库，可以直接从中央仓库获取 插件以及插件依赖，  认准 1.4.5-RC 版本及其以上版本
 */
//构建环境
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath 'io.github.dayeshing:apiface-gradle-plugin:1.8.9-SNAPSHOT'
    }
}

plugins {
    id 'org.springframework.boot' version '2.2.5.RELEASE'
    id 'io.spring.dependency-management' version '1.0.9.RELEASE'
    id 'java'
}

//使用文档构建插件，必须按这样配置
apply plugin : 'apiface'
apiface {
    host='127.0.0.1:28080' //打包生成的接口文档指向 该项目部署的测试服务器，用于用例测试
    basePath='/app'        //相当于 HttpServletRequest.getContextPath()，部署项目的实际baseUrl用于用例测试
    license='9527'
    licenseUrl='http://self.daysh.top'
    url='http://self.daysh.top'
    developer='Daye Shing|xuandeyu14@gmail.com'   //开发团队|开发团队联系 ，用于显示开发者信息
    description='apiface让您的接口文档更丰富、更优雅'//该接口平台的描述信息
    version='1.8.9-SNAPSHOT'  //该接口平台记录的项目的版本
    title='接口文档 APIs'   //该接口平台的标题
}
// 由于本人gradle不太熟练 ，所以要用下面的配合使用，必须
Task cp = task copyJars(type: Copy) {
    group('apiface')
    from configurations.runtimeClasspath
    into './build/jars'
}
Task c = project.tasks.getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
c.dependsOn(cp)
c.mustRunAfter(cp)

// 其他的配置，无关api打包构建

group = 'com.daysh.apiface'
version = '1.8.9-SNAPSHOT'
sourceCompatibility = '1.8'

repositories {
    mavenLocal()
    maven { url 'http://maven.aliyun.com/nexus/content/groups/public/'}
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    implementation  group: 'org.projectlombok', name: 'lombok', version: '1.18.12'
}
configurations.all {
    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
}

test {
    useJUnitPlatform()
}
```

* #### 第二步 编写你自己的代码然后在你的代码上写注释文档
##### 包装类注释写法
```java
/**
 * 描述该Model
 * @model Model
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 时间
 */
public class Model {
    
    /**
     * name描述
     * @hidden
     * @example 默认值
     */
    private String name;

    /**
     * 优先级比下面age get方法的优先级低(age的描述)
     * @required
     */
    private int age;

    /**
     * 优先级比上面age属性的优先级高(age的描述)
     * @required
     */
    public String getAge(){
        return age;
    }
}
```
##### 普通类注释写法
```java
/**
 * 描述
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @ignore
 * @date 时间
 */
public class Service {
    
    /**
     * 详细描述 （类上面使用了ignore ，方法上可以忽略，使用ignore是为了减少生成文档的注释）
     * @param name |参数类型|必须|默认值|参数类型 body/form/path/head/cookie|参数描述
     * @ignore
     * @return java.lang.String|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 21:47
     */
    public String text(String name){
        return "";
    }
}
```
##### 接口类注释写法
```java
/**
 * 描述
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @action 分组名称
 * @uri uri（使用springmvc 可以忽略，但是其他非spring的就可能要添加）
 * @date 时间
 */
public class Action {

    /**
     * 详细描述 (注释参数可以不用对于下面方法的形参)
     * @param name |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @uri text
     * @summary 摘要
     * @method post,get
     * @exclude age
     * @error 错误描述
     * @return java.lang.String|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 3:51
     */
    public String text(String name) {
        return "";
    }
}
```

* #### 第三步 获取文档并部署
> * 执行打包命令(mvn package)并获取target文件夹中的xxx.json 部署至apiface接口成模管理工具（推荐）
> * 或者任意部署到能用http访问到的资源，用apiface接口成模管理工具打开这个地址
> * 
>
>

* #### 第四步 (用apiface接口成模管理工具)调试接口
apiface接口成模管理工具 
1.静态网页（静态文件不能跨域）演示地址:<a href="https://dayeshing.github.io/apiface/apiface.html" target="_blank">apiface</a>
2.动态网页（访问后端）演示地址:<a href="http://daysh.top/index" target="_blank">apiface</a> 或 <a href="http://apiface.daysh.top/index" target="_blank">apiface</a>

* end
--- 

### apiface接口成模管理工具下载及部署

##### docker 方式部署(前后端)，只需要执行几项命令，以centos7.x 系统作说明
* 1.获取下载安装包下载位置：<a href="http://daysh.top/assets/IO.tar.gz" target="_blank">http://daysh.top/assets/IO.tar.gz</a> 或
                                <a href="http://apiface.daysh.top/assets/IO.tar.gz" target="_blank">http://apiface.daysh.top/assets/IO.tar.gz</a> 
* 2.修改配置:
    1.修改nginx 反向代理服务ip 指向部署应用的内网ip或公网ip(gong网ip需要开端口)
    |--IO
    |--|--INDEX
    |--|--|--nginx
    |--|--|--|--conf
    |--|--|--|--|--nginx.conf
    
    2.修改springboot项目中的东西发邮件的配置
    |--IO
    |--|--INDEX
    |--|--|--tomcat
    |--|--|--|--wabapps
    |--|--|--|--|--apiface
    |--|--|--|--|--|--classes
    |--|--|--|--|--|--|--application-dpev.yml
* 3.将文件夹IO及其内容上传至跟目录/
```shell script
# 安装docker
yum install docker -y
systemctl enable docker
# 启动docker
systemctl start docker
# 拉取 nginx，tomcat,mysql,redis 镜像
docker pull nginx
docker pull tomcat
docker pull mysql:5.7
docker pull redis
docker network create apiface 
# 运行 nginx，tomcat,mysql,redis 镜像
docker run -d -p 6379:6379 --name redis --network apiface --network-alias redis -v /IO/redis/redis.conf:/etc/redis/redis.conf -v /IO/redis/data:/data redis redis-server /etc/redis/redis.conf --appendonly yes
docker run -d -p 3306:3306 --name mysql --network apiface --network-alias mysql -e MYSQL_ROOT_PASSWORD=admin123 -v /IO/mysql/my.conf:/etc/mysql/my.conf -v /IO/mysql/mysqld.cnf:/etc/mysql/mysql.conf.d/mysqld.cnf -v /IO/mysql/logs:/var/log/mysql -v /IO/mysql/data:/var/lib/mysql mysql:5.7
docker run -d -p 28085:8080 -p 10090:10090 --name tomcat --network apiface --network-alias tomcat -v /IO/INDEX/assets:/IO/assets -v /IO/tomcat/logs:/usr/local/tomcat/logs -v /IO/tomcat/webapps:/usr/local/tomcat/webapps tomcat
docker run -d -p 80:80 -p 443:443 --name nginx -v /IO/INDEX:/IO/INDEX -v /IO/nginx/conf/nginx.conf:/etc/nginx/nginx.conf -v /IO/nginx/logs:/var/log/nginx nginx
docker network connect apiface nginx
# 开防火墙
systemctl start firewalld.service
firewall-cmd --zone=public --add-port=80/tcp --permanent
firewall-cmd --zone=public --add-port=443/tcp --permanent
firewall-cmd --zone=public --add-port=3306/tcp --permanent
firewall-cmd --zone=public --add-port=22/tcp --permanent
firewall-cmd --reload

# 后端不是很懂的人除配置文件的ip地址外最好不要改动 
```
访问：http://ip:port/index 即可


##### tomcat 方式部署 前端包含在jar里面(前后端)
* 1.获取下载安装包下载位置：<a href="http://daysh.top/assets/dist.zip" target="_blank">http://daysh.top/assets/dist.zip</a> 或
                <a href="http://apiface.daysh.top/assets/dist.zip" target="_blank">http://apiface.daysh.top/assets/dist.zip</a> 
* 2.修改配置:
    修改springboot项目中的数据库ip地址
    |--apiface.jar
    |--config
    |--|--appcation-depv.yaml
* 3.部署，需要环境 jdk8+,tomcat,redis,myysql
访问：http://ip:port/apiface/static/index.html 即可


##### 纯前端 方式部署 (无后端)
* 1.获取下载安装包下载位置：<a href="http://daysh.top/assets/apiface.war" target="_blank">http://daysh.top/assets/apiface.war</a> 或
<a href="http://apiface.daysh.top/assets/apiface.war" target="_blank">http://apiface.daysh.top/assets/apiface.war</a> 
* 2.将dist文件下的所有文件部署到任意一台web服务器
访问：http://ip:port/apiface.html 即可


##### <a id="javadocs">apiface的javadoc标记规范</a>
|标记|含义|格式|作用域|生效条件|描述|
|:------:|:------:|:------|:------| :------| :------|
|@ignore|忽略注释标记|@ignore|class,method,feild|所有文档注释|标记该注释不是文档对象不做处理|
|@author|开发者信息标记|@author username|class,method|所有文档注释|标记该类或方法作者信息|
|@author|开发者信息标记|@author username丨email|class,method|所有文档注释|标记该类或方法作者信息|
|@since|版本标记|@since version|class,method|所有文档注释|标明一个类，方法，或其它标识符是在哪个特定版本开始添加进来的|
|@action|接口类标记|@action group,group1|(abstract)class|无@ignore的文档注释|标记该注释类是一个接口类|
|@error|错误描述标记|@error 发送错误时的描述|method|含@action的文档注释|接口的错误描述|
|@date|时间标记|@date data-time|method|含@action的文档注释|接口的最后编辑时间|
|@hidden|隐藏标记|@hidden|class,method|含@action的文档注释|标记该注释中接口类或接口仅仅是一个被继承类引用的文档|
|@pack|使用统一包装|@pack 类全名<T>|class|含@action的文档注释|（现阶段没实现）通常用于指定返回全局包装格式，其中包装类必须是有且仅有一个泛型|
|@unpack|不使用统一包装|@unpack|class,method|含@action的文档注释|（现阶段没实现）标记具体接口不使用全局包装|
|@uri|标记资源uri|@uri /uri|class,method|含@action的文档注释|springmvc可忽略，标记具体接口使用的uri|
|@summary|标记摘要|@summary 摘要|method|含@action的文档注释|接口简短的描述|
|@method|标记请求方法|@method get,post|class,method|含@action的文档注释|springmvc可忽略，标记具体接口使用的请求方法|
|@produce|标记请求context类型|@produce text/html,text/xml|class,method|含@action的文档注释|springmvc可忽略，标记具体接口使用的请求内容|
|@consume|标记响应context类型|@consume text/html,text/xml|class,method|含@action的文档注释|springmvc可忽略，标记具体接口使用的响应内容|
|@exclude|接口排除参数|@exclude param1,param2.field|method|含@action的文档注释|标记具体接口的参数是一个包装类，但有些不想展示的参数|
|@return|接口返回值|@return 类全名<T,V>丨描述|method|含@action的文档注释|标记具体接口的返回是一个包装类|
|@return|接口返回值|@return 类全名或基本类型|method|含@action的文档注释|标记具体接口的返回是一个包装类或者基本类(java的基本类型+string,date)|
|@return|接口返回值|@return int丨描述|method|含@action的文档注释|标记具体接口的返回是一个基本类，描述可以省略|
|@param|接口参数标记|@param 参数名|method|含@action的文档注释|标记具体接口的参数是为某名的基类|
|@param|接口参数标记|@param 参数名丨参数类型|method|含@action的文档注释|标记具体接口的参数是为某名的包装类（类全名）或者基本类|
|@param|接口参数标记|@param 参数名丨参数类型丨描述|method|含@action的文档注释|标记具体接口的参数，带描述|
|@param|接口参数标记|@param 参数名丨参数类型丨必须丨描述|method|含@action的文档注释|标记具体接口的参数，带参数限制|
|@param|接口参数标记|@param 参数名丨参数类型丨必须丨默认值丨描述|method|含@action的文档注释|标记具体接口的参数，带默认值|
|@param|接口参数标记|@param 参数名丨参数类型丨必须丨默认值丨类型 body/form/path/head/cookie丨描述|method|含@action的文档注释|标记具体接口的参数，带类型|
|@model|模型类标记|@model onlyname|(abstract)class,interface|无@ignore的文档注释|标记该注释类是一个模型类|
|@hidden|隐藏标记|@hidden|(get)method,field|含@model的文档注释|标记该注释中模型类中属性字段是否展示|
|@required|是否必须|@required|(get)method,field|含@model的文档注释|标记该注释中模型类中属性字段是否必须|
|@example|隐藏标记|@example 默认值|(get)method,field|含@model的文档注释|标记该注释中模型类中属性字段默认值|