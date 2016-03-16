
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
        }
	}
	xhr.send();
})();