
var myTextarea = document.getElementById('editorTextArea');
console.log(myTextarea);
var CodeMirrorEditor = CodeMirror.fromTextArea(myTextarea, {
    mode: "text/javascript",
    lineNumbers: true ,
    undoDepth: 200,
    matchBrackets: true,
    styleActiveLine: true,
    dragDrop: true
});