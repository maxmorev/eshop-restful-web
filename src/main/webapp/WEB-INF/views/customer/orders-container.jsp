<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:url value="/commodity" var="showCommodityUrl"/>

<c:forEach items="${orders}" var="order">
<div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--2dp">
    <div class="mdl-grid">
        <div class="mdl-cell mdl-cell--12-col">
        Order #${order.id}<br/>
        Date <fmt:formatDate value="${order.dateOfCreation}" pattern="yy-MMM-dd HH:mm"/>&nbsp;
        Total price: ${order.totalPrice}
        </div>
        <c:forEach items="${order.purchases}" var="purchase">
        <a class="mdl-cell mdl-cell--6-col" href="${showCommodityUrl}/${purchase.commodityId}" target="_blank">
        <img  src="${purchase.images[0]}" width="150px" border="0" alt="" />
        <img  src="${purchase.images[1]}" width="150px" border="0" alt="" />
        </a>
        <div class="mdl-cell mdl-cell--6-col mdl-card__supporting-text">
        ${purchase.name}&nbsp;${purchase.shortDescription}<br/>
        Amount:${purchase.amount}<br/>
        Price:${purchase.price*purchase.amount}<br/>
        </div>
        </c:forEach>
        <!-- Accent-colored flat button -->
        <button class="mdl-cell mdl-cell--12-col mdl-button mdl-js-button mdl-button--accent">
          Cancel order
        </button>
    </div>

</div>
</c:forEach>