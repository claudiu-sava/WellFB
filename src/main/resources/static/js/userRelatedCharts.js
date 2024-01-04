$(document).ready(() =>{

    const usersCreationFrequencyLineChart = document.getElementById('usersCreationFrequency');
    //const postFrequencyLineChartLabels = Utils.months({count: 2});
    fetch('/admin/api/userFrequency', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(data => {
            var resposnse1 = JSON.parse(data);
            new Chart(usersCreationFrequencyLineChart, {
                type: 'line',
                data: {
                    labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                    datasets: [{
                        labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                        data: [resposnse1.jan, resposnse1.feb, resposnse1.mar, resposnse1.apr, resposnse1.may, resposnse1.jun, resposnse1.jul, resposnse1.aug, resposnse1.sep, resposnse1.oct, resposnse1.nov, resposnse1.dec],
                        borderWidth: 1,
                        fill: false,
                        backgroundColor: 'rgb(1, 182, 255)'
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });


    var adminCountPieChart = document.getElementById('adminCounterPie');

    fetch('/admin/api/adminCounter', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(data => {
            var resposnse1 = JSON.parse(data);
            new Chart(adminCountPieChart, {
                type: 'pie',
                data: {
                    labels: ['Users', "Admins"],
                    datasets: [{
                        labels: ['Users', "Admins"],
                        data: [resposnse1.totalUsers, resposnse1.totalAdmins],
                        borderWidth: 1,
                        backgroundColor: ['rgb(1, 182, 255)', 'rgb(255, 0, 0)']
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    responsive: true
                }
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });


    var allUsersPieChart = document.getElementById('allUsersPie');

    fetch('/admin/api/getAllUsers', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(data => {
            var resposnse1 = JSON.parse(data);
            new Chart(allUsersPieChart, {
                type: 'pie',
                data: {
                    labels: ['Online', "Total"],
                    datasets: [{
                        labels: ['Online', "Total"],
                        data: [resposnse1.onlineUsers, resposnse1.totalUsers],
                        borderWidth: 1,
                        backgroundColor: ['rgb(90,92,94)', 'rgb(236,215,215)']
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    responsive: true
                }
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });

});
