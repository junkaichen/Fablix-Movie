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
function handleMovieListResult(resultData) {
    console.log("handleMovieListResult: populating MovieList table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieListTableElement = jQuery("#movie_list_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        // Add a link to single-star.html with id passed with GET url parameter
        rowHTML +=
            "<th>" +
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_nameOfGenres"] + "</th>";
        // add Stars and hrefs
        rowHTML += "<th>";

        for (let x = 0; x < Math.min(3, resultData[i]['movie_nameOfStars'].length); x++) {
            console.log(resultData[i]['movie_nameOfStars'])
            if(x + 1 == Math.min(3, resultData[i]['movie_nameOfStars'].length )){
                rowHTML += '<a href="single-star.html?id=' + resultData[i]['star_ids'][x] + '">'
                    + resultData[i]['movie_nameOfStars'][x]  +
                    '</a>';

            }
            else
            {
                rowHTML += '<a href="single-star.html?id=' + resultData[i]['star_ids'][x] + '">'
                    + resultData[i]['movie_nameOfStars'][x] +
                    '</a>' + ", " ;
            }
        }
        rowHTML += "</th>";
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieListTableElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movielist", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleMovieListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});