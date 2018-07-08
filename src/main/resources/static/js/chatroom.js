var path = room;
console.log(path);
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');

var stompClient = null;
var hasMessages = true;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
sockConnect();

function sockConnect() {
    //Create New connect using SockJs
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected);
}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe("/topic/public." + path, onMessageReceived);


}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            content: messageInput.value
        };
        stompClient.send("/app/chat.sendMessage." + path, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if (message.kick == username) {
        window.location.href = "/404?error=Bạn+Đã+Bị+CHủ+Phòng+Kick+Ra";
        return;
    }
    if (message.kick != null) {
        var ulGroupUser = document.getElementById("listGroupUser");
        var strLi = "user" + message.kick;
        var liGroupUser = document.getElementById(strLi);
        ulGroupUser.removeChild(liGroupUser);
    }
    var messageElementRoot = document.createElement('div');
    messageElementRoot.id = message.id;
    var messageElement = document.createElement('div');

    messageElement.classList.add('chat-message');
    if (message.sender == username) messageElement.classList.add("right");
    else {
        messageElement.classList.add("left");


        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);
    }
    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    //date
    var datetimeElement = document.createElement('span');

    datetimeElement.classList.add("smaller");
    datetimeElement.style = "padding-left: 100px";
    var datetimeText = document.createTextNode(stringDate(message.date));
    datetimeElement.appendChild(datetimeText);
    messageElement.appendChild(datetimeElement);


    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    messageElementRoot.appendChild(messageElement);
    messageArea.appendChild(messageElementRoot);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function stringDate(date) {
    var d = new Date();
    d.setTime(date);
    strDate = d.getHours() + ' : ' + d.getMinutes() + ',' + d.getDate() + '/' + (d.getMonth() + 1) + '/' + d.getFullYear();
    return strDate;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function ajaxGetOldMessagesRoom() {
    var min = messageArea.childElementCount - 1;
    var json = {"min": min, "max": 1, "room": room};
    $.ajax({
        url: '/getoldMessagesChatRoom',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (messages) {
            if (messages.length != 0) {
                var data = messages[0];
                var html =
                    '<div class="chat-message right">' +
                    '<span >' + data.fromUser +
                    '</span>' +
                    '<span class="smaller" style="padding-left: 100px" >' + stringDate(data.time) +
                    '</span>' +
                    '<p  >' + data.content +
                    '</p>' +
                    '</div>';
                var oldMessages = document.createElement('div');
                oldMessages.id = data.id;
                oldMessages.innerHTML = html;

                messageArea.insertBefore(oldMessages, messageArea.childNodes[2]);
            } else hasMessages = false;
        }
    });
}

function onScrollMessages(event) {
    if (hasMessages) if (messageArea.scrollTop < 100) {
        ajaxGetOldMessagesRoom();
        messageArea.scrollTop = 110;
    }
    event.preventDefault();
}

function kickUser(username) {
    var json = {"username": username, "room": room};
    $.ajax({
        url: '/kickUser',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            console.log(data);
        }
    });
}

function findUserFun() {
    var json = {"username": this.value, "room": room};
    var ulListFindUser = document.getElementById("ulListFindUser");
    if (this.value.length == 0) ulListFindUser.innerHTML = '';
    if (this.value.length > 0) {
        $.ajax({
            url: '/lisUserLikeName',
            data: JSON.stringify(json),
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function (data) {
                console.log(data + "is data");
                ulListFindUser.innerHTML = '';

                if (data.length > 0) data.forEach(function (username) {

                    var newLiUser = document.createElement('li');
                    newLiUser.classList.add("list-group-item");
                    newLiUser.classList.add("btn");
                    newLiUser.classList.add("btn-outline-success");
                    strnewLiUser = "liOtherUser" + username;
                    newLiUser.innerText = username;
                    newLiUser.setAttribute("onclick", "addUserintoTheRoom('" + username + "')");
                    newLiUser.setAttribute("data-dismiss", "modal");
                    // var newLi = "<li class='list-group-item'   onclick='addUserintoTheRoom(' +
                    //     username+')' +
                    //     ''>' + username + '</li>";
                    ulListFindUser.appendChild(newLiUser);
                })
            }
        });
    }
}

var listGroupUser = document.getElementById("listGroupUser");

function addUserintoTheRoom(username) {
    var json = {"username": username, "room": room};
    console.log(json);
    $.ajax({
        url: '/addUserintoTheRoom',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            var newHtml =
                '<li class="list-group-item" id="user' + username + '">' +
                '    <div class="row">' +
                '         <span class="col-sm-4 btn btn-info btn-block">' +
                '              <i class="fa fa-group"></i>' +
                '              <span >' + username + '</span>' +
                '         </span>' +
                '         <a href="updateRoom/' + room + '/' + username + '" class="col-sm-4 btn btn-outline-success">' +
                '              <i class="fa fa-level-up">Up to lead</i>' +
                '         </a>' +
                '         <button data-dismiss="modal"' +
                '              onclick=" kickUser(\'' + username + '\')" class="col-sm-4 btn btn-outline-danger">' +
                '              <i class="fa fa-dot-circle-o">Kick out</i>' +
                '         </button>' +
                '    </div>' +
                '</li>';
            if (data) {
                listGroupUser.innerHTML += newHtml;
            }
        }
    });

}

var findUser = document.getElementById("findUser");
findUser.addEventListener('keyup', findUserFun, true);
messageForm.addEventListener('submit', sendMessage, true);
messageArea.addEventListener('scroll', onScrollMessages, true);