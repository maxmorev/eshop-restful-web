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
     <spring:url value="/commodity" var="showCommodityUrl"/>
<script type="text/javascript">

const shoppingCartJson = '${shoppingCart}';
const showCommodityUrl = '${showCommodityUrl}';

var shoppingCart = JSON.parse(shoppingCartJson);

function addToSet(setId){
}

function removeFromSet(setId){
}

function showShoppingCart(){

    var shoppingSet = shoppingCart.shoppingSet;
    var content = "";
    var totalItems = 0;
    var totalPrice = 0;
    shoppingSet.forEach(function(set){
        content += '<tr>';
        content += '<td class="mdl-data-table__cell--non-numeric"><a href="'+showCommodityUrl+'/'+set.commodityInfo.id+'"><img src="'+set.commodityInfo.images[0].uri+'" width="100px"/></a></td>';
        content += '<td class="mdl-data-table__cell--non-numeric"><a href="'+showCommodityUrl+'/'+set.commodityInfo.id+'">'+set.commodityInfo.type.name+': '+set.commodityInfo.name+'</a></td>';
        content += '<td>'+set.amount+'</td>';
        content += '<td>'+set.branch.price+'</td>';
        content += '<td>'+set.amount*set.branch.price+'</td>';
        content += '<td><button class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored" onclick="addToSet('+set.id+')"><i class="material-icons">add</i></button></td>';
        content += '<td><button class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored" onclick="removeFromSet('+set.id+')"><i class="material-icons">remove</i></button></td>';
        content += '</tr>';
        totalItems += set.amount;
        totalPrice += set.amount*set.branch.price;
    });
    $('#cart-container').empty();
    $('#cart-container').append(content);

    $('#total-items').empty();
    $('#total-items').append('<b>'+totalItems+'</b>');

    $('#total-cart-price').empty();
    $('#total-cart-price').append('<b>£'+totalPrice+'</b>');

    componentHandler.upgradeDom();
}


$(document).ready(function () {
  showShoppingCart();
});

</script>
<div class="mdl-grid portfolio-max-width">
     <div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--4dp">
        <c:if test="${not empty shoppingCart}">
            <div class="mdl-card__title">
                <h2 class="mdl-card__title-text commodity-name">SHOPPING CART</b></h2>
            </div>
            <div class="mdl-card__media" style="background-color:white" >

            </div>
            <div class="mdl-card__supporting-text">
            <strong>SHOPPING CART</strong>&#160;<span>${shoppingCart.id}</span>
            </div>
            <div class="mdl-grid">

                <div class="mdl-cell mdl-cell--6-col">&nbsp;</div>
                <div class="mdl-cell mdl-cell--6-col">Shopping Cart Subtotal (<div class="data-holder" id="total-items">5</div> items):&nbsp;<div class="data-holder" id="total-cart-price">£2</div></div>

                <div class="mdl-cell mdl-cell--12-col">
                <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp">
                  <thead>
                    <tr>
                      <th class="mdl-data-table__cell--non-numeric">Preview</th>
                      <th class="mdl-data-table__cell--non-numeric">Commodity</th>
                      <th>Quantity</th>
                      <th>Unit price</th>
                      <th>Price for Units</th>
                      <th>Add</th>
                      <th>Remove</th>
                    </tr>
                  </thead>
                  <tbody id="cart-container">

                  </tbody>
                </table>
                </div>
                <div class="mdl-cell mdl-cell--6-col">&nbsp;</div>
                <div class="mdl-cell mdl-cell--2-col">&nbsp;</div>
                <div class="mdl-cell mdl-cell--4-col">
                <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">${labelCheckout}</button>
                </div>

            </div>
        </c:if>
    </div>
</div>