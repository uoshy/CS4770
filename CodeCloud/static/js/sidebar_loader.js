
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
                if(jsonObj.activeRole === 0)
                    activeRoleSpan.innerHTML = "Instructor";
                else if(jsonObj.activeRole === 1)       
                    activeRoleSpan.innerHTML = "Marker";
                else if(jsonObj.activeRole === 2)
                    activeRoleSpan.innerHTML = "Student";

                var options = document.getElementById('sidebarRoleSelect');
                for(int i = 0; i < jsonObj.possibleRoles.length; i++)
                {
                    if(jsonObj.possibleRoles[i] === 0)

                    else if(jsonObj.possibleRoles[i] === 1)

                    else if(jsonObj.possibleRoles[i] === 2)

                }
            }
        }
    }
    xhr.send();
}