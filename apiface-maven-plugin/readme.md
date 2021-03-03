八、Maven相关知识点
8.1 生命周期
validate: 验证
initialize: 初始化构建状态，例如设置属性或创建目录
generate-sources: 生成包含在编译中的任何源代码
process-sources: 处理源代码
generate-resources: 生成包含在包中的资源
process-resources: 将资源复制并处理到目标目录中，准备打包
compile: 编译项目的源代码
process-classes: 处理编译后生成的文件
generate-test-sources: 生成包含在编译中的任何测试源代码
process-test-sources: 处理测试源代码
generate-test-resources: 创建测试资源
process-test-resources: 将资源复制并处理到测试目标目录中
test-compile: 将测试源代码编译到测试目标目录中
process-test-classes: 处理编译后产生的测试文件
test: 测试
prepare-package: 预打包
package: 打包
verify: 验证
install: 安装
deploy: 部署
8.2 属性
8.2.1 内置属性
Maven预定义,用户可以直接使用

${basedir} 表示项目根目录,即包含pom.xml文件的目录;
${version} 表示项目版本;
${project.basedir} 同${basedir};
${project.baseUri} 表示项目文件地址;
${maven.build.timestamp} 表示项目构件开始时间;
${maven.build.timestamp.format} 表示属性${maven.build.timestamp}的展示格式,默认值为yyyyMMdd-HHmm,可自定义其格式,其类型可参考java.text.SimpleDateFormat。用法如下：
<properties>
  <maven.build.timestamp.format>yyyy-MM-dd HH:mm:ss</maven.build.timestamp.format>
</properties>
8.2.2 POM属性
使用pom属性可以引用到pom.xml文件对应元素的值

${project.build.directory} 表示主源码路径;
${project.build.sourceEncoding} 表示主源码的编码格式;
${project.build.sourceDirectory} 表示主源码路径;
${project.build.finalName} 表示输出文件名称;
${project.version} 表示项目版本,与${version}相同;
