function ShoppingCart()
{
    window.location.replace("shopCart.html");
}

function handleSessionData(resultDataString) {
    console.log("handle session response");
    console.log(resultDataString);
}

function submitGenres()
{
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browsingGenres",
            success: (resultData) => handleMovieListResult(resultData)
        }
    );
}


function handleMovieListResult(resultData) {
    let genreTableElement = $("#genre_movie_list_table_body");
    genreTableElement.html("");
    console.log("loading genre table for id");

    console.log(resultData);
    console.log(resultData[0]);
    for (let i = 0; i < resultData.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>"
        rowHTML += "<td>"
        rowHTML +=
            '<a href="movielist.html?viewType=browseGenreView&genre='
            + resultData[i]['id'] + '"' + '>'
            + resultData[i]["name"] +
            '</a>';
        rowHTML += "</td>"
        rowHTML += "</tr>";
        genreTableElement.append(rowHTML);
    }
    console.log("Done populating genre table..")
}

submitGenres();

$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});
