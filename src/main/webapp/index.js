let search_form = $("#search_form");
var Genre_activation = false;
loadGenres();
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
    // clear input form


function myFunction() {
    let x = document.getElementById("search_form");
    if(x.style.display == "block")
    {
        x.style.display = "none";
        exit;
    }
    x.style.display = "block";

}

function browse_alpha()
{
    // if(Search_view)
    // {
    //     page_num = 0;
    //     Browse_view = true;
    //     Search_view = false;
    // }
    // let sort_by_rating = document.getElementById("sort_by_rating");
    // let sort_by_name = document.getElementById("sort_by_name");
    // console.log("sort by rating : " + sort_by_rating.value);
    // console.log("sort by name : " + sort_by_name.value);
    let t = document.getElementById("alpha_list");
    submitform();
    console.log(document.location);
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browse?starts_with=" + t.value,
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?starts_with=" + t.value;
    window.history.pushState({path:browse_url},'',browse_url);
}

function browse_numeric()
{
    // if(Search_view)
    // {
    //     page_num = 0;
    //     Browse_view = true;
    //     Search_view = false;
    // }
    // let sort_by_rating = document.getElementById("sort_by_rating");
    // let sort_by_name = document.getElementById("sort_by_name");
    // console.log("sort by rating : " + sort_by_rating.value);
    // console.log("sort by name : " + sort_by_name.value);
    let p = document.getElementById("numeric_list");
    submitform();
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browse?starts_with=" + p.value,
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?starts_with=" + p.value;
    window.history.pushState({path:browse_url},'',browse_url);
}

function submitform() {
    let x = document.getElementById("movie_list_table2");
    x.style.display = "block";
}

function collapseResults()
{
    let x = document.getElementById("movie_list_table2");
    if(x.style.display == "block")
    {
        x.style.display = "none";
    }
    else
    {
        x.style.display = "block";
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

function submitGenre()
{

    // if(Search_view)
    // {
    //     page_num = 0;
    //     Browse_view = true;
    //     Search_view = false;
    // }
    // let sort_by_rating = document.getElementById("sort_by_rating");
    // let sort_by_name = document.getElementById("sort_by_name");
    // console.log("sort by rating : " + sort_by_rating.value);
    // console.log("sort by name : " + sort_by_name.value);
    let p = document.getElementById("genre_list_table");
    console.log("genre id is " + p.value);
    submitform();
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            url: "api/browseGenre?genre=" + p.value,
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + "?genre=" + p.value;
    window.history.pushState({path:browse_url},'',browse_url);
}

function viewGenre(genre_id)
{
    console.log(genre_id);
    console.log("attempting to view genre");
    // jQuery.ajax(
    //     {
    //         dataType: "json",
    //         method: "GET",
    //         url: "api/browseGenre?genre=" + genre_id,
    //         success: (resultData) => handleSearchResult(resultData)
    //     }
    // );
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
        //
        //     // Append the row created to the table body, which will refresh the page

        movieListTableElement2.append(rowHTML);


    }
    console.log("Done populating table..")

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

    // if(Browse_view)
    // {
    //     Search_view = true;
    //     Browse_view = false;
    //     page_num = 0;
    // }
    let first = false;
    let title_info = {
        "param_type":"title",
        "value":document.getElementsByTagName("input")[0].value
    };
    let year_info = {
        "param_type":"year",
        "value":document.getElementsByTagName("input")[1].value
    };
    let director_info = {
        "param_type":"director",
        "value":document.getElementsByTagName("input")[2].value
    };
    let star_info = {
        "param_type":"star",
        "value":document.getElementsByTagName("input")[3].value
    };
    let params = [title_info,year_info,director_info,star_info];
    let confirmedParams = "?";
    for(i = 0; i < params.length; i++)
    {
        if(params[i].value != "")
        {
            if(!first)
            {
                first = true;
                confirmedParams += params[i].param_type + "=" + params[i].value;
            }
            else
            {
                confirmedParams += "&" + params[i].param_type + "=" + params[i].value
            }
        }

    }
    console.log("confirmed parameters");
    console.log(confirmedParams);
    jQuery.ajax(
        {
            dataType: "json",
            method: "GET",
            // Serialize the login form to the data sent by POST request
            data: confirmedParams.substr(1),
            url: "api/search",
            success: (resultData) => handleSearchResult(resultData)
        }
    );
    let browse_url = window.location.protocol + "//" + window.location.host + window.location.pathname + confirmedParams;
    window.history.pushState({path:browse_url},'',browse_url);
}

$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);