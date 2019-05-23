/* COMMODITY_TYPE */
var URL_SERVICES = "http://localhost:8080/restful-eshop";

//URL_SERVICES = "http://localhost:8888";

function deleteType(typeId){

    var snackbarContainer = document.querySelector('#demo-toast-example');


    var url = URL_SERVICES + "/type/" + typeId;
    $.ajax({
       url: url,
       type: 'DELETE',
       success: function(response) {
            refreshTypes();
            var toastMessage = {message: 'Success' };
            snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
       },
       error: function( json, status, error ) {
            //...
            showError(json);
            var toastMessage = {message: 'Error deleteType' + JSON.stringify( json ) };
            snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
            cleanProperty();
       }
    });

};

function refreshTypes(){

     var typeContainer = document.querySelector('#container-types');

     $.getJSON(URL_SERVICES + "/types/", function(json, status){

        var content = "";
        for(var i=0; i<json.length; i++){

            content += "<tr>";
            content += "<td >" + json[i].id + "</td>";
            content += "<td class='mdl-data-table__cell--non-numeric'>" + "<a class='edit-type-link' href='#typeId/" + json[i].id + "' onclick='onEditTypeClick("+json[i].id+");'>" +json[i].name + "</td>";
            content += "<td class='mdl-data-table__cell--non-numeric'>NAN</td>";
            content += "<td class='mdl-data-table__cell--non-numeric'><button class='mdl-button mdl-js-button mdl-button--icon mdl-button--colored' onclick='deleteType(" + json[i].id + ");'><i class='material-icons'>delete</i></button></td>";
            content += "</tr>";

        }
        $('#container-types').empty();
        $('#container-types').append(content);


	 })
	 .fail(function(json, status) {
	  		//$("#status").text(status);
	 });


};

function deletePropertyValue(valueId){

    var snackbarContainer = document.querySelector('#demo-toast-example');
    var url = URL_SERVICES + "/propertyValue/" + valueId;
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

     var propertiesURL = URL_SERVICES + "/properties/" + typeId;
     $.getJSON( propertiesURL, function(json, status){

        var content = "";
        for(var i=0; i<json.length; i++){
            if(json[i].values){
                for(var valIndex=0; valIndex < json[i].values.length; valIndex++){
                    content += "<tr>";
                    content += "<td class='mdl-data-table__cell--non-numeric'>" + json[i].name + "</td>";
                    if(json[i].name=="color"){
                        content += '<td><div class="colorCircleSml" style="background: #'+json[i].values[valIndex].value+'">&#160;' + json[i].values[valIndex].value + "&#160;</div></td>";
                    }else{
                        content += "<td>" + json[i].values[valIndex].value + "</td>";
                    }
                    content += "<td>" + json[i].dataType + "</td>";
                    if(json[i].measure!=null){
                        content += "<td>" + json[i].measure + "</td>";
                    }else{
                        content += "<td>undefined</td>";
                    }


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

    $.getJSON( URL_SERVICES + "/type/"+typeId, function(json, status){

        //FIX set onFocus smthing at elements

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

    $.getJSON( URL_SERVICES + "/property/value/dataTypes/", function(json, status){
        var content = "";
        for(var i=0; i < json.length; i++){
            content += '<div mdl-cell mdl-cell--2-col><span class="mdl-list__item-primary-content">';
            content += json[i] + '</span><span class="mdl-list__item-secondary-action">';
            content += '<label class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="datatype-option-'+i+'">';
            content += '<input type="radio" onclick="setDataType(this);" id="datatype-option-'+i+'" class="mdl-radio__button" name="dataType" value="'+json[i];
            if(i==0){
                content += '" checked/>';
                DATA_TYPE = json[i];
            }else{
                content += '" />';
            }

            content += '</label></span></div>';
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
        //var currentURL = btn.getAttribute("href");
        //var splited = currentURL.split("#");
        var toastMessage = {message: 'Hide -> typeId= ' + typeId };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
        if( typeId ){
            //var params = splited[1].split("/");
            //get type -> set params
            //currentTypeId = typeId;
            showEditType();
            getCommodityType(typeId);
            loadDataTypes();
            refreshProperties(typeId);

        }
};


function showNavigationCaption(text){
     $("#navigation-text").empty();
     $("#navigation-text").append(text);
}

  function showEditType(){
    $('#error-container-1').hide();
    $("#form-create").hide();
    $("#form-edit-type").show();
    $("#type-list").hide();
    $("#type-properties").show();
    $("#attributes-container").show();
    $("#btn-attribute-back").show();
    showNavigationCaption("Edit commodity type and attributes");
    componentHandler.upgradeDom();
  };

  function cleanCreateType(){
    $( "#commodityTypeName" ).val("");
    $( "#commodityTypeDesc" ).val("");
  };

  function cleanProperty(){
    $( "#propertyName" ).val("");
    $( "#attributeValue" ).val("");
  };

  function showCreateType(){
    cleanCreateType();
    refreshTypes();
    $('#error-container-1').hide();
    $("#form-create").show();
    $("#form-edit-type").hide();
    $("#type-list").show();
    $("#type-properties").hide();
    $("#attributes-container").hide();
    $("#btn-attribute-back").hide();
    showNavigationCaption("Create Commodity Type");
    componentHandler.upgradeDom();
  };


  function showForm(){
    var currentURL = window.location.href;
    var splited = currentURL.split("#");
    $('#error-container-1').hide();
    if( splited.length==1 ){

        showCreateType();

    }else{
        if( splited.length>1 && splited[1].includes("typeId") ){
            var typeId = getTypeIdFromUrl();
            cleanProperty();
            getCommodityType(typeId);
            refreshProperties(typeId);
            loadDataTypes();
            showEditType();
        }else{
            showCreateType();
        }
    }
  };

  function getTypeIdFromUrl(){
    var currentURL = window.location.href;
    var splited = currentURL.split("#");

    if( splited.length==1 ){
        return null;
    }else{
        if( splited.length>1 && splited[1].includes("typeId") ){
            var typeIdPara = splited[1].split("/");
            return typeIdPara[1];
        }else{
            return null;
        }
    }

  };


  function showError(json, errorTab){

    var message = "<b>" + json.responseJSON.status +"</b>:&nbsp;" + json.responseJSON.message;
    if(errorTab===undefined){
        $('#error-message-content-1').empty();
        $('#error-message-content-1').append(message);
        $('#error-message-content-1').show();
        $('#error-message').show();
    }else{
        $('#error-message-content-'+errorTab).empty();
        $('#error-message-content-'+errorTab).append(message);
        $('#error-message-content-'+errorTab).show();
        $('#error-message-'+errorTab).show();
    }

  }

  function showErrorFromText(text, errorTab){

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
    //showToast(data.typeId);
    var options = {
                    url: URL_SERVICES + "/property/",
                    type: 'post',
                    dataType: 'json',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(data), // Our valid JSON string
                    success: function( json, status, xhr ) {
                        showToast('Adding property Success');
                        callback.call();
                   },
                   error: function(  json, status  ) {
                        //...
                        showToast('Error while creating attribute');
                        showError(json, errorTab);

                   }
        };
    $.ajax( options );
}


$(document).ready(function () {

  showForm();

  var snackbarContainer = document.querySelector('#demo-toast-example');

  var showToastButton = document.querySelector('#btn-createtype');

  showToastButton.addEventListener('click', function() {
    //'use strict';
    var data = {
        name: $( "#commodityTypeName" ).val(),
        description: $( "#commodityTypeDesc" ).val()
     };

     showErrorFromText(URL_SERVICES);

     var options = {
                url: URL_SERVICES + "/type/",
                type: 'post',
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(data), // Our valid JSON string

                success: function( data, status, xhr ) {
                    //...

                    var toastMessage = {message: 'Success' };
                    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
                    refreshTypes();
                    cleanCreateType()
               },
               error: function( json, status, error ) {
                    //...
                    showToast("Error create type");
                    showError(json);
                    refreshTypes();
                    cleanCreateType()
               }
    };
    $.ajax( options );
  });

  var createPropertyButton = document.querySelector('#btn-create-attribute');

  createPropertyButton.addEventListener('click', function() {
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

    addAttribute(data, 1, function(){refreshProperties(typeId);});
    cleanProperty();

  });


  var btnPropertyBack = document.querySelector('#btn-attribute-back');
  btnPropertyBack.addEventListener('click', function() {
    window.location = '#commodityTypes';
    showCreateType();
  });

  var btnEditType = document.querySelector('#btn-edit-type');
  btnEditType.addEventListener('click', function() {
    var typeId = getTypeIdFromUrl();
    var data = {
        id: typeId,
        name: $( "#commodityTypeNameEdit" ).val(),
        description: $( "#commodityTypeDescEdit" ).val()
     };
     var options = {
                url: URL_SERVICES + "/type/",
                type: 'PUT',
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(data), // Our valid JSON string

                success: function( data, status, xhr ) {
                    //...
                    getCommodityType( typeId );
                    var toastMessage = {message: 'Success ' + data.status };
                    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);


               },
               error: function( json, status ) {
                    //error handling
                    showError(json);
                    var toastMessage = { message: 'Error'  };
                    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

               }
    };
    $.ajax( options );

  });



});

/****

    section tab2 COMMODITY

****/

function showToast(message){
    var snackbarContainer = document.querySelector('#demo-toast-example');
    var toastMessage = {message: message };
    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

}

var IMAGE_COUNT = 4;

function loadCreateCommodityForm() {

    showCommodityForm();
    initializeBehaviorOptions();
    $("#btn-add-commodity").hide();
    $("#btn-update-commodity").hide();
    selectType();
    $("#commodity-properties").empty();

};

function postFile(imageIndex){
        var snackbarContainer = document.querySelector('#demo-toast-example');

        $("#img-spinner-" + imageIndex).show();
        var fd = new FormData();
        //var files = $('#file')[0].files[0];
        //$("#status").val("file"+imageIndex);
        var file = document.getElementById("file"+imageIndex).files[0];
        fd.append('file',file);
        $.ajax({
            url: URL_SERVICES + '/upload/',
            type: 'post',
            data: fd,
            contentType: false,
            processData: false,
            success: function(data){
                if(data != 0){
                    var image = document.getElementById("img"+imageIndex);
                    //$("#status").val(data.uri);
                    image.src = data.uri;
                    //$("#img").attr("src", data.uri);
                    //$(".preview img").show(); // Display image element
                }else{
                    //$("#status").val( "Error in file upload" );
                    //error handling
                }
                $("#img-spinner-" + imageIndex).hide();
            },
            error: function( xhr, status, error ) {
                    //...
                    var toastMessage = {message: 'Error' };
                    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
                    refreshTypes();
                    cleanCreateType()
               }
        });
}

function selectType(){

     $.getJSON(URL_SERVICES + "/types/", function(json, status){
        var content = "";
        for(var i=0; i<json.length; i++){
            var typeid = "selectType-"+i;
            content += '<label class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="'+typeid+'">';
            content += '<input type="radio" id="' + typeid + '" class="mdl-radio__button" name="selectType" value="'+json[i].id+'" onclick="loadCreateCFProperties(' +json[i].id+ ');"';
            if(COMMODITY_TYPE>0){
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

//options of UI Behavior
var COMMODITY_TYPE = -1;
var BRANCH_ID = -1;
var PROPERTIES = [];

function initializeBehaviorOptions(){
    COMMODITY_TYPE = -1;
    BRANCH_ID = -1;
    PROPERTIES = [];
}

function isInPROPERTIES(valId){
    for(var i=0; i<PROPERTIES.length; i++){
        if(PROPERTIES[i]===valId){
            return true;
        }
    }
    return false;
}

var LOADED_ATTRIBUTES = [];

function loadCreateCFProperties(typeId){
    var snackbarContainer = document.querySelector('#demo-toast-example');
    var propertiesURL = "/properties/" + typeId;

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
                        content += '<td><div class="mdl-data-table__cell--non-numeric colorCircleSml" style="background: #'+json[i].values[valIndex].value+'">&#160;' + json[i].values[valIndex].value + "&#160;</div></td>";
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
        COMMODITY_TYPE = typeId;
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


function btnClickAddCommodity(branchId){

    var snackbarContainer = document.querySelector('#demo-toast-example');

    //process fields data and create object to send data

    var propertyList = [];
    var imageList = [];

    $.each( $("input[name='attribute-value']:checked"), function(){
        propertyList.push( $(this).val() );
    });



    for(var i=0; i < IMAGE_COUNT; i++){
        imageList.push( $("#img" + i).attr("src") );
    }


    var data = {
        name: $( "#commodityName" ).val(),
        shortDescription: $( "#commodityShortDesc" ).val(),
        overview: $( "#commodityOverview" ).val(),
        amount: $( "#commodityAmount" ).val(),
        price: $( "#commodityPrice" ).val(),
        typeId: COMMODITY_TYPE,
        propertyValues: propertyList,
        images: imageList

     };


    var urlFunction = URL_SERVICES + "/commodity/";
    var method;
    if(branchId===undefined){
        method = "POST";
    }else{
        method = "PUT";
        data["branchId"] = branchId;
    }

    //$('#test-data').empty();
    //$('#test-data').append(  JSON.stringify(data) );

    var options = {
                url: urlFunction,
                type: method,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(data), // Our valid JSON string

                success: function( data, status, xhr ) {
                    //...

                    var toastMessage = {message: method + ' Commodity Success' };
                    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
                    //refreshTypes();
                    //cleanCreateType()
               },
               error: function( xhr, status, error ) {
                    //...
                    var toastMessage = {message: 'Error in '+method+' Commodity' };
                    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
                    //refreshTypes();
                    //cleanCreateType()
               }
    };
    $.ajax( options );


};

function btnClickUpdateCommodity(){
    if(BRANCH_ID>0){
        btnClickAddCommodity(BRANCH_ID);
    }
};


function showCommodityForm(){

    for(var i=0; i < IMAGE_COUNT; i++){
        $("#img-spinner-" + i).hide();
    }
    $('#create-commodity').show();
    $('#commodities-container').hide();
    $("#attributes-cm-container").hide();
    $("#btn-add-commodity").hide();
    $('#btn-createupdate-back').show();
    $('#error-container-2').hide();
    componentHandler.upgradeDom();

};

function loadCommodity(id){

    var snackbarContainer = document.querySelector('#demo-toast-example');

    var getURL = "/commodity/id/" + id;

    $.getJSON( URL_SERVICES + getURL, function(json, status){

        //$("#create-commodity").show();
        COMMODITY_TYPE = json.typeId;
        selectType();
        loadCreateCFProperties(COMMODITY_TYPE);
        //load images
        var images = json.images;
        for(var i=0; i < images.length; i++){
            $("#img" + i).attr("src", images[i].uri);
        }
        //end load images
        $('#commodityName').val( json.name );
        //$('#commodityName').attr("class", "mdl-textfield mdl-js-textfield is-upgraded");
        //$('#commodityName').attr("data-upgraded", ",MaterialTextfield");

        $('#commodityShortDesc').val( json.shortDescription );
        $('#commodityOverview').val( json.overview );

        componentHandler.upgradeDom();

    })
	 .fail(function(json, status) {

	    toastMessage = {message: 'Error in Load Commodity id = ' + id };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 });

};

function loadCommodityBranch(branchId){


    showCommodityForm();

    var snackbarContainer = document.querySelector('#demo-toast-example');
    BRANCH_ID = branchId;

    window.location = '#branchId/' + BRANCH_ID;


    var getURL = "/commodityBranch/" + BRANCH_ID;

    $.getJSON( URL_SERVICES + getURL, function(json, status){

        loadCommodity(json.commodityId);

        //load properties
        PROPERTIES = [];
        var props = json.propertySet;
        for(var pi=0; pi<props.length; pi++){
            PROPERTIES.push( props[pi].attributeValueId );
        }

        $('#commodityAmount').val( json.amount );
        $('#commodityPrice').val( json.price );
        var toastMessage = {message: 'Load Commodity Branch id = ' + branchId };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 })
	 .fail(function(json, status) {

	    toastMessage = {message: 'Error in Load Commodity Branch id = ' + branchId };
        snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);

	 });




};

function loadListCommodity(){

    var snackbarContainer = document.querySelector('#demo-toast-example');
    var getURL = "/commodities/";

    $.getJSON( URL_SERVICES + getURL, function(json, status){

        var content = "";
        for(var i=0; i<json.length; i++){
            var cm = json[i];

            for(var ci=0; ci<cm.branches.length; ci++){
                var branch = cm.branches[ci];

                content += '<tr onclick="loadCommodityBranch('+branch.id+');"><td class="mdl-data-table__cell--non-numeric">'+ cm.name +'</td>';
                content += '<td class="mdl-data-table__cell--non-numeric">' + cm.type.name + '</td>';
                content += '<td class="mdl-data-table__cell--non-numeric">';

                //process properties
                var properties = branch.propertySet;

                for(var pi = 0; pi < properties.length; pi++){
                    content += properties[pi].attribute.name + ' : ' + properties[pi].attributeValue.value + '<br/>';
                };

                content += '</td>';
                content += '<td>' + branch.amount + '</td>';
                content += '<td>' + branch.price + '</td>';
                content += '<td class="mdl-data-table__cell--non-numeric">Button for action</td>';
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
    window.location = '#commodities';
    initializeBehaviorOptions();
    $('#btn-createupdate-back').hide();
    $('#create-commodity').hide();
    loadListCommodity();
    $('#error-container-2').hide();
    $('#commodities-container').show();
    componentHandler.upgradeDom();
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

