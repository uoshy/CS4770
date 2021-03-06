package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DBController {
    //try loading sqlite
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch(Exception e) {
        	System.out.println("Error: Unable to load SQLite.");
            System.err.println(e.printStackTrace());
            e.printStackTrace();
            System.exit(0);
        }
    }

    //users
    //get a user by username
    private static String selectUserStatement =
    	"SELECT * FROM User WHERE username=?";

    //get all users
    private static String selectAllUsersStatement =
    	"SELECT * FROM Users";

    //add a user
    private static String addUserStatement =
	"INSERT INTO Users (username, password) VALUES(?, ?)";

    //remove user
    private static String removeUserStatement =
    	"DELETE FROM Users WHERE username=?";

    //courses
    //get a course by courseID and term
    private static String selectCourseStatement =
    	"SELECT * FROM User WHERE username=? AND term=?";

    //get all courses
    private static String selectAllCoursesStatement =
    	"SELECT * FROM Courses";

    //add a course
    private static String addCourseStatement =
	"INSERT INTO Courses (courseID, term, name) VALUES(?, ?, ?)";

    //remove course
    private static String removeCourseStatement =
    	"DELETE FROM Courses WHERE courseID=? AND term=?";

    //enrollments
    //get an enrollment by username, courseID, and term
    private static String selectEnrollmentStatement =
    	"SELECT * FROM Courses WHERE username=? AND courseID=? AND term=?";

    //get all enrollments
    private static String selectAllEnrollmentsStatement =
    	"SELECT * FROM Enrollments";

    //add an enrollment
    private static String addEnrollmentStatement =
	"INSERT INTO Enrollments (username, courseID, term, role) VALUES(?, ?, ?, ?)";

    //remove enrollment
    private static String removeEnrollmentStatement =
    	"DELETE FROM Enrollments WHERE username=? AND courseID=? AND term=?";

    //assignments
    //get an assignment by courseID, term, and number
    private static String selectAssignmentStatement =
    	"SELECT * FROM Assignments WHERE courseID=? AND term=? AND number=?";

    //get all assignments
    private static String selectAllAssignmentsStatement =
    	"SELECT * FROM Assignments";

    //add an assignment
    private static String addAssignmentStatement =
	"INSERT INTO Assignments (courseID, term, number, name, path, testSuitePath, submissionLimit) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove assignment
    private static String removeMemberStatement =
    	"DELETE FROM Assignments WHERE courseID=? AND term=? AND number=?";

    //assignment solutions
    //get an assignment solution by courseID, term, and number
    private static String selectASolutionStatement =
    	"SELECT * FROM User WHERE courseID=? AND term=? AND number=?";

    //get all assignment solutions
    private static String selectAllASolutionsStatement =
    	"SELECT * FROM AssignmentSolutions";

    //add an assignment solution
    private static String addASolutionStatement =
	"INSERT INTO Users (courseID, term, number, path) VALUES(?, ?, ?, ?)";

    //remove assignment solution
    private static String removeMemberStatement =
    	"DELETE FROM AssignmentSolutions WHERE courseID=? AND term=? AND number=?";

    //assignment feedback
    //get an assignment's feedback
    private static String selectAFeedbackStatement =
    	"SELECT * FROM AssignmentFeedback WHERE username=? AND courseID=? AND term=? AND number=?";

    //get all feedback
    private static String selectAllAFeedbackStatement =
    	"SELECT * FROM AssignmentFeedback";

    //add feedback
    private static String addAFeedbackStatement =
	"INSERT INTO AssignmentFeedback (username, courseID, term, number, grade, feedbackText, feedbackPath) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove feedback
    private static String removeMemberStatement =
    	"DELETE FROM AssignmentFeedback WHERE username=? AND courseID=? AND term=? and number=?";

    //assignment submissions
    //get an assignmentFeedback by courseID, term, number, and username
    private static String selectAFeedbackStatement =
    	"SELECT * FROM AssignmentSubmissions WHERE username=? AND courseID=? AND term=? AND number=?";

    //get all feedback
    private static String selectAllAFeedbackStatement =
    	"SELECT * FROM AssignmentFeedback";

    //add feedback
    private static String addAFeedbackStatement =
	"INSERT INTO AssignmentFeedback (username, courseID, term, number, grade, feedbackText, feedbackPath) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove submission
    private static String removeMemberStatement =
    	"DELETE FROM AssignmentSubmissions WHERE username=? AND courseID=? AND term=? AND number=?";


    public static final String DB_FILENAME = "Database.db";
    public static String DB_URL;
    public static final int TIMEOUT = 20;

    public static void initialize() throws SQLException {
        //find path to database file
    	try {
    		DB_URL = DatabaseInterface.searchForDatabaseFile();
    	} catch (FileNotFoundException ex) {
    		System.err.println(ex.getMessage());
    		ex.printStackTrace();
    	}
    }

	private static String searchForDatabaseFile() throws FileNotFoundException {
		FilenameFilter dbFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equals(DB_FILENAME);
			}
    	};
    	//search for Database.db in the current directory and the two parent directories
    	File currentDir = new File(System.getProperty("user.dir"));
    	int i = 0;
    	do {
	    	File[] filesInCurrentDir = currentDir.listFiles(dbFilter);
	    	if (filesInCurrentDir.length > 0) {
	    		return "jdbc:sqlite:" + filesInCurrentDir[0];
	    	}
	    	currentDir = currentDir.getParentFile();
    	} while (i < 2);
		throw new FileNotFoundException("Database file Database.db not found!");
	}

	//Users
	public User getUser(String userName) throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement stmt = conn.prepareStatement(selectUserStatement);
		stmt.setQueryTimeout(timeout);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String username = rs.getString("username");
			String password = rs.getString("password");
			return new User(name, pass);
		}
		return null;
	}

	public static Map<String, User> getAllUsers() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllUsersStatement);
		Map<String, User> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
    			String username = rs.getString(1);
    			String password = rs.getString(2);
    			map.put(username, new User(username, password));
    		}
		return map;
	}

	public static void addUser(User user) throws SQLException {
		modifyStatement(addUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public static void removeUser(User user) throws SQLException {
		modifyStatement(removeUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	//Courses
	public Course getCourse(String courseID, String term) throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement stmt = conn.prepareStatement(selectCourseStatement);
		stmt.setQueryTimeout(timeout);
		stmt.setString(1, courseID);
		stmt.setString(2, term);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String cID = rs.getString("courseID");
			String t = rs.getString("term");
			return new Course(cID, t);
		}
		return null;
	}

	public static Map<String, Course> getAllCourses() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllCoursesStatement);
		Map<String, Course> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
    			String courseID = rs.getString(1);
    			String term = rs.getString(2);
    			map.put(username, new Course(courseID, term));
    		}
		return map;
	}

	public static void addCourse(Course course) throws SQLException {
		modifyStatement(addCourseStatement, new DBObject[]{new DBString(course.getCourseID()), new DBString(course.getTerm())});
	}

	public static void removeCourse(Course course) throws SQLException {
		modifyStatement(removeCourseStatement, new DBObject[]{new DBString(course.getCourseID()), new DBString(course.getTerm(), new DBString(course.getName())});
	}

	//Enrollments
	public Enrollment getEnrollment(String username, String courseID, String term) throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement stmt = conn.prepareStatement(selectEnrollmentStatement);
		stmt.setQueryTimeout(timeout);
		stmt.setString(1, username);
		stmt.setString(2, courseID);
		stmt.setString(3, term);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			String r = rs.getString("role");
			return new Enrollment(u, c, t, r);
		}
		return null;
	}

	public static Map<String, Enrollment> getAllEnrollments() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllAccounts);
		Map<String, User> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
    			String username = rs.getString(1);
    			String password = rs.getString(2);
    			map.put(username, new User(username, password));
    		}
		return map;
	}

	public static void addUser(User user) throws SQLException {
		modifyStatement(addUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public static void removeUser(User user) throws SQLException {
		modifyStatement(removeUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public User getUser(String userName) throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement stmt = conn.prepareStatement(selectUserStatement);
		stmt.setQueryTimeout(timeout);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String username = rs.getString("username");
			String password = rs.getString("password");
			return new User(name, pass);
		}
		return null;
	}

	public static Map<String, User> getAllUsers() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllAccounts);
    		//long ID, String name, long student id, Date DOB, String username, string password
		Map<String, User> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
    			String username = rs.getString(1);
    			String password = rs.getString(2);
    			map.put(username, new User(username, password));
    		}
		return map;
	}

	public static void addUser(User user) throws SQLException {
		modifyStatement(addUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public static void removeUser(User user) throws SQLException {
		modifyStatement(removeUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public User getUser(String userName) throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement stmt = conn.prepareStatement(selectUserStatement);
		stmt.setQueryTimeout(timeout);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String username = rs.getString("username");
			String password = rs.getString("password");
			return new User(name, pass);
		}
		return null;
	}

	public static Map<String, User> getAllUsers() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllAccounts);
    		//long ID, String name, long student id, Date DOB, String username, string password
		Map<String, User> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
    			String username = rs.getString(1);
    			String password = rs.getString(2);
    			map.put(username, new User(username, password));
    		}
		return map;
	}

	public static void addUser(User user) throws SQLException {
		modifyStatement(addUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public static void removeUser(User user) throws SQLException {
		modifyStatement(removeUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public User getUser(String userName) throws SQLException {
		Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement stmt = conn.prepareStatement(selectUserStatement);
		stmt.setQueryTimeout(timeout);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String username = rs.getString("username");
			String password = rs.getString("password");
			return new User(name, pass);
		}
		return null;
	}

	public static Map<String, User> getAllUsers() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllAccounts);
    		//long ID, String name, long student id, Date DOB, String username, string password
		Map<String, User> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
    			String username = rs.getString(1);
    			String password = rs.getString(2);
    			map.put(username, new User(username, password));
    		}
		return map;
	}

	public static void addUser(User user) throws SQLException {
		modifyStatement(addUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}

	public static void removeUser(User user) throws SQLException {
		modifyStatement(removeUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	}


	//For statements that change the state of a table (add/remove data)
	public static void modifyStatement(String statementType, DBObject[] dbArray) throws SQLException {
		Connection connection = DriverManager.getConnection(DB_URL);
  		PreparedStatement statement = connection.prepareStatement(statementType);
		for (int i = 0; i < dbArray.length; i++){
  			dbArray[i].addToStatement(statement, i+1);
		}
		statement.execute();
  	}
}
