//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");                                                                              
webSocket.onmessage = function (msg) { update(msg) };
webSocket.onclose = function () { alert("WebSocket connection closed") };


function sendOption() {
        webSocket.send(id("option").value);
}

function sendBuild() {
        webSocket.send("build");
}

//Update the chat-panel, and the list of connected users
function update(msg) {
    var data = JSON.parse(msg.data);

    if(data.results != ""){
        insert("results","<h2>" + data.results + "</h2>")
        insert("results", "<input value = Continuar type='button' class='btn-default' onclick= sendBuild()/>");
    }else{
        id("question").innerHTML = "";
        id("options").innerHTML = "";
        id("results").innerHTML = "";
        var text = "<input type='button' id = 'option'  class='btn-default' ";
        var option1 = new String(data.option1);
        var option2 = new String(data.option2);
        var option3 = new String(data.option3);
        var option4 = new String(data.option4);

        insert("question", "<h2>" + data.question + "</h2>");
        insert("options", text+"value= " + option1 + " onclick= sendOption()/>");
        insert("options", text+"value= " + option2 + " onclick= sendOption()/>");
        insert("options", text+"value= " + option3 + " onclick= sendOption()/>");
        insert("options", text+"value= " + option4 + " onclick= sendOption()/>");
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
