/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function ShoppingCart()
{
    window.location.replace("shopCart.html");
}

function handleSessionData(resultDataString) {
    console.log("handle session response");
    console.log(resultDataString);
}


function AddToCart(cartEvent) {
    console.log("submit cart");

    $.ajax("api/shopcart", {
        method: "POST",
        data: {"itemInfo": cartEvent, "option": "NONE"},
        success: (resultData) => handleSessionData(resultData)
    });
    alert("Added to Cart");

}


function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function submitTitle()
{
    let titleElement = document.getElementById("sort_by_title");
    webVariables.sortingTitleBy = titleElement.value;
    webVariables.pageNumber = 1;
    submitGenre();
}

function submitRating()
{
    let ratingElement = document.getElementById("sort_by_rating");
    webVariables.sortingRatingBy = ratingElement.value;
    console.log(webVariables.sortingRatingBy);
    console.log(ratingElement.value);
    webVariables.pageNumber = 1;
    submitGenre();
}

function submitSort()
{
    let sortingElement = document.getElementById("sort_by_first");
    if(sortingElement.value == "Rating")
    {
        webVariables.sortingRatingFirst = "true";
        webVariables.pageNumber = 1;
        submitGenre();
    }
    else {
        webVariables.sortingRatingFirst = "false";
        webVariables.pageNumber = 1;
        submitGenre();
    }
}

function submitNumberOfItems()
{
    let numberOfItems = document.getElementById("sort_by_numbers");
    webVariables.pageSize = numberOfItems.value;
    webVariables.pageNumber = 1;
    submitGenre();
}

function nextTPage()
{
    webVariables.pageNumber += 1;
    webVariables.stopNextPage = false;
    if(webVariables.browseAlphaView)
    {
        browse_alpha();
    }
    else if(webVariables.browseGenreView)
    {
        submitGenre();
    }
    else if(webVariables.browseNumericView)
    {
        browse_numeric();
    }
    else
    {
        search();
    }
}

function prevTPage()
{
    webVariables.pageNumber -= 1;
    webVariables.stopNextPage = false;
    if(webVariables.browseAlphaView)
    {
        browse_alpha();
    }
    else if(webVariables.browseGenreView)
    {
        submitGenre();
    }
    else if(webVariables.browseNumericView)
    {
        browse_numeric();
    }
    else
    {
        search();
    }
}


function submitGenre()
{

    let params = "pageSize=" + webVariables.pageSize.toString() + "&";
    params += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    params += "RatingFirst=" + webVariables.sortingRatingFirst + "&";
    params += "sortRating=" + webVariables.sortingRatingBy + "&";
    params += "sortTitle=" + webVariables.sortingTitleBy;
    params += "&genre=" + genreId;
    console.log("genre id is " + genreId);
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browseGenre?" + params,
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + param;
    window.history.pushState({path:browse_url},'',browse_url);
}


function handleMovieListResult(resultData) {
    console.log("handleSearchResult: populating MovieList table from resultData");
    if(webVariables.pageNumber == 1)
    {
        let x = document.getElementById("prevTablePage");
        x.style.display = "none";
    }
    else
    {
        let x = document.getElementById("prevTablePage");
        x.style.display = "block";
    }
    if(resultData.length != webVariables.pageSize)
    {
        let y = document.getElementById("nextTablePage");
        y.style.display = "none";
        webVariables.stopNextPage = true;
    }
    else if(resultData.length == webVariables.pageSize && !webVariables.stopNextPage)
    {
        let y = document.getElementById("nextTablePage");
        y.style.display = "block";
    }

    if(resultData.length != 0)
    {
        let movieListTableElement2 = $("#genre_movie_list_table_body");
        movieListTableElement2.html("");
        console.log(resultData[0]);
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
            rowHTML += "<td>" + resultData[i]["movie_director"] + "</td>"
            rowHTML += "<td>";
            for (let x = 0; x < Math.min(3, resultData[i]['genres'].length); x++) {
                if (x + 1 == Math.min(3, resultData[i]['genres'].length)) {
                    rowHTML += '<a href="browsingGenre.html?genre=' + resultData[i]['genres'][x].split(",")[1] + '">'
                        + resultData[i]['genres'][x].split(",")[0] +
                        '</a>';
                } else {
                    rowHTML += '<a href="browsingGenre.html?genre=' + resultData[i]['genres'][x].split(",")[1] + '">'
                        + resultData[i]['genres'][x].split(",")[0] +
                        '</a>' + ", ";
                }
            }

            rowHTML += "</td>";
            // add Stars and hrefs
            rowHTML += "<td>";
            for (let x = 0; x < Math.min(3, resultData[i]['movie_stars'].length); x++) {
                if (x + 1 == Math.min(3, resultData[i]['movie_stars'].length)) {
                    rowHTML += '<a href="single-star.html?id=' + resultData[i]['movie_stars'][x].split(",")[1] + '">'
                        + resultData[i]['movie_stars'][x].split(",")[0] +
                        '</a>';
                } else {
                    rowHTML += '<a href="single-star.html?id=' + resultData[i]['movie_stars'][x].split(",")[1] + '">'
                        + resultData[i]['movie_stars'][x].split(",")[0] +
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
        console.log("Done populating table..");
    }
    else
    {
        revertToPreviousPage();
        alert("Next page was not available for this search...\nReverting to previous page");
    }
}


var webVariables = {
    "pageNumber" : 1,
    "sortingRatingFirst" : "true",
    "sortingRatingBy" : "DESC",
    "sortingTitleBy" : "ASC",
    "pageSize" : 25
};

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
var genreId = getParameterByName('genre');
var param = "pageSize=" + webVariables.pageSize.toString() + "&";
param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
param += "RatingFirst=" + webVariables.sortingRatingFirst + "&";
param += "sortRating=" + webVariables.sortingRatingBy + "&";
param += "sortTitle=" + webVariables.sortingTitleBy;
param += "&genre=" + genreId;
console.log(genreId);

$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browseGenre?" + param, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleMovieListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});