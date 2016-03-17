function showFiles(elementID){
	//Does not expect a "/" on elementID input
	document.getElementById("mainMenu").value = "";
	document.getElementById("fileChooser").style.display = 'none';
	document.getElementById("newDirName").style.display = 'none';
	document.getElementById("menuButton").style.display = 'none';
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
					if (jsonObj.fileObjs[i].isDirectory){
						img.setAttribute('src', './img/folderImage.png');
						img.setAttribute('class', 'folder');
						img.setAttribute('width', 75);
						img.setAttribute('height', 75);
					}
					else {
						img.setAttribute('src', './img/fileImage.png');
						img.setAttribute('class', 'file');
						img.setAttribute('hspace', 23);
						img.setAttribute('width', 25);
						img.setAttribute('height', 25);
						img.setAttribute('vSpace', 25);
					}
					var li = document.createElement('li');
					var a = document.createElement('a');
					var space = document.createTextNode('\u00A0\u00A0\u00A0');
					var button = document.createElement('input');

					a.setAttribute('onclick', 'showFiles(\"' + document.getElementById('hTitle').innerHTML + jsonObj.fileObjs[i].fileName + '\")');
					a.innerHTML = document.getElementById('hTitle').innerHTML + jsonObj.fileObjs[i].fileName;

					button.setAttribute('type', 'button');
					button.setAttribute('value', 'Delete');
					button.addEventListener('click', deleteFileDelegate(a.innerHTML), false);

					li.appendChild(img);
					li.appendChild(a);
					li.appendChild(space);
					li.appendChild(button);
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
	document.getElementById("filesList").style.display = 'inline';
	document.getElementById("editor").style.display = 'none';
	document.getElementById("frame").style.display = 'none';

	var titleRef = document.getElementById('hTitle').innerHTML;
	if (titleRef === "static/") return;
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

function init(){
	var username = "";//TODO: Get username
	document.getElementById('hTitle').innerHTML = username;
	document.getElementById('editor').style.display = 'none';
	document.getElementById('frame').style.display = 'none';
	changeMenu();
	showFiles("static");
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
			img.setAttribute('src', './img/folderImage.png');
			img.setAttribute('class', 'folder');
			img.setAttribute('width', 75);
			img.setAttribute('height', 75);
			var list = document.getElementById("filesList");
			var li = document.createElement('li');
			var a = document.createElement('a');
			var space = document.createTextNode('\u00A0\u00A0\u00A0');
			var button = document.createElement('input');
			a.setAttribute('onclick', 'showFiles(\"' + document.getElementById('hTitle').innerHTML + dName + '\")');
			a.innerHTML = document.getElementById('hTitle').innerHTML + dName;
			button.setAttribute('type', 'button');
			button.setAttribute('value', 'Delete');
			button.addEventListener('click', deleteFileDelegate(a.innerHTML), false);
			li.appendChild(img);
			li.appendChild(a);
			li.appendChild(space);
			li.appendChild(button);
			list.insertBefore(li, list.firstChild);
		}
	}
	xhr.send(document.getElementById("hTitle").innerHTML + dName);
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

function changeMenu(){
	var menu = document.getElementById("mainMenu");
	var button_temp = document.getElementById("menuButton");
	var button = button_temp.cloneNode(true);
	button_temp.parentNode.replaceChild(button, button_temp);
	switch (menu.options[menu.selectedIndex].value){
		case "":
			document.getElementById("fileChooser").style.display = 'none';
			document.getElementById("newDirName").style.display = 'none';
			document.getElementById("menuButton").style.display = 'none';
			break;

		case "add":
			document.getElementById("fileChooser").style.display = 'none';
			document.getElementById("newDirName").style.display = 'inline';
			document.getElementById("menuButton").style.display = 'inline';
			document.getElementById("menuButton").value = 'Add';
			document.getElementById("menuButton").addEventListener('click', addDir, false);
			break;

		case "upload":
			document.getElementById("fileChooser").style.display = 'inline';
			document.getElementById("newDirName").style.display = 'none';
			document.getElementById("menuButton").style.display = 'inline';
			document.getElementById("menuButton").value = 'Upload';
			document.getElementById("menuButton").addEventListener('click', upload, false);
			break;

		case "delete":
			document.getElementById("fileChooser").style.display = 'none';
			document.getElementById("newDirName").style.display = 'none';
			document.getElementById("menuButton").style.display = 'inline';
			document.getElementById("menuButton").value = 'Delete';
			document.getElementById("menuButton").addEventListener('click', deleteCurrentDir, false);
			break;
	}
}

















