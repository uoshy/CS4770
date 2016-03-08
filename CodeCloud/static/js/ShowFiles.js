function showFiles(elementID){
	//Does not expect a "/" on elementID input
	if (elementID.charAt(elementID.length - 1) == '/'){
		elementID = elementID.substring(0, elementID.length);
	}
	var xhr = new XMLHttpRequest();
	xhr.open('POST', "/files/view", true);
	xhr.setRequestHeader("Content-Type", "text/plain");
	var pathParts = elementID.split('/');
	if (pathParts[pathParts.length - 1].indexOf('.') == -1){
		//Works only if no directories have '.'s in their names; should be made more robust
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
				var imgString;
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
						img.setAttribute('hspace', '23');
						img.setAttribute('width', 25);
						img.setAttribute('height', 25);
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
	console.log("elementID to send: " + elementID);
	xhr.send(elementID);
}

function back(){
	var titleRef = document.getElementById('hTitle').innerHTML;
	var pathParts = titleRef.split("/");
	var newPath = "";
	for (var i = 0; i < pathParts.length - 2; i++){
		newPath += pathParts[i] + "/";
	}
	newPath = newPath.substring(0, newPath.length - 1);
	console.log("back() showFiles() call: " + newPath);
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
