(function(){

var phraseText = null;
var speakerData = null;
var dialogDiv = null;
var phraseCount = 0;

function doAddPhrase( evt ) {
    var url = 'chat/add?';
    url += 'speaker=' + escape(speakerData.value);
    url += '&phrase=' + escape(phraseText.value);
    phraseText.value = '';

    var req = new XMLHttpRequest();
    req.open("GET", url, true );
    req.onreadystatechange = function() {
        if ( req.readyState == 4) {
            if ( req.status == 200 ) {
                checkAdd( req );
            }
            else {
                alert( req );
            }
        }
    }
    req.send( null );
}

function checkAdd( req ) {
    var r = req.responseXML;
    var d = r.documentElement;
    if ( d.tagName == 'error' ) {
        alert( d.textContent );
    }
}

function doGetChat() {
    var url = 'chat/next?start=' + phraseCount;

    var req = new XMLHttpRequest();
    req.open("GET", url, true );
    req.onreadystatechange = function() {
        if ( req.readyState == 4) {
            if ( req.status == 200 ) {
                addChat( req );
            }
            else {
                alert( req );
            }
        }
    }
    req.send( null );
}

function addChat( req ) {
    var r = req.responseXML;
    if ( r == null ) return;
    var d = r.documentElement;
    if ( d.tagName == 'error' ) {
        alert( d.textContent );
        return;
    }
    else if ( d.tagName == 'timeout' ) {
        doGetChat();
        return;
    }
    var l = d.childNodes;
    for( var i = 0 ; i < l.length; i++ ) {
        var e = l[i];
        if ( e.nodeType == Node.ELEMENT_NODE ) {
            var n = document.importNode( e, true );
            dialogDiv.appendChild( n );
            phraseCount++;
        }
    }
    // scroll to bottom
    dialogDiv.scrollTop = dialogDiv.scrollHeight;
    doGetChat();
}

function init_page( evt ) {
    //window.setInterval( doPing, 1000 );

     phraseText = document.getElementById( 'phraseData' );
     speakerData = document.getElementById( 'speakerData' );
     dialogDiv = document.getElementById( 'dialog' );

     b = document.getElementById( 'phraseFrom' );
     b.addEventListener('submit', doAddPhrase, false );

     // start fetching chat updates
     doGetChat();
}

window.addEventListener('load', init_page, false);
})();
