     <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
     <%@ page isELIgnored="false" %>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
     <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
     <%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
     <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    <spring:message code="application_name" var="appName"/>
    <spring:message code="label_help" var="labelHelp"/>
    <spring:message code="label_privacy" var="labelPrivacy"/>


    <footer class="mdl-mini-footer">
        <div class="mdl-mini-footer__left-section">
            <div class="mdl-logo">${appName}</div>
        </div>
        <div class="mdl-mini-footer__right-section">
            <ul class="mdl-mini-footer__link-list">
                <li><a href="#help">${labelHelp}</a></li>
                <li><a href="#privacy">${labelPrivacy}</a></li>
            </ul>
        </div>
    </footer>


