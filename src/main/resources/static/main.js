function httpGet(theUrl) {
    let xmlHttpReq = new XMLHttpRequest();
    xmlHttpReq.open("GET", theUrl, false); 
    xmlHttpReq.send(null);
    return xmlHttpReq.responseText;
}

let baseURL = "http://localhost:8080/run?q=";
var outputarea = document.getElementById('output');
var inputarea = document.getElementById('input');
document.getElementById("button").onclick = compile;

function compile() {
    try {
        outputarea.value = httpGet(baseURL + encodeURIComponent(inputarea.value));
        outputarea.style.visibility = "visible";
    } catch (error) {
        if (error.name = "NetworkError") {
            outputarea.value = "Network Error";
        } else {
            outputarea.value = "Error";
        }
        outputarea.style.visibility = "visible";
    }
}