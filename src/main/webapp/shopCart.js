










/**
 * Handle the data returned by ShopCartServlet
 * @param resultDataString jsonObject, consists of session info
 */
function handleSessionData(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle session response");




    // show cart information
    handleCartArray(resultDataString);
}

/**
 * Handle the items in item list
 * @param resultArray jsonObject, needs to be parsed to html
 */
function handleCartArray(resultData) {
    console.log("handleSearchResult: populating shopping cart from resultData");
    let resultDataJson = JSON.parse(resultData);
    let item_list = $("#item_list");
    // change it to html list
    let res = "<ul>";
    for (let i = 0; i < resultDataJson["previousItems"].; i++) {
        // each item will be in a bullet point
        res += "<li>" + resultDataJson["previousItems"][i] + "</li>" + "aa";
    }
    res += "</ul>";

    // clear the old array and show the new array in the frontend
    item_list.html("");
    item_list.append(res);
}















$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});