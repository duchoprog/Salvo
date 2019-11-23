
document.getElementById("add_player").addEventListener("click", e => {
    addPlayer()
})

function addPlayer() {


    fetch('/players?email=sfhlkfdjdslkjf&lastName=Drakoulias&firstName= George', {
        method: 'POST',
        dataType: "text",
        headers: new Headers({
            contentType: 'application/json'
        })
    }).then(function (response) {
        if (response.ok) {
            alert('Appointment saved');
        }
        throw new Error(response.statusText);
    }).catch(function (error) {
        alert('Appointment not saved: ' + error.message);
    });

}

