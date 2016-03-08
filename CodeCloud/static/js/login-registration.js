var passwordOK = false;
// indicates whether the script is used for register or account settings page
var register; 
var form;
// initialize forms with event listeners - register page
function initRegisterForm() {
    console.log("init form");
    form = document.querySelector("form");
    form.password_confirmation.addEventListener("keyup", checkPassword);
    //form.addEventListener("onsubmit", submitRegisterForm);
    // disable button by default
    form.button.disabled = true;
}

// check if passwords match when a new character is entered into password2
function checkPassword(evt) {
    evt.preventDefault();
    var msg = document.getElementById("passwordMsg");

    if(form.password.value != form.password_confirmation.value) {
        msg.innerHTML = "Passwords do not match!";
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
    if(passwordOK) {
        form.button.disabled = false;
    } else {
        form.button.disabled = true;
    }
}

//submit the registration form using AJAX to receive back a string/json object 
//for determining success of regisration
function submitRegisterForm() {
    console.log("Submit form!");
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'register', true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400) {
            var errorMessage = xhr.responseText;
            if(errorMessage.length > 0)
            {
                var errorP = document.getElementById("errorMsg");
                errorP.innerHTML = errorMessage;
            }
            else {
                window.location = window.location.origin + "/home.html";
            }
        }
    }

    var username = form.username.value;
    var password = form.password.value;
    var firstName = form.first_name.value;
    var lastName = form.last_name.value;
    var studentNum = form.student_num.value;
    var objToSend = { username : username, password : password, firstName : firstName, lastName : lastName, studentNum : studentNum};
    var jsonToSend = JSON.stringify(objToSend);
    xhr.send(jsonToSend);
    return false;
}