



function addOneMore(cartEvent)
{
    console.log("submit add one more into cart");

    $.ajax("api/shopcart", {
        method: "POST",
        data: {"itemInfo": cartEvent, "option": "ADD"},
        success: (resultData) => handleSessionData(resultData)

    });
}

function removeOneMore(cartEvent)
{
    console.log("submit cart");

    $.ajax("api/shopcart", {
        method: "POST",
        data: {"itemInfo": cartEvent, "option": "REMOVE"},
        success: (resultData) => handleSessionData(resultData)

    });
}


function removeitem(cartEvent)
{
    console.log("submit cart");

    $.ajax("api/shopcart", {
        method: "POST",
        data: {"itemInfo": cartEvent, "option": "DELETE"},
        success: (resultData) => handleSessionData(resultData)

    });
}



/**
 * Handle the data returned by ShopCartServlet
 * @param resultDataString jsonObject, consists of session info
 */
function handleSessionData(resultDataString) {

    console.log("handle session response");




    // show cart information
    handleCartArray(resultDataString);
}



function handleCartArray(resultArray) {
    console.log(resultArray);
    let itemlisttable = $("#item_list_table_body").empty();
    itemlisttable.html("");

    for (let i = 0; i < resultArray["previousItems"].length; i++)
    {
        let shpvalue = resultArray["previousItems"][i];
        let rowHTML = "";
        rowHTML += "<tr>";
        // Add a link to single-star.html with id passed with GET url parameter
        rowHTML += "<td>" + resultArray["moviesTitle"][i] + "</td>";
        rowHTML += "<td>" + resultArray["numItems"][i] + "</td>";
        rowHTML += "<td>" + "10" + "</td>";
        rowHTML += "<td>";
        rowHTML += "<button onclick=addOneMore('" + shpvalue + "')" + ">+</button> " +
            "<button onclick=removeOneMore('" + shpvalue + "')" + ">-</button>";
        rowHTML += "</td>";
        rowHTML += "<td>";
        rowHTML += "<button onclick=removeitem('" + shpvalue + "')" + ">Remove</button> ";
        rowHTML += "</td>";


        rowHTML += "</tr>";
        itemlisttable.append(rowHTML);
    }
    console.log("Done populating table..")
}







$.ajax("api/shopcart", {
    method: "GET",
    success: (resultData) => handleSessionData(resultData)
});