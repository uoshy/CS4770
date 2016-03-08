
var myTextarea = document.getElementById('editorTextArea');
console.log(myTextarea);
var editor = CodeMirror.fromTextArea(myTextarea, {
    mode: "text/javascript",
    lineNumbers: true ,
    undoDepth: 200,
    matchBrackets: true,
    styleActiveLine: true,
    dragDrop: true,
});

var input = document.getElementById("select");
function selectTheme() {
    var theme = input.options[input.selectedIndex].textContent;
    editor.setOption("theme", theme);
    location.hash = "#" + theme;
}
var choice = (location.hash && location.hash.slice(1)) ||
           (document.location.search &&
            decodeURIComponent(document.location.search.slice(1)));
if (choice) {
    input.value = choice;
    editor.setOption("theme", choice);
}
CodeMirror.on(window, "hashchange", function() {
    var theme = location.hash.slice(1);
    if (theme) { input.value = theme; selectTheme(); }
});
selectTheme();
editor.refresh();
init();
