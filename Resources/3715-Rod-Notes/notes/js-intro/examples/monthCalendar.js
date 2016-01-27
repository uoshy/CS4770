/*
 * calcuate the number of days in the month.
 */
function daysInMonth( year, month ) {
    var d = daysInMonth.days[ month ];
    if ( month != 1 ) return d;
    if ( (year % 4) != 0 ) return d;
    if ( (year % 400) == 0 ) return d+1;
    if ( (year % 100) == 0 ) return d;
    return d+1;
}
daysInMonth.days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

/*
 * Return an array of weeks, each week is an array of dates
 */
function monthCalendar( year, month )  {
    var d = new Date( year, month, 1 );
    var first = d.getDay() // 0:Sun .. 6:Sat

    var w = [];
    // add empty days for first week
    var date = 1;
    for(i = 0; i < 7; i++ )  w.push( i < first ? null : date++ );

    var cal = [];
    var noDays = daysInMonth( year, month );
    var offset = first-1;
    while ( date <= noDays ) {
        if ( ((date+offset) % 7) == 0 ) {
            cal.push( w );
            w = [];
        }
        w.push( date++ );
    }
    // finish week
    if ( w.length > 0 ) {
        for( i = w.length; i < 7; i++ )  w.push( null );
        cal.push( w );
    }
    return cal;
}

function genCalendar( doc, year, month ) {
    var cal = monthCalendar( year, month );
    var dayName = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" ];
    cal.unshift( dayName ); // add headers
    doc.writeln("<table class='calendar'>");
    var i;
    for( weeks = 0 ; weeks < cal.length ; weeks++ ) {
        var week = cal[weeks];
        doc.write("<tr>");
        for( d = 0 ; d < 7 ; d++ ) {
            doc.write("<td>");
            var s = week[d];
            if ( s == null ) s = ' ';
            doc.write( s );
            doc.write("</td>");
        }
        doc.writeln("</tr>");
    }
    doc.writeln("</table>");
}
