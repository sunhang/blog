<%-- 
    Document   : article
    Created on : 2020年9月10日, 下午10:51:44
    Author     : sunhang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=request.getAttribute("title")%></title>
        <c:if test="${!is_from_mobile}">
            <link rel="stylesheet" type="text/css" href="css/article.css" />
        </c:if>
    </head>
    <body>
        <h1 id="title"><%=request.getAttribute("title")%></h1>
        <br/>
        <div class="centerDiv">
            <%=request.getAttribute("content")%>
        </div>
    </body>
</html>
