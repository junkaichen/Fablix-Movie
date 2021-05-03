

/**
 * Handle the data returned by SearchServlet
 * @param resultData jsonObject
 */
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


function myFunction() {
    let x = document.getElementById("search_form");
    if(x.style.display == "block")
    {
        x.style.display = "none";
    }
    else
    {
        x.style.display = "block";
    }


}

function browse_alpha()
{
    webVariables.pageNumber = 1;
    webVariables.browseNumericView = false;
    webVariables.browseGenreView = false;
    webVariables.browseAlphaView = true;
    webVariables.searchView = false;
    let p = document.getElementById("alpha_list");
    submitform();
    let param = "pageSize=" + webVariables.pageSize.toString() + "&";
    param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    param += "RatingFirst=" + webVariables.sortingRatingFirst.toString() + "&";
    param += "sortRating=" + webVariables.sortingRatingBy + "&";
    param += "sortTitle=" + webVariables.sortingTitleBy;
    param += "&starts_with=" + p.value;
    console.log(document.location);
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browse?" + param,
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + param;
    window.history.pushState({path:browse_url},'',browse_url);
}

function browse_numeric()
{
    webVariables.pageNumber = 1;
    webVariables.browseNumericView = true;
    webVariables.browseGenreView = false;
    webVariables.browseAlphaView = false;
    webVariables.searchView = false;
    let p = document.getElementById("numeric_list");
    let param = "pageSize=" + webVariables.pageSize.toString() + "&";
    param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    param += "RatingFirst=" + webVariables.sortingRatingFirst.toString() + "&";
    param += "sortRating=" + webVariables.sortingRatingBy + "&";
    param += "sortTitle=" + webVariables.sortingTitleBy;
    param += "&starts_with=" + p.value;
    submitform();
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browse?" + param,
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + param;
    window.history.pushState({path:browse_url},'',browse_url);
}

function submitGenre()
{

    let p = document.getElementById("genre_list_table");
    webVariables.pageNumber = 1;
    webVariables.browseNumericView = false;
    webVariables.browseGenreView = true;
    webVariables.browseAlphaView = false;
    webVariables.searchView = false;
    let param = "pageSize=" + webVariables.pageSize.toString() + "&";
    param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    param += "RatingFirst=" + webVariables.sortingRatingFirst.toString() + "&";
    param += "sortRating=" + webVariables.sortingRatingBy + "&";
    param += "sortTitle=" + webVariables.sortingTitleBy;
    param += "&genre=" + p.value;
    console.log("genre id is " + p.value);
    submitform();
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browseGenre?" + param,
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + param;
    window.history.pushState({path:browse_url},'',browse_url);
}


function submitform() {
    let x = document.getElementById("movie_list_table2");
    let y = document.getElementById("sorting_information");
    y.style.display = "block";
    x.style.display = "block";

}

function collapseResults()
{
    let x = document.getElementById("movie_list_table2");
    let y = document.getElementById("sorting_information");


    if(x.style.display == "block")
    {
        x.style.display = "none";
        y.style.display = "none";
    }
    else
    {
        x.style.display = "block";
        y.style.display = "block";
    }

}

function loadGenres()
{
    if(!Genre_activation)
    {
        Genre_activation = true;
        jQuery.ajax(
            {
                dataType: "json",
                method: "GET",
                url: "api/browsingGenres",
                success: (resultData) => handleSearchResult2(resultData)
            }
        );
    }
}


function ShoppingCart()
{
    window.location.replace("shopCart.html");
}

function handleSearchResult2(resultData)
{
    console.log("loading genre table for id");
    let genreTableElement = $("#genre_list_table");
    for (let i = 0; i < Math.min(25, resultData.length); i++) {
        let rowHTML = "";
        rowHTML +=
            '<option value="'
            + resultData[i]['id'] + '"' + '>'
            + resultData[i]["name"] +
            '</option>';
        genreTableElement.append(rowHTML);
    }
    console.log("Done populating genre table..")

}

function submitTitle()
{
    let titleElement = $("#sort_by_title");
    if(titleElement.value != webVariables.sortingTitleBy)
    {
        webVariables.sortingTitleBy = titleElement.value;
        webVariables.pageNumber = 1;
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
}

function submitRating()
{
    let ratingElement = $("#sort_by_rating");
    if(ratingElement.value != webVariables.sortingRatingBy)
    {
        webVariables.sortingRatingBy = ratingElement.value;
        webVariables.pageNumber = 1;
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

}

function submitSort()
{
    let sortingElement = $("#sort_by_first");
    if(sortingElement.value == "Rating")
    {
        if(!webVariables.sortingRatingFirst) {
            webVariables.sortingRatingFirst = true;
            webVariables.pageNumber = 1;
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
    }
    else
    {
        if(webVariables.sortingRatingFirst)
        {
            webVariables.sortingRatingFirst = false;
            webVariables.pageNumber = 1;
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

    }

}

function submitNumberOfItems()
{
    let numberOfItems = $("#sort_by_numbers");
    if(webVariables.pageSize != numberOfItems)
    {
        webVariables.pageSize = numberOfItems;
        webVariables.pageNumber = 1;
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


}



function handleSearchResult(resultData) {
    console.log("handleSearchResult: populating MovieList table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieListTableElement2 = $("#movie_list_table_body2").empty();
    movieListTableElement2.html("");
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        let rowHTML = "";
        let shpvalue = resultData[i]["movie_id"];
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
        rowHTML += "<button onclick=AddToCart('" + shpvalue + "')" + ">ADD</button>";
        rowHTML += "</td>";
        rowHTML += "</tr>";
        movieListTableElement2.append(rowHTML);
    }
    console.log("Done populating table..")

}

function search()
{
    submitSearchForm();
}

function submitSearchForm() {
    console.log("submit search form");
    console.log(document.getElementsByTagName("input")[0].value);
    console.log(document.getElementsByTagName("input")[1].value);
    console.log(document.getElementsByTagName("input")[2].value);
    console.log(document.getElementsByTagName("input")[3].value);
    submitform();
    webVariables.pageNumber = 1;
    webVariables.browseNumericView = false;
    webVariables.browseGenreView = false;
    webVariables.browseAlphaView = false;
    webVariables.searchView = true;
    
    let param = "pageSize=" + webVariables.pageSize.toString() + "&";
    param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    param += "RatingFirst=" + webVariables.sortingRatingFirst.toString() + "&";
    param += "sortRating=" + webVariables.sortingRatingBy + "&";
    param += "sortTitle=" + webVariables.sortingTitleBy;
    let addParams;
    if(document.getElementsByTagName("input")[0].value != "")
    {
        param += "&title=" + document.getElementsByTagName("input")[0].value;
        addParams += "&title=" + document.getElementsByTagName("input")[0].value;
    }
    if(document.getElementsByTagName("input")[1].value)
    {
        param += "&year=" + document.getElementsByTagName("input")[1].value;
        addParams += "&year=" + document.getElementsByTagName("input")[1].value;
    }
    if(document.getElementsByTagName("input")[2].value)
    {
        param += "&director=" + document.getElementsByTagName("input")[2].value;
        addParams += "&director=" + document.getElementsByTagName("input")[2].value;
    }
    if(document.getElementsByTagName("input")[3].value)
    {
        param += "&star=" + document.getElementsByTagName("input")[3].value;
    }

    console.log("confirmed parameters");
    console.log(param);
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            data : param,
            url: "api/search",
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?" + param;
    window.history.pushState({path:browse_url},'',browse_url);
}

$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});

var Genre_activation = false;


// All the variables needed for the Main page
var webVariables = {
    "searchView" : false,
    "browseGenreView" : false,
    "browseAlphaView" : false,
    "browseNumericView" : false,
    "pageNumber" : 1,
    "sortingRatingFirst" : true,
    "sortingRatingBy" : "DESC",
    "sortingTitleBy" : "ASC",
    "pageSize" : 25
};
loadGenres();
