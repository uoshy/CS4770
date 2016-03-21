function upload() {
	console.log("uploading");
	var file = document.getElementById('fileChooser');
	if (typeof file.files[0] === 'undefined') return;
	var form = new FormData();
	console.log(file.files[0]);
	console.log(document.getElementById('hTitle').innerHTML + file.files[0].name);
	form.append("file", file.files[0], file.files[0].name);
	var newPath = "";
	var pathParts = document.getElementById('hTitle').innerHTML.split("/");
	//Change '/'s to '|'s to avoid spark errors
	for (var i = 0; i < pathParts.length - 1; i++){
			newPath += pathParts[i] + "|";
	}

	var xhr = new XMLHttpRequest();
	xhr.open('POST', "/files/upload/" + newPath, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) return;
		if (xhr.status == 200 || xhr.status == 400){
			console.log(xhr.responseText);
			if (xhr.responseText.length > 1){
				var path = document.getElementById('hTitle').innerHTML;
				path = path.substring(0, path.length - 1);
				//"Refresh" the page so the added file/directory is visible to the user
				showFiles(path);
			}
			else if (xhr.responseText === "0"){
				//alert("File aready exists");
			}
			else if (xhr.responseText === "2"){
				alert("Authorization failure");
			}
			else {
				alert("Upload failed");
			}
		}
        }
	xhr.send(form);
}
