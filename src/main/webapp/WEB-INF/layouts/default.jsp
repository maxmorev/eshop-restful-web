        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        <%@ page isELIgnored="false" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
        <%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!--
                  Created by maxim.morev on 05/09/19.
                  USING
                  Material Design Lite
                  Copyright 2019. All rights reserved.

                  Licensed under the Apache License, Version 2.0 (the "License");
                  you may not use this file except in compliance with the License.
                  You may obtain a copy of the License at

                      https://www.apache.org/licenses/LICENSE-2.0

                  Unless required by applicable law or agreed to in writing, software
                  distributed under the License is distributed on an "AS IS" BASIS,
                  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                  See the License for the specific language governing permissions and
                  limitations under the License
-->
<html>

<head>
    <spring:theme code="styleSheet" var="app_css" />
    <spring:theme code="styleSheetApp" var="app_css_app" />
    <spring:url value="/${app_css}" var="app_css_url" />
    <spring:url value="/${app_css_app}" var="app_css_app_url" />
    <spring:url value="/images" var="app_img_url" />

    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="description" content="A portfolio template that uses Material Design Lite."/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0"/>
    <spring:message code="application_name" var="app_name" htmlEscape="false"/>
    <title><spring:message code="welcome_h3" arguments="${app_name}" /></title>
    <!--<title>MDL-Static Website</title>-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en"/>
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.grey-pink.min.css" />
    <link rel="stylesheet" href="${app_css_url}" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link rel="stylesheet" href="${app_css_app_url}" />

    <!-- Get the user locale from the page context (it was set by Spring MVC's locale resolver) -->
    <c:set var="userLocale">
        <c:set var="plocale">${pageContext.response.locale}</c:set>
        <c:out value="${fn:replace(plocale, '_', '-')}" default="en" />
    </c:set>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <spring:url value="/" var="url_services" />
    <script type="text/javascript">
        var URL_SERVICES = '${url_services}';
    </script>
    <script src="${pageContext.request.contextPath}/scripts/application.js"></script>
    <style>
    .portfolio-header {
      position: relative;
      background-image: url(${app_img_url}/header-bg.jpg);
    }

    .portfolio-logo {
      background: url(${app_img_url}/logo.png) 50% no-repeat;
      background-size: cover;
      height: 150px;
      width: 150px;
      margin: auto auto 10px;
    }

    footer {
      background-image: url(${app_img_url}/footer-background.png);
      background-size: cover;
    }
    </style>
</head>

<body>
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">

        <tiles:insertAttribute name="header"/>

        <!-- main grid of parts -->
        <main class="mdl-layout__content">
            <tiles:insertAttribute name="body"/>
            <tiles:insertAttribute name="footer"/>
        </main>
        <!-- Toast place -->
        <div id="demo-toast-example" class="mdl-js-snackbar mdl-snackbar">
        <div class="mdl-snackbar__text"></div>
        <button class="mdl-snackbar__action" type="button"></button>
        </div>
        <!-- end Toast place -->
    </div>
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
</body>

</html>
