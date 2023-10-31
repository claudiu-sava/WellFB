function update1() {
    $.post("/post/like").done(function (fragment) {
        console.log(fragment);
        $("#buttons").replaceWith(fragment);
    });
}


function update(id){
    String
    var postId = id.replace("likeButton_", "");

     console.log(postId)

    fetch('/post/like' + "?id=" + postId, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            var response1 = JSON.parse(data);
            if (response1.liked){
                $('#likeImage_' + postId).attr('src','/drawable/like_icon_red_50.png');
            }else{
                $('#likeImage_' + postId).attr('src','/drawable/like_icon_50.png');
            }
            $("#likeCount_" + postId).text(response1.likes);
        })
        .catch(error => {
            console.error('Error:', error);
        });

}