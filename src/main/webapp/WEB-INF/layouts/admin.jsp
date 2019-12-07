<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<spring:theme code="mdlPath" var="mdl_Path" />
<spring:url value="/${mdl_Path}" var="mdl_url" />

<html>
<head>
    <meta charset="utf-8"/>
    <meta name="description" content="A portfolio template that uses Material Design Lite."/>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>

    <title>Welcome to Smart E-Shop</title>

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en"/>
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.yellow-pink.min.css" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/application.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin-page.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" />

    <!-- Get the user locale from the page context (it was set by Spring MVC's locale resolver) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>

    <script type="text/javascript">
        var URL_SERVICES = "<%=request.getContextPath().equals("")?"":request.getContextPath()%>/rest/api";
    </script>
    <script src="${pageContext.request.contextPath}/scripts/common.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/admin-page.js"></script>


</head>

<body>

<tiles:insertAttribute name="body"/>
<!-- Toast place -->
<div id="demo-toast-example" class="mdl-js-snackbar mdl-snackbar">
<div class="mdl-snackbar__text"></div>
<button class="mdl-snackbar__action" type="button"></button>
</div>



</body>
</html>