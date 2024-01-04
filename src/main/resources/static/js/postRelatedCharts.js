$(document).ready(() => {

    const allPostsBarChart = document.getElementById('allPostsBar');

    fetch('/admin/api/getAllPosts', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(data => {
            var resposnse1 = JSON.parse(data);
            new Chart(allPostsBarChart, {
                type: 'bar',
                data: {
                    labels: ['Total Posts', "Posts Without Comments", "Posts With no Likes"],
                    datasets: [{
                        label: 'Posts',
                        data: [resposnse1.totalPosts, resposnse1.postsWithoutComments, resposnse1.postsWithoutLikes],
                        borderWidth: 1
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


    const postFrequencyLineChart = document.getElementById('allPostsFrequency');
    //const postFrequencyLineChartLabels = Utils.months({count: 2});
    fetch('/admin/api/postFrequency', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(data => {
            var resposnse1 = JSON.parse(data);
            new Chart(postFrequencyLineChart, {
                type: 'line',
                data: {
                    labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                    datasets: [{
                        label: 'Posts',
                        data: [resposnse1.jan, resposnse1.feb, resposnse1.mar, resposnse1.apr, resposnse1.may, resposnse1.jun, resposnse1.jul, resposnse1.aug, resposnse1.sep, resposnse1.oct, resposnse1.nov, resposnse1.dec],
                        borderWidth: 1,
                        fill: false
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

});