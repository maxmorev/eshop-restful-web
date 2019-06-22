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
    <spring:message code="label_VendorCode" var="labelVendorCode"/>


<script type="text/javascript">

const shoppingCart = ${ShoppingCartCookie};
var branches = [];

function findBranch(){
    var branchId = branches[0].id;
    addToShoppingCartSet(shoppingCart, branchId, 1, showToast("Added to Shopping Cart"));
    //showToast( "SELECTED BRANCH : " + branchId );
}

function showAttributes(propertySet){
  var content="";
  propertySet.forEach(function(prop){
    content += prop.attribute.name + ": " + prop.attributeValue.value + " " + prop.attribute.measure + "<br/>";
  });
  $('#attribute-container').empty();
  $('#attribute-container').append(content);

};

function showAmount(branch){
    var content="${labelAmount}:&#160;"+branch.amount;
    $('#amount-container').empty();
    $('#amount-container').append(content);
}

$(document).ready(function () {
    var branches_str = '${commodity.branches}';
    branches = JSON.parse(branches_str);
    var propertySet = branches[0].attributeSet;
    propertySet.sort(function(a,b){ return a.attribute.measure>b.attribute.measure});
    showAttributes(propertySet);
    showAmount(branches[0]);
    var btnAddToBasket = document.querySelector('#btn-add-to-basket');
    btnAddToBasket.addEventListener('click', findBranch);

});
</script>

    <div class="mdl-grid portfolio-max-width">
        <div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--4dp">
            <c:if test="${not empty commodity}">
                <div class="mdl-card__title">
                    <h2 class="mdl-card__title-text commodity-name">${commodity.type.name}&#160;<b>${commodity.name}</b></h2>
                </div>
                <div class="mdl-card__media" style="background-color:white" >
                    <div class="images">
                        <img id="mainImage" width="100%" src="${commodity.images[0].uri}"/>
                    </div>
                    <div align="center">
                        <c:forEach items="${commodity.images}" var="image" varStatus="loop">
                            <c:if test="${loop.index==0}">
                                <img  id="img-nav" src="${image.uri}" width="100px" onClick="mark(this, '${image.uri}');" class="circleImgSelection"/>
                            </c:if>
                            <c:if test="${loop.index>0}">
                                <img id="img-nav" src="${image.uri}" width="100px" onClick="mark(this, '${image.uri}');" class="circleImgUnselected"/>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
                <div class="mdl-card__supporting-text">
                    <strong>${commodity.type.name}</strong>&#160;<span>${commodity.shortDescription}</span>
                </div>
                <div class="mdl-grid portfolio-copy">
                    <h3 class="mdl-cell mdl-cell--12-col mdl-typography--headline commodity-name">${commodity.type.name}&#160; ${commodity.name}</h3>
                    <div id="vendor-code" class="mdl-cell mdl-cell--12-col mdl-typography--headline">${labelVendorCode}: ${commodity.codeIfSingle}</div>
                    <div class="mdl-cell mdl-cell--12-col mdl-typography--headline" >${labelPrice} &#160;<b>${commodity.price} ₽</b></div>
                    <div class="mdl-cell mdl-cell--6-col mdl-card__supporting-text no-padding">
                        <p class="commodity-overview">${commodity.overview}</p>
                    </div>
                    <div class="mdl-cell mdl-cell--6-col">
                        <img class="article-image" src="${commodity.lastImageUri}" width="200px" border="0" alt=""/>
                    </div>

                    <div class="mdl-grid mdl-cell--12-col">
                        <div class="mdl-cell mdl-cell--8-col">

                            <h3 class="mdl-typography--headline">Attributes:</h3>
                            <h3 class="mdl-typography--headline">
                            <div id="attribute-container">
                            </div>
                            <div id="amount-container">

                            </div>
                            <div id="vendor-code-container">
                            ${labelVendorCode}: ${commodity.codeIfSingle}
                            </div>
                        </div>
                        <div class="mdl-cell mdl-cell--4-col">
                            <div id="action-container">
                                <br/><br/>
                                    <!-- ACTIONS WITH commodity -->
                                <!-- Accent-colored raised button with ripple -->
                                <button id="btn-add-to-basket" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                                ${labelBasket}
                                </button>&#160;<br/><br/>
                                <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                                  ${labelCheckout}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>