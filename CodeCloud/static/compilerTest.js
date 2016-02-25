var compilerStatus = -1;

function compile(var beforeExecution) {
    compilerStatus = -1;
    var editor = document.getElementById("editor");
    var editorText = editor.innerHTML;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', "editor/compile/java", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400) {
            var jsonObj = JSON.parse(xhr.responseText);
            var compilerExitStatus = jsonObj.compilerExitStatus;
            var messageToDisplay = jsonObj.compilerMessageToDisplay;
            // server responds with a text/html body
            var webConsole = document.getElementById("console");
            webConsole.innerHTML = messagToDisplay;
            compilerStatus = compilerExitStatus;
            if(beforeExecution && compilerStatus == 0)
                execute(className);
        } else {
            console.log("ERROR in compile!");
        }
    }

    var lowerText = editorText.toLowerCase();
    var startIndex = lowerText.indexOf("public class");
    var firstSpaceIndex = lowerText.indexOf(" ", startIndex+12);
    var secondSpaceIndex = lowerText.substr(firstSpaceIndex+1).search(/\s/) + firstSpaceIndex+1;
    console.log(lowerText.substr(firstSpaceIndex+1));
    console.log(firstSpaceIndex);
    console.log(secondSpaceIndex);
    var className = editorText.substring(firstSpaceIndex+1, secondSpaceIndex).trim();
    console.log(className);
    var objToSend = { fileContent : editorText, fileName : className};
    var jsonToSend = JSON.stringify(objToSend);
    xhr.send(jsonToSend);
}

function execute(var fileName, var directory) 
{   
    var webConsole = document.getElementById("console");
    webConsole.innerHTML = "";

    xhr.open('POST', "editor/execute/java", true);
    xhr.setRequestHeader("Conent-Type", "application/json");
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400) {
            var jsonObj = JSON.parse(xhr.responseText);
            var exitStatus = jsonObj.exitStatus;
            var processID = jsonObj.processID;
            var outputText = jsonObj.outputText;
            webConsole.innerHTML += outputText();

        } else {
            console.log("ERROR in execution!");
        }
    }

    var objToSend = { fileName: fileName, workingDirPath : directory};
    var jsonToSend = JSON.stringify(objToSend);
    xhr.send(obj);
    

}
