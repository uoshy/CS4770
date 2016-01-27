var add_mod = (function() {

// @snipit form1
function init_add1( evt ) {
    var f1 = document.querySelector("form[name='f1']");
    var doAddURL = window.location.origin + '/doAddGet';

    f1.doAdd.addEventListener('click', function( evt ) {
        evt.preventDefault();
        var v1 = f1.v1.value;
        var v2 = f1.v2.value;
        var result = f1.result;
        var url = encodeURI( '/' + v1 + '/' + v2 );
        url = doAddURL + url;
        // get an AJAX object
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true );
        xhr.onreadystatechange = function() {
            if ( xhr.readyState != 4) return;
            if ( xhr.status == 200 || xhr.status == 400) {
                result.value = xhr.responseText;
            }
            else {
                result.value = "Unknown ERROR";
            }
        };
        xhr.send( null );
    } );
}
// @snipit-end form1

// @snipit form2
function init_add2( evt ) {
    var f2 = document.querySelector("form[name='f2']");
    var doAddPostURL = window.location.origin + '/doAddPost';

    f2.doAdd.addEventListener('click', function( evt ) {
        evt.preventDefault();
        var v1 = f2.v1.value;
        var v2 = f2.v2.value;
        var result = f2.result;
        // get an AJAX object
        var xhr = new XMLHttpRequest();
        xhr.open('POST', doAddPostURL, true );
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            if ( xhr.readyState != 4) return;
            if ( xhr.status == 200 || xhr.status == 400) {
                result.value = xhr.responseText;
            }
            else {
                result.value = "Unknown ERROR";
            }
        };
        // slighty ugly
        var doc = "v1=" + encodeURI(v1) + "&v2=" + encodeURI(v2);
        xhr.send( doc );
    } );
}
// @snipit-end form2

// @snipit form3
function init_add3( evt ) {
    var f3 = document.querySelector("form[name='f3']");
    var doAddPostURL = window.location.origin + '/doAddJSON';

    f3.doAdd.addEventListener('click', function( evt ) {
        evt.preventDefault();
        var result = f3.result;
        var v1 = f3.v1.value;
        var v2 = f3.v2.value;
        v1 = parseInt( v1 );
        v2 = parseInt( v2 );
        // check for valid integers
        if ( isNaN(v1) || isNaN(v2) ) {
            result.value = "malformed";
            // inform the user, do not send a request
            return;
        }
        // create a Javascript object to send
        var sumObject = {v1: v1, v2: v2};
        // get an AJAX object
        var xhr = new XMLHttpRequest();
        xhr.open('POST', doAddPostURL, true );
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function() {
            if ( xhr.readyState != 4) return;
            if ( xhr.status == 200 || xhr.status == 400) {
                result.value = xhr.responseText;
            }
            else {
                result.value = "Unknown ERROR";
            }
        };
        // convert to the offical JSON syntax
        var doc = JSON.stringify( sumObject );
        xhr.send( doc );
    } );
}
// @snipit-end form3

function init_add(evt) {
    init_add1( evt );
    init_add2( evt );
    init_add3( evt );
}

window.addEventListener('load', init_add );

}());
