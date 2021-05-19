function browse_alpha()
{
   let p = document.getElementById("alpha_list");
   window.location.replace("movielist.html?viewType=browseAlphaView&starts_with="+p.value);
}

function browse_numeric()
{
    let p = document.getElementById("numeric_list");
    window.location.replace("movielist.html?viewType=browseAlphaView&starts_with="+p.value);
}
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