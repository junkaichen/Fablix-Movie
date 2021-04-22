// let search_form = $("#search_form");
//
// /**
//  * Handle the data returned by LoginServlet
//  * @param resultDataString jsonObject
//  */
//
//
//
// function getParameterByName(target) {
//     // Get request URL
//     let url = window.location.href;
//     // Encode target parameter name to url encoding
//     target = target.replace(/[\[\]]/g, "\\$&");
//
//     // Ues regular expression to find matched parameter value
//     let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
//         results = regex.exec(url);
//     if (!results) return null;
//     if (!results[2]) return '';
//
//     // Return the decoded parameter value
//     return decodeURIComponent(results[2].replace(/\+/g, " "));
// }
//
// function handleSearchResult(resultData) {
//     let movietitle = resultData[0]["title"];
//     // window.location.replace("api/movielist?title" + movietitle);
//     window.location.replace("movielist.html");
// }
//
// /**
//  * Submit the form content with POST method
//  * @param formSubmitEvent
//  */
// function submitSearchForm(formSubmitEvent) {
//     console.log("submit search form");
//     /**
//      * When users click the submit button, the browser will not direct
//      * users to the url defined in HTML form. Instead, it will call this
//      * event handler when the event is triggered.
//      */
//     formSubmitEvent.preventDefault();
//
//     jQuery.ajax(
//         /* this will lead us to LoginServlet */
//         "api/search", {
//             method: "GET",
//             // Serialize the login form to the data sent by POST request
//             data: search_form.serialize(),
//             success: (resultData) => handleSearchResult(resultData)
//         }
//     );
// }
//
//
//
// // Bind the submit action of the form to a handler function
// search_form.submit(submitSearchForm);



let search_form = $("#search_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleSearchResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        window.location.replace("movielist.html");
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#search_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        /* this will lead us to LoginServlet */
        "api/search", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: search_form.serialize(),
            success: handleSearchResult
        }
    );
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);