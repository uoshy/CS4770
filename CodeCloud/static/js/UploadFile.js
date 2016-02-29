function upload() {
	var file = document.getElementById('filename');
	var form = new FormData();
	console.log(file.files[0]);
	form.append("file", file.files[0], file.files[0].name);
	var xhr = new XMLHttpRequest();
	xhr.open('POST', "files/upload", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState != 4) return;
		if (xhr.status == 200 || xhr.status == 400){
			console.log(xhr.responseText);
			if (xhr.responseText.length > 1){
				var link = document.getElementById("downloadLink");
				link.href = xhr.responseText;
				link.innerHTML = "Download here";
			}
			else {
				alert("Upload failed");
			}
		}
        }
	xhr.send(form);
}
