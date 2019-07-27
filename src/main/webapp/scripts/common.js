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