<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    <spring:message code="label_buy" var="labelBuy"/>
    <spring:message code="label_price" var="labelPrice"/>
    <spring:message code="label_checkout" var="labelCheckout"/>
    <spring:message code="label_add_to_basket" var="labelBasket"/>
    <spring:message code="label_choose_props" var="labelChooseProps"/>
    <spring:message code="label_color" var="labelColor"/>
    <spring:message code="label_size" var="labelSize"/>
    <spring:message code="label_amount" var="labelAmount"/>
<script type="text/javascript">
const shoppingCart = '${shoppingCart}';
</script>
<div class="mdl-grid portfolio-max-width">
    <div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--4dp">
        <c:if test="${not empty shoppingCart}">
            <div class="mdl-card__title">
                <h2 class="mdl-card__title-text commodity-name">SHOPPING CART</b></h2>
            </div>
            <div class="mdl-card__media" style="background-color:white" >
            MEDiA BLOCK
            </div>
            <div class="mdl-card__supporting-text">
            <strong>SHOPPING CART</strong>&#160;<span>${shoppingCart.id}</span>
            </div>
            <div class="mdl-grid portfolio-copy">
            TABLE WITH COMMODITIES
            </div>
        </c:if>
    </div>
</div>