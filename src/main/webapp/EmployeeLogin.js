// let login_form = $("#login_form2");
//
// /**
//  * Handle the data returned by LoginServlet
//  * @param resultDataString jsonObject
//  */
// function handleLoginResult(resultDataString) {
//     let resultDataJson = JSON.parse(resultDataString);
//
//     console.log("handle login response");
//     console.log(resultDataJson);
//     console.log(resultDataJson["status"]);
//
//     // If login succeeds, it will redirect the user to index.html
//     if (resultDataJson["status"] === "success") {
//         window.location.replace("index.html");
//     } else {
//         // If login fails, the web page will display
//         // error messages on <div> with id "login_error_message"
//         console.log("show error message");
//         console.log(resultDataJson["message"]);
//         $("#login_error_message").text(resultDataJson["message"]);
//     }
// }
//
// /**
//  * Submit the form content with POST method
//  * @param formSubmitEvent
//  */
// function submitLoginForm(formSubmitEvent) {
//     console.log("submit employee login form");
//     /**
//      * When users click the submit button, the browser will not direct
//      * users to the url defined in HTML form. Instead, it will call this
//      * event handler when the event is triggered.
//      */
//     formSubmitEvent.preventDefault();
//
//     $.ajax(
//         /* this will lead us to LoginServlet */
//         "api/employeelogin", {
//             method: "POST",
//             // Serialize the login form to the data sent by POST request
//             data: login_form.serialize(),
//             success: handleLoginResult
//         }
//     );
// }
//
// // Bind the submit action of the form to a handler function
// login_form.submit(submitLoginForm);

let login_form = $("#login_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        window.location.replace("DashBoard.html");
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        alert(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
        window.location.replace("EmployeeLogin.html");
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit employee login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        /* this will lead us to LoginServlet */
        "api/employeelogin", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: login_form.serialize(),
            success: handleLoginResult,
        }
    );
}

function UsersEntry()
{
    window.location.replace("login.html");
}

// Bind the submit action of the form to a handler function
login_form.submit(submitLoginForm);