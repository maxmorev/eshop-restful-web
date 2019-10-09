<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    <spring:message code="label_buy" var="labelBuy"/>
    <spring:message code="label_price" var="labelPrice"/>
    <spring:message code="label_checkout" var="labelCheckout"/>
    <spring:message code="label_back_to_cart" var="labelBack"/>
    <spring:message code="label_back" var="labelBackVerify"/>
    <spring:message code="label_create_account" var="labelWelcome"/>
    <spring:message code="label_create_account" var="labelCreateAccount"/>
    <spring:message code="label_verify_email" var="labelVerifyEmail"/>
    <spring:url value="/shopping/cart/" var="backUrl"/>
    <spring:url value="/shopping/cart/checkout/" var="checkOutUrl"/>

<script type="text/javascript">
const shoppingCartJson = '${shoppingCart}';
const showCommodityUrl = '${showCommodityUrl}';

var shoppingCartObj;
const fromPage = '${fromPage}';``
var customer;
var customerId;
const verifyPrefix = "verify";

function createAccountSuccess(json){

    customer = json;
    customerId = json.id;
    $('#error-message').hide();
    $('#consumer-verify-email').show();
    $('#customer-info').hide();
    $('#customer-buttons').hide();
    componentHandler.upgradeDom();
    window.location.href += "#" + verifyPrefix +"/" + customer.id;
    showToast("Thank you! Please Verify Email.");
}

function createAccountError(json){
    var errorContent = json.responseJSON.message;
    errorContent = json.responseJSON.message.replace(/\n/g, "<br/>")
    var message = errorContent;
    showErrorMessage(message);
}

function createAccount(){
    $('#error-container').hide();

    var urlService = URL_SERVICES + "/public/customer/";
    var data = {
            email:    $( "#email" ).val(),
            password: $( "#password" ).val(),
            fullName: $( "#fullName" ).val(),
            country:  $( "#country" ).val(),
            postcode: $( "#postcode" ).val(),
            city:     $( "#city" ).val(),
            address:  $( "#address" ).val(),
            shoppingCartId: shoppingCartObj.id
    };
    return sendDataAsJson(urlService, 'POST', data, createAccountSuccess, createAccountError);
}

function showCreateForm(){
    $('#error-container').hide();
    $('#consumer-verify-email').hide();
    $('#customer-info').show();
    $('#customer-buttons').show();
    componentHandler.upgradeDom();
}

function showVerifyForm(){
    $('#error-container').hide();
    $('#consumer-verify-email').show();
    $('#customer-info').hide();
    $('#customer-buttons').hide();
    componentHandler.upgradeDom();
}

function getCustomerIdFromUrl(){
    var currentURL = window.location.href;
    var splited = currentURL.split("#");

    if( splited.length==1 ){
        return null;
    }else{
        if( splited.length>1 && splited[1].includes(verifyPrefix) ){
            var params = splited[1].split("/");
            return params[1];
        }else{
            return null;
        }
    }
}

function checkUrl(){
    var currentURL = window.location.href;
    var splited = currentURL.split("#");
    if(splited.length==1){
        showCreateForm();
    }else{
        if( splited.length>1 && splited[1].includes(verifyPrefix) ){
            customerId = getCustomerIdFromUrl();
            showVerifyForm();
        }else{
            showCreateForm();
        }
    }
}

function verifyAccountError(json){
    var errorContent = json.responseJSON.message;
    errorContent = json.responseJSON.message.replace(/\n/g, "<br/>");
    var message = errorContent;
    showErrorMessage(message);
}

function verifyAccountSuccess(json){
    console.log("success json is");
    console.log(json);
    if(json.verified==true){
        showToast("Verification Successful");
        //redirect to payment page
        window.location.href = "${checkOutUrl}";
    }else{
        var message = "Verification Failed";
        showErrorMessage(message);
        showToast(message);
    }
}

function verifyAccount(){
    var verifyCode = $( "#verifyCode" ).val();
    showToast("VERIFY customer > " + customerId + " with code > " + verifyCode);
    $('#error-container').hide();
    var urlService = URL_SERVICES + "/public/customer/verify/";
    var data = {
            id: customerId,
            verifyCode: verifyCode
    };
    sendDataAsJson(urlService, 'POST', data, verifyAccountSuccess, verifyAccountError);
}

$(document).ready(function () {
  if( shoppingCartJson.length>0 ){
    shoppingCartObj = JSON.parse(shoppingCartJson);
    showShoppingCart(shoppingCartObj);
    showToast("Please create account!");
  }


  checkUrl();

  var btnCreateAccount = document.querySelector('#btn-create-account');
  btnCreateAccount.addEventListener('click', function() {
    //sendJson
    //validate and if success
    createAccount();
  } );

  var btnEmailBack = document.querySelector('#btn-verify-email-back');
  btnEmailBack.addEventListener('click', function() {
      showCreateForm();
   } );



  var btnBack = document.querySelector('#btn-proceed-back');
  btnBack.addEventListener('click', function() {
    window.location.href = "${backUrl}";
  } );

  var btnVerify = document.querySelector('#btn-verify-email');
  btnVerify.addEventListener('click', function() {
    verifyAccount();
  });

});

</script>
<div class="mdl-grid portfolio-max-width">
     <div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--4dp">

            <div class="mdl-card__title">
                <h2 class="mdl-card__title-text commodity-name"></b></h2>
            </div>
            <div class="mdl-card__media" style="background-color:white" >

            </div>
            <div class="mdl-card__supporting-text">
            <span>Create an account to place an order and delivery</span>
            </div>
            <div class="mdl-grid">
                <div id="error-container" class="mdl-cell mdl-cell--12-col">
                    <h4>Error</h4>
                    <p id="error-message"></p>
                </div>
                <div class="mdl-cell mdl-cell--12-col">
                <h4>Create account</h4>
                </div>

                <tiles:insertAttribute name="customer_info"/>

                <div id="customer-buttons" class="mdl-cell mdl-cell--12-col">
                <div class="mdl-grid">
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                    <button id="btn-create-account" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">${labelWelcome}</button>
                    </div>

                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                    <button id="btn-proceed-back" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">${labelBack}</button>
                    </div>
                </div>
                </div>

                <div id="consumer-verify-email" class="mdl-cell mdl-cell--12-col">
                <div class="mdl-grid">
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                    <h2 class="mdl-card__title-text">Check your mail to get a verification code.</h2>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>

                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                    <h2 class="mdl-card__title-text">Verify your email</h2>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>

                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                        <div class="mdl-card__actions mdl-card--border">
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="verifyCode" maxlength="5">
                        <label class="mdl-textfield__label" for="verifyCode">Code</label>
                        </div>
                        </div>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>

                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                        <button id="btn-verify-email" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">${labelVerifyEmail}</button>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>

                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                        <button id="btn-verify-email-back" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">${labelBackVerify}</button>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                </div>
                </div>

            </div>

    </div>
</div>
