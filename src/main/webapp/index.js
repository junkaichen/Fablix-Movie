let search_form = $("#search_form");
let cart = $("#cart");
/**
 * Handle the data returned by SearchServlet
 * @param resultData jsonObject
 */

function handleSessionData(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);
    console.log("handle session response");
    console.log(resultDataJson);
    console.log(resultDataJson["sessionID"]);
    handleCartArray(resultDataJson["previousItems"]);

}

function handleCartArray(resultArray) {
    console.log(resultArray);
    let item_list = $("#item_list");
    // change it to html list
    let res = "<ul>";
    for (let i = 0; i < resultArray.length; i++) {
        // each item will be in a bullet point
        res += "<li>" + resultArray[i] + "</li>";
    }
    res += "</ul>";

    // clear the old array and show the new array in the frontend
    item_list.html("");
    item_list.append(res);
}



function AddToCart(cartEvent)
{
    console.log(cartEvent);

    $.ajax("api/shopcart", {
        method: "POST",
        data: {"itemInfo": cartEvent},
        success: resultDataString => {
            let resultDataJson = JSON.parse(resultDataString);
            handleCartArray(resultDataJson["previousItems"]);
        }
    });
    // clear input form
    cart[0].reset();




}

function myFunction() {
    var x = document.getElementById("search_form");
    if(x.style.display == "block")
    {
        x.style.display = "none";
        exit;
    }
    x.style.display = "block";

}
function submitform() {
    var x = document.getElementById("movie_list_table2");
    x.style.display = "block";

}

function handleSearchResult(resultData) {
    console.log("handleSearchResult: populating MovieList table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieListTableElement2 = $("#movie_list_table_body2");

    movieListTableElement2.html("");
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        let rowHTML = "";
        let shpvalue = resultData[i]['movie_id'];
        rowHTML += "<tr>";
        // Add a link to single-star.html with id passed with GET url parameter
        rowHTML +=
            "<td>" +
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +
            '</a>' +
            "</td>";
        rowHTML += "<td>" + resultData[i]["movie_year"] + "</td>";
        rowHTML += "<td>" + resultData[i]["movie_director"] + "</td>";
        rowHTML += "<td>" + resultData[i]["movie_genres"] + "</td>";
        // add Stars and hrefs
        rowHTML += "<td>";
        for (let x = 0; x < Math.min(3, resultData[i]['movie_star'].length); x++) {
            if (x + 1 == Math.min(3, resultData[i]['movie_star'].length)) {
                rowHTML += '<a href="single-star.html?id=' + resultData[i]['movie_starid'][x] + '">'
                    + resultData[i]['movie_star'][x] +
                    '</a>';
            } else {
                rowHTML += '<a href="single-star.html?id=' + resultData[i]['movie_starid'][x] + '">'
                    + resultData[i]['movie_star'][x] +
                    '</a>' + ", ";
            }
        }

        rowHTML += "</td>";
        rowHTML += "<td>" + resultData[i]["movie_rating"] + "</td>";
        rowHTML += "<td>";
        rowHTML += "<button onclick=AddToCart('" + shpvalue + "')>ADD</button>";
        rowHTML += "</td>";
        rowHTML += "</tr>";
        //
        //     // Append the row created to the table body, which will refresh the page

        movieListTableElement2.append(rowHTML);


    }
    // let rowHTML ="";
    // rowHTML += "</th>";
    // rowHTML += "<th>" + "gi" + "</th>";
    // rowHTML += "</tr>";
    // movieListTableElement2.append(rowHTML);



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

    jQuery.ajax(
      {
          dataType: "json",
          method: "GET",
          // Serialize the login form to the data sent by POST request
          data: search_form.serialize(),
          url: "api/search",
          success: (resultData) => handleSearchResult(resultData)
        }
    );
}

$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);