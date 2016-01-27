function mkSubstitue( x, y ) {
    return function( s ) {
        return s.replace(new RegExp(x,"g"), y );
    };
}

sub1 = mkSubstitue( "cat", "dog" )
sub1("the cat is an old cat" )

sub2 = mkSubstitue( "red", "blue" )
sub2("the red car is reduced" )
