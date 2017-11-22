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
            document.getElementById("option1").style.backgroundColor='#fff';
            document.getElementById("option2").style.backgroundColor='#fff';
            document.getElementById("option3").style.backgroundColor='#fff';
            document.getElementById("option4").style.backgroundColor='#fff';
            if(data.results != ""){
                updateButton("option1",data.answer);
                updateButton("option2",data.answer);
                updateButton("option3",data.answer);
                updateButton("option4",data.answer);
                document.getElementById("cuenta").classList.remove("container");
                document.getElementById("results").classList.add("container");
                document.getElementById("options").classList.add("container");
                document.getElementById("question").classList.add("container");
                text = "<button class='btn-default' onclick="
                insert("results","<h2>" + data.results + "</h2>")
                insert("results", text+"sendBuild()>Continuar</button>");
            }else{
            	document.getElementById('option1').disabled=false;
    			document.getElementById('option2').disabled=false;
    			document.getElementById('option3').disabled=false;
    			document.getElementById('option4').disabled=false;
                rta = false;
                id("question").innerHTML = "";
                id("results").innerHTML = "";
                document.getElementById("cuenta").classList.add("container");
                document.getElementById("results").classList.remove("container");
                document.getElementById("options").classList.add("container");
                document.getElementById("question").classList.add("container");
                insert("question","<h2>" + data.question + "</h2>");

              
                document.getElementById("option1").style.display='inline';
                document.getElementById("option2").style.display='inline';
                document.getElementById("option3").style.display='inline';
                document.getElementById("option4").style.display='inline';

                document.getElementById("option1").value=data.option1;
                document.getElementById("option2").value=data.option2;
                document.getElementById("option3").value=data.option3;
                document.getElementById("option4").value=data.option4;
                totalTiempo=15;
                updateReloj();
            }
        }else{
            id("cuenta").innerHTML = "";
            id("question").innerHTML = "";
            id("results").innerHTML = "";
                
            document.getElementById("cuenta").classList.remove("container");
            document.getElementById("results").classList.remove("container");
            document.getElementById("options").classList.remove("container");
            document.getElementById("question").classList.add("container");

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
        document.getElementById("cuenta").classList.remove("container");
        document.getElementById("results").classList.remove("container");
        document.getElementById("options").classList.remove("container");
        document.getElementById("question").classList.add("container");
        document.getElementById("option1").style.display='none';
        document.getElementById("option2").style.display='none';
        document.getElementById("option3").style.display='none';
        document.getElementById("option4").style.display='none';
        if(data.typeOfGame == "1PLAYER"){
            insert("question","<h2>Partida terminada. El Puntaje es: " + data.scorep1 + "</h2>");
        }else{
            if(data.winner!="empate"){
                insert("question","<h2>El Ganador es: " + data.winner + "Por "+data.scorep1+" a "+data.scorep2+" </h2>");
            }else{
                insert("question","<h2>El resultado de la Partida fue Empate "+data.scorep1+" a "+data.scorep2+"</h2>");
            }   
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
    document.getElementById('option1').disabled=true;
    document.getElementById('option2').disabled=true;
    document.getElementById('option3').disabled=true;
    document.getElementById('option4').disabled=true;
    webSocket.send(id("option1").value);
}

function sendOption2() {
    rta = true;
    document.getElementById('option1').disabled=true;
    document.getElementById('option2').disabled=true;
    document.getElementById('option3').disabled=true;
    document.getElementById('option4').disabled=true;
    webSocket.send(id("option2").value);
}

function sendOption3() {
    rta = true;
    document.getElementById('option1').disabled=true;
    document.getElementById('option2').disabled=true;
    document.getElementById('option3').disabled=true;
    document.getElementById('option4').disabled=true;
    webSocket.send(id("option3").value);
}

function sendOption4() {
    rta = true;
    document.getElementById('option1').disabled=true;
    document.getElementById('option2').disabled=true;
    document.getElementById('option3').disabled=true;
    document.getElementById('option4').disabled=true;
    webSocket.send(id("option4").value);
}
function sendBuild() {
    rta = true;
    document.getElementById('option1').disabled=false;
    document.getElementById('option2').disabled=false;
    document.getElementById('option3').disabled=false;
    document.getElementById('option4').disabled=false;
    webSocket.send("build");
}
function updateReloj(){
        document.getElementById('cuenta').innerHTML = "Tiempo Restante "+totalTiempo+" segundos";
        if(rta==false){
         document.getElementById('Salir').disabled=true;  
            if(totalTiempo==0){
            	document.getElementById('option1').disabled=true;
    			document.getElementById('option2').disabled=true;
    			document.getElementById('option3').disabled=true;
    			document.getElementById('option4').disabled=true;
                webSocket.send(""); 
                document.getElementById('Salir').disabled=false;   
            }else{
                /* Restamos un segundo al tiempo restante */
                totalTiempo-=1;
                /* Ejecutamos nuevamente la función al pasar 1000 milisegundos (1 segundo) */
                setTimeout("updateReloj()",1000);
            } 
        }else{
            document.getElementById('cuenta').innerHTML = "";
            document.getElementById('Salir').disabled=false;
        }
}

function updateButton(id,answer){
    if(document.getElementById(id).value == answer){
        document.getElementById(id).style.backgroundColor= '#58FA58';
    }else{
        document.getElementById(id).style.backgroundColor= '#FE2E2E';
    }
}
            