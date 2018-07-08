// Start of use strict
'use strict';


var arrayMessages = [];
$(function () {

    $("#messagesDropdown").click(function () {
        $("#messagesnew").text('');
    });

    // Configure tooltips for collapsed side navigation
    $('.navbar-sidenav [data-toggle="tooltip"]').tooltip({
        template: '<div class="tooltip navbar-sidenav-tooltip" role="tooltip" style="pointer-events: none;"><div class="arrow"></div><div class="tooltip-inner"></div></div>'
    })
    // Toggle the side navigation
    $("#sidenavToggler").click(function (e) {
        e.preventDefault();
        $("body").toggleClass("sidenav-toggled");

        $(".navbar-sidenav .nav-link-collapse").addClass("collapsed");
        $(".navbar-sidenav .sidenav-second-level, .navbar-sidenav .sidenav-third-level").removeClass("show");
    });
    // Force the toggled class to be removed when a collapsible nav link is clicked
    $(".navbar-sidenav .nav-link-collapse").click(function (e) {
        e.preventDefault();
        $("body").removeClass("sidenav-toggled");
    });
    // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
    // $('body.fixed-nav .navbar-sidenav, body.fixed-nav .sidenav-toggler, body.fixed-nav .navbar-collapse').on('mousewheel DOMMouseScroll', function (e) {
    //     var e0 = e.originalEvent,
    //         delta = e0.wheelDelta || -e0.detail;
    //     this.scrollTop += (delta < 0 ? 1 : -1) * 30;
    //     e.preventDefault();
    // });
    // Scroll to top button appear
    $(document).scroll(function () {
        var scrollDistance = $(this).scrollTop();
        if (scrollDistance > 100) {
            $('.scroll-to-top').fadeIn();
        } else {
            $('.scroll-to-top').fadeOut();
        }
    });
    // Configure tooltips globally
    $('[data-toggle="tooltip"]').tooltip()
    // Smooth scrolling using jQuery easing
    $(document).on('click', 'a.scroll-to-top', function (event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('href')).offset().top)
        }, 1000, 'easeInOutExpo');
        event.preventDefault();
    });
});

connect();
var ws;

function connect() {
    var socket = new SockJS('/ws');
    ws = Stomp.over(socket);
    ws.connect({}, wsOnConnect);
}

function wsOnConnect() {
    // Subscribe to the Public Topic
    ws.subscribe("/queue/" + principalName, onReplyReceived);
}

function onReplyReceived(payload) {
    var data = JSON.parse(payload.body);
    var cov;
    var bb = true;
    arrayMessages.forEach(function (value) {
        if (value.user == data.fromUser) {
            value.content = data.content;
            bb = false;
        }
    });
    if (bb) {
        var d = new Date();
        d.setTime(data.dateTime);
        cov = {
            'id': data.id,
            'user': data.fromUser,
            'principal': data.toUser,
            'content': data.content,
            'time': d.getDate() + '/' + d.getMonth() + ',' + d.getHours() + ' : ' + d.getMinutes()
        };
        arrayMessages.push(cov);
    }
    var nodeRootId = "ul" + data.fromUser;
    var rootNode = document.getElementById(nodeRootId);
    if (rootNode != null) {
        rootNode.appendChild(
            addMess(data.content, 'left', null));
        hasReaded(data.id, data.fromUser);
    } else {
        $("#messagesnew").text('News');
        arrayMessages.forEach(function (value) {
            addnote(value.user, value.principal, value.time, value.content);
        });
    }
}

function hasReaded(id, user) {
    var json = {"id": id};
    $.ajax({
        url: '/setReaded',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {

            var rootNode = document.getElementById("formMessages");
            var nodeMessages = document.getElementById("newMessages" + data);
            if (nodeMessages != null) rootNode.removeChild(nodeMessages);
        }
    });
    scrollElement(user);
}

function addnote(user, principal, time, content) {
    if (content.length > 30) content = content.substr(0, 30) + ' ...';
    var id = "newMessages" + user;
    var htmlNode = "<div class='dropdown-divider'></div>" +
        "<a class='dropdown-item' href=\"javascript:register_popup('" + user + "','" + principal + "')\">" +
        "<div class='row'>" +
        "<strong class='col-sm-8' >" + user + "</strong>" +
        "<span class='col-sm-4 float-right' >" + time + "</span>" +
        "</div>" +
        "<div class='dropdown-message small' >" + content + "</div>" +
        "</a>";
    var node = document.getElementById(id);
    var rootNode = document.getElementById('formMessages');
    if (node != null) rootNode.removeChild(node);

    node = document.createElement('div');
    node.setAttribute("id", id);
    node.innerHTML = htmlNode;
    rootNode.appendChild(node);
}

function sendMessages(name) {
    var client = {
        'content': document.getElementById("inputFor" + name).value,
        'sendto': name
    };
    var data = JSON.stringify(client);
    ws.send("/app/message", {}, data);
    var nodeRootId = "ul" + name;

    document.getElementById("inputFor" + name).value = '';
    document.getElementById(nodeRootId).appendChild(
        addMess(client.content, 'right', null));
    scrollElement(name);
}

function addMess(content, userLoR, id) {
    var node = document.createElement('li');
    if (id != null) node.id = id;
    node.innerHTML = '<span class="' + userLoR + '">' + content + '</span>' +
        '<div class="clear"></div>';
    return node;

}

//this function can remove a array element.
Array.remove = function (array, from, to) {
    var rest = array.slice((to || from) + 1 || array.length);
    array.length = from < 0 ? array.length + from : from;
    return array.push.apply(array, rest);
};

//this variable represents the total number of popups can be displayed according to the viewport width
var total_popups = 0;

//arrays of popups ids
var popups = [];

//this is used to close a popup
function close_popup(id) {
    for (var iii = 0; iii < popups.length; iii++) {
        if (id == popups[iii]) {
            Array.remove(popups, iii);
            document.getElementById("messagesBox").removeChild(document.getElementById(id));
            calculate_popups();

            return;
        }
    }
}

//this is used to hidden a popup
function hidden_popup(id) {
    $('#' + id).slideToggle();
}

//creates markup for a new popup. Adds the id to popups array.
function register_popup(name, principal) {
    var id = name;
    if (document.getElementById(id) == null) {
        for (var iii = 0; iii < popups.length; iii++) {
            //already registered. Bring it to front.
            if (id == popups[iii]) {
                Array.remove(popups, iii);

                popups.unshift(id);

                calculate_popups();


                return;
            }
        }


        var element = '<div ' +
            ' class="popup-box chat-popup" id="' + id + '">';
        element = element + '<div class="popup-head">';
        element = element + '<div class="popup-head-left">' + name + '</div>';

        element = element + '<div class="popup-head-right">' +
            '<a href="javascript:hidden_popup(\'z' + id + '\');" style="padding-right: 25px;color: blue">&DownArrowUpArrow;</a>' +
            ' ' +
            '<a href="javascript:close_popup(\'' + id + '\');" style="color:blue;">&#10005;</a>' +
            '</div>';

        element = element + '<div style="clear: both"></div></div>';
        element += '<div id="z' + id + '">' +
            '<div  id="scroll' + id + '"' +
            'onscroll="scrollTopOnUpdate(this,\'' + name + '\',\'' + principal + '\')" class="messages" >' +
            '<ul id="ul' + id + '" style="margin-top: 35px;margin-bottom: 35px">' +
            '</ul>' +
            '<div class="clear"></div>' +
            ' </div>';
        element += '<div class="row" style="margin-left: 5px">' +
            '<input type="text" id="inputFor' + id + '" width="100%" class="col-sm-8" placeholder="Type a message..." />\n' +
            '<div class="col-sm-4 text-right">\n' +
            '<button class="btn btn-success" onclick="sendMessages(\'' + name + '\')">Send</button>' +
            '</div>' +
            '</div>';
        element += '</div></div>';
        document.getElementsByTagName("messagesBox")[0].innerHTML =
            document.getElementsByTagName("messagesBox")[0].innerHTML + element;

        popups.unshift(id);
        getMessagesForBothUser(name, principal);
        calculate_popups();

    }
}

//calculate the total number of popups suitable and then populate the toatal_popups variable.
function calculate_popups() {
    var width = window.innerWidth;
    var friendBox = document.getElementById("messagesBox");
    if (width < 992) {
        total_popups = 0;

        friendBox.style.display = "none";
    }
    else {
        friendBox.style.display = "block";
        width = width - 300;
        //320 is width of a single popup box
        total_popups = parseInt(width / 400);
    }

    display_popups();

}

//displays the popups. Displays based on the maximum number of popups that can be displayed on the current viewport width
function display_popups() {
    var right = 270;

    var iii = 0;
    for (iii; iii < total_popups; iii++) {
        if (popups[iii] != undefined) {
            var element = document.getElementById(popups[iii]);
            element.style.right = right + "px";
            right = right + 300;
            element.style.display = "block";
            scrollElement(element.id);
        }
    }

    for (var jjj = iii; jjj < popups.length; jjj++) {
        var element = document.getElementById(popups[jjj]);
        element.style.display = "none";
    }

}

//recalculate when window is loaded and also when window is resized.
window.addEventListener("resize", calculate_popups);
window.addEventListener("load", calculate_popups);

function scrollElement(user) {
    var scrollUser = "scroll" + user;
    var nodeScroll = document.getElementById(scrollUser);
    if (nodeScroll != undefined) nodeScroll.scrollTop = nodeScroll.scrollHeight;
}

//get message for both User when regis popup
function getMessagesForBothUser(fromUser, toUser) {
    var json = {"min": 0, "max": 12, "from_user": fromUser, "to_user": toUser};
    var nodeRootId = "ul" + fromUser;
    $.ajax({
        url: '/getoldMessages',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            data.reverse().forEach(function (value) {
                document.getElementById(nodeRootId).appendChild(
                    addMess(value.content, value.fromUser == json.from_user ? 'left' : 'right', value.id));

            });
            if (data.length > 0) hasReaded(data[data.length - 1].id, fromUser);
        }
    });

}

//load old messages for both User when scroll to top
function scrollTopOnUpdate(element, name, principal) {
    if (element.scrollTop < 10) {
        element.scrollTo(10, 10);
        loadUpdateMessages(name, principal, document.getElementById("ul" + name).childElementCount);
    }
}

function loadUpdateMessages(fromUser, toUser, izc) {
    var nodeRootId = "ul" + fromUser;
    var nodeRoot = document.getElementById(nodeRootId);
    var json = {"min": izc, "max": 1, "from_user": fromUser, "to_user": toUser};

    $.ajax({
        url: '/getoldMessages',
        data: JSON.stringify(json),
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function (data) {
            if (data.length == 0) {
                document.getElementById("scroll" + fromUser).removeAttribute("onscroll");
                return;
            }
            ;
            var rootNo = document.getElementById(nodeRootId);
            data.forEach(function (value) {
                rootNo.insertBefore(
                    addMess(value.content, value.title == json.to_user ? 'right' : 'left', value.id),
                    rootNo.firstChild);
            });
        }
    });
}
