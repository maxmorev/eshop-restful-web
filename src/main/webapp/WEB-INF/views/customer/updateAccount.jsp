<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:message code="label_update_account" var="labelWelcome"/>

<script type="text/javascript">
const customer = '${customer}';
const orders = '${orders}'
var customerObj = JSON.parse(customer);
var ordersObj = JSON.parse(orders);

function errorUpdate(json){
    showErrorFromJson(json);
}

function successUpdateAccount(json){
    showToast("Account Information Updated");
}

function updateAccount(){
    $('#error-container').hide();
    var data = {
                email:    $( "#email" ).val(),
                password: $( "#password" ).val(),
                fullName: $( "#fullName" ).val(),
                country:  $( "#country" ).val(),
                postcode: $( "#postcode" ).val(),
                city:     $( "#city" ).val(),
                address:  $( "#address" ).val()
        };
    updateCustomerAccount(data, successUpdateAccount, errorUpdate);
}

function loadForm(customerObj){
    $('#password').val( customerObj.password );
    $('#password').prop('disabled', true);
        $('#password').parent().addClass("is-dirty");
    $('#email').val( customerObj.email );
    $('#email').prop('disabled', true);
        $('#email').parent().addClass("is-dirty");
    $('#fullName').val( customerObj.fullName );
        $('#fullName').parent().addClass("is-dirty");
    $('#country').val( customerObj.country );
        $('#country').parent().addClass("is-dirty");
    $('#postcode').val( customerObj.postcode );
        $('#postcode').parent().addClass("is-dirty");
    $('#city').val( customerObj.city );
        $('#city').parent().addClass("is-dirty");
    $('#address').val( customerObj.address );
        $('#address').parent().addClass("is-dirty");
}

$(document).ready(function () {
    activateTab('tab-account-update');
    $('#error-container').hide();
    $('#update-info').hide();
    $('#orders-info').hide();
    loadForm(customerObj);

    var btnUpdate = document.querySelector('#btn-update-account');
    btnUpdate.addEventListener('click', function() {
        updateAccount();
    });

    var btnShowUpdate = document.querySelector('#show-update');
    btnShowUpdate.addEventListener('click', function() {
        $('#update-info').show();
        $('#orders-info').hide();
    });

    var btnShowOrders = document.querySelector('#show-orders');
    btnShowOrders.addEventListener('click', function() {
        $('#update-info').hide();
        $('#orders-info').show();
    });

});

</script>
<div class="mdl-grid portfolio-max-width">
     <div class="mdl-cell mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-phone">
     <button id="show-update" class="mdl-button mdl-js-button mdl-button--accent">
       Update delivery info
     </button>
     <button id="show-orders" class="mdl-button mdl-js-button mdl-button--accent">
       My Orders
     </button>
     </div>
     <div id="update-info" class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--2dp">
        <c:if test="${not empty customer}">
            <div class="mdl-card__title">
                <h2 class="mdl-card__title-text commodity-name"></b></h2>
            </div>
            <div class="mdl-card__media" style="background-color:white" >

            </div>
            <div class="mdl-card__supporting-text">
            <span>Update your account info to place an order and delivery</span>
            </div>
            <div class="mdl-grid">
                <div id="error-container" class="mdl-cell mdl-cell--12-col">
                    <h4>Error</h4>
                    <p id="error-message"></p>
                </div>
                <div class="mdl-cell mdl-cell--12-col">
                <h4>Update account info</h4>
                </div>

                <tiles:insertAttribute name="customer_info"/>

                <div id="customer-buttons" class="mdl-cell mdl-cell--12-col">
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">&nbsp;</div>
                    <div class="mdl-cell mdl-cell--4-col">
                    <button id="btn-update-account" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">${labelWelcome}</button>
                    </div>
                </div>
            </div>

        </c:if>
    </div>

    <div id="orders-info">
        <c:if test="${not empty orders}">
        <tiles:insertAttribute name="orders_container"/>
        </c:if>
        <c:if test="${empty orders}">
        <div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--2dp">
        <h2>You have no orders yet</h2>
        </div>
        </c:if>
    </div>
</div>
