var checkUserURL = window.location.origin + '/checkUser';
var checkPasswordURL = window.location.origin + '/checkPassword';
var usernameOK = false;
var currentPasswordOK = false;
var passwordOK = false;
var form = null;
// indicates whether the script is used for register or account settings page
var register; 

// initialize forms with event listeners - register page
function initRegisterForm() {
    form = document.getElementById("registerForm");
    form.username.addEventListener("keyup", checkUsername);
    form.password1.addEventListener("keyup", checkPassword);
    form.password2.addEventListener("keyup", checkPassword);
    // disable button by default
    form.button.disabled = true;
    register = true;
}

// initialize forms with event listeners - account settings page
function initSettingsForm() {
    form = document.getElementById("accountSettingsForm");
    form.currentPassword.addEventListener("keyup", checkCurrentPassword);
    form.password1.addEventListener("keyup", checkPassword);
    form.password2.addEventListener("keyup", checkPassword);
    // disable button by default
    form.button.disabled = true;
    register = false;
}

// check if username already exists when a new character is entered into
// username
function checkUsername(evt) {
    evt.preventDefault();
    var msg = document.getElementById("usernameMsg");

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    if(form.username.value.length > 0) {
        xhr.open('GET', checkUserURL + '/' + form.username.value.toLowerCase(), true);
        xhr.onreadystatechange = function() {
            if(xhr.status == 200 || xhr.status == 400) {
                if(xhr.responseText == "exists") {
                    msg.innerHTML = "Username already exists!";
                    usernameOK = false;
                } else {
                    msg.innerHTML = "";
                    usernameOK = true;
                }
                
                // change button status if needed
                buttonStatus();
            }
        }
        xhr.send();
    }
}

// check if current password is correct when new character is entered
function checkCurrentPassword(evt)
{
    evt.preventDefault();
    var msg = document.getElementById("currentMsg");

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    if(form.currentPassword.value.length > 0) {
        xhr.open('GET', checkPasswordURL + '/' + form.currentPassword.value, true);
        xhr.onreadystatechange = function() {
            if(xhr.status == 200 || xhr.status == 400) {
                console.log(xhr.responseText);
                if(xhr.responseText == "exists") {
                    msg.innerHTML = "";
                    currentPasswordOK = true;
                } else {
                    msg.innerHTML = "Current password is incorrect";
                    currentPasswordOK = false;
                }

                // change button status if needed
                buttonStatus();
            }
        }
        xhr.send();
    }

}

// check if passwords match when a new character is entered into password2
function checkPassword(evt) {
    evt.preventDefault();
    var msg = document.getElementById("passwordMsg");

    if(form.password1.value != form.password2.value) {
        msg.innerHTML = "Passwords don't match!";
        passwordOK = false;
    } else if(form.password1.value.length == 0 && 
            form.password2.value.length == 0) {
        // don't allow empty passwords
        msg.innerHTML = "Password cannot be empty!";
        passwordOK = false;
    } else {
        msg.innerHTML = "";
        passwordOK = true;
    } 

    // change button status if needed
    buttonStatus();
}

// enable/disable button if input is valid/invalid. check when a new character
// is entered into any of the text fields
function buttonStatus() {
    if(register) { // register
        if(usernameOK && passwordOK) {
            form.button.disabled = false;
        } else {
            form.button.disabled = true;
        }
    } else { // account settings
        if(passwordOK && currentPasswordOK) {
            form.button.disabled = false;
        } else {
            form.button.disabled = true;
        }
    }
}