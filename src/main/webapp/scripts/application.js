/**
    Created by maxim.morev on 05/06/19.
**/
//IMG GALLERY SECTION
function drawImgBorders(){
    var elms = document.querySelectorAll("[id='img-nav']");

    for(var i = 0; i < elms.length; i++){
        //elms[i].style.display='none';
        elms[i].className = "circleImgUnselected";
    }
}

function mark(el, imgURI) {
    drawImgBorders();
    el.className = "circleImgSelection";
    document.getElementById('mainImage').src=imgURI;
}

//COMMODITY SELECT PROPERTIES SECTION
/**
    APPLICATION PARAMETERS
**/

function addToBasket(errorMessage, successMessage){
    var elms = document.getElementsByClassName("circleSelection");
    var colorId = elms[0].getAttribute("id");
    showToast("SELECTED COLOR : " + colorId);
}



function showToast(message){
    var snackbarContainer = document.querySelector('#demo-toast-example');
    var toastMessage = {message: message };
    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
}

function sendDataAsJson(url, method, data){
    var options = {
        url: url,
        type: method,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data), // Our valid JSON string
        success: function( data, status, xhr ) {
            //...
            showToast("Success");
        },
        error: function( json, status ) {
            showToast("Error");
        }
    };
    $.ajax( options );
}

function addToShoppingCartSet(cartId, branchId, amount){
    //showToast('URL SERVICES is '+URL_SERVICES)
    var urlService = URL_SERVICES + "shoppingCart/";
    var data = {
            shoppingCartId: cartId,
            branchId: branchId,
            amount: amount
    };
    sendDataAsJson(urlService, 'POST', data)
}

