<%-- 
    Document   : article
    Created on : 2020年9月10日, 下午10:51:44
    Author     : sunhang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=request.getAttribute("title")%></title>
        <style type="text/css">
            .centerDiv {
                width:50%;
                margin-left: 25%
            }
        </style>
    </head>
    <body>
        <h1 style="text-align:center;"><%=request.getAttribute("title")%></h1>
        <br/>
        <div class="centerDiv">
            <%=request.getAttribute("content")%>
        </div>
    </body>
</html>
