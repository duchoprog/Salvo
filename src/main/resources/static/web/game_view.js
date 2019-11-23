//fetch ("/api/login?name=j.bauer@ctu.gov&pwd=24",{method:"POST"})


//URLSearchParams
var yo = "w"
var miRival = 0
let gameData = {}
let misShipLoc = []
var dataSalvos = {}
document.getElementById("botEnviar").classList.remove("shown")
document.getElementById("botEnviar").classList.add("hidden")

function apiGameView() {
    params = new URLSearchParams(location.search).get('gp')

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
            console.log(miRival)
            document.getElementById("versus").innerHTML = `Hoy la batalla es contra ${rival}`

            tablaSalvos(json)

            if (json["Mis barcos"].length == 0) {

                //barcos al dock
                createShips('carrier', 5, 'horizontal', document.getElementById('dock'), false)
                createShips('battleship', 4, 'horizontal', document.getElementById('dock'), false)
                createShips('submarine', 3, 'horizontal', document.getElementById('dock'), false)
                createShips('destroyer', 3, 'horizontal', document.getElementById('dock'), false)
                createShips('patrol_boat', 2, 'horizontal', document.getElementById('dock'), false)
            } else {
                console.log("else")
                for (b = 0; b < json["Mis barcos"].length; b++) {
                    let direccion = ""
                    temp = json["Mis barcos"][b].shipLocs
                    //misShipLoc.concat(temp)
                    misShipLoc = [...misShipLoc, ...temp]

                    if (json["Mis barcos"][b].shipLocs[0][0] != json["Mis barcos"][b].shipLocs[1][0]) {
                        direccion = "vertical"
                    } else { direccion = "horizontal" }

                    misBarcos.push(json["Mis barcos"][b].shipType)
                    createShips(json["Mis barcos"][b].shipType, json["Mis barcos"][b].shipLocs.length, direccion, document.getElementById("ships" + json["Mis barcos"][b].shipLocs[0]), true)

                }
            }

            checkSalvo(gameData)
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
    for (s = 0; s < json.Salvoes.length; s++) {
        if (json.Salvoes[s].GamePlayer == miRival) {
            recibirTiro(json.Salvoes[s].salvoLocs)
            turno = json.Salvoes[s].salvoTurno
            cuantosTiros = json.Salvoes[s].salvoLocs.length
            dataSalvos.rival[turno] = { "quien": "rival", "cuantosTiros": cuantosTiros }


        } else {
            turno = json.Salvoes[s].salvoTurno
            cuantosTiros = json.Salvoes[s].salvoLocs.length
            dataSalvos.yo[turno] = { "quien": "yo", "cuantosTiros": cuantosTiros }

            tirar(json.Salvoes[s].salvoLocs)

        }
    }
    console.log(dataSalvos)
}

function recibirTiro(tiros) {
    for (t = 0; t < tiros.length; t++) {
        if (misShipLoc.indexOf(tiros[t]) > -1) {
            quemar(tiros[t], "ships")
        }
    }
}

/* function disparar(locs) {
    //   console.log(locs)
}
 */
function tirar(tiros) {
    for (t = 0; t < tiros.length; t++) {
        if ("q") {
            quemar(tiros[t], "misTiros")
        }
    }
}


function quemar(cell, grilla) {
    let hit = document.createElement('img')
    hit.src = `assets/fire2.gif`
    hit.classList.add("fuoco")

    document.getElementById(grilla + cell).appendChild(hit)
}

function enviarBarco(array, gp) {
    gp = new URLSearchParams(location.search).get('gp')

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

    console.log(JSON.stringify(paqueton))
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


                console.log("barcos deployados")
            }
            return r.json()
        }

        ).then(r => {
            console.log(r.data)
        })
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

function tablaSalvos(json) {

}




