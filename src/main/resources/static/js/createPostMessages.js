var listUserTag = [];
$('#sendPost').click(function () {
    var post = {
        "title": $('#titelPost').val(), "content": $('#contentPost').val(),
        "postTags": listUserTag
    };
    $.post("/createPost", JSON.stringify(post), function (data) {
        console.log(data);
    });
});

var $findTagsUser = $('#findTagsUser');
var $ulListTag = $("#ulListTag");
$.ajaxSetup({
    headers: {
        "Accept": "application/json",
        "Content-Type": "application/json"
    }
});
$findTagsUser.keyup(function () {
    if ($findTagsUser.val().length > 0) $.post("/getUserForTags", "%" + $findTagsUser.val() + "%", function (data) {
        $ulListTag.html('');
        data.forEach(function (value) {
            var $li = $("<li></li>");
            $li.text(value);
            $li.addClass("list-group-item li-tag");
            $ulListTag.append($li);
        })
    });
    else $ulListTag.html('');
});

$(document).on("click", 'span.tag-user', function () {
    $(this).remove();
});
$(document).on("hide.bs.modal", '#tagUserModal', function () {
    listUserTag = [];
    if ($(".tag-user").length == 0) {
        $("#addnewUser").html('<i class="stat-item fa fa-tags ">Gắn thẻ</i>');
        return;
    }
    var listTags = '';
    for (var i = 0; i < $(".tag-user").length; i++) {
        if (i > 0) listTags += " , ";
        listTags += $(".tag-user")[i].innerText;
        listUserTag.push({"username": $(".tag-user")[i].innerText});
    }
    $("#addnewUser").html(listTags);
});
$(document).on("click", "li.li-tag", function () {
    for (var i = 0; i < $(".tag-user").length; i++) {
        if ($(".tag-user")[i].innerText == $(this).text()) return;
    }
    $('#listWithTag').removeClass("tag-hidden");
    var $span = $('<span></span>');
    $span.addClass("fa fa-remove mr-2 btn btn-outline-warn tag-user");
    $span.text($(this).text());
    $('#listWithTag').append($span);
});

