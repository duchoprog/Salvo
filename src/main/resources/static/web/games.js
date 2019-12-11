
var logueado = ""
let reingreso = null
window.onload = function () {
    fetch(`/api/logueado`, { method: "POST" }).then(r => { return r.text() }).
        then(text => {
            logueado = text

            if (logueado != "guest") {
                document.getElementById("loginForm").classList.remove("shown")
                document.getElementById("loginForm").classList.add("hidden")
                document.getElementById("logoutForm").classList.remove("hidden")
                document.getElementById("logoutForm").classList.add("shown")

                document.getElementById("nuevo").classList.remove("hidden")
                document.getElementById("nuevo").classList.add("shown")
                document.querySelector("div > p").innerHTML = sessionStorage.getItem("quien")
            }
        })


    /*         document.querySelector("input").value = sessionStorage.getItem("quien")
        document.getElementById("nuevo").classList.remove("shown")
        document.getElementById("nuevo").classList.add("hidden")
        console.log(document.getElementById("nuevo").classList)
        if (sessionStorage.getItem("quien")) {
            console.log("ya hay alguien!!!!!")
            document.getElementById("loginForm").classList.remove("shown")
            document.getElementById("loginForm").classList.add("hidden")
            document.getElementById("logoutForm").classList.remove("hidden")
            document.getElementById("logoutForm").classList.add("shown")

            console.log(document.getElementById("nuevo").classList)
            document.getElementById("nuevo").classList.remove("hidden")
            document.getElementById("nuevo").classList.add("shown")
            console.log(document.getElementById("nuevo").classList)
            document.querySelector("div > p").innerHTML = sessionStorage.getItem("quien") */
}
    ;



function leaderboard(url, options) {
    fetch(url, options)
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                alert(response.statusText);
            }
        })
        .then(function (json) {
            let nombres = []
            let tabla = []
            let cada = {}
            for (l = 0; l < json.leader.length; l++) {
                //nombres nuevos
                if (nombres.indexOf(json.leader[l].player) == -1) {
                    cada = {}
                    nombres.push(json.leader[l].player)
                    cada.name = (json.leader[l].player)

                    switch (json.leader[l].puntos) {
                        case 0:
                            cada.win = 0
                            cada.tie = 0
                            cada.loss = 1

                            break
                        case 0.5:
                            cada.win = 0
                            cada.tie = 1
                            cada.loss = 0
                            break
                        case 1:
                            cada.win = 1
                            cada.tie = 0
                            cada.loss = 0
                            break
                        default:
                            cada.win = 0
                            cada.tie = 0
                            cada.loss = 0
                    }
                    if (json.leader[l].puntos != null) {
                        cada.puntos = (json.leader[l].puntos)


                    }
                    else { cada.puntos = 0 }
                    tabla.push(cada)

                }
                else {
                    ///nombres usados
                    if (json.leader[l].puntos != null) {
                        tabla[nombres.indexOf(json.leader[l].player)].puntos += json.leader[l].puntos
                        switch (json.leader[l].puntos) {
                            case 0:

                                tabla[nombres.indexOf(json.leader[l].player)].loss += 1
                                break
                            case 0.5:
                                tabla[nombres.indexOf(json.leader[l].player)].tie += 1

                                break
                            case 1:
                                tabla[nombres.indexOf(json.leader[l].player)].win += 1

                                break
                        }
                    }
                }


            }
            console.log(tabla)
            dibujarTabla(tabla)
            //console.log(tabla[1])
            //console.log(nombres)

        })
}
function crearJuego() {

    if (logueado != "guest") {
        fetch(`/api/newGame`, { method: "POST" }).then(r => {
            //(console.log("nuevo juego"))
            return r.json()
        })
            .then(json => {
                console.log(json)
                window.location.assign(`game_view.html?gp=${json.data}`)
            })
    } else {
        display("You need to be logged in to create a game")
    }

}

function dibujarTabla(t) {
    let posiciones = `<thead><tr><th> JUGADOR</th><th> J</th><th> G</th><th> E</th><th> P</th><th>PUNTOS</th></tr></thead>`

    t.sort(function (b, a) {
        return a.puntos - b.puntos;
    })
    for (p = 0; p < t.length; p++) {
        posiciones += `<tr><td>${t[p].name}</td><td> ${t[p].win + t[p].tie + t[p].loss}</td><td>${t[p].win}</td><td>${t[p].tie}</td><td>${t[p].loss}</td><td>${t[p].puntos}</tr>`
    }

    let tabPos = document.createElement("table")
    tabPos.setAttribute("width", "90%")
    tabPos.innerHTML = posiciones
    document.body.appendChild(tabPos)
}


function leerJSON(url, options) {
    fetch(url, options)
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                alert(response.statusText);
            }
        })
        .then(json => armarTabla(json))
}

function armarTabla(json) {

    fetch(`/api/logueado`, { method: "POST" }).then(r => { return r.text() }).
        then(text => {
            logueado = text
            if (sessionStorage.getItem("quien")) {
                display(`Logueado como ${logueado}`)
            }
            console.log(logueado)
            let lista = `<tr>
            <thead>
                <th >Juego</th>
                <th>Jugador 1</th>
                <th>Jugador 2</th>
            </thead>
        </tr>`
            let match = 0
            let local = ""
            let visitante = ""
            let fecha = ""
            let mails = []
            let mailsString = ""
            let nombres = []
            let btnReingreso = null
            x = json;
            for (let i = 0; i < x.length; i++) {
                match = x[i].gameId
                local = ` ${x[i].gamePlayer[0].player.fullName} ${x[i].gamePlayer[0].gPid}`
                if (x[i].gamePlayer[0].player.email == logueado && x[i].gamePlayer[1]) {
                    //console.log("game: ", match)
                    reingreso = x[i].gamePlayer[0].gPid
                    btnReingreso = `<button onclick="reingresar(${x[i].gamePlayer[0].gPid})"> ${x[i].gamePlayer[0].player.fullName}, volver a este juego</button>`

                } else if (x[i].gamePlayer[1] && x[i].gamePlayer[1].player.email == logueado) {
                    console.log("game: ", match)
                    console.log(x[i].gamePlayer[1])
                    reingreso = x[i].gamePlayer[1].gPid
                    btnReingreso = `<button onclick="reingresar(${x[i].gamePlayer[1].gPid})"> ${x[i].gamePlayer[1].player.fullName}, volver a este juego</button>`

                }

                else {

                    btnReingreso = `<p>    </p>`
                }
                if (x[i].gamePlayer[1]) {

                    visitante = `${x[i].gamePlayer[1].player.fullName} ${x[i].gamePlayer[1].gPid}`
                    mails.push(x[i].gamePlayer[1].player.email)
                    nombres.push(x[i].gamePlayer[1].player.fullName)

                }
                else if (x[i].gamePlayer[0].player.email != logueado && logueado != "guest") {
                    visitante = `<button onclick="sumarse(this.value)" value=${match}>Jugar con ${x[i].gamePlayer[0].player.fullName} !</button>`
                } else { visitante = `<p>Sin rival aún</p>` }
                nombres.push(x[i].gamePlayer[0].player.fullName)

                options = { weekday: 'long', year: 'numeric', month: "long", day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' }
                fecha = new Date(x[i].gameTime)
                fecha = fecha.toLocaleDateString("es-AR", options)
                lista += `<tr><td> ${match}</td><td> ${local} </td><td> ${visitante} </td><td>${btnReingreso}</td></tr>`
                //                console.log(x[i].gameId + " " + local + " " + visitante)


            }
            mails = [...new Set(mails)]
            mailsString = mails.join(", ")
            //lista += `<li> Emails: ${mailsString}`
            document.querySelector("#lista").innerHTML = lista
        })

}


function reingresar(gpId) {
    window.location.href = `game_view.html?gp=${gpId}`
}
function display(mensaje) {
    document.querySelector("#display>p").innerHTML = mensaje
}
function sumarse(d) {
    console.log(d)
    fetch(`/api/game/${d}/players`, { method: "POST" }).then(r => {
        //(console.log("nuevo juego"))
        return r.json()
    })
        .then(json => {
            console.log(json)
            leerJSON("/api/games")
            window.location.assign(`game_view.html?gp=${json.data}`)
        })

}
function signUp() {
    let email = document.getElementById("email").value
    let password = document.getElementById("password").value
    fetch(`/api/players?name=${email}&pwd=${password}`, { method: "POST" }).then(r => {
        if (!r.ok) {
            console.log(r)
            return Promise.reject(r.json())
        }
        return r.json()
    })
        .then(json => {
            //console.log(json.email)
            display(json.email)
        }).catch(error => {
            console.log(error)
            return error
        })
        .then(jsonerror => {
            if (jsonerror) {
                console.log(jsonerror.error)
                display(jsonerror.error)
            }
        })



    //console.log(`/api/players?name=${email}&pwd=${password}`)

}

function login() {

    let email = document.getElementById("email").value
    let password = document.getElementById("password").value
    fetch(`/api/login?name=${email}&pwd=${password}`, { method: "POST" }).then(r => {
        //console.log(r.ok)
        if (r.ok) {
            document.getElementById("email").value = "";
            document.getElementById("password").value = "";

        } else {
            display('HTTP error, todo mal, status = ' + r.status)
            throw new Error('HTTP error, todo mal, status = ' + r.status)
            //Promise.reject(r.json())  
        }
    }).then(r => {
        document.getElementById("loginForm").classList.remove("shown")
        document.getElementById("loginForm").classList.add("hidden")
        document.getElementById("logoutForm").classList.remove("hidden")
        document.getElementById("logoutForm").classList.add("shown")
        document.getElementById("nuevo").classList.remove("hidden")
        document.getElementById("nuevo").classList.add("shown")
        //document.querySelector("div > p").innerHTML = email
        logueado = email
        sessionStorage.setItem("quien", email)
        if (sessionStorage.quien) { display("LOGIN logueado como " + sessionStorage.getItem("quien")) }
        else {
            display("LOGIN No estas logueado")
        }

        leerJSON("/api/games")
    })

        .catch(error => console.log(error))

}


function logout() {
    fetch(`/api/logout`, { method: "POST" }).then(r => (console.log("nos vamos"))).then(r => {
        window.location.reload(true)
        sessionStorage.setItem("quien", "")
        document.getElementById("nuevo").classList.remove("shown")
        document.getElementById("nuevo").classList.add("hidden")

    })

}
leerJSON("/api/games")

leaderboard("/api/leaderboard")