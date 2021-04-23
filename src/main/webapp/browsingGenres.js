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
function handleGenreListResult(resultData) {
    console.log("handleMovieListResult: populating MovieList table from resultData");

    // Populate the movie_genre table
    // Find the empty table body by id "genre_list_table_body"
    let genreListTableElement = jQuery("#genre_list_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(100, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        // Add a link to single-star.html with id passed with GET url parameter
        rowHTML +=
            "<th>" +
            '<a href="movielist.html?genre_id=' + resultData[i]['id'] + '">'
            + resultData[i]["name"] +
            '</a>' +
            "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        genreListTableElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browsingGenres", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleGenreListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});