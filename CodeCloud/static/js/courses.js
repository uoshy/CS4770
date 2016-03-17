


function getAllCourses() {
	console.log("Getting courses...");
	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/courses/listCourses', true);
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
    		var response = JSON.parse(xhr.responseText);

            var activeRoleSpan = document.getElementById('sidebarActiveRole');

    		var coursesList = document.getElementById("courses-list");
    		console.log(response);
    		var prevTerm = "FOOBAR06";
    		var currentTermUL;
    		for(var i = response.courseNames.length-1; i >= 0; i--) //reverse chronological
    		{
    			if(response.courseTerms[i] != prevTerm)
    			{
    				var currentTermLI = document.createElement("li");
    				coursesList.appendChild(currentTermLI);
    				currentTermLI.innerHTML = "<b>" + response.courseTerms[i] + "</b>";
    				var currentTermUL = document.createElement("UL");
    				currentTermLI.appendChild(currentTermUL);
    			}

    			var courseLI = document.createElement("li");
    			if(response.courseIsActive[i])
	    			courseLI.innerHTML = "<a href=/courses/course.html?courseTerm=" + response.courseTerms[i] + "&courseID=" + response.courseIDs[i] + "&role=" + activeRoleSpan.innerHTML + ">"
	    									+ response.courseIDs[i] + ": " + response.courseNames[i]+"</a>";
	    		else
    				courseLI.innerHTML = response.courseIDs[i] + ": " + response.courseNames[i];
    			currentTermUL.appendChild(courseLI);
    		}
        }
    }

    xhr.send();


}

function getQueryVariable(variable)
{
   var query = window.location.search.substring(1);
   var vars = query.split("&");
   for (var i=0;i<vars.length;i++) {
           var pair = vars[i].split("=");
           if(pair[0] == variable){return pair[1];}
   }
   return(false);
}