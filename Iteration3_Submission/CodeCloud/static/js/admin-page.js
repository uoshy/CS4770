var previous; //previous href
//clicking-a-link-display-it-in-div-on-same-page
$("#admin-menu span a").click(function(e){ //show-hide page contents
    e.preventDefault();
    var toShow = $(this).attr('href');
    var usersList = document.getElementById("users-list");
    usersList.innerHTML = "";
    var coursesList = document.getElementById("courses-list");
    coursesList.innerHTML = "";
            
    //if previous href was clicked & is visible
    if($(previous) && $(previous).is(':visible')){ 
        if($(previous).is($(toShow))) { //if hrefs are same
            $(toShow).toggle(); //hide div
        } else {
            $(previous).toggle(); //hide previous
            $(toShow).toggle(); //show div
        };
    } else {
        $(toShow).toggle(); //toggle visibility
    };
    
    previous = toShow; //save last href clicked
});

//- - - - - - - - - - - - - - - - - - - - - - -
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
//for determining success of registration
function submitRegisterForm() {
    console.log("Submit form!");
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'admin/register', true);
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
                form.reset(); // reset form for more input
            }
        }
    }
    
    //TODO: set user starting role

    var username = form.username.value;
    var password = form.password.value;
    var firstName = form.first_name.value;
    var lastName = form.last_name.value;
    var studentNum = form.student_num.value;
    var userRole = form.main_role.value;
    var objToSend = { username : username, password : password, firstName : firstName, lastName : lastName, studentNum : studentNum, userRole : userRole};
    var jsonToSend = JSON.stringify(objToSend);
    xhr.send(jsonToSend);
    return false;
}

//TODO: Fix so no function call when hiding div on click
function getAllUsers(callingObj) {
    console.log("Getting users...");
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/admin/listUsers', true);
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
            var response = JSON.parse(xhr.responseText);
   
            var activeRoleSpan = document.getElementById('sidebarActiveRole');
            var usersList = document.getElementById("users-list");
            var formEle = document.createElement("form");
                
            //dynamically add form onsubmit function
            if(callingObj.id == "span-delete-u") {
                formEle.onsubmit = "deleteUsers(); return false;";
            } else if(callingObj.id == "span-enrol-u") {
                formEle.onsubmit = "enrolUsers(); return false;";
            } else if(callingObj.id == "span-remove-u") {
                formEle.onsubmit = "removeUsers(); return false;";
            };
                
            for(var i=0; i < response.usernames.length; i++)
            {
                formEle.innerHTML += "<input class='checked-users' type='checkbox'> " + response.usernames[i] + " - " + response.userFullNames[i] + "<br>";
            }
            usersList.appendChild(formEle);
        }
    }

    xhr.send();
}

//TODO: Fix so no function call when hiding div on click
function getAllCourses(callingObj) {
        console.log("Getting courses...");
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/admin/listCourses', true);
        xhr.onreadystatechange = function() {
            if(xhr.readyState != 4) return;
            if(xhr.status == 200 || xhr.status == 400)
            {
                var response = JSON.parse(xhr.responseText);
    
                var activeRoleSpan = document.getElementById('sidebarActiveRole');
                var coursesList = document.getElementById("courses-list");
                var formEle = document.createElement("form");
                var prevTerm = "FOOBAR06";
                
                //dynamically add form onsubmit function
                if(callingObj.id == "span-delete-u") {
                    formEle.onsubmit = "deleteUsers(this); return false;";
                } else if(callingObj.id == "span-enrol-u") {
                    formEle.onsubmit = "enrolUsers(this); return false;";
                } else if(callingObj.id == "span-remove-u") {
                    formEle.onsubmit = "removeUsers(this); return false;";
                };
                
                for(var i = response.courseNames.length-1; i >= 0; i--) //reverse chronological
                {
                    if(response.courseTerms[i] != prevTerm)
                    {
                        var currentTermLI = document.createElement("li");
                        formEle.appendChild(currentTermLI);
                        currentTermLI.innerHTML = "<b>" + response.courseTerms[i] + "</b>";
                        var currentTermUL = document.createElement("ul");
                        currentTermLI.appendChild(currentTermUL);
                    }
                    var courseLI = document.createElement("li");
                    if(response.courseIsActive[i]) {
                        courseLI.innerHTML = "<a href=/courses/course.html?courseTerm=" + response.courseTerms[i] + 
                                                    "&courseID=" + response.courseIDs[i] + 
                                                    "&role=" + activeRoleSpan.innerHTML + ">"
	    									        + response.courseIDs[i] + ": " + response.courseNames[i]+"</a>";
                    } else {
                        courseLI.innerHTML = response.courseIDs[i] + ": " + response.courseNames[i];
                        currentTermUL.appendChild(courseLI);
                    };
                }
                coursesList.appendChild(formEle);
            }
        }

        xhr.send();
}

function getCoursesAndUsers(callingObj) {
    getAllCourses(callingObj);
    getAllUsers(callingObj);
}

//TODO: get working
function deleteUsers(callingObj) {
    console.log("Submit form to delete users!");
    var checkboxes = document.getElementsByClassName("checked-users");
    var checkboxesChecked = [];
    
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', '/admin/listUsers', true);
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
            // loop over all checkboxes
            for (var i=0; i<checkboxes.length; i++) {
                // And stick the checked ones onto an array...
                if (checkboxes[i].checked) {
                    checkboxesChecked.push(checkboxes[i].value);
                }
            }
            
        }
    }

    var jsonToSend = JSON.stringify(checkboxesChecked);
    xhr.send(jsonToSend);
    return false;
}