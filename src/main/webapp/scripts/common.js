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

function showErrorMessage(message){
    $('#error-message').empty();
    $('#error-message').append(message);
    $('#error-container').show();
}

function showErrorFromJson(json){

    var errorContent = json.responseJSON.message.replace(/\n/g, "<br/>");
    if(json.responseJSON.errors && json.responseJSON.errors.length>0){
        errorContent = "";
        json.responseJSON.errors.forEach(function(val){ errorContent += val.message + "<br/>"});
    }
    showErrorMessage(errorContent);
}

function formatDate(date) {
    var yyyy = date.getFullYear();
    var dd = date.getDate();
    var mm = (date.getMonth() + 1);

    if (dd < 10)
        dd = "0" + dd;

    if (mm < 10)
        mm = "0" + mm;

    var cur_day = dd + "/"+ mm + "/" + yyyy;
    var hours = date.getHours()
    var minutes = date.getMinutes()
    var seconds = date.getSeconds();

    if (hours < 10)
        hours = "0" + hours;

    if (minutes < 10)
        minutes = "0" + minutes;

    if (seconds < 10)
        seconds = "0" + seconds;

    return cur_day + " " + hours + ":" + minutes + ":" + seconds;
}