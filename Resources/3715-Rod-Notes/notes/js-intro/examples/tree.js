/*
 * Set the display to none for all the UL children.
 */
function display( li ) {
    var children = li.childNodes;
    var i;
    for( i = 0 ; i < children.length; i++ ) {
        if ( children[i].tagName == 'UL') {
            children[i].style.display = 'none';
        }
    }
}

/*
 * Set the display to block for all the UL children.
 */
function undisplay( li ) {
    var children = li.childNodes;
    var i;
    for( i = 0 ; i < children.length; i++ ) {
        if ( children[i].tagName == 'UL') {
            children[i].style.display = 'block';
        }
    }
}

/*
 * Handle click on a list item, by displaying or hiding
 * any children. Change the marker to indicate if any
 * children are possible hiden.
 */
function toggle( evt ) {
    // not interested in other phases
    if ( evt.eventPhase != Event.AT_TARGET  ) return;
    var elem = evt.target;
    var liStyle = elem.style;
    if( ! liStyle.listStyleImage ||
        liStyle.listStyleImage.search('plus') != -1 ) {
        undisplay( elem );
        elem.style.listStyleImage = 'url(minus.png)';
    }
    else {
        display( elem );
        elem.style.listStyleImage = 'url(plus.png)';
    }
}

/*
 * See the event handler on all the list items
 * with a ul parent with a class="tree".
 */
function initTree() {
    var lis = document.getElementsByTagName('li');
    var i;
    for( i = 0; i < lis.length; i++ ) {
        var parent = lis[i].parentNode;
        var pClass = parent.getAttribute('class');
        if ( pClass != null && pClass.search('tree') != -1 ) {
            lis[i].onclick = toggle;
        }
    }
}
