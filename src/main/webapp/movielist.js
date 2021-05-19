/**
 * Handle the data returned by SearchServlet
 * @param resultData jsonObject
 */
function handleSessionData(resultDataString) {
    console.log("handle session response");
    console.log(resultDataString);
}

function ShoppingCart()
{
    window.location.replace("shopCart.html");
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

function browse_alphanumeric()
{
    //change this
    let param = "pageSize=" + webVariables.pageSize.toString() + "&";
    param += "viewType=browseAlphaView&";
    param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    param += "RatingFirst=" + webVariables.sortingRatingFirst + "&";
    param += "sortRating=" + webVariables.sortingRatingBy + "&";
    param += "sortTitle=" + webVariables.sortingTitleBy;
    param += "&starts_with=" + webVariables.starts_with;
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



function submitGenre()
{
    //change this
    let param = "pageSize=" + webVariables.pageSize.toString() + "&";
    param += "viewType=browseGenreView&";
    param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    param += "RatingFirst=" + webVariables.sortingRatingFirst + "&";
    param += "sortRating=" + webVariables.sortingRatingBy + "&";
    param += "sortTitle=" + webVariables.sortingTitleBy;
    param += "&genre=" + webVariables.genre;
    console.log("genre id is " + webVariables.genre);
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



function submitSearchForm() {

    let param = "pageSize=" + webVariables.pageSize.toString() + "&";
    param += "viewType=searchView&";
    param += "pageNumber=" + webVariables.pageNumber.toString() + "&";
    param += "RatingFirst=" + webVariables.sortingRatingFirst + "&";
    param += "sortRating=" + webVariables.sortingRatingBy + "&";
    param += "sortTitle=" + webVariables.sortingTitleBy;

    if(webVariables.title != null && webVariables.title != "")
    {
        param += "&title=" + webVariables.title;
    }
    if(webVariables.year != null && webVariables.year != "")
    {
        param += "&year=" + webVariables.year;

    }
    if(webVariables.director != null && webVariables.director != "")
    {
        param += "&director=" + webVariables.director;
    }
    if(webVariables.star != null && webVariables.star != "")
    {
        param += "&star=" + webVariables.star;
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


function submitTitle()
{
    let titleElement = document.getElementById("sort_by_title");

    webVariables.sortingTitleBy = titleElement.value;
    webVariables.pageNumber = 1;
    let y = document.getElementById("nextTablePage");
    y.style.display = "block";
    webVariables.stopNextPage = false;
    if(webVariables.viewType == "searchView")
    {
        submitSearchForm();
    }
    else if(webVariables.viewType == "browseAlphaView")
    {
        browse_alphanumeric();
    }
    else
    {
        submitGenre();
    }

}

function submitRating()
{
    let ratingElement = document.getElementById("sort_by_rating");
    let y = document.getElementById("nextTablePage");
    y.style.display = "block";
    webVariables.sortingRatingBy = ratingElement.value;
    webVariables.pageNumber = 1;
    webVariables.stopNextPage = false;
    if(webVariables.viewType == "searchView")
    {
        submitSearchForm();
    }
    else if(webVariables.viewType == "browseAlphaView")
    {
        browse_alphanumeric();
    }
    else
    {
        submitGenre();
    }

}

function submitSort()
{
    let sortingElement = document.getElementById("sort_by_first");
    let y = document.getElementById("nextTablePage");
    y.style.display = "block";
    if(sortingElement.value === "Rating")
    {
        webVariables.stopNextPage = false;
        webVariables.sortingRatingFirst = "true";
        webVariables.pageNumber = 1;
        if(webVariables.viewType == "searchView")
        {
            submitSearchForm();
        }
        else if(webVariables.viewType == "browseAlphaView")
        {
            browse_alphanumeric();
        }
        else
        {
            submitGenre();
        }

    }
    else
    {
        webVariables.stopNextPage = false;
        webVariables.sortingRatingFirst = "false";
        webVariables.pageNumber = 1;
        if(webVariables.viewType == "searchView")
        {
            submitSearchForm();
        }
        else if(webVariables.viewType == "browseAlphaView")
        {
            browse_alphanumeric();
        }
        else
        {
            submitGenre();
        }

    }

}

function submitNumberOfItems()
{
    let numberOfItems = document.getElementById("sort_by_numbers");
    webVariables.pageSize = numberOfItems.value;
    webVariables.pageNumber = 1;
    webVariables.stopNextPage = false;
    let y = document.getElementById("nextTablePage");
    y.style.display = "block";
    if(webVariables.viewType == "searchView")
    {
        submitSearchForm();
    }
    else if(webVariables.viewType == "browseAlphaView")
    {
        browse_alphanumeric();
    }
    else
    {
        submitGenre();
    }
}

function nextTPage()
{
    webVariables.pageNumber += 1;
    webVariables.stopNextPage = false;
    if(webVariables.viewType == "searchView")
    {
        submitSearchForm();
    }
    else if(webVariables.viewType == "browseAlphaView")
    {
        browse_alphanumeric();
    }
    else
    {
        submitGenre();
    }
}

function prevTPage()
{
    webVariables.pageNumber -= 1;
    webVariables.stopNextPage = false;
    if(webVariables.viewType == "searchView")
    {
        submitSearchForm();
    }
    else if(webVariables.viewType == "browseAlphaView")
    {
        browse_alphanumeric();
    }
    else
    {
        submitGenre();
    }
}


function revertToPreviousPage()
{
    webVariables.pageNumber -= 1;
    webVariables.stopNextPage = true;
    if(webVariables.viewType == "searchView")
    {
        submitSearchForm();
    }
    else if(webVariables.viewType == "browseAlphaView")
    {
        browse_alphanumeric();
    }
    else
    {
        submitGenre();
    }
}


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleSearchResult(resultData) {
    console.log("handleSearchResult: populating MovieList table from resultData");

    let size = resultData.length;
    console.log("length of result data is " + size);
    if(size == webVariables.pageSize && !webVariables.stopNextPage)
    {
        let y = document.getElementById("nextTablePage");
        y.style.display = "block";

    }
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
    if(size < webVariables.pageSize)
    {
        let y = document.getElementById("nextTablePage");
        y.style.display = "none";
        webVariables.stopNextPage = true;
    }

    if(resultData.length >= 1)
    {
        // Populate the star table
        // Find the empty table body by id "star_table_body"
        let movieListTableElement2 = $("#movie_list_table_body").empty();
        movieListTableElement2.html("");
        for (let i = 0; i < Math.min(webVariables.pageSize, resultData.length); i++) {
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
                if (x + 1 === Math.min(3, resultData[i]['genres'].length)) {
                    rowHTML += '<a href="movielist.html?viewType=browseGenreView&genre=' + resultData[i]['genres'][x].split(",")[1] + '">'
                        + resultData[i]['genres'][x].split(",")[0] +
                        '</a>';
                } else {
                    rowHTML += '<a href="movielist.html?viewType=browseGenreView&genre=' + resultData[i]['genres'][x].split(",")[1] + '">'
                        + resultData[i]['genres'][x].split(",")[0] +
                        '</a>' + ", ";
                }
            }
            rowHTML += "</td>";
            // add Stars and hrefs
            rowHTML += "<td>";
            for (let x = 0; x < Math.min(3, resultData[i]['movie_stars'].length); x++) {
                if (x + 1 === Math.min(3, resultData[i]['movie_stars'].length)) {
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
    if(size === 0 && webVariables.pageNumber != 1)
    {
        revertToPreviousPage();
        alert("Next page was not available for this search...\nReverting to previous page");
    }

}

function getViewType()
{
    return getParameterByName("viewType").toString();
}

function movieInfo()
{
    this.viewType = getViewType();
    console.log(this.viewType);
    this.pageNumber = 1;
    this.sortingRatingFirst = "true";
    this.sortingRatingBy = "DESC";
    this.sortingTitleBy = "ASC";
    this.stopNextPage = false;
    this.pageSize = 25;
    if(this.viewType === "searchView")
    {
        this.title = getParameterByName("title");
        this.director = getParameterByName("director");
        this.year = getParameterByName("year");
        this.star = getParameterByName("star");
        this.genre = null;
        this.starts_with = null;
    }
    else if(this.viewType === "browseAlphaView")
    {
        this.starts_with = getParameterByName("starts_with");
        this.title = null;
        this.director = null;
        this.year = null;
        this.star = null;
        this.genre = null;
    }
    else
    {
        this.genre = getParameterByName("genre");
        this.title = null;
        this.director = null;
        this.year = null;
        this.star = null;
        this.starts_with = null;
    }
}

function execute()
{
    if(webVariables.viewType == "searchView")
    {
        submitSearchForm();
    }
    else if(webVariables.viewType == "browseAlphaView")
    {
        browse_alphanumeric();
    }
    else
    {
        submitGenre();
    }
}
$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});

var webVariables = new movieInfo();
console.log(webVariables.pageSize);
execute();