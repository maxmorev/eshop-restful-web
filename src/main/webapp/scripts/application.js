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

function getShoppingCart(id, callBack) {
  var urlService = URL_SERVICES + "/public/shoppingCart/id/"+id;
  return sendDataAsJson(urlService, 'GET', null, callBack);
}

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
    var notWearAttributes = attributes.filter( function(a){return !isWear(a.name) ; });
    if(notWearAttributes.length>0)
        notWearAttributes.sort(function(a,b){ return a.measure>b.measure});
    return notWearAttributes;
}


function showCommodityAttribute(prop){
    return prop.name + ": " + prop.value + " " + prop.measure + "<br/>";
}
function showWearAttrubute(attr){
    var attributesContent = '';
    if(attr.name=="size"){
        attributesContent += attr.name + ':' + showSizeElement(attr.value);
    }
    if(attr.name=="color"){
        attributesContent += attr.name + ':' + showColorElement(attr.value);
    }
    return attributesContent;
}

function updateCustomerAccount(data, callBack, errorCallBack){
    var urlService = URL_SERVICES + "/customer/update/";

    return sendDataAsJson(urlService, 'PUT', data, callBack, errorCallBack);
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

/**
Shopping Cart tab
**/

function getAmountByBranch(shoppingCart, branchId){
    var sets = shoppingCart.shoppingSet;
    for(var i=0; i<sets.length; i++){
        set = sets[i];
        if(set.branch.id==branchId){
            fromAmountName = set.commodityInfo.name;
            return set.amount;
        }
    }
}

function refreshShoppingCart(json){
    shoppingCartUpdate = json;
    showShoppingCart(json);
    if(json.shoppingSet.length==0){
        hideShoppingCart();
        showToast("Shopping cart is Empty");
    }else{
        var oldAmount = getAmountByBranch(shoppingCartObj, currentBranchId);
        var newAmount = getAmountByBranch(shoppingCartUpdate, currentBranchId);
        shoppingCartObj = shoppingCartUpdate;
        if( oldAmount!=newAmount ){
            showToast("Done!");
        }else{
            showToast("Total available: "+oldAmount +" " + fromAmountName);
        }
    }
}

function addToSet(cartId, branchId, setId){
    currentBranchId = branchId;
    addToShoppingCartSet(cartId, branchId, 1, refreshShoppingCart);
}

function removeFromSet(cartId, branchId, setId){
    currentBranchId = branchId;
    removeFromShoppingCartSet(cartId, branchId, 1, refreshShoppingCart);
}

function showShoppingCartMeta(shoppingCart){

    var shoppingSet = shoppingCart.shoppingSet;
    var content = "";
    var totalItems = 0;
    var totalPrice = 0;
    shoppingSet.forEach(function(set){
        totalItems += set.amount;
        totalPrice += set.amount*set.branch.price;
    });
    $('#total-items').empty();
    $('#total-items').append('<b>'+totalItems+'</b>');
    $('#total-cart-price').empty();
    $('#total-cart-price').append('<b>£'+totalPrice+'</b>');
    showShoppingCartIconDataBadge(totalItems);
    componentHandler.upgradeDom();

}

function showShoppingCart(shoppingCart){

    var shoppingSet = shoppingCart.shoppingSet;
    var content = "";
    var totalItems = 0;
    var totalPrice = 0;
    shoppingSet.forEach(function(set){

        //show attributes
        var attributes = set.branch.attributes;
        var attributesContent = '';
        var notWearAttributes = getNotWearAttributes(attributes);
        if(notWearAttributes.length>0){
            notWearAttributes.forEach(function(attr){
                attributesContent += showCommodityAttribute(attr);
            });
        }else{
            attributes.forEach(function(attr){
                attributesContent += showWearAttrubute(attr)+'<br/>';
            });
        }
        content += '<a class="mdl-cell mdl-cell--4-col"  href="'+showCommodityUrl+'/'+set.commodityInfo.id+'"><img src="'+set.commodityInfo.images[0]+'" width="250px"/>';
        content += '</a>'
        content += '<div class="mdl-cell mdl-cell--4-col">';
        content += '<b>'+set.branch.price+' £</b>&nbsp;';
        content += '<a href="'+showCommodityUrl+'/'+set.commodityInfo.id+'">'+set.commodityInfo.name+'</a><br/>';
        content += attributesContent;
        content += 'quantity: ' + set.amount +' | price: '+set.branch.price +'<br/><br/>';
        content += '<button class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored" onclick="addToSet('+shoppingCart.id+','+set.branch.id+','+set.id+')"><i class="material-icons">add</i></button>';
        content += '&nbsp;<button class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored" onclick="removeFromSet('+shoppingCart.id+','+set.branch.id+','+set.id+')"><i class="material-icons">remove</i></button>';
        content += '</div>';
        content += '<div class="mdl-cell mdl-cell--4-col">';
        content += '</div>';
        totalItems += set.amount;
        totalPrice += set.amount*set.branch.price;
    });
    $('#cart-container').empty();
    $('#cart-container').append(content);

    $('#total-items').empty();
    $('#total-items').append('<b>'+totalItems+'</b>');

    $('#total-cart-price').empty();
    $('#total-cart-price').append('<b>£'+totalPrice+'</b>');
    showShoppingCartIconDataBadge(totalItems);
    componentHandler.upgradeDom();

}

function confirmPaymentOrder(orderId, paymentId, paymentProvider, successCallback, errorCallback){
    var urlService = URL_SERVICES + "/customer/order/confirm/";
    var confirmReq = {
        orderId: orderId,
        paymentId: paymentId,
        paymentProvider: paymentProvider
    };
    console.log("> " + confirmReq);

    sendDataAsJson(urlService, "POST", confirmReq, successCallback, errorCallback)
}

function showSpinner(){
    $('#spinner').show();
}

function hideSpinner(){
    var timeoutID = window.setTimeout(function(){ $('#spinner').hide(); }, 50);

}
