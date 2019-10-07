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
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <!--<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">-->

        <!--local scripts -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/mdl/fonts.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/mdl/material.min.css">
        <!-- shop css -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/application.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin-page.css">

        <script defer src="${pageContext.request.contextPath}/mdl/material.min.js"></script>
        <script src="${pageContext.request.contextPath}/mdl/jquery.min.js"></script>
        <!-- commodity css -->
        <!-- end of commodity css -->
    <script type="text/javascript">
    var URL_SERVICES = "<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath().equals("")?"":request.getContextPath()%>/rest/api";
    </script>
     <script src="${pageContext.request.contextPath}/scripts/common.js"></script>

</head>

<body>
<tiles:insertAttribute name="body"/>
<!-- Toast place -->
<div id="demo-toast-example" class="mdl-js-snackbar mdl-snackbar">
<div class="mdl-snackbar__text"></div>
<button class="mdl-snackbar__action" type="button"></button>
</div>
<!-- end Toast place -->
<script defer src="${mdl_url}/material.min.js"></script>
</body>
</html>