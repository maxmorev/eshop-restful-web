function showToast(message){
    var snackbarContainer = document.querySelector('#demo-toast-example');
    var toastMessage = {message: message };
    snackbarContainer.MaterialSnackbar.showSnackbar(toastMessage);
}


function sendDataAsJson(url, method, data, successCallback, errorCallback){

    var options = {
        url: url,
        type: method,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data), // Our valid JSON string
        success: function( json, status, xhr ) {
            if (successCallback) {
                successCallback(json);
            }
        },
        error: function( json, status ) {
            if (errorCallback) {
                errorCallback(json);
            }
        }
    };
    $.ajax( options );
}

function showColorElement(color){
    return '<div class="colorCircleSml" style="background: '+color+'">&#160;&#160;&#160;&#160;</div>&#160;';
}

function showSizeElement(size){
    return '<div class="textCircle">&#160;'+size+'&#160;</div>&#160;';
}