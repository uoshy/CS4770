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
import assignments.Grade;
import assignments.Comments;
import assignments.Course;
import files.UserFile;
import testing.TestSuite;
import users.Role;
import users.User;

/**
 * The interface which which this java application accesses the sqlite database.
 * 
 * @author Tim
 *
 */
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
			"SELECT * FROM Users WHERE username=?";

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
			"SELECT * FROM Courses WHERE courseID=? AND term=?";

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

	private static String selectEnrollmentsForUserStatement = 
			"SELECT * FROM Courses C, Enrollments E WHERE E.username=? AND E.courseID = C.courseID AND E.term = C.term AND E.role = ?";

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

	private static String getMaxAssignmentNumberStatement = 
			"SELECT MAX(number) from Assignments WHERE courseID=? AND term=?";

	//add an assignment
	private static String addAssignmentStatement =
			"INSERT INTO Assignments (courseID, term, number, name, path, testSuitePath, submissionLimit) VALUES(?, ?, ?, ?, ?, ?, ?)";

	//remove assignment
	private static String removeAssignmentStatement =
			"DELETE FROM Assignments WHERE courseID=? AND term=? AND number=?";

	//assignment solutions
	//get an assignment solution by courseID, term, and number
	private static String selectASolutionStatement =
			"SELECT * FROM Users WHERE courseID=? AND term=? AND number=?";

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
			"SELECT * FROM Grade WHERE username=? AND courseID=? AND term=? AND number=?";

	//get all feedback
	private static String selectAllAFeedbackStatement =
			"SELECT * FROM Grade";

	//add feedback
	private static String addAFeedbackStatement =
			"INSERT INTO Grade (username, courseID, term, number, grade, feedbackText, feedbackPath) VALUES(?, ?, ?, ?, ?, ?, ?)";

	//remove feedback
	private static String removeAFeedbackStatement =
			"DELETE FROM Grade WHERE username=? AND courseID=? AND term=? and number=?";

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


	/**
	 * The name of the database file.
	 */
	public static final String DB_FILENAME = "CodeCloud.db";
	
	/**
	 * The URL to the database as needed for JDBC.
	 */
	public static String DB_URL;
	
	/**
	 * The number of seconds to wait for a database connection before timing out.
	 */
	public static final int TIMEOUT = 20;

	/**
	 * Initialize the database url and prepare it for use.
	 * @throws SQLException if the databbase cannot be found
	 */
	public static void initialize() throws SQLException {
		//find path to database file
		try {
			DB_URL = DBController.searchForDatabaseFile();
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Search for the database file.
	 * @return the name of the database file.
	 * @throws FileNotFoundException if the database file is not found
	 */
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
	
	/**
	 * Get a user based on its username. 
	 * @param userName the user's username.
	 * @return the User with the specified username or null if User was not found
	 * @throws SQLException if a sql error occured
	 */
	public static User getUser(String userName) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectUserStatement); ){
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, userName);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String username = rs.getString("username");
			String password = rs.getString("password");
			String firstname = rs.getString("firstname");
			String lastname = rs.getString("lastname");
			long studentNumber = rs.getLong("studentNumber");
			return new User(username, password, firstname, lastname, studentNumber);
		}

		return null;
		}
	}

	/**
	 * Get all Users. A Map is returned from usernames to User objects.
	 * @return a map contianing all users
	 * @throws SQLException if an sql error occurs
	 */
	public static Map<String, User> getAllUsers() throws SQLException {
		try(Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAllUsersStatement);){
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
	}

	/**
	 * Add a User to the database.
	 * @param user the User object to add
	 * @throws SQLException if an sql error occurs
	 */
	public static void addUser(User user) throws SQLException {
		modifyStatement(addUserStatement, new DBObject[]{new DBStringObject(user.getUsername()), new DBStringObject(user.getPassword()), new DBStringObject(user.getFirstname()), new DBStringObject(user.getLastname()), new DBFloatObject(user.getStudentNumber())});
	}

	/**
	 * Remove a User from the database.
	 * @param user the User object to remove
	 * @throws SQLException if an sql error occurs
	 */
	public static void removeUser(User user) throws SQLException {
		modifyStatement(removeUserStatement, new DBObject[]{new DBStringObject(user.getUsername()), new DBStringObject(user.getPassword())});
	}

	//Courses
	
	/**
	 * Get a course based on its courseID and term.
	 * @param courseID the course's ID, eg: COMP4770-001 
	 * @param term the semester which this course offering occured, eg: WINTER16
	 * @return the Course object or null if not found
	 * @throws SQLException if an SQL error occured
	 */
	public static Course getCourse(String courseID, String term) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectCourseStatement); ){
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, courseID);
		stmt.setString(2, term);
		ResultSet rs = stmt.executeQuery();

		System.out.println("courseID: " + courseID);
		System.out.println("term: " + term);
		//COMP4770-001|WINTER16|
		if (rs.next()) {
			String cID = rs.getString("courseID");
			String t = rs.getString("term");
			String n = rs.getString("name");
			return new Course(cID, t, n);
		}
		System.out.println("Returning null...");
		return null;
		}
	}

	/**
	 * Get all courses of the data base. A map is returned such that the key is couseID|term. 
	 * Eg: COMP4770-001|WINTER16
	 * @return a map container all courses in the database
	 * @throws SQLException if an sql error occurs
	 */
	public static Map<String, Course> getAllCourses() throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAllCoursesStatement); ){
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
	}

	/**
	 * Add a course to the database.
	 * @param course the Course to add
	 * @throws SQLException if an sql error occurs 
	 */
	public static void addCourse(Course course) throws SQLException {
		modifyStatement(addCourseStatement, new DBObject[]{new DBStringObject(course.getCourseID()), new DBStringObject(course.getTerm())});
	}

	/**
	 * Remove a course from the database.
	 * @param course the Course to remove.
	 * @throws SQLException if an sql error occurs
	 */
	public static void removeCourse(Course course) throws SQLException {
		modifyStatement(removeCourseStatement, new DBObject[]{new DBStringObject(course.getCourseID()), new DBStringObject(course.getTerm()), new DBStringObject(course.getName())});
	}

	//Enrollments
	//Enrollments are treated as a String array with values [username, courseID, term, role]
	
	/**
	 * Get an enrollment table entry based on a username and a course offering. 
	 * The username uniquely identifies a User while a course ID and a term uniquely identify a course offering.
	 * The return is a String array with entries: username, courseID, term, and role, in that order. Null is
	 * returned if there is no such enrollment entry.
	 * @param username the username of the User to get enrollment for
	 * @param courseID the ID of the course 
	 * @param term the term the course was offered
	 * @return a String array representing the enrollment or null if no such enrollment
	 * @throws SQLException if an sql error occurs
	 */
	public static String[] getEnrollment(String username, String courseID, String term) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectEnrollmentStatement); ) {
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, username);
		stmt.setString(2, courseID);
		stmt.setString(3, term);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			String r = rs.getString("role");
			return new String[]{u, c, t, r};
		}
		return null;
		}
	}

	/**
	 * Given a course get the instructor for the course. 
	 * @param course the course to find the Instructor for
	 * @return the User object representing the instructor or null if not found
	 * @throws SQLException if an sql error occurs
	 */
	public static User getInstructor(Course course) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(getInstructorStatement); ) {
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, course.getCourseID());
		stmt.setString(2, course.getTerm());
		stmt.setString(3, "Instructor");
		ResultSet rs = stmt.executeQuery();
		User instructor = null;
		if (rs.next()){
			instructor = DBController.getUser(rs.getString("username"));
		}
		return instructor;
		}
	}

	/**
	 * Get all the enrollments for a particular User based on their current active role. A map is returned where the key is 
	 * a String representing the course of the form courseID|term and the object is a Course.
	 * @param user the user to the find enrollments for
	 * @return a map representing all enrollments for the user.
	 * @throws SQLException
	 */
	public static Map<String, Course> getAllEnrollmentsForUser(User user) throws SQLException {
		try (
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectEnrollmentsForUserStatement); ) {
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, user.getUsername());
		stmt.setString(2, user.getActiveRole().toString());
		ResultSet rs = stmt.executeQuery();
		System.out.println("REsult set sucessful");
		Map<String, Course> map = new HashMap<>();
		while (rs.next()){
			String cID = rs.getString(1);
			String term = rs.getString(2);
			String name = rs.getString(3);
			map.put(cID + "|" + term, new Course(cID, term, name));
		}
		return map;
		}
	}

	/**
	 * Get all enrollments for all Users. A map is returned with keys of the form username|courseID|term|role and 
	 * the object is a String array of the key split on the pipe symbol.
	 * @return a map representing all enrollments
	 * @throws SQLException if an sql error occurs
	 */
	public static Map<String, String[]> getAllEnrollments() throws SQLException {
		try (
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAllEnrollmentsStatement); ){
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
	}

	/**
	 * Add an enrollment to the database. The enrollment array should contain strings in the order:
	 * username, courseID, term, role.
	 * @param enrollment an array containing the necessary strings to define an enrollment
	 * @throws SQLException if an sql error occurs
	 */
	public static void addEnrollment(String[] enrollment) throws SQLException {
		if (enrollment.length == 4) modifyStatement(addEnrollmentStatement, new DBObject[]{new DBStringObject(enrollment[0]), new DBStringObject(enrollment[1]), new DBStringObject(enrollment[2]), new DBStringObject(enrollment[3])});
	}

	
	/**
	 * Remove a User from all roles of a given course. Enrollment should contain stirngs in the order: 
	 * username, courseID, term.
	 * @param enrollment the array of String which specify an enrollment
	 * @throws SQLException if an sql error occurs
	 */
	public static void removeEnrollment(String[] enrollment) throws SQLException {
		if (enrollment.length == 3 || enrollment.length == 4) modifyStatement(removeEnrollmentStatement, new DBObject[]{new DBStringObject(enrollment[0]), new DBStringObject(enrollment[1]), new DBStringObject(enrollment[2])});
	}

	//Assignment
	/**
	 * Get an assignment based on the Course and the assignment number
	 * @param courseID the course ID of the course
	 * @param term the term of the Course offering
	 * @param number the assignment number
	 * @return the Assignment object or null if not found
	 * @throws SQLException if an sql error occurs
	 */
	public static Assignment getAssignment(String courseID, String term, int number) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAssignmentStatement); ){
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
	}

	/**
	 * Get the number of assignments currently present for a course. 
	 * @param courseID the course ID of the course
	 * @param term the term the course was offered
	 * @return the number of assignments currently associated with the Course
	 * @throws SQLException if an sql error occurs
	 */
	public static int getMaxAssignmentNumberNumber(String courseID, String term) throws SQLException
	{
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(getMaxAssignmentNumberStatement);
		) {
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, courseID);
		stmt.setString(2, term);
		ResultSet rs = stmt.executeQuery();
		int toRet = 0;
		if (rs.next()) 
		{
			toRet = rs.getInt(1);
		}
		return toRet;
		}
	}
	
	/**
	 * Get all Assignments currently on the system. 
	 * @return a map where keys are of the form courseID|term|assignmentNumber and the objects are Assigments.
	 * @throws SQLException
	 */
	public static Map<String, Assignment> getAllAssignments() throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAllAssignmentsStatement); ){
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
	}

	/**
	 * Add an assignment to the database.  
	 * @param courseID the course ID of the Course the Assignment is for
	 * @param term the term of the course offering
	 * @param number the number this assignment represents 
	 * @param assignName the name of the assignment
	 * @param assignmentPath the path to the root directory of the assignment
	 * @param testPath the path to the test suites for the assignment
	 * @param submissionLimit the number of possible submissions for this assignment
	 * @throws SQLException
	 */
	public static void addAssignment(String courseID, String term, int number, String assignName, String assignmentPath, String testPath, int submissionLimit) throws SQLException
	{
		modifyStatement(addAssignmentStatement, new DBObject[]{new DBStringObject(courseID), 
															   new DBStringObject(term), 
															   new DBIntObject(number), 
															   new DBStringObject(assignName),
															   new DBStringObject(assignmentPath), 
															   new DBStringObject(testPath),
															   new DBIntObject(submissionLimit)});
	}
	
	/**
	 * Add an assignment to the data base
	 * @param assignment the Assignment to add.
	 * @throws SQLException if an sql error occurs
	 */
	public static void addAssignment(Assignment assignment) throws SQLException {
		modifyStatement(addAssignmentStatement, new DBObject[]{new DBStringObject(assignment.getCourse().getCourseID()), 
															   new DBStringObject(assignment.getCourse().getTerm()), 
															   new DBIntObject(assignment.getNumber()), 
															   new DBStringObject("/courses/" + assignment.getCourse().getTerm() + "/" + assignment.getCourse().getCourseID() + "/assignments/" + assignment.getNumber()), 
															   new DBStringObject("/courses/" + assignment.getCourse().getTerm() + "/" + assignment.getCourse().getCourseID() + "/assignments/ " + assignment.getNumber() + "/tests"), 
															   new DBIntObject(assignment.getSubmissionLimit())});
	}

	/**
	 * Remove an Assignment from the database
	 * @param assignment the Assignment to remove
	 * @throws SQLException if an sql error occurs
	 */
	public static void removeAssignment(Assignment assignment) throws SQLException {
		modifyStatement(removeAssignmentStatement, new DBObject[]{new DBStringObject(assignment.getCourse().getCourseID()), new DBStringObject(assignment.getCourse().getTerm()), new DBIntObject(assignment.getNumber())});
	}

	//Assignment Submission
	
	/**
	 * Get a User's assignment submission
	 * @param username the username of the User to get the submssion for
	 * @param courseID the ID of the course 
	 * @param term the term of the course
	 * @param number the assignmetn number
	 * @return the AssignmentSubmission or null if not found
	 * @throws SQLException if an sql error occurs
	 */
	public static AssignmentSubmission getAssignmentSubmission(String username, String courseID, String term, int number) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectASubmissionStatement); ){
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
	}

	/**
	 * Get all AssignmentSubmisisonss. Returns a map where keys are of the form  username|courseID|term|assignmentNumber
	 * and objects are AssignmentSubmission.
	 * @return a map containing all AssignmentSubmisisons 
	 * @throws SQLException if an sql error occurs
	 */
	public static Map<String, AssignmentSubmission> getAllAssignmentSubmissions() throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAllASubmissionStatement); ){
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
	}

	/**
	 * Add an AssignmentSubmission to the database. 
	 * @param aSub the AssignmentSubmission to add
	 * @throws SQLException if an sql error occurs
	 */
	public static void addAssignmentSubmission(AssignmentSubmission aSub) throws SQLException {
		modifyStatement(addASubmissionStatement, new DBObject[]{new DBStringObject(aSub.getOwner().getUsername()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getTerm()), new DBIntObject(aSub.getRelatedAssignment().getNumber()),
				new DBStringObject("/courses/" + aSub.getRelatedAssignment().getCourse().getTerm() + "/" + aSub.getRelatedAssignment().getCourse().getCourseID() + "/submissions/" + aSub.getRelatedAssignment().getNumber() + "/" + aSub.getOwner().getUsername()), new DBIntObject(aSub.getSubmissionNum())});
	}

	/**
	 * Remove an AssignmentSubmission from the database.
	 * @param aSub the AssignmetnSubmission to remove.
	 * @throws SQLException if an sql error occurs.
	 */
	public static void removeAssignmentSubmission(AssignmentSubmission aSub) throws SQLException {
		modifyStatement(removeASubmissionStatement, new DBObject[]{new DBStringObject(aSub.getOwner().getUsername()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(aSub.getRelatedAssignment().getCourse().getTerm()), new DBIntObject(aSub.getSubmissionNum())});
	}

	//Assignment Grade
	/**
	 * Get the Grade associated with a User's Assignment
	 * @param username the username of the User for which to get the Assignment mark
	 * @param courseID the ID of the course the Assignment is for
	 * @param term the term of the course offering
	 * @param number the assignment number for which the grade is being retrieved
	 * @return a Grade object encapsulating Assignment feedback.
	 * @throws SQLException if an sql error occurs
	 */
	public static Grade getGrade(String username, String courseID, String term, int number) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAFeedbackStatement); ){
		stmt.setQueryTimeout(TIMEOUT);
		stmt.setString(1, username);
		stmt.setString(2, courseID);
		stmt.setString(3, term);
		stmt.setInt(4, number);
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			float f = rs.getFloat("grade");
			//String fp = rs.getString("feedbackPath");
			String ft = rs.getString("feedbackText");
			AssignmentSubmission aSub = DBController.getAssignmentSubmission(username, courseID, term, number);
			return new Grade(aSub, f, new Comments(ft, DBController.getUser(username), aSub));
		}
		return null;
		}
	}

	/**
	 * Get all Grades in the database. Returns a map where the keys are strings of the form:
	 * username|courseID|term|number such that the username describes the User who submitted the assignment
	 * for the course described by courseID and term. The number is the assignment number for the particular course.
	 * @return a map containing all Grades in the database
	 * @throws SQLException if an sql error occurs
	 */
	public static Map<String, Grade> getAllGrades() throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAllAFeedbackStatement); ){
		Map<String, Grade> map = new HashMap<>();
		stmt.setQueryTimeout(TIMEOUT);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()){
			String u = rs.getString("username");
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int n = rs.getInt("number");
			float f = rs.getFloat("grade");
			//String fp = rs.getString("feedbackPath");
			String ft = rs.getString("feedbackText");
			AssignmentSubmission aSub = DBController.getAssignmentSubmission(u, c, t, n);
			map.put(String.format("%s|%s|%s|%d", u, c, t, n), new Grade(aSub, f, new Comments(ft, DBController.getUser(u), aSub)));
		}
		return map;
		}
	}

	/**
	 * Add a Grade to the database.
	 * @param aFeed the Grade to add
	 * @throws SQLException
	 */
	public static void addGrade(Grade aFeed) throws SQLException {
		modifyStatement(addAFeedbackStatement, new DBObject[]{new DBStringObject(aFeed.getAssignmentSubmission().getOwner().getUsername()), new DBStringObject(aFeed.getAssignmentSubmission().getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(aFeed.getAssignmentSubmission().getRelatedAssignment().getCourse().getTerm()), new DBIntObject(aFeed.getAssignmentSubmission().getRelatedAssignment().getNumber()),
				new DBFloatObject(aFeed.getGrade()), new DBStringObject(aFeed.getComments().getCommentFiles().getPath()), new DBStringObject(aFeed.getComments().getFeedback())});
	}

	/**
	 * Remvoe a Grade from the database.
	 * @param aFeed the Grade to remove 
	 * @throws SQLException if an sql error occurs.
	 */
	public static void removeGrade(Grade aFeed) throws SQLException {
		modifyStatement(removeAFeedbackStatement, new DBObject[]{new DBStringObject(aFeed.getComments().getRecipient().getUsername()), new DBStringObject(aFeed.getAssignmentSubmission().getRelatedAssignment().getCourse().getCourseID()), new DBStringObject(aFeed.getAssignmentSubmission().getRelatedAssignment().getCourse().getTerm()), new DBIntObject(aFeed.getAssignmentSubmission().getRelatedAssignment().getNumber())});
	}

	//Assignment solutions\
	/**
	 * Get an assignment solution based on the courseID, term, and assignmetn number 
	 * @param courseID the ID of the course
	 * @param term the term of the course offering
	 * @param number the assignment number
	 * @return the AssignmentSolution as described by the parameters or null if not found
	 * @throws SQLException if an sql error occurs
	 */
	public static AssignmentSolution getAssignmentSolution(String courseID, String term, int number) throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectASolutionStatement); ){
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
			return new AssignmentSolution(DBController.getAssignment(c, t, num), new UserFile(DBController.getInstructor(new Course(c, t, "")), "/courses/" + t + "/" + c + "/assignments/" + num + "/solutions"));
		}
		return null;
		}
	}

	/**
	 * Get all AssignmentSolutions in the database. Returns a map where the keys are of the form 
	 * courseID|term|number and the objects are AssignmentSolution objects.
	 * @return a map of all AssignmentSolutions on the database.
	 * @throws SQLException
	 */
	public static Map<String, AssignmentSolution> getAllASolutions() throws SQLException {
		try(
		Connection conn = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = conn.prepareStatement(selectAllASolutionsStatement); ){
		Map<String, AssignmentSolution> map = new HashMap<>();
		stmt.setQueryTimeout(TIMEOUT);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()){
			String c = rs.getString("courseID");
			String t = rs.getString("term");
			int num = rs.getInt("number");
			String p = rs.getString("path");
			map.put(String.format("%s|%s|%d", c, t, num), new AssignmentSolution(DBController.getAssignment(c, t, num), new UserFile(DBController.getInstructor(new Course(c, t, "")), "/courses/" + t + "/" + c + "/assignments/" + num + "/solutions")));
		}
		return map;
		}
	}

	/** 
	 * Add an AssignmentSolution to the data base
	 * @param aSol the AssignmentSolution to add.
	 * @throws SQLException if an sql error occurs
	 */
	public static void addAssignmentSolution(AssignmentSolution aSol) throws SQLException {
		modifyStatement(addASolutionStatement, new DBObject[]{new DBStringObject(aSol.getAssignment().getCourse().getCourseID()), new DBStringObject(aSol.getAssignment().getCourse().getTerm()), new DBIntObject(aSol.getAssignment().getNumber()), new DBStringObject(aSol.getFiles().getPath())});
	}

	/**
	 * Remove an AssignmetnSOlution from the database.
	 * @param aSol the AssignmentSolution to remove
	 * @throws SQLException if an sql error occurs.
	 */
	public static void removeAssignment(AssignmentSolution aSol) throws SQLException {
		modifyStatement(removeASolutionStatement, new DBObject[]{new DBStringObject(aSol.getAssignment().getCourse().getCourseID()), new DBStringObject(aSol.getAssignment().getCourse().getTerm()), new DBIntObject(aSol.getAssignment().getNumber())});
	}

	//For statements that change the state of a table (add/remove data)
	/**
	 * Given a prepared statement String, and an array of DBObjects, insert those database objects into the PreparedStatement.
	 * @param statementType the string describing the prepared statement
	 * @param dbArray an array of objects to insert into the PreparedStatemetn
	 * @throws SQLException if an sql error occurs
	 */
	public static void modifyStatement(String statementType, DBObject[] dbArray) throws SQLException {
		try(
		Connection connection = DriverManager.getConnection(DB_URL);
		PreparedStatement stmt = connection.prepareStatement(statementType); ){
		for (int i = 0; i < dbArray.length; i++){
			dbArray[i].addToStatement(stmt, i+1);
		}
		System.out.println("About to execute DB statement");
		stmt.execute();
		}
	}
}
