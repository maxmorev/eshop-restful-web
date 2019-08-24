<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    <spring:message code="label_update_account" var="labelWelcome"/>

<script type="text/javascript">
const customer = '${customer}';
var customerObj = JSON.parse(customer);

function errorUpdate(json){
    var errorContent = json.responseJSON.message;
    errorContent = json.responseJSON.message.replace(/\n/g, "<br/>");
    var message = errorContent;
    showErrorMessage(message);
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
    $('#email').val( customerObj.email );
    $('#email').prop('disabled', true);
    $('#fullName').val( customerObj.fullName );
    $('#country').val( customerObj.country );
    $('#postcode').val( customerObj.postcode );
    $('#city').val( customerObj.city );
    $('#address').val( customerObj.address );
}

$(document).ready(function () {
    $('#error-container').hide();
    loadForm(customerObj);
    var btnUpdate = document.querySelector('#btn-update-account');
    btnUpdate.addEventListener('click', function() {
        updateAccount();
    });
});

</script>
<div class="mdl-grid portfolio-max-width">
     <div class="mdl-cell mdl-cell--12-col mdl-card mdl-shadow--4dp">
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
</div>
