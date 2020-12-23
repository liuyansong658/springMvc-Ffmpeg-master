# SpringMVCUpDown
## SpringMVC实现上传下载
### 注意点：
    web.xml
    1、异步上传需在servlet标签中配置
```xml
    <!--异步上传必须添加-->
    <multipart-config>
      <max-file-size>52428800</max-file-size>
      <max-request-size>52428800</max-request-size>
      <file-size-threshold>0</file-size-threshold>
    </multipart-config>
```
