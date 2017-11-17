//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");                                                                              
webSocket.onmessage = function (msg) { update(msg) };
webSocket.onclose = function () { alert("WebSocket connection closed") };
var rta = false;
var totalTiempo=30;

function update(msg) {
    var data = JSON.parse(msg.data);
    if(data.endGame!="true"){
        if(data.play=="yes"){
            if(data.results != ""){
                text = "<button class='btn-default' onclick="
                insert("results","<h2>" + data.results + "</h2>")
                insert("results", text+"sendBuild()>Continuar</button>");
            }else{
                rta = false;
                id("question").innerHTML = "";
                id("results").innerHTML = "";
                insert("question","<h2>" + data.question + "</h2>");

              
                document.getElementById("option1").style.display='inline';
                document.getElementById("option2").style.display='inline';
                document.getElementById("option3").style.display='inline';
                document.getElementById("option4").style.display='inline';

                document.getElementById("option1").value=data.option1;
                document.getElementById("option2").value=data.option2;
                document.getElementById("option3").value=data.option3;
                document.getElementById("option4").value=data.option4;
                totalTiempo=30;
                updateReloj();
            }
        }else{
            id("cuenta").innerHTML = "";
            id("question").innerHTML = "";
            id("results").innerHTML = "";

            document.getElementById("option1").style.display='none';
            document.getElementById("option2").style.display='none';
            document.getElementById("option3").style.display='none';
            document.getElementById("option4").style.display='none';
            insert("question","<h2>" + data.msgEspera + "</h2>")
        }
    }else{
        id("cuenta").innerHTML = "";
        id("question").innerHTML = "";
        id("results").innerHTML = "";
        document.getElementById("option1").style.display='none';
        document.getElementById("option2").style.display='none';
        document.getElementById("option3").style.display='none';
        document.getElementById("option4").style.display='none';
        if(data.winner!="empate"){
            insert("question","<h2>El Ganador es: " + data.winner + "</h2>");
        }else{
            insert("question","<h2>El resultado de la Partida fue Empate</h2>");
        }
        
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

function sendOption1() {
    rta = true;
    webSocket.send(id("option1").value);
}

function sendOption2() {
    rta = true;
    webSocket.send(id("option2").value);
}

function sendOption3() {
    rta = true;
    webSocket.send(id("option3").value);
}

function sendOption4() {
    rta = true;
    webSocket.send(id("option4").value);
}
function sendBuild() {
    rta = true;
    webSocket.send("build");
}
function updateReloj(){
        document.getElementById('cuenta').innerHTML = "Tiempo Restante "+totalTiempo+" segundos";
        if(rta==false){   
            if(totalTiempo==0){
                webSocket.send("");    
            }else{
                /* Restamos un segundo al tiempo restante */
                totalTiempo-=1;
                /* Ejecutamos nuevamente la función al pasar 1000 milisegundos (1 segundo) */
                setTimeout("updateReloj()",1000);
            } 
        }else{
            document.getElementById('cuenta').innerHTML = "";
        }
}
            