# SpringMVCUpDown
## SpringMVC实现上传下载

    Created by IntelliJ IDEA.
    User: 刘岩松
    Date: 2020/12/25

### 注意点：
    1.下载yasm-1.3.0.tar.gz这个版本
    wget http://www.tortall.net/projects/yasm/releases/yasm-1.3.0.tar.gz
    tar xzvf yasm-1.3.0.tar.gz
    cd yasm-1.3.0
    ./configure
    make
    make install

    2.下载3.1.3 release ffmpeg
    安装完成之后输入  ffmpeg -version 可以看到版本说明安装成功
    wget http://ffmpeg.org/releases/ffmpeg-3.1.3.tar.gz
    tar -zxvf ffmpeg-3.1.3.tar.gz
    cd ffmpeg-3.1.3
    ./configure
    make
    make install

    3. 安装JDK1.8以上
    https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
      Tomcat 8.0以上
    http://tomcat.apache.org/download-80


    4.war包放入tomcat里直接可以运行

    修改app.properties里面的ffmpeg路径地址。


    访问地址：http://localhost:8080/SpringMVCUpDown/to_upload

    4.  web.xml
      异步上传需在servlet标签中配置

    5. 项目启动依赖pom内
    <!-- https://mvnrepository.com/artifact/it.sauronsoftware/jave -->
    <dependency>
      <groupId>it.sauronsoftware</groupId>
      <artifactId>jave</artifactId>
      <version>1.0.2</version>
    </dependency>
    该jar包已经放入WEB-INF下的lib下，可以选择手动依赖
```xml
    <!--异步上传必须添加-->
    <multipart-config>
      <max-file-size>52428800</max-file-size>
      <max-request-size>52428800</max-request-size>
      <file-size-threshold>0</file-size-threshold>
    </multipart-config>
```


