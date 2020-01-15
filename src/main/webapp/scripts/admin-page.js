/**
COMMONS
*/
function showNavigationCaption(text, idx){
    if(idx==undefined) {
        $("#navigation-text").empty();
        $("#navigation-text").append(text);
    }else{
        $("#navigation-text-" + idx).empty();
        $("#navigation-text-" + idx).append(text);
    }

}

const ERROR_CONTAINERS = 2;

function errorContainerHide(i){
    $('#error-container-'+i).empty();
    $('#error-container-'+i).hide();
}
/**
 Commodity Type Section
**/

function deleteType(typeId){

    var snackbarContainer = document.querySelector('#demo-toast-example');


    var url = URL_SERVICES + "/private/type/" + typeId;
    $.ajax({
       url: url,
       type: 'DELETE',
       success: function(response) {
            refreshTypes();
            var toastMessage = {message: 'Success' };
            snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
       },
       error: function( json, status, error ) {
            showError(json);
            var toastMessage = {message: 'Error deleteType' + JSON.stringify( json ) };
            snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
            cleanProperty();
       }
    });

};

function refreshTypes(){

     var typeContainer = document.querySelector('#container-types');

     $.getJSON(URL_SERVICES + "/public/types/", function(json, status){

        var content = "";
        for(var i=0; i<json.length; i++){

            content += "<tr>";
            content += "<td class='mdl-data-table__cell--non-numeric'>" + "<a class='edit-type-link' href='#typeId/" + json[i].id + "' onclick='onEditTypeClick("+json[i].id+");'>" +json[i].name + "</td>";
            content += "<td class='mdl-data-table__cell--non-numeric'><button class='mdl-button mdl-js-button mdl-button--icon mdl-button--colored' onclick='setupType(" + json[i].id + ");'><i class='material-icons'>build</i></button></td>";
            content += "<td class='mdl-data-table__cell--non-numeric'><button class='mdl-button mdl-js-button mdl-button--icon mdl-button--colored' onclick='deleteType(" + json[i].id + ");'><i class='material-icons'>delete</i></button></td>";
            content += "</tr>";

        }
        $('#container-types').empty();
        $('#container-types').append(content);


	 })
	 .fail(function(json, status) {
	    showToast("Error on load types");
	 });
};

function deletePropertyValue(valueId){

    var snackbarContainer = document.querySelector('#demo-toast-example');
    var url = URL_SERVICES + "/private/attributeValue/" + valueId;
    $.ajax({
           url: url,
           type: 'DELETE',
           success: function(response) {
                var typeId = getTypeIdFromUrl();
                refreshProperties(typeId);
                var toastMessage = {message: 'Delete Property Value Success '};
                snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
           },
           error: function( json, status, error ) {
                //...
                showError(json);
                var toastMessage = {message: 'Error in Delete Property Value ' };
                snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
           }
        });

};


function refreshProperties(typeId){
     var snackbarContainer = document.querySelector('#demo-toast-example');

     if(typeId===undefined){
        showToast("TypeId undefined");
        return null;
     }

     var propertiesURL = URL_SERVICES + "/public/attributes/" + typeId;
     $.getJSON( propertiesURL, function(json, status){

        var content = "";
        for(var i=0; i<json.length; i++){
            if(json[i].values){
                for(var valIndex=0; valIndex < json[i].values.length; valIndex++){
                    content += "<tr>";
                    content += "<td class='mdl-data-table__cell--non-numeric'>" + json[i].name+' : ';
                    if(json[i].name=="color"){
                        content += showColorElement(json[i].values[valIndex].value);
                    }else{
                        if(json[i].name=="size"){
                            content += showSizeElement(json[i].values[valIndex].value);
                        }else{
                            content += json[i].values[valIndex].value;
                        }
                    }
                    content += '</td>';
                    content += "<td> type : " + json[i].dataType + "<br/> measure :";
                    console.log("json[i].measure="+json[i].measure)
                    if(json[i].measure!=null){
                        content += json[i].measure;
                    }else{
                        content += "undefined";
                    }
                    content += "</td>";
                    content += "<td><button class='mdl-button mdl-js-button mdl-button--icon mdl-button--colored' onclick='deletePropertyValue(" + json[i].values[valIndex].id + ");'><i class='material-icons'>delete</i></button></td>";
                    content += "</tr>";
                }
            }

        }
        $('#container-properties').empty();
        $('#container-properties').append(content);


	 })
	 .fail(function(json, status) {
	    //$('#container-properties').empty();
	    showToast("Error on load properties." + propertiesURL);
        showError(json);
	 });


};

function getCommodityType(typeId){
    var snackbarContainer = document.querySelector('#demo-toast-example');
    $.getJSON( URL_SERVICES + "/public/type/"+typeId, function(json, status){
        $('#commodityTypeNameEdit').val( json.name );
        $('#commodityTypeDescEdit').val( json.description );
	 })
	 .fail(function(json, status) {
        var toastMessage = {message: 'Error: ' + json.message };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
	 });
};

var DATA_TYPE;
function setDataType(el){
    DATA_TYPE = el.getAttribute("value");
}

function loadDataTypes(){

    $.getJSON( URL_SERVICES + "/public/attribute/value/dataTypes/", function(json, status){
        var content = "";
        for(var i=0; i < json.length; i++){
            content += '<span class="mdl-list__item-primary-content">';
            content += json[i] + '</span><span class="mdl-list__item-secondary-action">';
            content += '<label class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="datatype-option-'+i+'">';
            content += '<input type="radio" onclick="setDataType(this);" id="datatype-option-'+i+'" class="mdl-radio__button" name="dataType" value="'+json[i];
            if(i==0){
                content += '" checked/>';
                DATA_TYPE = json[i];
            }else{
                content += '" />';
            }

            content += '</label></span>';
        }
        $('#data-type-container').empty();
        $('#data-type-container').append(content);
        componentHandler.upgradeDom();
    })
    .fail(function(json, status) {
            var toastMessage = {message: 'Error: ' + json.message };
            snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
    });
}

function onEditTypeClick(typeId) {
        var snackbarContainer = document.querySelector('#demo-toast-example');
        var toastMessage = {message: 'Hide -> typeId= ' + typeId };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
        if( typeId ){
            drawEditType();
            getCommodityType(typeId);
        }
};

function cleanCreateType() {
  $( "#commodityTypeName" ).val("");
  $( "#commodityTypeDesc" ).val("");
};

function cleanProperty() {
  $( "#propertyName" ).val("");
  $( "#attributeValue" ).val("");
};

function drawCreateTypeBtnAction() {
    //
    $("#form-create").show();
    $("#form-create-btns").show();
    $("#draw-create-type-btn").hide();
    $("#type-list").hide();
}

function drawEditType() {
  errorContainerHide(1);
  $("#type-list").hide();
  $("#form-edit-type").show();
  $("#form-edit-type-btns").show();
  showNavigationCaption("Edit commodity type");
  componentHandler.upgradeDom();
};

function drawTypeList() {
    //show
    window.location = '#view/typeList';
    showNavigationCaption("Here you can Create/Edit Type of your Commodities");
    $("#type-list").show();
    $("#draw-create-type-btn").show();
    //hide
    errorContainerHide(1);
    $("#form-create").hide();
    $("#form-create-btns").hide();
    $("#form-edit-type").hide();
    $("#form-edit-type-btns").hide();
    $("#add-attributes-container").hide();
    $("#add-attributes-container-btns").hide()
    $("#attributes-container").hide();
    $("#attributes-container-btns").hide();
    $("#btn-attribute-back-container").hide();
    componentHandler.upgradeDom();
};

function addAttributeAction() {
    var typeId = getTypeIdFromUrl();

    var measure = null;
    if(  !($( "#attributeMeasure" ).val() === undefined) ){
        if( $( "#attributeMeasure" ).val().length > 0 ){
            measure = $( "#attributeMeasure" ).val();
        }
    }
    var data = {
        typeId: typeId,
        dataType: DATA_TYPE,
        measure: measure,
        name: $( "#propertyName" ).val(),
        value: $( "#attributeValue" ).val()
    };

    addAttribute(data, 1, function(){
        refreshProperties(typeId);
        setupType(typeId);
        });
    cleanProperty();
}

function setupType(typeId) {
    if(typeId==undefined){
        typeId = getTypeIdFromUrl();
    }
    window.location = '#typeId/'+typeId+'/view/setupType';
    errorContainerHide(1);
    refreshProperties(typeId);
    $("#add-attributes-container-btns").hide();
    $("#add-attributes-container").hide();
    $("#attributes-container").show();
    $("#attributes-container-btns").show();
    $("#type-list").hide();
    $("#form-create").hide();
    $("#draw-create-type-btn").hide();
    $("#type-list").hide();
    showNavigationCaption("Edit type attributes");
}

function drawAddAttributes() {
    var typeId = getTypeIdFromUrl();
    window.location = '#typeId/'+typeId+'/view/addAttribute/';

    loadDataTypes();
    $("#attributes-container").hide();
    $("#attributes-container-btns").hide();
    $("#add-attributes-container").show();
    $("#add-attributes-container-btns").show();
    showNavigationCaption("Here you can add attribute to type");
    componentHandler.upgradeDom();
}


  function showCreateType(){
    cleanCreateType();
    refreshTypes();
    drawTypeList();
  };


  function showForm(){
    var currentURL = window.location.href;
    var splited = currentURL.split("#");
    errorContainerHide(1);
    if( splited.length==1 ){

        showCreateType();

    }else{
        if( splited.length>1 && splited[1].includes("typeId") ){
            var typeId = getTypeIdFromUrl();
            cleanProperty();
            getCommodityType(typeId);
            refreshProperties(typeId);
            loadDataTypes();
            drawEditType();
        }else{
            showCreateType();
        }
    }
  };

var currentURLcache;
var currentTypeId;
function getTypeIdFromUrl(){
  var currentURL = window.location.href;
  if(currentURL == currentURLcache){
    return currentTypeId;
  }
  currentURLcache = currentURL;
  var splited = currentURL.split("#");

  if( splited.length==1 ){
      currentTypeId = null;
      return null;
  }else{
      if( splited.length>1 && splited[1].includes("typeId") ){
          var typeIdPara = splited[1].split("/");
          currentTypeId = typeIdPara[1];
          return currentTypeId;
      }else{
        currentTypeId = null;
        return null;
      }
  }

};

function drawChip(text){
    var chip = '<span class="mdl-chip"><span class="mdl-chip__text">'+text+'</span></span>';
    return chip;
}

function showError(json, errorTab) {
  console.log(json);
  var errorContent = drawChip(json.responseJSON.message.replace(/\n/g, "<br/>"));
  if(json.responseJSON.errors && json.responseJSON.errors.length>0){
      errorContent = "";
      json.responseJSON.errors.forEach(function(val){ console.log(val.field); errorContent += drawChip(val.message); });

  }
  var message = errorContent;
  if(errorTab===undefined){
      $('#error-container-1').empty();
      $('#error-container-1').append(message);
      $('#error-container-1').show();
  }else{
      $('#error-container-'+errorTab).empty();
      $('#error-container-'+errorTab).append(message);
      $('#error-container-'+errorTab).show();
  }
  componentHandler.upgradeDom();
}

function showErrorFromText(text, errorTab) {

    var message = text;
    if(errorTab===undefined){
      $('#error-message-content-1').empty();
      $('#error-message-content-1').append(message);
      $('#error-message-content-1').show();
      $('#error-message-1').show();
      $('#error-container-1').show();
    }else{
      $('#error-message-content-'+errorTab).empty();
      $('#error-message-content-'+errorTab).append(message);
      $('#error-message-content-'+errorTab).show();
      $('#error-message-'+errorTab).show();
      $('#error-container-'+errorTab).show();
    }

}

function addAttribute(data, errorTab, callback){
    sendDataAsJson(URL_SERVICES + "/private/attribute/",
    'POST',
    data,
    function( json){
        showToast('Adding property Success');
        callback.call();},
    function(json){
        showToast('Error while creating attribute');
        showError(json, errorTab);
    });
}

function addTypeSuccess(json){
    showToast('Success');
    refreshTypes();
    cleanCreateType()
    $("#type-list").show();
    $("#draw-create-type-btn").show();
    //hide
    errorContainerHide(1);
    $("#form-create").hide();
}

function addTypeError(json){
    showToast("Error create type");
    showError(json);
    refreshTypes();
}

function updateTypeSuccess(json){
    $('#commodityTypeNameEdit').val( json.name );
    $('#commodityTypeDescEdit').val( json.description );
    showToast('Update Type Success');
}
function updateTypeError(json){
    showError(json);
    showToast('Error');
}

function btnAttributeBackAction(){
    window.location = '#commodityTypes';
    showCreateType();
}

/**
 END: Commodity Type Section
**/

$(document).ready(function () {


  showForm();

  var snackbarContainer = document.querySelector('#demo-toast-example');

  var showToastButton = document.querySelector('#btn-createtype');

  showToastButton.addEventListener('click', function() {
    var data = {
        name: $( "#commodityTypeName" ).val(),
        description: $( "#commodityTypeDesc" ).val()
     };
     sendDataAsJson(URL_SERVICES + "/private/type/", 'POST', data, addTypeSuccess, addTypeError);
  });

  var createPropertyButton = document.querySelector('#btn-create-attribute');

  createPropertyButton.addEventListener('click', function() {
    //add attribute
    addAttributeAction();
  });

  var btnEditType = document.querySelector('#btn-edit-type');
  btnEditType.addEventListener('click', function() {
    var typeId = getTypeIdFromUrl();
    var data = {
        id: typeId,
        name: $( "#commodityTypeNameEdit" ).val(),
        description: $( "#commodityTypeDescEdit" ).val()
     };
     sendDataAsJson(URL_SERVICES + "/private/type/", 'PUT', data, updateTypeSuccess, updateTypeError);
  });


  var btnStep2 = document.querySelector('#btn-create-commodity-step-2');
  btnStep2.addEventListener('click', addCommodityStep2Action);

  var btnStep3 = document.querySelector('#btn-create-commodity-step-3');
  btnStep3.addEventListener('click', addCommodityAttributesAction);


});

/****
    section tab2 COMMODITY
****/

/* list commodities */

var page = 1;
var rows = 5;
var totalPages;
var currentPage;

function loadBeforeListCommodity(){
    if(currentPage>1) {
        page = currentPage-1;
        loadListCommodity(page, 5);
    }
}

function loadNextListCommodity(){
    if(currentPage<totalPages) {
        page = currentPage+1;
        loadListCommodity(page, 5);
    }
}

function showCommodityNavigate() {
            if(currentPage<totalPages){
                //show next
                $('#navigate_next').show();
            }else{
                //hide next
                $('#navigate_next').hide();
            }
            if(currentPage>1){
                //show prev
                $('#navigate_before').show();

            }else{
                //hide prev
                $('#navigate_before').hide();
            }
}

function hideCommodityNavigate() {
    $('#navigate_before').hide();
    $('#navigate_next').hide();
}

function loadListCommodity(pageParam, rowsParam) {

    if(pageParam===undefined){
        page = 1;
    }else{
        page = pageParam;
    }
    if(rowsParam===undefined){
        rows = 5;
    }else{
        rows = rowsParam;
    }

    var snackbarContainer = document.querySelector('#demo-toast-example');
    var getURL = "/public/commodities/?page="+page+"&rows="+rows;

    $.getJSON( URL_SERVICES + getURL, function(json, status){
        console.log("commodities");
        console.log(json);
        var content = "";
        var commodities = json.commodityData;
        totalPages = json.totalPages;
        currentPage = json.currentPage;
        showCommodityNavigate();
        //show controls
        for(var i=0; i<commodities.length; i++){
            var cm = commodities[i];

            for(var ci=0; ci<cm.branches.length; ci++){
                var branch = cm.branches[ci];

                var properties = branch.attributeSet;
                var propsContent = '';
                //process properties
                for(var pi = 0; pi < properties.length; pi++){
                    if(properties[pi].attribute.name=="color"){
                        propsContent += properties[pi].attribute.name + ":" + showColorElement(properties[pi].attributeValue.value) + "<br/>";
                    }else{
                        propsContent += properties[pi].attribute.name + ' : ' + properties[pi].attributeValue.value + '<br/>';
                    }
                };
                content += '<tr onclick="loadCommodityBranch(' + branch.id + ');">';
                content += '<td class="mdl-data-table__cell--non-numeric">' + cm.type.name + '<br/>';
                content += propsContent;
                content += '</td>';
                content += '<td class="mdl-data-table__cell--non-numeric">' + branch.code + ' ' + cm.name + '<br/>';
                content += 'amount: <b>' + branch.amount + '</b><br/>price: <b>' + branch.price + '</b> ' + branch.currency + '<br/>';
                content += '</td>';
                content += '</tr>';
            }

        }
        $('#commodities').empty();
        $('#commodities').append(content);

        toastMessage = {message: "Commodities loaded" };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 })
	 .fail(function(json, status) {

	    toastMessage = {message: "Error on load commodities." };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 });



};

function loadBranchList(){
    //initialize behaviour options
    showNavigationCaption("List of commodities", 2);
    window.location = '#commodities';
    initializeBehaviorOptions();
    loadListCommodity();
    errorContainerHide(2);
    $('#create-commodity-step-1').hide();
    $('#create-commodity-step-2').hide();
    $('#create-commodity-step-3').hide();
    $('#edit-commodity-tabs').hide();
    $('#commodity-list-bottom-btn').show();
    $('#commodities-container').show();
    componentHandler.upgradeDom();
};

/* end: list commodities */

/*
    section: add commodity
*/

var IMAGE_COUNT = 6;
//options of UI Behavior
var COMMODITY_TYPE = -1;
var BRANCH_ID = -1;
var PROPERTIES = [];
var IMAGES = [];
const PLACE_HOLDER = "../images/placeholder.png";

var commodityObject = null;
var commodityObjectValidator = null;

function initializeBehaviorOptions() {
    COMMODITY_TYPE = -1;
    BRANCH_ID = -1;
    PROPERTIES = [];
    IMAGES = [];
    for(var i=0; i<IMAGE_COUNT;i++){IMAGES.push(PLACE_HOLDER);}
    commodityObject = {
            name: null,
            shortDescription: null,
            overview: null,
            amount: null,
            price: null,
            currencyCode: 'EUR',
            typeId: null,
            propertyValues: [],
            images: []
         };
    commodityObject.currencyCode = 'EUR';
}

function isInPROPERTIES(valId) {
    for(var i=0; i<PROPERTIES.length; i++){
        if(PROPERTIES[i]===valId){
            return true;
        }
    }
    return false;
}

function drawAddCommodity() {
    initializeBehaviorOptions();
    selectType();
    showNavigationCaption("Add commodity to E-SHOP", 2);
    drawAddCommodityStep1();
};

var validators = new Map();
function validateElementPattern(elementName){
    var validator = validators.get(elementName);
    if(validator==undefined){
        validator = new RegExp(document.getElementById(elementName).getAttribute("pattern"));
        validators.set(elementName, validator);
    }
    var isValid = true;
    var el = document.getElementById(elementName);
    var parent = $('#'+elementName).parent();
    if(validator.test(el.value)){
        //parent.removeClass("is-dirty");
        parent.removeClass("is-invalid");
        parent.addClass("is-upgraded");
        parent.addClass("is-dirty");
        componentHandler.upgradeDom();
    }else{
        parent.addClass("is-upgraded");
        parent.addClass("is-dirty");
        parent.addClass("is-invalid");
        componentHandler.upgradeDom();
        isValid = false;
    }
    return isValid;
}

function validateForm(className){
    var elements = document.getElementsByClassName(className);
    var isValid = true;
    for(var i=0; i < elements.length; i++){
       var element = elements[i];
       console.log("element.id " + element.id);
       isValid = isValid && validateElementPattern(element.id);
    }
    console.log("validateForm " + isValid);
    return isValid;
}

function validateAddCommodityStep1() {
    $("#btn-create-commodity-step-2").hide();
    var isValid = true;
    isValid = validateForm("create-commodity-step-1");
    if(!isValid){
        return false;
    }
    commodityObject.currencyCode = 'EUR';
    $("#btn-create-commodity-step-2").show();
    return true;
}

function addCommodityStep2Action(){
    bindCommodityInfo(commodityObject);
    drawAddCommodityStep2();
}

function drawAddCommodityStep1(){
    errorContainerHide(2);
    validateAddCommodityStep1();
    $("#commodities-container").hide();
    $("#commodity-list-bottom-btn").hide();

    $("#create-commodity-step-1").show();
    $('#create-commodity-step-2').hide();
    $('#create-commodity-step-3').hide();
    componentHandler.upgradeDom();
};

function bindCommodityInfo(commodityObject){
    commodityObject.name = $( "#commodityName" ).val();
    commodityObject.overview = $( "#commodityOverview" ).val();
    commodityObject.shortDescription = $( "#commodityShortDesc" ).val();
    commodityObject.amount = $( "#commodityAmount" ).val();
    commodityObject.price = $( "#commodityPrice" ).val();
}

function drawImageContainer(postFileCallback) {
    var content = "";
    var callBackName = postFileCallback==undefined? 'undefined': postFileCallback.name;
    for(var i=0; i < IMAGE_COUNT; i++) {
        content += '<div class="img-parent mdl-cell mdl-cell--6-col-phone mdl-cell--6-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">';
        content += '<img src="'+IMAGES[i]+'" id="img'+i+'" class="img-placeholder" alt="'+IMAGES[i]+'"/>';
        content += '<div id="img-spinner-'+i+'" class="mdl-spinner mdl-js-spinner is-active img-upload-spinner" ></div>';
        content += '<div class="header-content imgupload imgupload img-upload-btn" >';
        content += '<label class="input-custom-file mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored">';
        content += '<i class="material-icons">add</i>';
        content += '<input id="file'+i+'" type="file" onchange="postFile('+i+','+callBackName+');">';
        content += '</label></div></div>';
    }
    $('#image-container').empty();
    $('#image-container').append(content);
    componentHandler.upgradeDom();

}

function hideSpinners() {
    for(var i=0; i < IMAGE_COUNT; i++) {
        $("#img-spinner-" + i).hide();
    }
}

function validateAddCommodityStep2(){
    console.log("call validateAddCommodityStep2");
    $("#btn-create-commodity-step-3").hide();
    var isValid = true;
    isValid = !IMAGES.some(function(val){return PLACE_HOLDER==val;});
    if(!isValid){
        return false;
    }

    $("#btn-create-commodity-step-3").show();
    return true;
}

function drawAddCommodityStep2() {
    errorContainerHide(2);
    $("#create-commodity-step-1").hide();
    $('#create-commodity-step-2').show();
    $('#create-commodity-step-3').hide();
    drawImageContainer(validateAddCommodityStep2);
    hideSpinners();
    validateAddCommodityStep2();
    componentHandler.upgradeDom();
};

function postFile(imageIndex, callback){
        var snackbarContainer = document.querySelector('#demo-toast-example');
        $("#img-spinner-" + imageIndex).show();
        var fd = new FormData();
        //var files = $('#file')[0].files[0];
        //$("#status").val("file"+imageIndex);
        var file = document.getElementById("file"+imageIndex).files[0];
        fd.append('file',file);
        $.ajax({
            url: URL_SERVICES + '/private/upload/',
            type: 'post',
            data: fd,
            contentType: false,
            processData: false,
            success: function(data){
                console.log(data);
                if(data != 0){
                    var image = document.getElementById("img"+imageIndex);
                    image.src = data.uri;
                    IMAGES[imageIndex] = data.uri;
                }else{
                    //error handling
                }
                $("#img-spinner-" + imageIndex).hide();
                if(callback!=undefined){
                    callback.call();
                }
            },
            error: function( xhr, status, error ) {
                    //...
                    console.log(xhr);
                    console.log(error);
                    var toastMessage = {message: 'Error file upload' };
                    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
                    //refreshTypes();
                    //cleanCreateType()
               }
        });
}


function addCommodityAttributesAction(){
    commodityObject.images = IMAGES;
    errorContainerHide(2);
    $("#create-commodity-step-1").hide();
    $('#create-commodity-step-2').hide();
    $('#create-commodity-step-3').show();
    selectType();
    componentHandler.upgradeDom();
}


function drawCommodityUpdateForm() {
    showNavigationCaption("Update commodity", 2);
    errorContainerHide(2);
    $("#commodities-container").hide();
    $("#commodity-list-bottom-btn").hide();

    $("#create-commodity-step-1").show();
    componentHandler.upgradeDom();
}

function openCommodityTab(evt, tabName) {
  var i, tabcontent, tablinks;
  tabcontent = document.getElementsByClassName("tabcontent");
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }
  tablinks = document.getElementsByClassName("tablinks");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }
  document.getElementById(tabName).style.display = "block";
  evt.currentTarget.className += " active";
}


function selectType(){

     $.getJSON(URL_SERVICES + "/public/types/", function(json, status){
        var content = "";
        for(var i=0; i<json.length; i++){
            var typeid = "selectType-"+i;
            content += '<label class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="'+typeid+'">';
            content += '<input type="radio" id="' + typeid + '" class="mdl-radio__button" name="selectType" value="'+json[i].id+'" onclick="loadCreateCFProperties(' +json[i].id+ ');"';
            if(COMMODITY_TYPE>0) {
                if(COMMODITY_TYPE==json[i].id){
                    content += ' checked>';
                }else{
                    content += ' disabled>';
                }
            }else{
                content += '>';
            }
            content += '<span class="mdl-radio__label">' + json[i].name + '</span>';
            content += '</label>';

        }
        $('#commodity-type-container').empty();
        $('#commodity-type-container').append(content);
	 })
	 .fail(function(json, status) {
	  	//error handling

	 });

};

var LOADED_ATTRIBUTES = [];

function loadCreateCFProperties(typeId){
    var snackbarContainer = document.querySelector('#demo-toast-example');
    var propertiesURL = "/public/attributes/" + typeId;

    $.getJSON( URL_SERVICES + propertiesURL, function(json, status){
        LOADED_ATTRIBUTES = json;
        var content = "";
        for(var i=0; i<json.length; i++){
            if(json[i].values){
                for(var valIndex=0; valIndex < json[i].values.length; valIndex++){
                    content += "<tr>";
                    content += "<td>";
                    content += '<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="attribute-' + json[i].values[valIndex].id + '">';
                    content += '<input type="checkbox" name="attribute-value" id="attribute-' + json[i].values[valIndex].id + '" value="' + json[i].values[valIndex].id + '" class="mdl-checkbox__input" ';

                    if(PROPERTIES.length>0){
                        var valId = json[i].values[valIndex].id;
                        if( isInPROPERTIES(valId) ){
                            content += ' checked>';
                        }else{
                            content += ' >';
                        }

                    }else{
                        content += ' >';
                    }
                    content += '<span class="mdl-checkbox__label"></span></label>';
                    content += "</td>";
                    content += "<td class='mdl-data-table__cell--non-numeric'>" + json[i].name + "</td>";
                    if(json[i].name=="color"){
                        content += '<td>'+showColorElement(json[i].values[valIndex].value)+'</td>';
                    }else{
                        content += "<td class='mdl-data-table__cell--non-numeric'>" + json[i].values[valIndex].value + "</td>";
                    }
                    if(json[i].measure!=null){
                        content += "<td class='mdl-data-table__cell--non-numeric'>" + json[i].measure + "</td>";
                    }else{
                        content += "<td class='mdl-data-table__cell--non-numeric'></td>";
                    }
                    content += "</tr>";
                }
            }

        }
        $('#commodity-properties').empty();
        $('#commodity-properties').append(content);
        if(PROPERTIES.length == 0){
            $("#btn-add-commodity").show();
        }else{
            $("#btn-update-commodity").show();
        }
        //COMMODITY_TYPE = typeId;
        commodityObject.typeId = typeId;
        $("#attributes-cm-container").show();
        //RELOAD
        componentHandler.upgradeDom();
        toastMessage = {message: "Properties loaded" };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 })
	 .fail(function(json, status) {

	    toastMessage = {message: "Error on load properties." };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 });


};

function addCommoditySuccess(json){
    showToast('Operation with Commodity Success');
    loadBranchList();
}

function addCommodityError(json){
    showError(json, 2);
    showToast('Error in operation with Commodity' );
}

function btnClickAddCommodity(branchId){

    var snackbarContainer = document.querySelector('#demo-toast-example');

    //process fields data and create object to send data

    var propertyList = [];
    var imageList = [];

    $.each( $("input[name='attribute-value']:checked"), function(){
        propertyList.push( $(this).val() );
    });

//    for(var i=0; i < IMAGE_COUNT; i++){
//        imageList.push( $("#img" + i).attr("src") );
//    }

    commodityObject.propertyValues = propertyList;
    var data = commodityObject;

    var urlFunction = URL_SERVICES + "/private/commodity/";
    var method;
    branchId = BRANCH_ID;
    if(branchId<0){
        method = "POST";
    }else{
        method = "PUT";
        data["branchId"] = branchId;
    }
    console.log("prepared commodityObject");
    console.log(data);
    sendDataAsJson(urlFunction, method, data, addCommoditySuccess, addCommodityError);

};

function addAttributeToCm(){
    showToast("Attributes loaded="+LOADED_ATTRIBUTES.length);
    //showErrorFromText("Test Error message", 2);
    var name = $('#property-add-to-cm').val();
    name = name.trim();
    var val = $('#value-add-to-cm').val();
    val = val.trim();
    var attribute = null;
    for(var i=0;i<LOADED_ATTRIBUTES.length; i++){
        if(LOADED_ATTRIBUTES[i].name==name){
            attribute = LOADED_ATTRIBUTES[i];
        }
    }

    if(attribute!=null){

        var data = {
                typeId: attribute.typeId,
                dataType: attribute.dataType,
                measure: attribute.measure,
                name: name,
                value: val
        };

        addAttribute(data, 2, function(){loadCreateCFProperties(attribute.typeId);});

    }

};

/*
    end: section: add commodity
*/

function btnClickUpdateCommodity(){
    if(BRANCH_ID>0){
        btnClickAddCommodity(BRANCH_ID);
    }
};

function loadCommodity(id){

    var snackbarContainer = document.querySelector('#demo-toast-example');

    var getURL = "/public/commodity/id/" + id;

    $.getJSON( URL_SERVICES + getURL, function(json, status){

        //$("#create-commodity").show();
        COMMODITY_TYPE = json.type.id;
        selectType();
        loadCreateCFProperties(COMMODITY_TYPE);
        //load images
        var images = json.images;
        console.log(json);
        console.log(images);
        IMAGES = [];
        for(var i=0; i < images.length; i++){
            IMAGES.push(images[i].uri);
            //$("#img" + i).attr("src", images[i].uri);
        }
        //end load images
        $('#commodityName').val( json.name );
        $('#commodityName').parent().addClass("is-dirty");
        //$('#commodityName').attr("class", "mdl-textfield mdl-js-textfield is-upgraded");
        $('#commodityShortDesc').val( json.shortDescription );
        $('#commodityShortDesc').parent().addClass("is-dirty");
        $('#commodityOverview').val( json.overview );
        $('#commodityOverview').parent().addClass("is-dirty");
        componentHandler.upgradeDom();

    })
	 .fail(function(json, status) {
	    toastMessage = {message: 'Error in Load Commodity id = ' + id };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
	 });

};

function loadCommodityBranch(branchId){
    drawCommodityUpdateForm();
    var snackbarContainer = document.querySelector('#demo-toast-example');
    BRANCH_ID = branchId;
    window.location = '#branchId/' + BRANCH_ID;
    var getURL = "/public/commodityBranch/" + BRANCH_ID;

    $.getJSON( URL_SERVICES + getURL, function(json, status){

        loadCommodity(json.commodityId);
        console.log(IMAGES);

        //load properties
        PROPERTIES = [];
        var props = json.attributeSet;
        for(var pi=0; pi<props.length; pi++){
            PROPERTIES.push( props[pi].attributeValue.id );
        }

        $('#commodityAmount').val( json.amount );
        $('#commodityAmount').parent().addClass("is-dirty");
        $('#commodityPrice').val( json.price );
        $('#commodityPrice').parent().addClass("is-dirty");
        var toastMessage = {message: 'Load Commodity Branch id = ' + branchId };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 })
	 .fail(function(json, status) {

	    toastMessage = {message: 'Error in Load Commodity Branch id = ' + branchId };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 });




};
