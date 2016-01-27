#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>

int
main( int ac, char *av[] ) {
    int sock;
    int sock_status;
    char buf[1024];
    struct sockaddr_in addr;
    struct sockaddr_in destaddr;
    struct hostent *hp;  
    int datasize;
    int server_port;

    if ( ac != 2 ) {
        printf("usage: client port\n");
        exit( 0 );
    }
    server_port = atoi( av[1] );

    sock = socket( PF_INET, SOCK_STREAM, 0 );

    if ( sock == -1 ) {
        perror("socket" );
        exit( 1 );
    }
    addr.sin_family = AF_INET;
    addr.sin_port = htons( 0 ); /* OS picks port */
    addr.sin_addr.s_addr = INADDR_ANY;

    if ( -1 == bind( sock, (const struct sockaddr *)&addr, sizeof(addr) ) ) {
        perror("bind");
        exit( 1 );
    }

    hp = gethostbyname( "localhost" );
    if ( hp == 0 ) {
        fprintf(stderr,"name lookup failed\n" );
        exit( 1 );
    }
 
    destaddr.sin_family = AF_INET;
    destaddr.sin_port = htons( server_port );
    memcpy(&destaddr.sin_addr, hp->h_addr, hp->h_length);   
    sock_status = connect( sock, (const struct sockaddr *)&destaddr, sizeof(destaddr) );
    if ( sock_status == -1 ) {
        perror("connect" );
        exit( 1 );
    }

    datasize = write( sock, "hello world\n", 13 );
    if ( datasize != 13 ) {
        perror("read" );
        exit( 1 );
    }

    close( sock );
    return 0;
}
