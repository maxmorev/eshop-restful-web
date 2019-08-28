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

function addToShoppingCartSet(cartId, branchId, amount, callBack){
    //showToast('URL SERVICES is '+URL_SERVICES)
    var urlService = URL_SERVICES + "/public/shoppingCart/";
    var data = {
            shoppingCartId: cartId,
            branchId: branchId,
            amount: amount
    };
    return sendDataAsJson(urlService, 'POST', data, callBack);
}

function removeFromShoppingCartSet(cartId, branchId, amount, callBack){
    //showToast('URL SERVICES is '+URL_SERVICES)
    var urlService = URL_SERVICES + "/public/shoppingCart/";
    var data = {
            shoppingCartId: cartId,
            branchId: branchId,
            amount: amount
    };
    return sendDataAsJson(urlService, 'DELETE', data, callBack);
}


function activateTab(className){
    var elements = document.getElementsByClassName(className);
    var i;
    for (i = 0; i < elements.length; i++) {
        var element = elements[i];
        element.className = className+ ' mdl-navigation__link is-active';
    }
    componentHandler.upgradeDom();
}

function isWear(name){
    if( name=="size" || name=="color" ) {
        return true;
    }
    return false;
}

function getNotWearAttributes(attributes){
    var notWearAttributes = attributes.filter( function(a){return !isWear(a.attribute.name) ; });
    if(notWearAttributes.length>0)
        notWearAttributes.sort(function(a,b){ return a.attribute.measure>b.attribute.measure});
    return notWearAttributes;
}


function showCommodityAttribute(prop){
    return prop.attribute.name + ": " + prop.attributeValue.value + " " + prop.attribute.measure + "<br/>";
}
function showWearAttrubute(attr){
    var attributesContent = '';
    if(attr.attribute.name=="size"){
        attributesContent += attr.attribute.name + ':' + showSizeElement(attr.attributeValue.value);
    }
    if(attr.attribute.name=="color"){
        attributesContent += attr.attribute.name + ':' + showColorElement(attr.attributeValue.value);
    }
    return attributesContent;
}

function updateCustomerAccount(data, callBack, errorCallBack){
    var urlService = URL_SERVICES + "/customer/update/";

    return sendDataAsJson(urlService, 'PUT', data, callBack, errorCallBack);
}

function showErrorMessage(message){
    $('#error-message').empty();
    $('#error-message').append(message);
    $('#error-container').show();
}

function showShoppingCartIconDataBadge(itemsAmount){
    if(itemsAmount>0)
        $( ".shopping-cart-nav" ).attr( "data-badge", itemsAmount );
    else
        $( ".shopping-cart-nav" ).removeAttr("data-badge");
}

function getAmountItemsInShoppingCart(shoppingCart){

    var shoppingSet = shoppingCart.shoppingSet;
    var totalItems = 0;
    var totalPrice = 0;
    shoppingSet.forEach(function(set){
        totalItems += set.amount;
        totalPrice += set.amount*set.branch.price;
    });
    var data = {
        amount: totalItems,
        price: totalPrice
            };
    return data;
}