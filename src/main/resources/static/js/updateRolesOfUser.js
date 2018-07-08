var rou = document.getElementById('rolesOfUser');
var roou = document.getElementById('rolesOutOfUser');

function chagneRoleOutOfUser() {
    var opt = roou.options[roou.selectedIndex];
    rou.appendChild(opt);
    roou.removeChild(opt);
}

function chagneRoleOfUser() {
    var opt = rou.options[rou.selectedIndex];
    roou.appendChild(opt);
    rou.removeChild(opt);
}

function show() {
    var opt = document.getElementById('rolesOfUser');
    var val = [];
    for (var i = 0; i < opt.length; i++) {
        val.push(opt[i].value);
    }
    var roles = document.getElementById("roles");
    roles.value = val;
}
