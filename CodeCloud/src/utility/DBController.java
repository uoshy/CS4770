//TODO: s# pk of asub?
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

import assignments.Assignment;
import assignments.AssignmentSolution;
import assignments.AssignmentSubmission;
import assignments.Comments;
import assignments.Course;
import files.UserFile;
import testing.TestSuite;
import users.Role;
import users.User;

public class DBController {
    //try loading sqlite
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch(Exception e) {
        	System.out.println("Error: Unable to load SQLite.");
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
	"INSERT INTO Users (username, password, firstname, lastname, studentNumber) VALUES(?, ?, ?, ?, ?)";

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
    	"SELECT * FROM Enrollments WHERE username=? AND courseID=? AND term=?";

    //get a course's instructor
    private static String getInstructorStatement =
    	"SELECT * FROM Enrollments WHERE courseID=? AND term=?";

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
	"INSERT INTO Assignments (courseID, term, number, path, testSuitePath, submissionLimit) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove assignment
    private static String removeAssignmentStatement =
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
    private static String removeASolutionStatement =
    	"DELETE FROM AssignmentSolutions WHERE courseID=? AND term=? AND number=?";

    //assignment feedback
    //get an assignment's feedback
    private static String selectAFeedbackStatement =
    	"SELECT * FROM Comments WHERE username=? AND courseID=? AND term=? AND number=?";

    //get all feedback
    private static String selectAllAFeedbackStatement =
    	"SELECT * FROM Comments";

    //add feedback
    private static String addAFeedbackStatement =
	"INSERT INTO Comments (username, courseID, term, number, grade, feedbackText, feedbackPath) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove feedback
    private static String removeAFeedbackStatement =
    	"DELETE FROM Comments WHERE username=? AND courseID=? AND term=? and number=?";

    //assignment submissions
    //get an assignment ubmission by courseID, term, number, and username
    private static String selectASubmissionStatement =
    	"SELECT * FROM AssignmentSubmissions WHERE username=? AND courseID=? AND term=? AND number=?";

    //get all submissions
    private static String selectAllASubmissionStatement =
    	"SELECT * FROM AssignmentSubmissions";

    //add a submission
    private static String addASubmissionStatement =
	"INSERT INTO AssignmentSubmissions (username, courseID, term, number, path, submissionNumber, grade) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove submission
    private static String removeASubmissionStatement =
    	"DELETE FROM AssignmentSubmissions WHERE username=? AND courseID=? AND term=? AND number=?";


    public static final String DB_FILENAME = "Database.db";
    public static String DB_URL;
    public static final int TIMEOUT = 20;

    public static void initialize() throws SQLException {
        //find path to database file
    	try {
    		DB_URL = DBController.searchForDatabaseFile();
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
    		}
		while (i < 2);
			throw new FileNotFoundException("Database file Database.db not found!");
	}

	//Users
	public static User getUser(String userName) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectUserStatement);
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, userName);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String username = rs.getString("username");
			String password = rs.getString("password");
			String firstname = rs.getString("firstname");
			String lastname = rs.getString("lastname");
			long studentNumber = rs.getLong("studentNumber");
			return new User(username, password, firstname, lastname, studentNumber);
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
    			String username = rs.getString("username");
    			String password = rs.getString("password");
			String firstname = rs.getString("firstname");
			String lastname = rs.getString("lastname");
			long studentNumber = rs.getLong("studentNumber");
    			map.put(username, new User(username, password, firstname, lastname, studentNumber));
    		}
		return map;
	}

	public static void addUser(User user) throws SQLException {
		modifyStatement(addUserStatement, new DBObject[]{new DBStringObject(user.getUsername()), new DBStringObject(user.getPassword()), new DBStringObject(user.getFirstname()), new DBStringObject(user.getLastname()), new DBFloatObject(user.getStudentNumber())});
	}

	public static void removeUser(User user) throws SQLException {
		modifyStatement(removeUserStatement, new DBObject[]{new DBStringObject(user.getUsername()), new DBStringObject(user.getPassword())});
	}

	//Courses
	public static Course getCourse(String courseID, String term) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectCourseStatement);
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, courseID);
		stmt.setString(2, term);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String cID = rs.getString("courseID");
			String t = rs.getString("term");
			String n = rs.getString("name");
			return new Course(cID, t, n);
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
    			String cID = rs.getString("courseID");
    			String term = rs.getString("term");
			String name = rs.getString("name");
    			map.put(cID + "|" + term, new Course(cID, term, name));
    		}
		return map;
	}

	public static void addCourse(Course course) throws SQLException {
		modifyStatement(addCourseStatement, new DBObject[]{new DBStringObject(course.getCourseID()), new DBStringObject(course.getTerm())});
	}

	public static void removeCourse(Course course) throws SQLException {
		modifyStatement(removeCourseStatement, new DBObject[]{new DBStringObject(course.getCourseID()), new DBStringObject(course.getTerm()), new DBStringObject(course.getName())});
	}

	//Enrollments
	//Enrollments are treated as a String array with values [username, courseID, term, role]
	public static String[] getEnrollment(String username, String courseID, String term) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectEnrollmentStatement);
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, username);
		stmt.setString(2, courseID);
		stmt.setString(3, term);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			String r = rs.getString("role");
			return new String[]{u, c, t, r};
		}
		return null;
	}

	public static User getInstructor(Course course) throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(getInstructorStatement);
    		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, course.getCourseID());
		stmt.setString(2, course.getTerm());
		stmt.setString(3, "Instructor");
    		ResultSet rs = stmt.executeQuery();
		User instructor = null;
    		if (rs.first()){
			instructor = DBController.getUser(rs.getString("username"));
    		}
		return instructor;
	}

	public static Map<String, String[]> getAllEnrollments() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllEnrollmentsStatement);
		Map<String, String[]> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			String r = rs.getString("role");
			map.put(String.format("%s|%s|%s|%s", u, c, t, r), new String[]{u, c, t, r});
    		}
		return map;
	}

	public static void addEnrollment(String[] enrollment) throws SQLException {
		if (enrollment.length == 4) modifyStatement(addEnrollmentStatement, new DBObject[]{new DBStringObject(enrollment[0]), new DBStringObject(enrollment[1]), new DBStringObject(enrollment[2]), new DBStringObject(enrollment[3])});
	}

	public static void removeEnrollment(String[] enrollment) throws SQLException {
		if (enrollment.length == 3 || enrollment.length == 4) modifyStatement(removeEnrollmentStatement, new DBObject[]{new DBStringObject(enrollment[0]), new DBStringObject(enrollment[1]), new DBStringObject(enrollment[2])});
	}

	//Assignment
	public static Assignment getAssignment(String courseID, String term, int number) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectAssignmentStatement);
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, courseID);
		stmt.setString(2, term);
		stmt.setInt(3, number);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String cID = rs.getString("courseID");
			String t = rs.getString("term");
			Course c = DBController.getCourse(cID, t);
			int num = rs.getInt("number");
			String p = rs.getString("path");
			String tsp = rs.getString("testSuitePath");
			int sl = rs.getInt("submissionLimit");
			User instructor = DBController.getInstructor(c);
			//UserFile suiteFile = new UserFile(instructor, "/courses/term/courseID/assignments/" + num + "/tests");
			//Course course, UserFile assignmentDir, UserFile submissions, int number, int submissionLimit
			return new Assignment(c, new UserFile(instructor, "/courses/" + t + "/" + cID + "/assignments" + num), new UserFile(instructor, "/courses/" + t + "/" + cID + "/submissions" + num), num, sl);
		}
		return null;
	}

	public static Map<String, Assignment> getAllAssignments() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllAssignmentsStatement);
		Map<String, Assignment> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
			String cID = rs.getString("courseID");
			String t = rs.getString("term");
			Course c = DBController.getCourse(cID, t);
			int num = rs.getInt("number");
			String p = rs.getString("path");
			String tsp = rs.getString("testSuitePath");
			int sl = rs.getInt("submissionLimit");
			User instructor = DBController.getInstructor(c);
			//UserFile suiteFile = new UserFile(instructor, "/courses/term/courseID/assignments/" + num + "/tests");
			//Course course, UserFile assignmentDir, UserFile submissions, int number, int submissionLimit
    			map.put(String.format("%s|%s|%d", cID, t, num), new Assignment(c, new UserFile(instructor, "/courses/" + t + "/" + cID + "/assignments"), new UserFile(instructor, "/courses/" + t + "/" + cID + "/submissions/" + num + "/submissions"), num, sl));
    		}
		return map;
	}

	public static void addAssignment(Assignment assignment) throws SQLException {
		modifyStatement(addAssignmentStatement, new DBObject[]{new DBStringObject(assignment.getCourse().getCourseID()), new DBStringObject(assignment.getCourse().getTerm()), new DBIntObject(assignment.getNumber()), new DBStringObject("/courses/" + assignment.getCourse().getTerm() + "/" + assignment.getCourse().getCourseID() + "/assignments/" + assignment.getNumber()), new DBStringObject("/courses/" + assignment.getCourse().getTerm() + "/" + assignment.getCourse().getCourseID() + "/assignments/ " + assignment.getNumber() + "/tests"), new DBIntObject(assignment.getSubmissionLimit())});
	}

	public static void removeAssignment(Assignment assignment) throws SQLException {
		modifyStatement(removeAssignmentStatement, new DBObject[]{new DBStringObject(assignment.getCourse().getCourseID()), new DBStringObject(assignment.getCourse().getTerm()), new DBIntObject(assignment.getNumber())});
	}

	//Assignment Submission
	public static AssignmentSubmission getAssignmentSubmission(String username, String courseID, String term, int number) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectASubmissionStatement);
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, username);
		stmt.setString(2, courseID);
		stmt.setString(3, term);
		stmt.setInt(4, number);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int n = rs.getInt("number");
			String p = rs.getString("path");
			int sn = rs.getInt("submissionNumber");
			User user = DBController.getUser(u);
			Assignment assignment = DBController.getAssignment(c, t, n);
			return new AssignmentSubmission(user, assignment, new UserFile(user, "/courses/" + term + "/" + courseID + "/submissions/" + number + "/" + username), sn);
		}
		return null;
	}

	public static Map<String, AssignmentSubmission> getAllAssignmentSubmissions() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllASubmissionStatement);
		Map<String, AssignmentSubmission> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int n = rs.getInt("number");
			String p = rs.getString("path");
			int sn = rs.getInt("submissionNumber");
			User user = DBController.getUser(u);
			Assignment assignment = DBController.getAssignment(c, t, n);
			map.put(String.format("%s|%s|%s|%d", u, c, t, n), new AssignmentSubmission(user, assignment, new UserFile(user, "/courses/" + t + "/" + c + "/submissions/" + n + "/" + u), sn));
    		}
		return map;
	}

	public static void addAssignmentSubmission(AssignmentSubmission aSub) throws SQLException {
		modifyStatement(addASubmissionStatement, new DBObject[]{new DBStringObject(aSub.getOwner().getUsername()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getTerm()), new DBIntObject(aSub.getRelatedAssignment().getNumber()),
			new DBStringObject("/courses/" + aSub.getRelatedAssignment().getCourse().getTerm() + "/" + aSub.getRelatedAssignment().getCourse().getCourseID() + "/submissions/" + aSub.getRelatedAssignment().getNumber() + "/" + aSub.getOwner().getUsername()), new DBIntObject(aSub.getSubmissionNum())});
	}

	public static void removeAssignmentSubmission(AssignmentSubmission aSub) throws SQLException {
		modifyStatement(removeASubmissionStatement, new DBObject[]{new DBStringObject(aSub.getOwner().getUsername()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getTerm()), new DBIntObject(aSub.getSubmissionNum())});
	}

	//Assignment Comments
	public static Comments getComments(String username, String courseID, String term, int number) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectAFeedbackStatement);
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, username);
		stmt.setString(2, courseID);
		stmt.setString(3, term);
		stmt.setInt(4, number);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int n = rs.getInt("number");
			float f = rs.getFloat("grade");
			String fp = rs.getString("feedbackPath");
			String ft = rs.getString("feedbackText");
			return new AssignmentSubmission(u, c, t, n, f, fp, ft);
		}
		return null;
	}

	public static Map<String, Comments> getAllComments() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllAFeedbackStatement);
		Map<String, Comments> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int n = rs.getInt("number");
			float f = rs.getString("grade");
			String fp = rs.getString("feedbackPath");
			String ft = rs.getString("feedbackText");
			map.put(String.format("%s|%s|%s|%d|%f|%s|%s", u, c, t, n, f, fp, ft), new AssignmentSubmission(u, c, t, n, f, fp, ft));
    		}
		return map;
	}

	public static void addComments(Comments aFeed) throws SQLException {
		modifyStatement(addAFeedbackStatement, new DBObject[]{new DBStringObject(aFeed.getRecipient().getUsername()), new DBStringObject(aFeed.getRelatedSubmission().getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(aFeed.getRelatedSubmission().getRelatedAssignment().getCourse().getTerm()), new DBIntObject(aFeed.getRelatedSubmission().getRelatedAssignment.getCourse().getNumber()),
			new DBFloatObject(aFeed.getGrade()), new DBStringObject(aFeed.getFeedbackPath()), new DBStringObject(aFeed.getFeedbackText())});
	}

	public static void removeComments(Comments comm) throws SQLException {
		modifyStatement(removeAFeedbackStatement, new DBObject[]{new DBStringObject(comm.getRecipient().getUsername()), new DBStringObject(comm.getRelatedSubmission().getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(comm.getRelatedSubmission().getRelatedAssignment().getCourse().getTerm()), new DBIntObject(comm.getRelatedSubmission().getRelatedAssignment().getNumber())});
	}

	//Assignment solutions
	public static AssignmentSolution getAssignmentSolution(String courseID, String term, int number) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectASolutionStatement);
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, courseID);
		stmt.setString(2, term);
		stmt.setInt(3, number);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int num = rs.getInt("number");
			String p = rs.getString("path");
			return new AssignmentSolution(c, t, num, p);
		}
		return null;
	}

	public static Map<String, AssignmentSolution> getAllASolutions() throws SQLException {
    		Connection conn = DriverManager.getConnection(DB_URL);
    		PreparedStatement stmt = conn.prepareStatement(selectAllASolutionsStatement);
		Map<String, AssignmentSolution> map = new HashMap<>();
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()){
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int num = rs.getInt("number");
			String p = rs.getString("path");
    			map.put(String.format("%s|%s|%d|%s", c, t, num, p), new AssignmentSolution(c, t, num, p));
    		}
		return map;
	}

	public static void addAssignmentSolution(AssignmentSolution aSol) throws SQLException {
		modifyStatement(addASolutionStatement, new DBObject[]{new DBStringObject(aSol.getAssignment().getCourse().getCourseID()), new DBStringObject(aSol.getAssignment().getCourse().getTerm()), new DBIntObject(aSol.getAssignment().getNumber()), new DBStringObject(aSol.getPath())});
	}

	public static void removeAssignment(AssignmentSolution aSol) throws SQLException {
		modifyStatement(removeASolutionStatement, new DBObject[]{new DBStringObject(aSol.getAssignment().getCourse().getCourseID()), new DBStringObject(aSol.getAssignment().getCourse().getTerm()), new DBIntObject(aSol.getAssignment().getNumber())});
	}

	//For statements that change the state of a table (add/remove data)
	public static void modifyStatement(String statementType, DBObject[] dbArray) throws SQLException {
		Connection connection = DriverManager.getConnection(DB_URL);
  		PreparedStatement stmt = connection.prepareStatement(statementType);
		for (int i = 0; i < dbArray.length; i++){
  			dbArray[i].addToStatement(stmt, i+1);
		}
		stmt.execute();
  	}
}
