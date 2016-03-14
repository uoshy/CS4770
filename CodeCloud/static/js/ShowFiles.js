function showFiles(elementID){
	//Does not expect a "/" on elementID input
	document.getElementById("newDirName").value = "";
	if (elementID.charAt(elementID.length - 1) == '/'){
		elementID = elementID.substring(0, elementID.length - 1);
	}
	if (elementID != "static"){
		console.log("Not static");
	}
	var xhr = new XMLHttpRequest();
	xhr.open('POST', "/files/view", true);
	xhr.setRequestHeader("Content-Type", "text/plain");
	var pathParts = elementID.split('/');
	if (!isRecognizedFileType(pathParts[pathParts.length - 1])){
		xhr.onreadystatechange = function(){
			if (xhr.readyState != 4) return;
			if (xhr.status == 200 || xhr.status == 400){
				console.log("Header on XML response: " + document.getElementById("hTitle").innerHTML);
				document.getElementById("hTitle").innerHTML = elementID + "/";
				var jsonObj = JSON.parse(xhr.responseText);
				if (jsonObj == "" || typeof jsonObj == 'undefined'){
					console.log("Null response");
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
					a.setAttribute('onclick', 'showFiles(\"' + document.getElementById('hTitle').innerHTML + jsonObj.fileObjs[i].fileName + '\")');
					a.innerHTML = document.getElementById('hTitle').innerHTML + jsonObj.fileObjs[i].fileName;
					li.appendChild(img);
					li.appendChild(a);
					list.appendChild(li);
				}
			}
		}
	}
	else {
		//TODO fix this
		/*
		xhr.onreadystatechange = function(){
			if (xhr.readyState != 4) return;
			if (xhr.status == 200 || xhr.status == 400){
				document.getElementById("hTitle").innerHTML += elementID;
				document.getElementById("filesList").style.display = 'none';
				var jsonObj = JSON.parse(xhr.responseText);
				if (jsonObj != ""){
					if (jsonObj.fileObjs.isDirectory){
						//TODO: Handle files not displayable in iframe
						document.getElementById('frame').style.display = 'block';
						document.getElementById('frame').setAttribute('src', jsonObj[1]);
					}
					else {
						document.getElementById('editor').style.display = 'block';
						editor.setValue(jsonObj[1]);
					}
				}
			}
		}
		*/
	}
	xhr.send(elementID);
}

function isRecognizedFileType(endingString){
	if (endingString.indexOf('.html') != -1 || endingString.indexOf('.js') != -1|| endingString.indexOf('.txt') != -1|| endingString.indexOf('.doc') != -1|| endingString.indexOf('.docx') != -1|| endingString.indexOf('.pdf') != -1|| endingString.indexOf('.xml') != -1|| endingString.indexOf('.xlsx') != -1|| endingString.indexOf('.ppt') != -1) return true;
	return false;
}

function back(){
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
			a.setAttribute('onclick', 'showFiles(\"' + document.getElementById('hTitle').innerHTML + dName + '\")');
			a.innerHTML = document.getElementById('hTitle').innerHTML + dName;
			li.appendChild(img);
			li.appendChild(a);
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
	console.log("Got to xhr creation");
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
	console.log(path);
	if (path === "static") return true;
	var secondPart = path.split("/")[1];
	if (secondPart === "codemirror-5.12" || secondPart === "courses" || secondPart === "css" || secondPart === "font-awesome-4.5.0" || secondPart === "fonts" || secondPart === "img" || secondPart === "js" || secondPart === "temp") return true;
	return false;
}


















