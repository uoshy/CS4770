/**
 * Object o is send via the Link with send, 
 * recv is used to retrieve the object,
 * close indicates that no more objects will be
 * sent via the link.
 * The number of object stored in the link is given
 * by getSize().
 */
interface Link {
    void send( Object o );
    void close();
    Object recv();
    int getSize();
}
