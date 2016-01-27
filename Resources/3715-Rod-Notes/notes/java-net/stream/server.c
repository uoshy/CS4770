#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <unistd.h>

int
main( int ac, char *av[] ) {
    int sock;
    int newsock;
    char buf[1024];
    struct sockaddr_in server_addr;
    int sa_len;
    struct sockaddr_in addr;
    struct sockaddr_in newaddr;
    int newsize, datasize;

    /* domain,  type,        protocol */
    sock = socket( PF_INET, SOCK_STREAM, 0 );

    if ( sock == -1 ) {
        perror("socket" );
        exit( 1 );
    }
    addr.sin_family = AF_INET;
    addr.sin_port = htons( 0 );
    addr.sin_addr.s_addr = INADDR_ANY;

    if ( -1 == bind( sock, (struct sockaddr *)&addr, sizeof(addr) ) ) {
        perror("bind");
        exit( 1 );
    }

    sa_len = sizeof(server_addr);
    if ( getsockname(sock, (struct sockaddr *)&server_addr, &sa_len) < 0 ) {
        perror("listen");
        exit( 1 );
    }
    printf("port number = %d\n", ntohs(server_addr.sin_port));

    if ( -1 == listen( sock, 5) ) {
        perror("listen");
        exit( 1 );
    }

    newsize = sizeof( newaddr );
    newsock = accept( sock, (struct sockaddr *)&newaddr, &newsize );
    if ( newsock == -1 ) {
        perror("accept" );
        exit( 1 );
    }

    datasize = read( newsock, buf, sizeof(buf) );
    if ( datasize == -1 ) {
        perror("read" );
        exit( 1 );
    }

    /* assume that data send is a nul terminated character string */
    printf("mesg = %s\n", buf );

    close( newsock );
    close( sock );
    return 0;
}
