<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: 刘岩松
  Date: 2020/12/25
  Time: 下午 03:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }
        html, body {
            height: 100%;
        }
        body {
            background: url(img/bg.jpg) no-repeat center center;
            background-size: cover;
        }
        .main {
            width: 800px;
            height: 880px;
            position: absolute;
            left: 50%;
            top: 50%;
            box-sizing: border-box;
            padding: 25px 30px;
            margin-left: -185px;
            margin-top: -190px;
            border-radius: 5px;
            background-image: url("img/tmbg-white.png");
        }

        .main > h1 {
            color: #333;
            font-size: 24px;
            font-weight: lighter;
            text-align: center;
        }
        .music, .password, .verification-code {
            margin-top: 20px;
            position: relative;
        }
        .music {
            margin-top: 25px;
        }
        .music > input, .name > input {
            width: 100%;
            height: 46px;
            font-size: 16px;
            box-sizing: border-box;
            padding: 10px;
            border: 1px solid #ddd;
            outline: 0 none;
            line-height: 24px;
            border-radius: 3px;
        }

        ul{
            list-style: none;
        }
        .nav>li{
            float: left;
        }
        ul a{
            display: block;
            text-decoration: none;
            width: 100px;
            height: 50px;
            text-align: center;
            line-height: 50px;
            color: white;
            background-color: #00CCFF;
        }

    </style>
</head>
<body>
<!-- 文件上传必须 method="post" enctype="multipart/form-data" -->

<div class="main">
    <ul class="nav">
        <li><a href="to_upload">上传文件</a></li>
        <li><a href="getDownloads">下载文件</a></li>
    </ul>
<%--    <h1>文件上传</h1>--%>
    <br/> <br/>
    <form action="do_upload" method="post" enctype="multipart/form-data">
        <br/> <br/>
        <input type="file" name="myFile"/>
        <br/> <br/>
        <input type="checkbox" name="music" >智能配乐
        <br/> <br/>
        上下滤镜倍数：<input type="text" name="multi" value="1">
        <br/> <br/>
        <input type="submit" value="上传"/>
        <br/> <br/>
    </form>
    <hr>
    <%--<h2>多文件上传</h2>
    <form action="do_eupload" method="post" enctype="multipart/form-data">
        <input type="file" name="files"/>
        <input type="file" name="files"/>
        <input type="file" name="files"/>
        <input type="submit" value="上传"/>
    </form>--%>
    <hr>
    <img id="img" src="" width="200" height="200" style="display: none"/>
    <hr>
    <h1>异步上传</h1>
    <br/>
    <input type="file" name="ajaxfile" id="file"/>
    <br/> <br/>
    <input type="button" value="上传" onclick="upload()"/>

</div>


<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
    function  upload() {
        //js file 对象
        var file=$("#file")[0].files[0];

        //js form
        var form=new FormData();
        form.append("ajaxfile",file);
        form.append("music",document.getElementsByName("music")[0].checked);
        form.append("multi",document.getElementsByName("multi")[0].value);
        //jquery ajax
        var opt={
            url:"ajax_upload",
            type:"post",
            contentType:false,
            processData:false,
            data:form,
            success:function(data){
                var json=eval("("+data+")");
                $("#img").attr("src","/upload/"+json.url);
                $("#img").attr('style','display:block')
            }
        };
        $.ajax(opt);
    }

</script>
</body>
</html>
