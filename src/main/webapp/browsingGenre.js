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


function handleMovieListResult(resultData) {
    console.log("handleSearchResult: populating MovieList table from resultData");
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
        for (let x = 0; x < Math.min(3, resultData[i]['genre_names'].length); x++) {
            if (x + 1 == Math.min(3, resultData[i]['genre_names'].length)) {
                rowHTML += '<a href="browsingGenre.html?genre=' + resultData[i]['genre_ids'][x] + '">'
                    + resultData[i]['genre_names'][x] +
                    '</a>';
            } else {
                rowHTML += '<a href="browsingGenre.html?genre=' + resultData[i]['genre_ids'][x] + '">'
                    + resultData[i]['genre_names'][x] +
                    '</a>' + ", ";
            }
        }

        rowHTML += "</td>";
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
        // rowHTML += "<td>";
        // rowHTML += "<button onclick=AddToCart('" + shpvalue + "')>ADD</button>";
        // rowHTML += "</td>";
        rowHTML += "</tr>";
        //
        //     // Append the row created to the table body, which will refresh the page

        movieListTableElement2.append(rowHTML);


    }
    console.log("Done populating table..");

}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let genreId = getParameterByName('genre');
console.log(genreId);
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browseGenre?genre=" + genreId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleMovieListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});