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
    <spring:url value="/shopping/cart/checkout/" var="proceedToCheckoutUrl"/>


<script type="text/javascript">
const shoppingCart = ${ShoppingCartCookie};
const commodityId = ${commodity.id};
var BRANCHES = [];
var content = "";
var SELECTED_SIZE;
var SELECTED_COLOR;
var SELECTED_BRANCH;
var AMOUNT;
var shoppingCartObj;

function addToShoppingCartSuccess(json){
    shoppingCartObj = json;
    var shoppingCartReview = getAmountItemsInShoppingCart(shoppingCartObj);
    showShoppingCartIconDataBadge(shoppingCartReview.amount);
    showToast("Added to Shopping Cart");
}

function addToShoppingCart(){
    addToShoppingCartSet(shoppingCart, SELECTED_BRANCH, 1, addToShoppingCartSuccess);
}

function showVendorCode(){
    var content = '${labelVendorCode}: ' + commodityId +'-'+SELECTED_BRANCH;
    $('#vendor-code-container').empty();
    $('#vendor-code-container').append(content);
    $('#vendor-code-container').show();

    $('#vendor-code').empty();
    $('#vendor-code').append(content);
    $('#vendor-code').show();
}

function findBranch(){
    BRANCHES.forEach( function(value){ if( value.sizes[0].value===SELECTED_SIZE && value.colors[0].value==SELECTED_COLOR ){ SELECTED_BRANCH=value.id; AMOUNT=value.amount;} } );
    content = "${labelAmount}:&#160;" + AMOUNT;
    $('#amount-container').empty();
    $('#amount-container').append(content);
    $('#amount-container').show();

    //show vendor code
    showVendorCode();

    //showToast( "SELECTED BRANCH : " + SELECTED_BRANCH );
};

function onColorClick(el){

    var elms = document.getElementsByClassName("colorCircleSelect");
    //
    for(var i = 0; i < elms.length; i++){
       elms[i].className = "colorCircleSelect";
    }
    el.className = "colorCircleSelect circleSelection";
    var color = el.getAttribute("value");
    SELECTED_COLOR = color;

    //DRAW SIZES
    $('#action-container').show();
    findBranch();
    //showToast("SELECTED SIZE & COLOR : " +SELECTED_SIZE + " | " + SELECTED_COLOR);
    componentHandler.upgradeDom();
}

function genColorContent(value, index){
    content += '<div id="color-'+value+'" class="colorCircleSelect" style="background:'+value+';" onclick="onColorClick(this);" value="'+value+'">&#160;&#160;&#160;&#160;&#160;</div>&#160;';
};

function showColors(el){
    var size = el.getAttribute("value");
    SELECTED_SIZE = size;
    //showToast("showColors " + SELECTED_SIZE);
    var colors = [];
    BRANCHES.forEach( function(value){ if( value.sizes[0].value===size ){colors.push( value.colors[0].value );} } );
    colors.sort();
    colors.reverse();
    content = "${labelColor}:&#160;";
    colors.forEach(genColorContent);

    $('#color-container').empty();
    $('#color-container').append(content);
    $('#color-container').show();
    componentHandler.upgradeDom();

    //find element auto select first color
    var element = document.getElementById('color-'+colors[0]);
    onColorClick(element);

};

function genSizeContent(value, index){
   content += '<label class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="size-'+index+'">';
   content += '<input type="radio" id="size-'+index+'" class="mdl-radio__button" name="size" value="'+value+'" onclick="showColors(this);"/>';
   content += '<span class="mdl-radio__label commodity-name">'+value+'</span>';
   content += '</label>&#160;';

};

function showSizes(){
    $('#action-container').hide();
    $('#color-container').hide();
    $('#amount-container').hide();

    var uniqSizes = [];
    BRANCHES.forEach( function(value){ if(!uniqSizes.includes(value.sizes[0].value)){uniqSizes.push(value.sizes[0].value)} });
    uniqSizes.sort();
    uniqSizes.reverse();
    content = "${labelSize}:&#160;";
    uniqSizes.forEach(genSizeContent);
    $('#size-container').empty();
    $('#size-container').append(content);
};

$(document).ready(function () {
<c:if test="${not empty commodity}">
var str_branches = '${commodity.branches}';
var objJson = JSON.parse(str_branches);
for(var i=0; i<objJson.length; i++){
    var br = objJson[i];
    var sizes = [];
    var colors = [];
    br.attributeSet.forEach( function(propertySet){
        if(propertySet.attribute.name=="size"){sizes.push({ id: propertySet.attributeValue.id, value: propertySet.attributeValue.value });}
        if(propertySet.attribute.name=="color"){colors.push({ id: propertySet.attributeValue.id, value: propertySet.attributeValue.value });}
    });
    var branch = {
        id : br.id,
        amount : br.amount,
        sizes : sizes,
        colors :  colors
      };
    BRANCHES.push(branch);
}
</c:if>

var btnPropertyBack = document.querySelector('#btn-add-to-basket');
btnPropertyBack.addEventListener('click', addToShoppingCart);
showSizes();

var btnProceed = document.querySelector('#btn-proceed');
btnProceed.addEventListener('click', function() {
    window.location.href = "${proceedToCheckoutUrl}";
} );

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
                    <h3 class="mdl-cell mdl-cell--12-col mdl-typography--headline commodity-name">${commodity.type.name}&#160; ${commodity.name} </h3>
                    <div id="vendor-code" class="mdl-cell mdl-cell--12-col mdl-typography--headline"></div>
                    <div class="mdl-cell mdl-cell--12-col mdl-typography--headline" >${labelPrice} &#160;<b>${commodity.price} ${commodity.currencyCode}</b></div>
                    <div class="mdl-cell mdl-cell--6-col mdl-card__supporting-text no-padding">
                        <p class="commodity-overview">${commodity.overview}</p>
                    </div>
                    <div class="mdl-cell mdl-cell--6-col">
                        <img class="article-image" src="${commodity.lastImageUri}" width="200px" border="0" alt=""/>
                    </div>

                    <div class="mdl-grid mdl-cell--12-col">
                        <div class="mdl-cell mdl-cell--8-col">

                            <h3 class="mdl-typography--headline">${labelChooseProps} ${labelCheckout}</h3>
                            <h3 class="mdl-typography--headline">
                            <div id="size-container">
                            </div>
                            <div id="color-container">
                            </div>
                            </h3>
                            <div id="amount-container">
                            </div>
                            <div id="vendor-code-container">
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
                                <button id="btn-proceed" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                                  ${labelCheckout}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

