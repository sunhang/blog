<%@ page import="sunhang.blog.model.Article"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>乐在行走-程序员小航</title>
        <c:if test="${!is_from_mobile}">
            <link rel="stylesheet" type="text/css" href="css/index.css" />
        </c:if>
    </head>
    <body>
        <div id="page_title_div">
            <h1 id="enjoy_run">乐在行走</h1>
            <a id="about_me" href="about.html">关于我</a>
        </div>
        <div id="recommended">
            友情推荐：
            <a href="https://blog.codingnow.com/">云风</a>&nbsp;
            <a href="https://www.xiaohui.com/">小辉</a>&nbsp;
            <a href="http://www.heqiangfly.com/">寒江蓑笠</a>&nbsp;
            <a href="more_recommended.html">更多>></a>
            &nbsp;
            您是第${visitor_count}位访客
        </div>
        <br/>
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
        <br/>
        <nav id="nav_page">           
            <c:if test="${pre_page != -1}">
                <a href="default?page_index=0">首页</a>
                &nbsp;
                <a href="default?page_index=${pre_page}">上一页</a>
                &nbsp;
            </c:if>
            <c:if test="${next_page != -1}">
                <a href="default?page_index=${next_page}">下一页</a>
            </c:if>
        </nav>
        <br/>
    <center> <a id="beian" href="http://beian.miit.gov.cn">京ICP备2020038765号</a> </center>
</body>
</html>
