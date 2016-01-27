var colours = [ "red", "green", "blue",
    "cyan", "magenta", "yellow" ];

// global variable
var index = -1;

function genPara( id ) {
    index = (index+1) % colours.length;
    var o = "<p style='color:";
    o += colours[ index ];
    o += ";'> ";
    o += colours[ index ];
    o += "</p>";
    var e = document.getElementById( id );
    e.innerHTML = e.innerHTML + o;
}
