const interval = setInterval(function() {
    console.log("HERE");

    fetch('/status', {
        method: 'GET',
    })
        .then(response => response.text())
        .catch(error => {
            console.error('Error:', error);
        });

}, 5000);