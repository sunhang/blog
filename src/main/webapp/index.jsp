<%@ page import="sunhang.blog.model.Article"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <body>
    <center>
        <h2>乐在行走</h2>
        <c:forEach items="${articles}" var="article">
            <!-- 这里不能用/article，不能加/ -->
            <a href="article?file=${article.getFile().getName()}">${article.getTitle()}</a>    
            <br>
        </c:forEach>
    </center>
</body>
</html>
