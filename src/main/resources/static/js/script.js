function deletePost(){
    var id = $('#postDeleteModalDeleteButton').attr("post-id");
    fetch('/post/delete' + "?id=" + id, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function deleteComment(){

    var commentId = $('#commentDeleteModalDeleteButton').attr("comment-id");
    var postId = $('#commentDeleteModalDeleteButton').attr("post-id");

    fetch('/post/deleteComment' + "?comment-id=" + commentId + "&post-id=" + postId, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });

};


function likePost(id){
    var postId = id.replace("likeButton_", "");

    fetch('/post/like' + "?id=" + postId, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            const response1 = JSON.parse(data);
            if (response1.liked){
                $('.like-icon').attr('src','/drawable/like_icon_red_50.png');
                $('#likeImage_' + postId).attr('src','/drawable/like_icon_red_50.png');
            }else{
                $('.like-icon').attr('src','/drawable/like_icon_50.png');
                $('#likeImage_' + postId).attr('src','/drawable/like_icon_50.png');
            }
            $("#likeCount_" + postId).text(response1.likes);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function editComment() {

    var commentId = $("#commentEditModalEditButton").attr("comment-id");
    var comment = $("#commentEditModalComment").val();

    fetch('/post/editComment' + "?id=" + commentId + "&comment=" + comment, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            var response2 = JSON.parse(data);
            $('#comment_' + commentId).text(response2.comment);
            $("#commentEditModalComment").val("");
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function editPost() {
    var postId = $("#postEditModalEditButton").attr("post-id");
    var htmlInput = $("#postEditModalPostDesc");
    var desc = htmlInput.val();

    fetch('/post/edit' + "?id=" + postId + "&desc=" + desc, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            var response2 = JSON.parse(data);
            $('#postDesc_' + postId).text(response2.desc);
            $('.postDesc').text(response2.desc);
            htmlInput.val("");
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function followUser(id) {
    fetch('/users/followUser' + "?id=" + id, {
        method: 'POST',
    })
        .then(response => response.text())
        .then(data => {
            var response1 = JSON.parse(data);
            if (response1.isFollowing) {
                $("#" + id).text("Followed").toggleClass('btn-primary btn-secondary');
            } else {
                $("#" + id).text("Follow").toggleClass('btn-secondary btn-primary');
            }
            $("#userHeaderFollowedByText").text("Followed By " + response1.followersCount);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function addComment() {
    var htmlInput = $('.modal-full .form-control');
    var comment = htmlInput.val();
    if (comment === "") {
        return;
    }

    var postId = $('.modal-full .form-control').attr('post-id');

    fetch('/post/comment', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({"commentBody": comment, "postId": parseInt(postId)})
    })
        .then(response => response.text())
        .then(data => {
            htmlInput.val("");
            loadComments(postId);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function checkNewPostModal() {
    if ($('#newPostModalFileInput').get(0).files.length !== 0) {
        $('#newPostModelForm').submit();
    } else {
        $('#newPostModalAlert').show();
    }
}

function loadComments(id) {

    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/post/getComments?id=' + id, true);

    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 400) {
            // Parse the response
            var fragmentContent = xhr.responseText;

            var fragmentContainer = document.getElementById('commentSection');
            fragmentContainer.innerHTML = fragmentContent;

        } else {
            console.error('Request failed with status:', xhr.status);
        }
    };


    xhr.onerror = function () {
        console.error('Request failed');
    };

    // Send the request
    window.setTimeout(() => {
        xhr.send();
    }, 100);

}

$('#newPostModal').on('show.bs.modal', function (e) {
    //get data-id attribute of the clicked element
    $('#newPostModalAlert').hide();
});

$(document).ready(function () {


    $('#postDeleteModal').on('show.bs.modal', function (e) {
        //get data-id attribute of the clicked element
        var postId = $(e.relatedTarget).attr('post-id');

        $("#postDeleteModalDeleteButton").attr("post-id", postId);
    });

    $('#commentDeleteModal').on('show.bs.modal', function (e) {
        //get data-id attribute of the clicked element
        var commentId = $(e.relatedTarget).attr('comment-id');
        var postId = $(e.relatedTarget).attr('post-id');

        $("#commentDeleteModalDeleteButton").attr("comment-id", commentId).attr("post-id", postId);
    });

    $('#postModal').on('show.bs.modal', function (e) {
        const postId = $(e.relatedTarget).attr('post-id');

        fetch('/post/getPost' + "?id=" + postId, {
            method: 'GET',
        })
            .then(response => response.text())
            .then(data => {
                var jsonResponse = JSON.parse(data);

                if (!jsonResponse.userAuthor) {
                    $('.modal-full .editButton').hide();
                    $('.modal-full .deleteButton').hide();
                }

                $('.modal-full .image').attr('src', jsonResponse.hqUpload);
                $('.modal-full .profile-pic-icon').attr('src', jsonResponse.avatar);
                $('.modal-full .modal-title').text(jsonResponse.user);
                $('.modal-full .username').text(jsonResponse.user);
                $('.postModalDesc').text(jsonResponse.postDesc);

                $('.modal-full .likeButton').attr("id", 'likeButton_' + jsonResponse.postId);
                $('.modal-full .deleteButton').attr("post-id", jsonResponse.postId);
                $('.modal-full .editButton').attr("post-id", jsonResponse.postId);

                if (jsonResponse.hasUserLiked) {
                    $('.like-icon').attr('src', '/drawable/like_icon_red_50.png');
                } else {
                    $('.like-icon').attr('src', '/drawable/like_icon_50.png');
                }

                $("#postEditModalPostDesc").val(jsonResponse.postDesc);

                $('.modal-full .form-control').attr("post-id", jsonResponse.postId);

                $('.modal-full .image').one('load', function () {
                    window.setTimeout(() => {

                        var postModalHeight = $('#postModal .modal-body').height();
                        var postModalDescArea = $('#postModal .postModalDescArea').height();
                        var postModalSendMessageArea = $('#postModal .postModalSendMessageArea').height();
                        $('#postModal .comments-scroll-modal').css("height", postModalHeight - (postModalDescArea + postModalSendMessageArea) + 'px');

                        loadComments(jsonResponse.postId);
                    }, 200);

                });

            })
            .catch(error => {
                console.error('Error:', error);
            });
    });


    $('#postEditModal').on('show.bs.modal', function (e) {
        //get data-id attribute of the clicked element
        var postId = $(e.relatedTarget).attr('post-id');
        var postDesc = $(e.relatedTarget).attr('post-desc');

        if (postId !== undefined && postDesc !== undefined) {
            $("#postEditModalPostDesc").val(postDesc);
            $("#postEditModalEditButton").attr("post-id", postId);
        } else if(postId !== undefined){
            $("#postEditModalEditButton").attr("post-id", postId);
        }
    });


    $('#commentEditModal').on('show.bs.modal', function (e) {
        //get data-id attribute of the clicked element
        var commentId = $(e.relatedTarget).attr('comment-id');
        var comment = $(e.relatedTarget).attr('comment');

        console.log(commentId);
        console.log(comment);

        if (commentId !== undefined && comment !== undefined) {
            $("#commentEditModalComment").val(comment);
            $("#commentEditModalEditButton").attr("comment-id", commentId);
        }
    });


    // FOOTER
    var height = $('footer').height();
    var windowHeight = $(window).height();
    var bodyHeight = $('body').height();
    if (windowHeight > bodyHeight) {
        $('footer').addClass("fixed-bottom");
    } else {
        $('body').css('margin-bottom', height + 'px');
    }

});