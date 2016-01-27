import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class UserAuthDBManger {

    //@snipit static-loading
    // try loading sqlite
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() );
            System.exit( 0 );
        }
    }
    //@snipit-end static-loading

    //@snipit db-rest-sql
    // create the users table, each user id must be unique
    private static String createUserTable = 
        "CREATE TABLE IF NOT EXISTS Users " +
        "(key INTEGER PRIMARY KEY, id TEXT UNIQUE, " +
        "pw TEXT, name TEXT, role TEXT)";

    // find a particular user
    private static String selectByUser = 
        "SELECT * FROM Users WHERE id = ?";

    // add a new user
    private static String insertIntoUser = 
        "INSERT INTO Users VALUES(NULL, ?, ?, ?, ?)";

    // change a user's password
    private static String updatePassword = 
        "UPDATE Users set pw=? where id=?";

    // change a user's role
    private static String updateRole = 
        "UPDATE Users set role=? where id=?";

    // delete a user
    private static String deleteUser = 
        "DELETE FROM Users where id=?";
    //@snipit-end db-rest-sql

    private String dbURL;
    private int timeout;

    //@snipit UserAuthDBManger
    public UserAuthDBManger( String dbPath, int timeout ) throws SQLException {
        this.dbURL = "jdbc:sqlite:" + dbPath;
        this.timeout = timeout;
        try (
            Connection conn = DriverManager.getConnection( dbURL );
            PreparedStatement stmt = conn.prepareStatement( createUserTable );
        ) {
            stmt.setQueryTimeout(timeout);
            stmt.executeUpdate();
        }
    }
    //@snipit-end UserAuthDBManger

    //@snipit getUserById
    public User getUserById( String id ) throws SQLException {
        try (
            Connection conn = DriverManager.getConnection( dbURL );
            PreparedStatement stmt = conn.prepareStatement( selectByUser );
        ) {
            stmt.setQueryTimeout(timeout);
            stmt.setString(1, id );
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() ) {
                String nid = rs.getString("id");
                String pw = rs.getString("pw");
                String name = rs.getString("name");
                String role = rs.getString("role");
                return new User( nid, pw, name, role );
            }
        }
        return null;
    }
    //@snipit-end getUserById

    //@snipit addUser
    public void addUser(String id, String password, String name, String role)
        throws SQLException
    {
        try (
            Connection conn = DriverManager.getConnection( dbURL );
            PreparedStatement stmt = conn.prepareStatement( insertIntoUser );
        ) {
            stmt.setQueryTimeout(timeout);
            stmt.setString(1, id);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, role);
            stmt.executeUpdate();
        }
    }
    //@snipit-end addUser

    //@snipit changePassword
    public void changePassword( String id, String password )
        throws SQLException
    {
        try (
            Connection conn = DriverManager.getConnection( dbURL );
            PreparedStatement stmt = conn.prepareStatement( updatePassword );
        ) {
            stmt.setQueryTimeout(timeout);
            stmt.setString(1, password);
            stmt.setString(2, id);
            stmt.executeUpdate();
        }
    }
    //@snipit-end changePassword

    //@snipit changeRole
    public void changeRole( String id, String role ) throws SQLException {
        try (
            Connection conn = DriverManager.getConnection( dbURL );
            PreparedStatement stmt = conn.prepareStatement( updateRole );
        ) {
            stmt.setQueryTimeout(timeout);
            stmt.setString(1, role);
            stmt.setString(2, id);
            stmt.executeUpdate();
        }
    }
    //@snipit-end changeRole

    //@snipit deleteUser
    public void deleteUser( String id ) throws SQLException {
        try (
            Connection conn = DriverManager.getConnection( dbURL );
            PreparedStatement stmt = conn.prepareStatement( deleteUser );
        ) {
            stmt.setQueryTimeout(timeout);
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
    //@snipit-end deleteUser

    public static class User {
        public final String id;
        public final String password;
        public final String name;
        public final String role;

        public User( String id, String password, String name, String role ) {
            this.id = id;
            this.password = password;
            this.name = name;
            this.role = role;
        }

        public String toString() {
            return id + " " + password + " " + name + " " + role;
        }
    }

    // a simple test framework
    public static void main( String[] args ) throws SQLException {
        UserAuthDBManger db = new UserAuthDBManger( "u.db", 5 );
        User u = db.getUserById("foo");
        if ( u == null ) {
            db.addUser( "foo", "bar", "Foo Bar", "admin" );
            u = db.getUserById("foo");
        }
        System.out.println("u = " + u );
        db.changePassword( "foo", "FB" );
        u = db.getUserById("foo");
        System.out.println("u = " + u );
        db.changeRole("foo", "user" );
        u = db.getUserById("foo");
        System.out.println("u = " + u );
        db.deleteUser("foo");
    }
}
