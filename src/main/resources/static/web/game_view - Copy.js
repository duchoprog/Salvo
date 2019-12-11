//fetch ("/api/login?name=j.bauer@ctu.gov&pwd=24",{method:"POST"})


//URLSearchParams
var rivalesHundidosArray = []
var yo = "w"
var miRival = 0
var gameData = {}
let misShipLoc = []
var dataSalvos = {}
var misHundidos = ['None!']
var misAciertos = []
var miGP = 0
var yaDispare = 0
var yaRecibi = 0
document.getElementById("botEnviar").classList.remove("shown")
document.getElementById("botEnviar").classList.add("hidden")

function apiGameView() {

    params = new URLSearchParams(location.search).get('gp')
    miGP = params

    let url = "/api/game_view/" + params

    fetch(url)
        .then(response => {
            if (response.status === 200) {
                console.log(response.status)
                return response.json();
            } else {
                console.log("else");
                //throw new Error(response)
                return Promise.reject(response.text())
            }
        })
        .then(function (json) {
            console.log(json.yo)
            gameData = json
            yo = json.yo
            if (termino()) {
                terminar()
            }
            json.misAciertos.forEach(cadaAcierto => {
                misAciertos.push(cadaAcierto.acertado)
                //          console.log("Mis aciertos array: ", misAciertos)
            })
            document.querySelector("#display p").innerText += yo
            let rival = ""
            let misBarcos = []
            document.getElementById("titulo").innerHTML = `Acá mando yo, ${yo}`
            if (json["Mi juego"].gamePlayer[0].player.fullName != yo) {
                rival = json["Mi juego"].gamePlayer[0].player.fullName
                miRival = json["Mi juego"].gamePlayer[0].gPid
            } else {
                if (json["Mi juego"].gamePlayer[1]) {
                    rival = json["Mi juego"].gamePlayer[1].player.fullName
                    miRival = json["Mi juego"].gamePlayer[1].gPid
                } else {
                    rival = "...nadie se atrevió aún"
                }

            }
            document.getElementById("versus").innerHTML = `Hoy la batalla es contra ${rival}`

            hundir(json)
            if (json["Mis barcos"].length == 0) {

                //barcos al dock
                createShips('carrier', 5, 'horizontal', document.getElementById('dock'), false)
                createShips('battleship', 4, 'horizontal', document.getElementById('dock'), false)
                createShips('submarine', 3, 'horizontal', document.getElementById('dock'), false)
                createShips('destroyer', 3, 'horizontal', document.getElementById('dock'), false)
                createShips('patrol_boat', 2, 'horizontal', document.getElementById('dock'), false)


            } else {
                for (b = 0; b < json["Mis barcos"].length; b++) {
                    let direccion = ""
                    temp = json["Mis barcos"][b].shipLocs
                    misShipLoc = [...misShipLoc, ...temp]

                    if (json["Mis barcos"][b].shipLocs[0][0] != json["Mis barcos"][b].shipLocs[1][0]) {
                        direccion = "vertical"
                    } else { direccion = "horizontal" }

                    misBarcos.push(json["Mis barcos"][b].shipType)
                    createShips(json["Mis barcos"][b].shipType, json["Mis barcos"][b].shipLocs.length, direccion, document.getElementById("ships" + json["Mis barcos"][b].shipLocs[0]), true)

                }
                if (gameData.flotaRival != 0 && yaDispare <= yaRecibi) {
                    document.getElementById("botDisparar").classList.remove("hidden")
                    document.getElementById("botDisparar").classList.add("shown")
                    document.getElementById("esperandoDisp").innerHTML = "Waiting for rival fleet to show up"
                    document.getElementById("esperandoDisp").classList.remove("hidden")
                    document.getElementById("esperandoDisp").classList.add("shown")
                }
            }

            checkSalvo(gameData)

            armarTablaEstado(gameData, params)
            //document.getElementById("shipsB2").appendChild(hit)

        })
    /*  .catch(error => {

         console.log(error)

         return error
     }) */
    /*  .then(
         error2 => {
             if (error2) {
                 document.querySelector("body").innerHTML = `<h1>${error2}</h1> <br> <button onclick="volverAGames()"> Camine a cucha!</button>`
                 console.log(error2)
             }
         }
     ) */


}
apiGameView()
function volverAGames() {
    window.location.href = "games.html"
}
function checkSalvo(json) {
    dataSalvos.rival = {}
    dataSalvos.yo = {}
    var salvosRiv = 0
    var salvosMios = 0
    for (s = 0; s < json.Salvoes.length; s++) {
        if (json.Salvoes[s].GamePlayer == miRival) {
            recibirTiro(json.Salvoes[s].salvoLocs, json)
            turno = json.Salvoes[s].salvoTurno
            cuantosTiros = json.Salvoes[s].salvoLocs.length
            dataSalvos.rival[turno] = { "quien": "rival", "cuantosTiros": cuantosTiros }
            salvosRiv++


        } else {
            turno = json.Salvoes[s].salvoTurno
            cuantosTiros = json.Salvoes[s].salvoLocs.length
            dataSalvos.yo[turno] = {
                "quien": "yo", "cuantosTiros": cuantosTiros
            }
            salvosMios++
            yaDispare = salvosMios
            yaRecibi = salvosRiv
            tirar(json.Salvoes[s].salvoLocs)

        }
    }



}

function recibirTiro(tiros, gameData) {

    //console.log("recibir")
    let tirosUnicos = Array.from(new Set(tiros))
    ///recorro mis barcos
    gameData["Mis barcos"].forEach(cadaBarco => {
        let hitCount = 0
        let barcoSize = cadaBarco.shipLocs.length
        //console.log("defino barco size:", cadaBarco.shipType, barcoSize)
        cadaBarco.shipLocs.forEach(cadaLoc => {
            if (tirosUnicos.indexOf(cadaLoc) > -1) {
                hitCount++
                quemar(cadaLoc, 'ships')
                // console.log(cadaBarco.shipType, "hitcount:", hitCount, "barco size: ", barcoSize)
            }

        })
        if (gameData.misHundidos) {
            misHundidos = misHundidos.filter(function (e) { return e == 'culo' })
            Object.keys(gameData.misHundidos).forEach(hundido => {
                misHundidos.push(hundido)

            });
            //console.log(" Mis hundidos: ", misHundidos)

        }
    })
}

function tirar(tiros) {
    for (t = 0; t < tiros.length; t++) {
        bombear(tiros[t], "misTiros")

    }
    console.log("ya dispare: ", yaDispare, " ya recibi: ", yaRecibi)
    if (yaDispare > yaRecibi) {
        document.getElementById("esperandoDisp").classList.remove("hidden")
        document.getElementById("esperandoDisp").classList.add("shown")

        document.getElementById("botDisparar").classList.remove("shown")
        document.getElementById("botDisparar").classList.add("hidden")
    }
}


function bombear(cell, grilla) {
    let hit = document.createElement('img')
    //  console.log ( Object.values(gameData.misAciertos), "mis aciertos ", cell )
    if (misAciertos.indexOf(cell) > -1) {
        hit.src = `assets/fire2.gif`
    } else {
        hit.src = `assets/smoke3.png`
    }

    hit.classList.add("fuoco")

    document.getElementById(grilla + cell).appendChild(hit)

}


function quemar(cell, grilla) {
    let hit = document.createElement('img')
    hit.src = `assets/fire2.gif`
    hit.classList.add("fuoco")

    document.getElementById(grilla + cell).appendChild(hit)
}


function enviarBarco(array, gp) {
    gp = new URLSearchParams(location.search).get('gp')
    let misBarcos = []

    let paqueton = []

    let barcos = document.querySelectorAll('.grid-item')
    console.log(barcos)
    for (b = 0; b < barcos.length; b++) {
        let cada = {}
        let cells = []
        cada.shipType = barcos[b].id
        let celdas = document.querySelectorAll(`.${barcos[b].id}-busy-cell`)
        cada.shipSize = celdas.length
        for (c = 0; c < celdas.length; c++) {
            cells.push(celdas[c].dataset.y + celdas[c].dataset.x)
        }
        cada.shipLoc = cells
        //console.log(cada)
        paqueton.push(cada)
    }

    //    console.log(JSON.stringify(paqueton))
    /*  console.log(JSON.stringify([{ shipType: "destroyer", shipSize: 3, shipLoc: ["A1", "A2", "A3"] }
     ]))
  */
    fetch(`/api/games/players/${gp}/ships`, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paqueton)
    }

    )
        .then(r => {
            if (r.ok) {
                document.getElementById("botEnviar").classList.add("hidden")
                document.getElementById("botEnviar").classList.remove("shown")

                //createGrid(11, document.getElementById('grid'), 'ships', "NAVES")
                console.log(r)
                ////
                console.log("barcos deployados")
            }
            return r.json()
        }
        ).then(r => {
            console.log(r.data)
        })
    //   location.reload(true)
    if (gameData.flotaRival > 0 && yaDispare <= yaRecibi) {


        document.getElementById("botDisparar").innerHTML = "CULO"
        document.getElementById("botDisparar").classList.remove("hidden")
        document.getElementById("botDisparar").classList.add("shown")

        document.getElementById("esperandoDisp").classList.remove("shown")
        document.getElementById("esperandoDisp").classList.add("hidden")



    } else {
        document.getElementById("esperandoDisp").innerHTML = "Waiting for rival fleet to show up"
        document.getElementById("esperandoDisp").classList.remove("hidden")
        document.getElementById("esperandoDisp").classList.add("shown")
    }

}

function display(mensaje) {
    document.querySelector("#display>p").innerHTML = mensaje
}

/*
fetch("/echo/json/",
{
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    method: "POST",
    body: JSON.stringify({a: 1, b: 2})
})
.then(function(res){ console.log(res) })
.catch(function(res){ console.log(res) }) */



function armarTablaEstado(gameData, params) {
    if (termino()) {
        console.log("TERMINO!")
        terminar()
    }
    gameData.Salvoes.forEach(element => {
        params = Number(params)
        //        console.log(element.GamePlayer, params)
        if (element.GamePlayer == params) {
            //yaDispare++
        } else {
            //la data de mi rival
            //yaRecibi++
        }
        if (yaDispare <= yaRecibi) {
            document.getElementById("botDisparar").classList.remove("hidden")
            document.getElementById("botDisparar").classList.add("shown")
            document.getElementById("esperandoDisp").classList.remove("shown")
            document.getElementById("esperandoDisp").classList.add("hidden")
        } else {
            document.getElementById("botDisparar").classList.remove("shown")
            document.getElementById("botDisparar").classList.add("hidden")
            document.getElementById("esperandoDisp").classList.remove("hidden")
            document.getElementById("esperandoDisp").classList.add("shown")
            document.getElementById("esperandoDisp").innerHTML = "Waiting for enemy to fire"
        }

        document.getElementById("salvoes").innerHTML = `<td> I have fired <b>${yaDispare}</b> salvoes</td><td> I have received <b>${yaRecibi}</b> salvoes</td>`
    }

    );

    if (gameData.rivalesHundidos && Object.keys(gameData.rivalesHundidos).length > 0) {
        rivalesHundidosArray = Object.keys(gameData.rivalesHundidos)
    }
    else { rivalesHundidosArray = [] }
    //  console.log("rivales hundidos", rivalesHundidosArray)
    miosLeft = gameData["Mis barcos"].length - Object.keys(gameData.misHundidos).length
    rivalLeft = gameData.flotaRival - Object.keys(gameData.rivalesHundidos).length
    document.getElementById("hundidos").innerHTML = `<td>This enemy vessels already destroyed: ${rivalesHundidosArray.join(", ")}. 
    Enemy vessels left: ${rivalLeft}</td><td>This vessels in my fleet already destroyed: ${misHundidos.join(", ")}. 
    Own vessels left:${miosLeft}</td>`
}

function hundir(gameData) {
    let locs = []
    if (gameData.rivalesHundidos && gameData.rivalesHundidos.length > 0) { //console.log(Object.values(gameData.rivalesHundidos)[0], "cuantos hundi", Object.keys(gameData.rivalesHundidos).length)
        for (h = 0; h < Object.keys(gameData.rivalesHundidos).length; h++) {
            locs = locs.concat(Object.values(gameData.rivalesHundidos)[h])
            console.log(locs)

        }

        for (t = 0; t < locs.length; t++) {
            document.getElementById("misTiros" + locs[t]).classList.add("hundido")


        }
    }
}
function termino() {//
    console.log(gameData.Estado)
    if (gameData.Estado == "perdi" || gameData.Estado == "empate" || gameData.Estado == "gane") {
        return true
    }
    return false
}

function terminar() {
    var botones = document.querySelectorAll("button.shown")
    for (b = 0; b < botones; b++) {
        if (botones[b].id != "botVolver") {
            botones[b].classList.remove("shown")
            botones[b].classList.add("hidden")
        }
    }
    document.querySelector("h3").innerHTML = ""
    function bombazo() {
        console.log("basta papi, se acabo")
    }
}
