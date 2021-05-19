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


$.ajax("api/shopcart", {
    method: "GET",
    success: handleSessionData
});