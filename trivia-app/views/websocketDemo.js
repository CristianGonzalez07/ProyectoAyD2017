//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
webSocket.onmessage = function (msg) { update(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };

//Send a message if it's not empty, then clear the input field
function sendMessage() {
        webSocket.send(id("option").value);
}


//Update the chat-panel, and the list of connected users
function update(msg) {
    var data = JSON.parse(msg.data);
    id("question").innerHTML = "";
    id("options").innerHTML = "";
    text = "<button id='option' onclick="
    insert("question", "<h2>" + data.question + "</h2>");
    insert("options", text+"sendMessage()>" + data.option1 + "</button>");
    insert("options", text+"sendMessage()>" + data.option2 + "</button>");
    insert("options", text+"sendMessage()>" + data.option3 + "</button>");
    insert("options", text+"sendMessage()>" + data.option4 + "</button>");
    
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
