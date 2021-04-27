let payment_form = $("#payment_form");






function handletotalprice(resultDataString) {
    console.log("handling totalprice");
    $("#totalprice").text(resultDataString["totalPrice"]);

}






function handlePaymentResult(resultDataString) {

    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);


    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        window.location.replace("confirmation.html");
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson ["message"]);
        $("#payment_error_message").text(resultDataJson ["message"]);
    }
}






function submitPaymentForm(formSubmitEvent) {
    console.log("submit payment form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();
    $.ajax(
        /* this will lead us to LoginServlet */
        "api/payment", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: payment_form.serialize(),
            success: handlePaymentResult
        }
    );
}






//
$.ajax(
    /* this will lead us to LoginServlet */
    "api/payment", {
        method: "GET",
        // Serialize the login form to the data sent by POST request
        success: (resultData) => handletotalprice(resultData)
    });







payment_form.submit(submitPaymentForm);