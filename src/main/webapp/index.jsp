<%@ page import="sunhang.blog.model.Article"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <style type="text/css">
            .articleDiv {
                display:flex;
                height:40px;
                line-height:40px;
            }
            .titleDiv {
                width:450px;
                margin-left:50%;
                transform: translate(-225px, 0);
                font-size: 18px
            }
            .timeDiv {
                width:150px;
                transform: translate(-225px, 0);
                font-size: 14px
            }
        </style>
    </head>
    <body>
    <center>
        <h2>乐在行走</h2>
    </center>
    <c:forEach items="${articles}" var="article">
        <div class="articleDiv">
            <div class="titleDiv">
                <!-- 这里不能用/article，不能加/ -->
                <a href="article?file=${article.getFile().getName()}">
                    ${article.getTitle()}
                </a>
            </div>
            <div class="timeDiv">
                ${article.getTime()}
            </div>
        </div>
    </c:forEach>
</body>
</html>
