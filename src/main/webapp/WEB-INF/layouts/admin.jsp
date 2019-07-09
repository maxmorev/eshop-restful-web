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
    <link rel="stylesheet" href="${mdl_url}/material.min.css">
    <!--local scripts -->
    <link rel="stylesheet" href="${mdl_url}/fonts.css">
    <link rel="stylesheet" href="${mdl_url}/material.min.css">

    <script defer src="${mdl_url}/material.min.js"></script>
    <script src="${mdl_url}/jquery.min.js"></script>
    <!-- commodity css -->
    <!-- end of commodity css -->
    <script type="text/javascript">

    var URL_SERVICES = "<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath().equals("")?"":request.getContextPath()%>";
    </script>

</head>

<body>
<tiles:insertAttribute name="body"/>
</body>
</html>