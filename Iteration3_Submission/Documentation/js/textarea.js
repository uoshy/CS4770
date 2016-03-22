
var myTextarea = document.getElementById('editor');
var CodeMirrorEditor = CodeMirror.fromTextArea(myTextarea, {
    mode: "text/javascript",
    lineNumbers: true ,
    undoDepth: 200,
    dragDrop: true;
});