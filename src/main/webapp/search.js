
function search()
{
    submitSearchForm();
}

function submitSearchForm() {
    let param = "?viewType=searchView";

    if(document.getElementsByTagName("input")[0].value)
    {
        param += "&title=" + document.getElementsByTagName("input")[0].value;
    }
    if(document.getElementsByTagName("input")[1].value)
    {
        param += "&year=" + document.getElementsByTagName("input")[1].value;

    }
    if(document.getElementsByTagName("input")[2].value)
    {
        param += "&director=" + document.getElementsByTagName("input")[2].value;
    }
    if(document.getElementsByTagName("input")[3].value)
    {
        param += "&star=" + document.getElementsByTagName("input")[3].value;
    }

    console.log("confirmed parameters");
    console.log(param);
    window.location.replace("movielist.html"+param);

}