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
    <spring:message code="label_shoppingCartWelcome" var="labelWelcome"/>
    <spring:message code="label_choosePM" var="labelChoosePM"/>
    <spring:message code="label_checkout_proceed" var="labelProceedCheckout"/>

    <spring:url value="/commodity" var="showCommodityUrl"/>
    <spring:url value="/shopping/cart/checkout/" var="proceedToCheckoutUrl"/>
    <spring:url value="/customer/account/create/cart/" var="createAccountUrl"/>

<script type="text/javascript">
const shoppingCartJson = '${shoppingCart}';
const showCommodityUrl = '${showCommodityUrl}';

var shoppingCartObj = JSON.parse(shoppingCartJson);
var currentBranchId; //current branchId for update
var shoppingCartUpdate;
var fromAmountName;


$(document).ready(function () {
  activateTab('tab-shopping-cart');
  showShoppingCart(shoppingCartObj);
  showToast("Welcome to shopping cart!");

  var btnProceed = document.querySelector('#cart-btn-proceed');
  btnProceed.addEventListener('click', function() {
    window.location.href = "${proceedToCheckoutUrl}";
  } );

});

</script>
<div class="mdl-grid portfolio-max-width">
     <div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--4dp">
        <c:if test="${not empty shoppingCart}">
            <div class="mdl-card__title">
                <h2 class="mdl-card__title-text commodity-name">${labelWelcome}</b></h2>
            </div>
            <div class="mdl-card__media" style="background-color:white" >

            </div>
            <div class="mdl-card__supporting-text">
            <span>Comment for page</span>
            </div>
            <div class="mdl-grid">

                <tiles:insertAttribute name="cart-container"/>

                <div class="mdl-cell mdl-cell--6-col">&nbsp;</div>
                <div class="mdl-cell mdl-cell--2-col">&nbsp;</div>
                <div class="mdl-cell mdl-cell--4-col">
                <button id="cart-btn-proceed" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">${labelProceedCheckout}</button>
                </div>

            </div>

        </c:if>
    </div>
</div>
