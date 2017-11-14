//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");                                                                              
webSocket.onmessage = function (msg) { update(msg) };
webSocket.onclose = function () { alert("WebSocket connection closed") };

function sendOption1() {
        webSocket.send(id("option1").value);
}

function sendOption2() {
        webSocket.send(id("option2").value);
}
function sendOption3() {
        webSocket.send(id("option3").value);
}
function sendOption4() {
        webSocket.send(id("option4").value);
}
function sendBuild() {
        webSocket.send("build");
}

//Update the chat-panel, and the list of connected users
function update(msg) {
    var data = JSON.parse(msg.data);
    if(data.play=="yes"){
        if(data.results != ""){
        text = "<button class='btn-default' onclick="
        insert("results","<h2>" + data.results + "</h2>")
        insert("results", text+"sendBuild()>Continuar</button>");
        }else{
            id("question").innerHTML = "";
            id("results").innerHTML = "";
            insert("question","<h2>" + data.question + "</h2>");
            document.getElementById("option1").value=data.option1;
            document.getElementById("option2").value=data.option2;
            document.getElementById("option3").value=data.option3;
            document.getElementById("option4").value=data.option4;
        }
    }else{
        id("question").innerHTML = "";
        id("results").innerHTML = "";
        insert("question","<h2>" + data.msgEspera + "</h2>")
    }
    
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}

//Helper function for selecting element by name
function name(name) {
    return document.getElementsByName(name);
}
