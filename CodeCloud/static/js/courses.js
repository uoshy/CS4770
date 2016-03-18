


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
	    			courseLI.innerHTML = "<a href=/courses/course.html?courseTerm=" + response.courseTerms[i] + 
                                                    "&courseID=" + response.courseIDs[i] + 
                                                    "&role=" + activeRoleSpan.innerHTML + ">"
	    									        + response.courseIDs[i] + ": " + response.courseNames[i]+"</a>";
	    		else
    				courseLI.innerHTML = response.courseIDs[i] + ": " + response.courseNames[i];
    			currentTermUL.appendChild(courseLI);
    		}
        }
    }

    xhr.send();


}

function initCoursePage()
{
    var courseID = getQueryVariable("courseID");
    var courseTerm = getQueryVariable("courseTerm");
    var courseName = getQueryVariable("courseName");
    document.getElementById("courseHeader").innerHTML = courseID + " " + courseName;

    var assignLink = document.getElementById("assignmentsLink");
    assignLink.href = "/courses/assignments.html" + window.location.search;
    getCourseTitle(courseID, courseTerm);
}

function initAssignmentsPage()
{
    var courseID = getQueryVariable("courseID");
    var courseTerm = getQueryVariable("courseTerm");
    var courseName = getQueryVariable("courseName");
    document.getElementById("courseHeader").innerHTML = courseID + " " + courseName;

    var assignLink = document.getElementById("courseContentLink");
    assignLink.href = "/courses/course.html" + window.location.search;
    getCourseTitle(courseID, courseTerm);
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

function getCourseTitle(courseID, courseTerm)
{
    var xhr = new XMLHttpRequest();
                    // /courses/courseName/:courseID/:courseTerm
    xhr.open('GET', '/courses/courseName/'+courseID +'/' + courseTerm, 'true');
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
            document.getElementById("courseHeader").innerHTML = courseID + ": " + xhr.responseText;
        }
    }
    xhr.send();
}

var courseRootDir = "";
function init() {
    console.log("init files for course");
    document.getElementById('editor').style.display = 'none';
    document.getElementById('frame').style.display = 'none';

    if(window.location.pathname.indexOf("assignments") != -1)
        courseRootDir = "static/courses/" + getQueryVariable("courseTerm") + "/" + getQueryVariable("courseID") + "/assignments/";
    else
        courseRootDir = "static/courses/" + getQueryVariable("courseTerm") + "/" + getQueryVariable("courseID") + "/content/";

    setupForRole();


    showFiles(courseRootDir);

}


var activeRole = -1;
function setupForRole()
{
    console.log("setting up for role");
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "/users/activeUser", true);
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
            var jsonObj = JSON.parse(xhr.responseText);
            activeRole = jsonObj.activeRole;
            if(jsonObj.activeRole != -1)
            {
                var uploadControls = document.getElementById("uploadControls");
                if(jsonObj.activeRole === INSTRUCTOR)
                {
                    uploadControls.style.display = "block";
                    editor.readOnly = false;
                }
                else if(jsonObj.activeRole === MARKER || jsonObj.activeRole === STUDENT)   
                {    
                    uploadControls.style.display = "none"
                    editor.readOnly = true;
                }
            }
        }
    }
    xhr.send();
    
}

function showFiles(elementID){
    //Does not expect a "/" on elementID input
    document.getElementById("newDirName").value = "";
    if (elementID.charAt(elementID.length - 1) == '/'){
        elementID = elementID.substring(0, elementID.length - 1);
    }
    var pathParts = elementID.split('/');
    
    if (!isRecognizedFileType(pathParts[pathParts.length - 1])){
        var xhr = new XMLHttpRequest();
        xhr.open('POST', "/files/view", true);
        xhr.setRequestHeader("Content-Type", "text/plain");
        xhr.onreadystatechange = function(){
            if (xhr.readyState != 4) return;
            if (xhr.status == 200 || xhr.status == 400){
                console.log("Header on XML response: " + document.getElementById("hTitle").innerHTML);
                document.getElementById("hTitle").innerHTML = elementID + "/";
                var jsonObj = JSON.parse(xhr.responseText);
                console.log(jsonObj);
                if (jsonObj == "authFail" || jsonObj[0] == "authFail" || typeof jsonObj == 'undefined'){
                    console.log("Authorization failure");
                    return;
                }
                var list = document.getElementById("filesList");
                list.innerHTML = "";
                for (var i = 0; i < jsonObj.fileObjs.length; i++){
                    var img = document.createElement("img");
                    img.setAttribute('alt', jsonObj.fileObjs[i].fileName);
                    img.setAttribute('width', 75);
                    img.setAttribute('height', 75);

                    var a = document.createElement('a');

                    if (jsonObj.fileObjs[i].isDirectory){
                        img.setAttribute('src', '/img/folderImage.png');
                        img.setAttribute('class', 'folder');
                        img.setAttribute('width', 75);
                        img.setAttribute('height', 75);
                        a.setAttribute('onclick', 'showFiles(\"' + document.getElementById('hTitle').innerHTML + jsonObj.fileObjs[i].fileName + '\")');

                    }
                    else {
                        img.setAttribute('src', '/img/fileImage.png');
                        img.setAttribute('class', 'file');
                        img.setAttribute('hspace', 23);
                        img.setAttribute('width', 25);
                        img.setAttribute('height', 25);
                        img.setAttribute('vSpace', 25);
                        var filePath = document.getElementById("hTitle").innerHTML;
                        var index = filePath.indexOf("static");
                        filePath = filePath.substring(index + 6);
                        filePath = filePath + jsonObj.fileObjs[i].fileName;

                        a.setAttribute('href', filePath);

                    }
                    var li = document.createElement('li');
                    var space = document.createTextNode('\u00A0\u00A0\u00A0');

                    a.innerHTML = document.getElementById('hTitle').innerHTML + jsonObj.fileObjs[i].fileName;

                    li.appendChild(img);
                    li.appendChild(a);
                    li.appendChild(space);

                    if(activeRole === INSTRUCTOR)
                    {
                        var button = document.createElement('input');
                        button.setAttribute('type', 'button');
                        button.setAttribute('value', 'Delete');
                        button.addEventListener('click', deleteFileDelegate(a.innerHTML), false);
                        li.appendChild(button);
                    }
                    list.appendChild(li);
                }
            }
        }
        xhr.send(elementID);
    }
    else if (isIframeCompatible(pathParts[pathParts.length - 1])){
        console.log("Opening a document in iframe...");
        var temp = elementID.substring(7);
        console.log(elementID);
        document.getElementById("hTitle").innerHTML = elementID + "/";
        hideFiles();
        document.getElementById("frame").setAttribute('src', temp);
        document.getElementById("frame").style.display = 'inline';
    }
    else if (pathParts[pathParts.length - 1].substring(pathParts[pathParts.length - 1].length - 4) === ".txt"){
        console.log("Opening text file...");
        document.getElementById("hTitle").innerHTML = elementID + "/";
        hideFiles();
        var xhr = new XMLHttpRequest();
        xhr.open('POST', "/files/getcontents", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function(){
            if (xhr.readyState != 4) return;
            if (xhr.status == 200 || xhr.status == 400){
                console.log("Success");
                var jsonObj = JSON.parse(xhr.responseText);
                console.log(jsonObj);
                console.log(jsonObj[0]);
                console.log(jsonObj[1]);
                document.getElementById("editor").style.display = 'inline';
                editor.setValue(jsonObj[1]);
            }
        }
        xhr.send(elementID);
    }

    else {
        alert("Error: Unknown file type");
    }
}

function isRecognizedFileType(endingString){
    if (endingString.indexOf('.html') != -1 || endingString.indexOf('.js') != -1|| endingString.indexOf('.txt') != -1|| endingString.indexOf('.doc') != -1|| endingString.indexOf('.docx') != -1|| endingString.indexOf('.pdf') != -1|| endingString.indexOf('.xml') != -1|| endingString.indexOf('.xlsx') != -1|| endingString.indexOf('.ppt') != -1) return true;
    return false;
}

function back(){
    document.getElementById("addDelete").style.display = 'inline';
    document.getElementById("uploadFile").style.display = 'inline';
    document.getElementById("downloadLink").style.display = 'inline';
    document.getElementById("filesList").style.display = 'inline';
    document.getElementById("editor").style.display = 'none';
    document.getElementById("frame").style.display = 'none';

    var titleRef = document.getElementById('hTitle').innerHTML;
    console.log(titleRef);
    console.log(courseRootDir);
    if (titleRef === courseRootDir) return;
    var pathParts = titleRef.split("/");
    var newPath = "";
    for (var i = 0; i < pathParts.length - 2; i++){
        newPath += pathParts[i] + "/";
    }
    newPath = newPath.substring(0, newPath.length - 1);
    showFiles(newPath);
    newPath = "";
    for (var i = 0; i < pathParts.length - 3; i++){
        newPath += pathParts[i] + "/";
    }
    document.getElementById('hTitle').innerHTML = newPath;
}

function addDir(){
    var dName = document.getElementById("newDirName").value;
    document.getElementById("newDirName").value = "";
    var ul = document.getElementById("filesList").getElementsByTagName("li");
    /*
    if (dName === "" || dName.match(/^[0-9a-zA-Z]+$/)){
        alert("Error: Invalid name. You may only use alphanumeric characters.");
        return;
    }**/
    for (var i = 0; i < ul.length; i++){
        if (ul[i].getElementsByTagName("img")[0].getAttribute("class") === "folder" && ul[i].getElementsByTagName("a")[0].innerHTML.indexOf(document.getElementById("hTitle").innerHTML + dName) != -1){
            console.log(ul[i].innerHTML);
            alert("Error: Directory already exists.");
            return;
        }
    }
    if (isRecognizedFileType(dName)){
        alert("Error: Invalid directory name");
        return;
    }
    var xhr = new XMLHttpRequest();
    xhr.open('POST', "/files/add", true);
    xhr.setRequestHeader("Content-Type", "text/plain");
    xhr.onreadystatechange = function(){
        if (xhr.readyState != 4) return;
        if (xhr.status == 200 || xhr.status == 400){
            var jsonObj = JSON.parse(xhr.responseText);
            console.log(jsonObj);
            var img = document.createElement("img");
            img.setAttribute('alt', dName);
            img.setAttribute('width', 75);
            img.setAttribute('height', 75);
            img.setAttribute('src', '/img/folderImage.png');
            img.setAttribute('class', 'folder');
            img.setAttribute('width', 75);
            img.setAttribute('height', 75);
            var list = document.getElementById("filesList");
            var li = document.createElement('li');
            var a = document.createElement('a');
            var button = document.createElement('input');
            a.setAttribute('onclick', 'showFiles(\"' + document.getElementById('hTitle').innerHTML + dName + '\")');
            a.innerHTML = document.getElementById('hTitle').innerHTML + dName;
            button.setAttribute('type', 'button');
            button.setAttribute('value', 'Delete');
            button.addEventListener('click', deleteFileDelegate(a.innerHTML), false);
            li.appendChild(img);
            li.appendChild(a);
            li.appendChild(button);
            list.insertBefore(li, list.firstChild);
        }
    }
    xhr.send(document.getElementById("hTitle").innerHTML + dName);
}

function addAssignment() {
    var xhr = new XMLHttpRequest();
    var requestPath = '/courses/addAssignment/';
    requestPath += getQueryVariable("courseID");
    requestPath += "/" + getQueryVariable("courseTerm");
    xhr.open('POST', requestPath, true);
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400)
        {
            if(xhr.responseText === "0")
                window.location.reload();
            else
                console.log("Adding assignment failed!");
        }
    }

    var subLimit = document.getElementById("submissionLimitInput").value;
    var intSubLimit = parseInt(subLimit)
    if(isNaN(intSubLimit))
        window.alert('Submission Limit is not a valid number');
    else
        xhr.send(subLimit);
}

function deleteCurrentDir(){
    deleteFile("");
}

function deleteFile(path){
    var currentDir = false;
    if (path === ""){
        path = document.getElementById('hTitle').innerHTML;
        currentDir = true;
    }
    if (shouldntBeDeleted(path)){
        alert("Error: You can not delete this file");
        return;
    }
    var xhr = new XMLHttpRequest();
    xhr.open('POST', "/files/delete", true);
    xhr.setRequestHeader("Content-Type", "text/plain");
    xhr.onreadystatechange = function(){
        if (xhr.readyState != 4) return;
        if (xhr.status == 200 || xhr.status == 400){
            console.log("Success");
            var jsonObj = JSON.parse(xhr.responseText);
            console.log(jsonObj);
            var parent;
            var child;
            if (!currentDir){
                parent = document.getElementById("filesList");
                for (var i = 0; i < parent.getElementsByTagName("li").length; i++){
                    if (parent.getElementsByTagName("li")[i].innerHTML.indexOf(path) != -1){
                        parent.removeChild(parent.getElementsByTagName("li")[i]);
                    }
                }
            }
            if (currentDir) back();
        }
    }
    xhr.send(path);
}

function shouldntBeDeleted(path){
    if (path.charAt(path.length - 1) == '/') path = path.substring(0, path.length - 1);
    if (path === "static") return true;
    var secondPart = path.split("/")[1];
    if (secondPart === "codemirror-5.12" || secondPart === "courses" || secondPart === "css" || secondPart === "font-awesome-4.5.0" || secondPart === "fonts" || secondPart === "img" || secondPart === "js" || secondPart === "temp") return true;
    return false;
}

//Fix scoping for adding listeners dynamically
function deleteFileDelegate(path){
    return function(){deleteFile(path)};
}

function isIframeCompatible(extension){
    if (extension.length >= 4){
        if (extension.substring(extension.length - 4) === ".pdf" || extension.substring(extension.length - 4) === ".ppt" || extension.substring(extension.length - 5) === ".docx" || extension.substring(extension.length - 5) === ".xlsx" || extension.substring(extension.length - 4) === ".doc") return true;
    }
    return false;
}

function hideFiles(){
    document.getElementById("addDelete").style.display = 'none';
    document.getElementById("uploadFile").style.display = 'none';
    document.getElementById("downloadLink").style.display = 'none';
    document.getElementById("filesList").style.display = 'none';
}

















