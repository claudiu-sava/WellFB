function deletePost(id){
    fetch('/post/delete' + "?id=" + id, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            console.log(data)
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


function update(id){
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

//triggered when modal is about to be shown

function editPost(id, desc){
    fetch('/post/edit' + "?id=" + id + "&desc=" + desc, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            console.log(data)
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });

}

$( document ).ready(function() {
    $('#postDeleteModal').on('show.bs.modal', function (e) {
        //get data-id attribute of the clicked element
        var postId = $(e.relatedTarget).data('post-id');

        $("#postDeleteModalDeleteButton").attr("id", postId);
    });

    $('#postEditModal').on('show.bs.modal', function (e) {
        //get data-id attribute of the clicked element
        var postId = $(e.relatedTarget).data('post-id');
        var postDesc = $(e.relatedTarget).data('post-desc');

        $("#postEditModalPostDesc").val(postDesc);
        $("#postEditModalHiddenInput").val(postId);
        $("#postEditModalEditButton").attr("id", postId);
    });
});