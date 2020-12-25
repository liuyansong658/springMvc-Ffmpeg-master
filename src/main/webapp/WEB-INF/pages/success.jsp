<%--
  Created by IntelliJ IDEA.
  User: 刘岩松
  Date: 2020/12/25
  Time: 下午 03:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>多文件下载</title>
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

        ul{
            list-style: none;
        }
        .nav>li{
            float: left;
        }
        ul a{
            display: list-item;
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
<div class="main">
    <ul class="nav">
        <li><a href="to_upload">上传文件</a></li>
        <li><a href="getDownloads">下载文件</a></li>
    </ul>
    <br/> <br/>
    <form action="do_upload" method="post" enctype="multipart/form-data">
        <br/> <br/>
        <h1>已转换完成视频</h1>
        <br/> <br/>
        <c:forEach items="${list}" var="item">
            <c:choose>
                <c:when test="${item.fileName eq 'no data!'}">
                    <a href="getDownloads"> ${item.fileName} </a>
                </c:when>
                <c:otherwise>
                    <a href="do_download?filename=${item.fileName}">${item.fileName}</a>

                </c:otherwise>
            </c:choose>
            <br/> <br/>
        </c:forEach>
        <hr>
    </form>
    <hr>

</div>
<%--        <a href="do_download?filename=${fileName}">${fileName}</a>--%>
</body>
</html>
