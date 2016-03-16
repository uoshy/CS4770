
(function fillSidebar() {	
	var xhr = new XMLHttpRequest();
	xhr.open('GET', 'sidebar.html');
	xhr.onreadystatechange = function() {
		console.log(xhr.readyState);
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400) 
        {
        	var divHolder = document.getElementById("sidebarHolderDiv");
        	console.log(divHolder);
        	divHolder.innerHTML = xhr.responseText;

            getUserDetails();
        }
	}
	xhr.send();
})();

var INSTRUCTOR = 0;
var MARKER = 1;
var STUDENT = 2;

function getUserDetails() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "users/activeUser", true);
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
            var jsonObj = JSON.parse(xhr.responseText);
            if(jsonObj.activeRole != -1)
            {
                document.getElementById('sidebarUser').innerHTML = jsonObj.username;
                var activeRoleSpan = document.getElementById('sidebarActiveRole');
                if(jsonObj.activeRole === INSTRUCTOR)
                    activeRoleSpan.innerHTML = "Instructor";
                else if(jsonObj.activeRole === MARKER)       
                    activeRoleSpan.innerHTML = "Marker";
                else if(jsonObj.activeRole === STUDENT)
                    activeRoleSpan.innerHTML = "Student";
                var options = document.getElementById('sidebarRoleSelect');
                for(var i = 0; i < jsonObj.possibleRoles.length; i++)
                {
                    if(jsonObj.possibleRoles[i] === INSTRUCTOR)
                        options.innerHTML += "<option>Instructor</option>";
                    else if(jsonObj.possibleRoles[i] === MARKER)
                        options.innerHTML += "<option>Marker</option>";
                    else if(jsonObj.possibleRoles[i] === STUDENT)
                        options.innerHTML += "<option>Student</option>";

                }

                document.getElementById('sidebarChangeRoleButton').addEventListener("click", changeUserRole);
            }
        }
    }
    xhr.send();
}

function changeUserRole(evt) {
    evt.preventDefault();
    console.log("change user role!");

    var xhr = new XMLHttpRequest();
    xhr.open('POST', "users/changeRole", true);
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
            console.log('response: ' + xhr.responseText);
            if(xhr.responseText == 0) //it worked
            {
                var select = document.getElementById('sidebarRoleSelect');
                document.getElementById("sidebarActiveRole").innerHTML = select.value;
            }
        }
    }

    var select = document.getElementById('sidebarRoleSelect');
    var toSend;
    if(select.value === "Instructor")
        toSend = INSTRUCTOR;
    else if(select.value === "Marker")
        toSend = MARKER;
    else if(select.value === "Student")
        toSend = STUDENT;
    
    console.log("Sending user role:: " + toSend);
    xhr.send(toSend);

}