window.addEventListener("beforeunload", function (e) {
    var confirmationMessage = "\o/";
   
    window.location.replace('http://localhost:8080');
    // (e || window.event).returnValue = confirmationMessage; //Gecko + IE
    // return confirmationMessage;                            //Webkit, Safari, Chrome
});