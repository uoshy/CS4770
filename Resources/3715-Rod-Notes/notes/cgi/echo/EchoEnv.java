public class EchoEnv {
    public static void main( String[] args ) {
	 System.out.print("Content-Type: text/plain\r\n\r\n");
	 System.out.println("server name = " + System.getenv("SERVER_NAME"));
	 System.out.println("server port = " + System.getenv("SERVER_PORT"));
	 System.out.println("method = " + System.getenv("REQUEST_METHOD"));
	 System.out.println("query = " + System.getenv("QUERY_STRING"));
	 System.out.println("info = " + System.getenv("PATH_INFO"));
	 System.out.println("script = " + System.getenv("SCRIPT_NAME"));
	 System.out.println("addr = " + System.getenv("REMOTE_ADDR"));
	 System.out.println("user = " + System.getenv("HTTP_USER_AGENT"));
	 System.out.println("type = " + System.getenv("CONTENT_TYPE"));
	 System.out.println("length = " + System.getenv("CONTENT_LENGTH"));
    }
}
