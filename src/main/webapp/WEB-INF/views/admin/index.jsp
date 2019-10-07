        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        <%@ page isELIgnored="false" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
        <%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- Simple header with scrollable tabs. tiles -->
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
            <!-- Title -->
            <span class="mdl-layout-title">Smart eShop</span>
        </div>
        <!-- Tabs -->
        <div class="mdl-layout__tab-bar mdl-js-ripple-effect">
            <a href="#scroll-tab-1" class="mdl-layout__tab is-active">Commodity Types</a>
            <a href="#scroll-tab-2" class="mdl-layout__tab" onclick="loadBranchList();">Commodity</a>

        </div>
    </header>
    <div class="mdl-layout__drawer">
        <span class="mdl-layout-title">Smart eShop</span>
    </div>
    <main class="mdl-layout__content">
        <!-- section 1 -->
        <tiles:insertAttribute name="section-type"/>
        <tiles:insertAttribute name="section-commodity"/>
        <!-- section 2 -->

    </main>
</div>