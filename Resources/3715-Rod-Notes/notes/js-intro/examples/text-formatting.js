var formatElement; // initially undefined
function getFE() {
    if ( formatElement ) return formatElement;
    formatElement = document.getElementById('format-area' );
    return formatElement;
}

function setColour( colour ) {
    var e = getFE();
    e.style.color = colour;
}

function setBackgroundColour( colour ) {
    var e = getFE();
    e.style.background = colour;
}

function setFont( font ) {
    var e = getFE();
    e.style.fontFamily = font;
}

function setFontSize( size ) {
    var e = getFE();
    e.style.fontSize = size;
}
