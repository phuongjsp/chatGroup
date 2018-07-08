var oldPassword = document.getElementById("oldPassword");
var newPassword = document.getElementById("newPassword");
var confirmPassword = document.getElementById("confirmPassword");
var buttonChangePassword = document.getElementById("buttonChangePassword");

function setValidate() {
    if (oldPassword.value.length > 6) {
        newPassword.removeAttribute("disabled");
    } else {
        newPassword.setAttribute("disabled", 'true');
        confirmPassword.setAttribute("disabled", 'true');
        buttonChangePassword.setAttribute("disabled", 'true');
        return;
    }
    if (newPassword.value.length > 6) {
        confirmPassword.removeAttribute("disabled");
    } else {
        confirmPassword.setAttribute("disabled", 'true');
        buttonChangePassword.setAttribute("disabled", 'true');
        return;
    }
    if (newPassword.value == confirmPassword.value) {
        buttonChangePassword.removeAttribute("disabled");
    } else {
        buttonChangePassword.setAttribute("disabled", 'true');
        return;
    }
}

oldPassword.addEventListener("keyup", setValidate, true);
newPassword.addEventListener("keyup", setValidate, true);
confirmPassword.addEventListener("keyup", setValidate, true);
