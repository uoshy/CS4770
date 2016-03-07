function upload() {
	var file = document.getElementById('filename');
	var form = new FormData();
	console.log(file.files[0]);
	console.log(document.getElementById('hTitle').innerHTML + file.files[0].name);
	form.append("file", file.files[0], file.files[0].name);
	var newPath = "";
	var pathParts = document.getElementById('hTitle').innerHTML.split("/");
	for (var i = 0; i < pathParts.length - 1; i++){
			newPath += pathParts[i] + "|";
	}

	var xhr = new XMLHttpRequest();
	xhr.open('POST', "files/upload/" + newPath, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) return;
		if (xhr.status == 200 || xhr.status == 400){
			console.log(xhr.responseText);
			if (xhr.responseText.length > 1){
				var link = document.getElementById("downloadLink");
				link.href = xhr.responseText;
				link.innerHTML = "Download here";
				var path = document.getElementById('hTitle').innerHTML;
				path = path.substring(0, path.length - 1) 
				showFiles(path);
			}
			else {
				alert("Upload failed");
			}
		}
        }
	xhr.send(form);
}
