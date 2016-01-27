function make_draggable( drawing, circle, hit_x, hit_y ) {
    function mkPt( x, y ) {
        var pt = drawing.createSVGPoint();
        pt.x = x;
        pt.y = y;
        return pt;
    }

    circle.classList.add("selected");

    var mat = circle.getScreenCTM();
    // circle in screen coordinates
    var cpt = mkPt(circle.cx.animVal.value, circle.cy.animVal.value);
    cpt = cpt.matrixTransform( mat );
    var hit_offset_x = cpt.x - hit_x;
    var hit_offset_y = cpt.y - hit_y;
    var inv = mat.inverse();

    function drag( evt ) {
        var pt = mkPt( evt.clientX, evt.clientY );
        var trans_pt = pt.matrixTransform( inv );
        circle.setAttribute("cx", trans_pt.x + hit_offset_x );
        circle.setAttribute("cy", trans_pt.y + hit_offset_y );
    }
    function stop_drag( evt ) {
        circle.classList.remove("selected");
        drawing.removeEventListener("mouseleave", stop_drag );
        drawing.removeEventListener("mouseup", stop_drag );
        drawing.removeEventListener("mousemove", drag );
    }
    drawing.addEventListener("mouseleave", stop_drag );
    drawing.addEventListener("mouseup", stop_drag );
    drawing.addEventListener("mousemove", drag );
}

window.addEventListener("load", function(evt) {
    var svg_drawing = document.querySelector("svg#drawing");
    svg_drawing.addEventListener("mouseenter", function(evt) {
        console.log("enter", evt );
    } );

    // register mouse down to select circles to drag
    svg_drawing.addEventListener("mousedown", function(evt) {
        //console.log(document.elementFromPoint(evt.clientX, evt.clientY));
        console.log( evt );
        var target = evt.target;
        // needed for firefox to prevent default dragging
        evt.preventDefault();
        // only drag circles
        if ( target === null || target.tagName !== "circle" ) {
            return;
        }
        make_draggable( svg_drawing, target, evt.clientX, evt.clientY );
    });
    
    var save_button = document.querySelector("button#save");
    save_button.addEventListener("click", function(evt) {
        var pic_path = document.location.pathname.split('/');
        var new_path = [];
        new_path.push( pic_path[1] );
        new_path.push( pic_path[3] );
        new_path.push( pic_path[4] );
        new_path = new_path.join('/');
        var url = document.location.protocol + '//';
        url += document.location.host + '/' + new_path;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if ( xhr.readyState == 4) {
                console.log(xhr)
            }
        };
        xhr.open( "PUT", url );
        var xml = new XMLSerializer().serializeToString(svg_drawing);
        xhr.send( xml );
        console.log( xml );
    });

    // reload the replace, to reload the current picture
    var reset_button = document.querySelector("button#reset");
    reset_button.addEventListener("click", function(evt) {
        document.location.reload( true );
    });
} );
