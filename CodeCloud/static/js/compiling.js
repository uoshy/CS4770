

function initCompiling()
{
    console.log("init");
    var inputField = document.getElementById("consoleInput");
    inputField.addEventListener("keyup", consoleInputListener);

    var form = document.getElementById("textEditorForm");
    console.log(form.language.value);
    currentLanguage = form.language.value;
    editor.setOption("mode", "text/x-java");

}

var currentLanguage;
function switchLanguage(radioBtn)
{
    if(radioBtn.value != currentLanguage)
    {
        currentLanguage = radioBtn.value;
        var text;
        if(radioBtn.value === "Java")
        {   
            editor.setOption("mode", "text/x-java");
            text = document.getElementById("javaExample").textContent;
        }
        else if(radioBtn.value === "C++")
        {
            editor.setOption("mode", "text/x-c++src");
            text = document.getElementById("cppExample").textContent;
        }
        //editor.setValue(text);
    }
    console.log(currentLanguage);
}

function saveEditorFile(beforeExecution) {

    var form = document.getElementById("textEditorForm");
    var fileName = form.fileName.value
    console.log(fileName);
    if(fileName.length < 1)
    {
        window.alert("File name cannot be empty!");
        return;
    }
    
    var editorText = editor.getValue();
    var xhr = new XMLHttpRequest();
    
    xhr.open('POST', "editor/saveFile", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400) {
            var saveResponse = document.getElementById("saveFileMessage");
            if(xhr.responseText != '0')
                saveResponse.innerHTML = "Error saving file!";
            else
                saveResponse.innerHTML = "File Saved!";
        }
    }
  
    var objToSend = { fileContent : editorText, fileName : fileName};
    var jsonToSend = JSON.stringify(objToSend);
    xhr.send(jsonToSend);
}


function compile(beforeExecution) {
    compilerStatus = -1;

    var success = saveTxt(false);
    console.log("compile save success:" + success);
    if(success != 0)
        return;

    console.log("file saved");

    // var form = document.getElementById("textEditorForm");
    // var fileName = form.fileName.value
    // console.log(fileName);
    // if(fileName.length < 1)
    // {
    //     window.alert("File name cannot be empty!");
    //     return;
    // }

    var status = document.getElementById("consoleStatus");
    status.innerHTML = "Compiling..."
    
    var editorText = editor.getValue();
    var xhr = new XMLHttpRequest();
    var compilePath = "";
    if(currentLanguage === "Java")
        compilePath = "editor/compile/java";
    else if(currentLanguage === "C++")
        compilePath = "editor/compile/cpp";

    xhr.open('POST', compilePath, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400) {
            status.innerHTML = "Compiled."
            var jsonObj = JSON.parse(xhr.responseText);
            var compilerExitStatus = jsonObj.compilerExitStatus;
            var messageToDisplay = jsonObj.compilerMessageToDisplay;
            // server responds with a text/html body
            var webConsole = document.getElementById("consoleOutput");
            webConsole.innerHTML = messageToDisplay;
            var compilerStatus = compilerExitStatus;
            if(beforeExecution && compilerStatus == 0)
            {
                var path = document.getElementById("hTitle").innerHTML;
                console.log("caputed workingDir: " + workingDir);
                if(currentLanguage == "Java")
                    execute(fileName, workingDir);
                else
                    execute("a.out", workingDir);
            }
        } else {
            console.log("ERROR in compile!");
        }
    }

//previously used to parse test and get class name
    // var lowerText = editorText.toLowerCase();
    // var startIndex = lowerText.indexOf("public class");
    // var firstSpaceIndex = lowerText.indexOf(" ", startIndex+12);
    // var secondSpaceIndex = lowerText.substr(firstSpaceIndex+1).search(/\s/) + firstSpaceIndex+1;
    // console.log(lowerText.substr(firstSpaceIndex+1));
    // console.log(firstSpaceIndex);
    // console.log(secondSpaceIndex);
    // var className = editorText.substring(firstSpaceIndex+1, secondSpaceIndex).trim();
    // var lastChar = className.charAt(className.length-1);
    // if(lastChar == "{")
    //     className = className.substring(0,className.length-1);
    // console.log(className);

  
    //var objToSend = { fileContent : editorText, fileName : fileName};
    var path = document.getElementById("hTitle").innerHTML;
    var workingDir = "";
    var fileName = "";
    var pathParts = path.split("/");
    for(var i = 0; i < pathParts.length - 2; i++)
        workingDir += pathParts[i] + "/";
    fileName = pathParts[pathParts.length - 2];
    var objToSend = {workingDir: workingDir, fileName : fileName};
    var jsonToSend = JSON.stringify(objToSend);
    xhr.send(jsonToSend);
}

var exitStatus = 0;
var processID = -1;
var asyncReadTimer = -1;

function execute(fileName, directory) 
{   
    var webConsole = document.getElementById("consoleOutput");
    webConsole.innerHTML = "";
    exitStatus = -1;
    var status = document.getElementById("consoleStatus");
    status.innerHTML = "Running...";
    var killButton = document.getElementById("consoleKillButton");
    killButton.style.display = "inline-block";
    console.log(killButton);

    var xhr = new XMLHttpRequest();

    if(currentLanguage === "Java")
        executePath = "editor/execute/java";
    else if(currentLanguage === "C++")
        executePath = "editor/execute/cpp";

    console.log("execute path: " + executePath);
    xhr.open('POST', executePath , true);
    xhr.setRequestHeader("Conent-Type", "application/json");
    xhr.onreadystatechange = executionResponseHandler;

    var objToSend = { fileName: fileName, workingDirPath : directory};

    var jsonToSend = JSON.stringify(objToSend);
    xhr.send(jsonToSend);  
}

function executionResponseHandler() {

    if(this.readyState != 4) return;
    if(this.status == 200 || this.status == 400) {
        console.log(this.responseText);
        var jsonObj = JSON.parse(this.responseText);
        exitStatus = jsonObj.exitStatus;
        if(exitStatus == -1)
        {
            processID = jsonObj.processID;
            setTimeout(readFromProgram,300);
        }
        else
        {
            var status = document.getElementById("consoleStatus");
            status.innerHTML = "Finished Running.";
            var killButton = document.getElementById("consoleKillButton");
            killButton.style.display = "none";
            processID = -1;
        }
        var outputText = jsonObj.outputText;
        var webConsole = document.getElementById("consoleOutput");
        if(outputText.length >= 1)
        {
            webConsole.innerHTML += outputText;
            webConsole.scrollTop = webConsole.scrollHeight; //scroll to the buttom
        }
        if(exitStatus != -1)
        {
            webConsole.innerHTML += "\nExit Status: " + exitStatus + "\n";
        }



    } else {
        console.log("ERROR in execution!");
    }
}

function kill() {
    if(exitStatus == -1)
    {
        if(processID != -1)
        {
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'editor/execute/active/kill/' + processID, true);
            xhr.onreadystatechange = executionResponseHandler;
            xhr.send();
        }
    }
}

function readFromProgram() {
    if(exitStatus == -1) //program still active
    {
        if(processID == -1) //waiting for first execute request to return
        {
            setTimeout(readFromProgram, 200);
            return;
        }
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'editor/execute/active/readOutput/' + processID, true);
        xhr.onreadystatechange = executionResponseHandler;
        xhr.send();
    }
}

function consoleInputListener(event)
{
    event.preventDefault();
    console.log("up");
    if(event.keyCode == 13 && exitStatus == -1)
    {
        waiting = true;
        checkProcess(sendInput);
        setTimeout(stopWaiting, 3100);
    }

}

function checkProcess(callback)
{
    if(processID == -1)
        wait(callback);
    else
        callback();
}

var waiting = false;

function wait(callback)
{
    if(waiting)
        setTimeout(function(){
            checkProcess(callback);
        },200);
}

function stopWaiting()
{
    waiting = false;
}

function sendInput()
{
    var inputConsole = document.getElementById("consoleInput");
    var outputConsole = document.getElementById("consoleOutput");
    var input = inputConsole.value;
    outputConsole.innerHTML += input + "\n";
    inputConsole.value = "";
    outputConsole.scrollTop = outputConsole.scrollHeight; //scroll to the buttom

    var xhr = new XMLHttpRequest();
    xhr.open('POST', "editor/execute/active/writeInput/" + processID, true);
    xhr.setRequestHeader("Content-Type", "text/plain");
    xhr.onreadystatechange = function() {

        if(xhr.readyState != 4) return;
        if(xhr.status == 200 || xhr.status == 400) {
            console.log('input successfully sent!');
        }
    }

    xhr.send(input);
}
