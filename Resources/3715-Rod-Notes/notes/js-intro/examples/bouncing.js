/*
 * constructs a BounceText object, which manages the 
 * movement (bouncing).
 */
function BounceText( e, a ) {
    this.arena = a;
    this.element = e;
    this.x = e.offsetLeft; // offset from left edge of e
    this.y = e.offsetTop;  // offset from top edge of e
    /*
     * Reduce the width and height of the arena
     * ensures that the text box will not go outside
     * of the arena.
     */
    this.w = a.offsetWidth - e.offsetWidth;
    this.h = a.offsetHeight - e.offsetHeight;
    /* defines the movement vector. */
    this.dx = Math.random()*20 - 10;
    this.dy = Math.random()*20 - 10;
}

/*
 * performs the movement.
 */
BounceText.prototype.update = function() {
   this.x += this.dx;
   if ( this.x < 0 ) {
       this.x -= this.dx;
       this.dx = Math.random()*10 + 5;
   }
   else if ( this.x > this.w ) {
       this.x -= this.dx;
       this.dx = -(Math.random()*10 + 5);
   }
   this.y += this.dy;
   if ( this.y < 0 ) {
       this.y -= this.dy;
       this.dy = Math.random()*10 + 5;
   }
   else if ( this.y > this.h ) {
       this.y -= this.dy;
       this.dy = -(Math.random()*10 + 5 );
   }
   this.element.style.left = this.x.toString();
   this.element.style.top = this.y.toString();
}

/*
 * bouncing contains all the BounceText objects.
 * doAnaimation() is invoked 10 times a second
 * to update the postions of the text elements.
 */
var bouncing = [];
function doAnaimation() {
    var i;
    for( i = 0 ; i < bouncing.length; i++ ) {
        bouncing[i].update();
    }
}

/*
 * Creates the BounceText objects from
 * the web pages's document.
 * Invoked when the web page is loaded by the onload event.
 */
function startAnimation() {
    var i,j;
    var arenas = document.getElementsByTagName( "div" );
    for( i = 0 ; i < arenas.length; i++ ) {
        var cl = arenas[i].getAttribute("class");
        if ( cl == null || cl.search("arena") == -1 ) continue;
        var spans = arenas[i].getElementsByTagName( "span" );
        for( j = 0 ; j < spans.length; j++ ) {
            spans[j].style.top = 10*j + 'mm';
            spans[j].style.left = 20*j + 'mm';
            bouncing.push( new BounceText(spans[j], arenas[i] ) );
        }
    }
    // doAnaimation() will be invoked every 100ms
    setInterval("doAnaimation()", 100 );
}
