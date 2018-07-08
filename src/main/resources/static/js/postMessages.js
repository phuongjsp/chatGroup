//set number comments show in a page
var commentSize = 4;

var postSize = 5;

var postPage = 0;
ajaxGetPost(postPage);

function addPost(userPost) {
    var morePost = document.createElement('div');
    morePost.id = "post" + userPost.id;
    morePost.classList.add("col-sm-11");
    var elementPost =
        '<div class="panel panel-white post panel-shadow">' +
        '<div class="post-heading">' +
        '<div class="pull-left image">' +
        '<img class="img-circle avatar avatar' + userPost.username + '"     alt="' + userPost.altImg + '">' +
        '</div>' +
        '<div class="pull-left meta">' +
        '<div class="title h5">' +
        '<strong><a href="#">' + userPost.username + '</a></strong> ' +
        userPost.doSomeThing +
        '</div>' +
        '<h6 class="text-muted time">' + userPost.time + '</h6>' +
        '</div>';
    if (userPost.username == username)
        elementPost += '<div class="float-right"><i onclick="updatePost(\'' + userPost.id + '\')" class="fa fa-flask"></i></div>';

    elementPost += '</div>' +
        '<div class="post-description" id="postDiv' + userPost.id + '">' +
        '<p id="contentOfPost' + userPost.id + '" >' + userPost.content +
        '</p>' +
        '</div>' +
        '<div class="stats">' +
        '<div class="card-body py-2 small" id="likeCommentToggle' + userPost.id + '">' +
        '<a class="mr-3 d-inline-block" href="javascript:votePost(\'' + userPost.id + '\',\'LIKE\')" ><span id="countLike' + userPost.id + '">' + userPost.countlike +
        '</span><i class="fa fa-fw fa-thumbs-o-up"></i>Like</a>' +
        '<a class="mr-3 d-inline-block" href="javascript:votePost(\'' + userPost.id + '\',\'HATE\')" ><span id="countHate' + userPost.id + '">' + userPost.countHate +
        '</span><i class="fa fa-fw fa-thumbs-o-down"></i>Hate</a>' +
        '<a class="mr-3 d-inline-block" href="javascript:fistClickToComment(' + userPost.id + ');"  id="countCommentLink' + userPost.id + '"><span id="countComment' + userPost.id + '">' + userPost.countcomment +
        '</span><i class="fa fa-fw fa-comment"></i>Comment</a>' +

        '</div> ' +
        '</div>' +
        '<div class="post-footer">' +
        '<div class="comments-list" id="listComment' + userPost.id + '">' + '</div>';

    elementPost += '<div   id="messageForm' + userPost.id + '"  >' +
        '<div class="form-group">' +
        '<div class="input-group clearfix">' +
        '<input type="text" id="content' + userPost.id + '" placeholder="Type a message..." autocomplete="off" class="form-control"/>' +
        '<button type="submit" onclick="comment(\'' + userPost.id + '\')" class="btn-primary">Send</button>' +

        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    morePost.innerHTML = elementPost;
    var loadMorePost = document.getElementById('loadMorePost');
    document.getElementById("listPost").insertBefore(morePost, loadMorePost);
    ajaxGetAvatar(userPost.username);
}

function loadMorePost() {
    postPage++;
    ajaxGetPost(postPage);
}

function addcomment(comment, oldComment) {
    var listComment = document.getElementById("listComment" + comment.postId);

    var createComment = document.createElement('div');
    createComment.classList.add("comment");

    for (var i = 0; i < comment.comment.length; i++) {
        if (document.getElementById("commentNum" + comment.comment[i].id) != null) return;
        createComment.id = "commentNum" + comment.comment[i].id;

        createComment.innerHTML +=
            '<div class="comment-heading">' +
            '<div class="user">' +
            '<img class="img-circle avatar avatar' + comment.comment[i].username + '"   alt="' + comment.comment[i].username + '">' +
            comment.comment[i].username +
            '</div>' +
            '<div class="time">' + comment.comment[i].time + '</div>' +
            '</div>' +
            '<div class="comment-body">' + comment.comment[i].content + '</div>';

    }

    if (oldComment == "old") listComment.insertBefore(createComment, listComment.childNodes[1]);
    else listComment.appendChild(createComment);
    for (var i = 0; i < comment.comment.length; i++) {
        ajaxGetAvatar(comment.comment[i].username);
    }
    var countComment = document.getElementById("countComment" + comment.postId).innerText;
    //remove node if exist
    if (document.getElementById("loadMore" + comment.postId) != null) {
        if ((listComment.childNodes.length) > parseInt(countComment)) {
            document.getElementById("loadMore" + comment.postId).innerHTML = 'NODATA';
        }
        ;
        // listComment.removeChild(document.getElementById("loadMore"+comment.postId));
        return;
    }
    //check get current Page
    if ((listComment.childNodes.length - 1) < parseInt(countComment)) {
        var loadmore = document.createElement('div');
        loadmore.id = "loadMore" + comment.postId;
        loadmore.classList.add("comment");
        loadmore.classList.add("text-center");
        loadmore.innerHTML = '<a id="countPage' + comment.postId + '" name="0" href="javascript:loadMoreComment(\'' + comment.postId + '\');">Load more....</a>';
        listComment.insertBefore(loadmore, listComment.firstChild);
    }

}

function loadMoreComment(postId) {
    var listComment = document.getElementById("listComment" + postId);
    var fistComment = listComment.childNodes[1];
    var fistCMTID = fistComment.id;
    var maxId = fistCMTID.substr(10, 10);
    ajaxgetComment(postId, maxId, "old");
}

function ajaxGetPost(postPage) {
    var json = {"page": postPage, "size": postSize};
    $.ajax({
        url: '/getPost',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            if (data.length <= 0) {
                document.getElementById("loadMorePost").innerText = "NODATA";
            }
            data.forEach(function (value) {
                addPostFromData(value);
            });
        }
    });
}

function ajaxCountComment(postId) {
    var json = {"id": parseInt(postId)};
    $.ajax({
        url: '/countComment',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            document.getElementById("countComment" + postId).innerText = data;
        }
    });
}

function addPostFromData(value) {
    var userPost = {
        "id": null,
        "altImg": "#",
        "username": null,
        "doSomeThing": null,
        "time": null,
        "content": null,
        "countlike": 0,
        "countHate": 0,
        "countcomment": 0
    };
    var numLike = 0;
    var numHate = 0;
    value.postVotes.forEach(function (vote) {
        if (vote.type == 'LIKE') numLike++;
        if (vote.type == 'HATE') numHate++;
    });
    userPost.id = value.id;
    userPost.username = value.username;
    userPost.content = value.content;
    userPost.time = stringDate(value.dateTime);
    userPost.doSomeThing = doSomeThing(value.title, value.postTags);
    userPost.countlike = numLike;
    userPost.countHate = numHate;
    addPost(userPost);

    ajaxCountComment(userPost.id);
}

function ajaxGetAvatar(username) {
    var avatar = document.getElementsByClassName("avatar" + username);
    for (var i = 0; i < avatar.length; i++) {
        var srrrrz = avatar[i].getAttribute("src");
        var ch = srrrrz != null;
        if (ch) {
            for (var j = 0; j < avatar.length; j++) {
                avatar[j].setAttribute("src", srrrrz);
            }
            return true;
        }
    }
    $.ajax({
        url: '/avatar',
        data: username,
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            for (var i = 0; i < avatar.length; i++) {
                avatar[i].setAttribute("src", data.src);
            }
        }
    });
}

function fistClickToComment(postId) {
    var maxId = 0;
    var listComment = document.getElementById("listComment" + postId);
    var fistComment = listComment.childNodes[1];
    if (fistCMTID != undefined) {
        var fistCMTID = fistComment.id;
        maxId = fistCMTID.substr(10, 10);
    }
    ajaxgetComment(postId, maxId, "old");
    toggleComment(postId, 'down');
    var countComment = document.getElementById("countCommentLink" + postId);
    countComment.removeAttribute("href");
}

function toggleComment(postId, type) {
    var likeCommentToggle = document.getElementById("likeCommentToggle" + postId);
    var $listComment = $("#listComment" + postId);

    var upToggle = document.getElementById("togglePost" + postId);
    if (upToggle == null) {
        upToggle = document.createElement('i');
        upToggle.classList.add("fa");
        upToggle.id = "togglePost" + postId;
        upToggle.classList.add("float-right");
        likeCommentToggle.appendChild(upToggle);
    }

    if (type === 'up') {
        $listComment.slideUp();
        upToggle.classList.remove("fa-toggle-up");
        upToggle.classList.add("fa-toggle-down");
        upToggle.setAttribute("onclick", "toggleComment(" + postId + ",\'down\')");
    }
    else {
        $listComment.slideDown();
        upToggle.classList.remove("fa-toggle-down");
        upToggle.classList.add("fa-toggle-up");
        upToggle.setAttribute("onclick", "toggleComment(" + postId + ",\'up\')");
    }
}


function ajaxgetComment(postId, maxId, old) {
    var json = {"postId": postId, "page": 0, "size": commentSize, "maxId": parseInt(maxId)};
    $.ajax({
        url: '/getComment',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {

            data.forEach(function (value) {
                var commentUser = {
                    "postId": postId, "comment": [
                        {
                            "id": value.id,
                            "username": value.username,
                            "time": stringDate(value.dateTime),
                            "content": value.content
                        }
                    ]
                };
                addcomment(commentUser, old);
            });

        }
    });

}

function doSomeThing(title, tags) {
    var result = title;
    if (tags.length == 0) return result;
    result += " with ";
    for (var i = 0; i < tags.length; i++) {
        if (i > 0) result += " and ";
        result += tags[i].username;
    }
    return result;
}

function stringDate(date) {
    var d = new Date();
    d.setTime(date);
    strDate = d.getHours() + ' : ' + d.getMinutes() + ',' + d.getDate() + '/' + (d.getMonth() + 1) + '/' + d.getFullYear();
    return strDate;
}

function comment(postId) {
    var contentComment = document.getElementById("content" + postId);
    var newDate = new Date();

    var json = {"postId": parseInt(postId), "content": contentComment.value};
    $.ajax({
        url: '/createComment',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            var commentUser = {
                "postId": postId, "comment": [
                    {
                        "id": data.id,
                        "username": data.username,
                        "time": stringDate(data.dateTime),
                        "content": data.content
                    }
                ]
            };
            addcomment(commentUser);
            contentComment.value = '';
            ajaxCountComment(postId);
            toggleComment(postId, 'down');
        }
    });


}

function votePost(postId, type) {
    var json = {"type": type};
    $.ajax({
        url: "/votePost/" + postId,
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            var numLike = 0;
            var numHate = 0;
            data.forEach(function (vote) {
                if (vote.type == 'LIKE') numLike++;
                if (vote.type == 'HATE') numHate++;
            });
            var countLike = document.getElementById("countLike" + postId);
            countLike.innerText = numLike;
            var countLike = document.getElementById("countHate" + postId);
            countLike.innerText = numHate;
        }
    });
}

function updatePost(postId) {
    console.log(postId);
    updateContent(postId);
}

function updateContent(postId) {
    var contentOfPost = document.getElementById("contentOfPost" + postId);
    contentOfPost.remove();
    var textElement = '<div class="form-group"> <div class="input-group clearfix">' +
        '<textarea class="form-control" id="textAreaContentOfPost' + postId + '" >' +
        contentOfPost.innerText + '</textarea><button onclick="saveUpdatePost(' + postId + ')">Save</button></div></div>';
    document.getElementById("postDiv" + postId).innerHTML = textElement;
}

function saveUpdatePost(postId) {
    var newContent = document.getElementById("textAreaContentOfPost" + postId).value;
    var pContent = '<p id="contentOfPost' + postId + '" >' + newContent + '</p>';
    document.getElementById("postDiv" + postId).innerHTML = pContent;
    var json = {"postId": postId, "content": newContent};
    $.post("updatePost", JSON.stringify(json), function (data) {
        console.log(data);
    })
}