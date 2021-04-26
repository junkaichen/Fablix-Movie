










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
        let rowHTML = "";
        rowHTML += "<tr>";
        // Add a link to single-star.html with id passed with GET url parameter
        rowHTML += "<td>" + resultArray["previousItems"][i] + "</td>";
        rowHTML += "<td>" + resultArray["numItems"][i] + "</td>";
        rowHTML += "<td>" + "10" + "</td>";
        rowHTML += "<td>";
        rowHTML += "<button onclick=addOneMore('" + i + "')" + ">ADD</button> " +
            "<button onclick=addOneMore('" + i + "')" + ">ADD</button>";
        rowHTML += "</td>";
        rowHTML += "<td>";
        rowHTML += "<button onclick=addOneMore('" + i + "')" + ">ADD</button> ";
        rowHTML += "</td>";


        rowHTML += "</tr>";
        itemlisttable.append(rowHTML);
    }
    console.log("Done populating table..")
}






    // let item_list = $("#item_list");
    // // change it to html list
    // let res = "<ul>";
    // for (let i = 0; i < resultArray["previousItems"].length; i++) {
    //     // each item will be in a bullet point
    //     res += "<li>" + resultArray["previousItems"][i] + "</li>";
    //     res += "<li>" + resultArray["moviesTitle"][i] + "</li>";
    //     res += "<li>" + resultArray["NumOfItems"][i] + "</li>";
    // }
    // res += "</ul>";
    //
    // // clear the old array and show the new array in the frontend
    // item_list.html("");
    // item_list.append(res);















$.ajax("api/shopcart", {
    method: "GET",
    success: (resultData) => handleSessionData(resultData)
});