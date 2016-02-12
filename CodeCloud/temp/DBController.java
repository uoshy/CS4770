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
    	// search for Database.db in the current directory and the two parent directories
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


    public User getUser(String userName) {
        try {
                Connection conn = DriverManager.getConnection(dbURL);
                PreparedStatement stmt = conn.prepareStatement(selectUserStatement);
            } catch(SQLException sqle){
              sqle.printStackTrace();
            }
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

    public static Map<String, User> getAllUsers() {
    	try{
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(selectAllAccounts);
			}
      catch (SQLException sqle){
        sqle.printStackTrace();
      }

    		//long ID, String name, long student id, Date DOB, String username, string password
    		Map<String, User> map = new HashMap<>();

    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next())
    		{
    			String username = rs.getString(1);
    			String password = rs.getString(2);
    			map.put(username, new User(username, password));
    		}

    		return map;
    }

   public static void addUser(User user){
	    modifyStatement(addUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
	  }

    public static void removeUser(User user){
       modifyStatement(removeUserStatement, new DBObject[]{new DBString(user.getUsername()), new DBString(user.getPassword())});
    }


    //For statements that change the state of a table (add/remove data)
  	public static void modifyStatement(String statementType, DBObject[] dbArray){
  		try{
  			Connection connection = DriverManager.getConnection(DB_URL);
  			PreparedStatement statement = connection.prepareStatement(statementType);
  		}
      catch (SQLException sqle){
        sqle.printStackTrace();
      }
  		for (int i = 0; i < dbArray.length; i++){
  			dbArray[i].addToStatement(statement, i+1);
			}
  	}


    /**
     * Can also be used to set a society members type
     * @param societyID
     * @param accountID
     * @param memberType
     * @throws SQLException
     */
/*
    public static void addMemberToSociety(long societyID, long accountID, MemberType.eMemberType memberType) throws SQLException
    {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(addMemberToSociety);

			){
	    	stmt.setLong(1, societyID);
	    	stmt.setLong(2, accountID);
	    	stmt.setInt(3, memberType.ordinal());
	    	stmt.execute();
    	}
    }

    public static void removeMember(long societyID, long accountID) throws SQLException
    {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(removeMember);

			){
	    	stmt.setLong(1, accountID);
	    	stmt.setLong(2, societyID);
	    	stmt.execute();
    	}
    }

    public static Map<Long, Collection<Election>> getPassedElections() throws SQLException {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(selectAllElections);

			){
    		stmt.setQueryTimeout(TIMEOUT);

    		Map<Long, Collection<Election>> map = new HashMap<Long, Collection<Election>>();

    		ResultSet rs = stmt.executeQuery();
    		while(rs.next())
    		{
    			long societyID = rs.getLong(1);
    			long time = rs.getLong(2);
    			long candidateID = rs.getLong(3);
    			int votes = rs.getInt(4);
    			Date endDate = new Date(time);
    			Date now = new Date();
    			if(now.after(endDate))
    			{
    				if(map.containsKey(societyID))
    				{
    					Collection<Election> elections = map.get(societyID);
    					boolean foundElection = false;
    					for(Election e : elections)
    					{
    						if(e.getInfo().getEndDate().equals(endDate))
    						{
		    					e.addCandidate(candidateID);
		    					e.setVotes(candidateID, votes);
		    					foundElection = true;
    						}
    					}
    					if(!foundElection)
    					{
    						Election e = new Election(endDate, societyID);
    						e.addCandidate(candidateID);
    						e.setVotes(candidateID, votes);
    						elections.add(e);
    					}
    				}
    				else
    				{
    					Election e = new Election(endDate, societyID);
    					e.addCandidate(candidateID);
    					e.setVotes(candidateID, votes);
    					Collection<Election> elections = new LinkedList<Election>();
    					elections.add(e);
    					map.put(societyID, elections);
    				}
    			}
    		}
    		return map;
    	}
    }

    public static void removeElectionVote(long socID, long vID) throws SQLException {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(removeElectionVote);
			){
    		stmt.setLong(1, socID);
    		stmt.setLong(2, vID);
    		stmt.execute();
    	}
    }

    public static void addElectionVoter(Election e, long socID, long vID)throws SQLException{
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(addElectionVoter);

			){
	    	stmt.setLong(1, socID);
	    	stmt.setLong(2, e.getInfo().getEndDate().getTime());
	    	stmt.setLong(3, vID);
	    	stmt.execute();
    	}
    }
    public static void addElectionCandidate(Election e, long socID, long cID)throws SQLException{
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(addElectionCandidate);

			){
	    	stmt.setLong(1, socID);
	    	stmt.setLong(2, e.getInfo().getEndDate().getTime());
	    	stmt.setLong(3, cID);
	    	stmt.setLong(4, 0);
	    	stmt.execute();
    	}
    }

    public static void addElectionVote(Election e, long societyID, long canID) throws SQLException{
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(addElectionVote);

			){

    		stmt.setLong(1, societyID);
    		stmt.setLong(2,  e.getInfo().getEndDate().getTime());
    		stmt.setLong(3, canID);
    		stmt.execute();
    	}
    }**/





    //For reference on using JDBC

//    public User getUserById( String name ) throws SQLException {
//        try (
//                Connection conn = DriverManager.getConnection(dbURL);
//                PreparedStatement stmt = conn.prepareStatement(selectByUser);
//            ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, name);
//            ResultSet rs = stmt.executeQuery();
//            if ( rs.next() ) {
//                String uName = rs.getString("name");
//                String pw = rs.getString("pw");
//                String role = rs.getString("role");
//                int gamesPlayed = Integer.parseInt(rs.getString("gamesPlayed"));
//                int wins = Integer.parseInt(rs.getString("wins"));
//                int losses = Integer.parseInt(rs.getString("losses"));
//                return new User(uName, pw, role, gamesPlayed, wins, losses );
//            }
//            }
//        return null;
//    }
//
//    public Collection<User> getAllUsers() throws SQLException {
//        try (
//                Connection conn = DriverManager.getConnection(dbURL);
//                PreparedStatement stmt = conn.prepareStatement(selectAll);
//            ) {
//                stmt.setQueryTimeout(timeout);
//                ResultSet rs = stmt.executeQuery();
//                Collection<User> coll = new ArrayList<User>();
//                while (rs.next()) {
//                    String uName = rs.getString("name");
//                    String pw = rs.getString("pw");
//                    String role = rs.getString("role");
//                    int gamesPlayed = Integer.parseInt(rs.getString("gamesPlayed"));
//                    int wins = Integer.parseInt(rs.getString("wins"));
//                    int losses = Integer.parseInt(rs.getString("losses"));
//                    coll.add(new User(uName, pw, role, gamesPlayed, wins, losses));
//                }
//                return coll;
//            }
//    }
//
//    public void addUser(String name, String password, String role)
//        throws SQLException
//    {
//        try (
//                Connection conn = DriverManager.getConnection( dbURL );
//                PreparedStatement stmt = conn.prepareStatement( insertIntoUser );
//            ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, name);
//            stmt.setString(2, password);
//            stmt.setString(3, role);
//            stmt.setString(4, "0");
//            stmt.setString(5, "0");
//            stmt.setString(6, "0");
//            stmt.executeUpdate();
//            }
//    }
//
//    public void changePassword( String name, String password )
//        throws SQLException
//    {
//        try (
//                Connection conn = DriverManager.getConnection( dbURL );
//                PreparedStatement stmt = conn.prepareStatement( updatePassword );
//            ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, password);
//            stmt.setString(2, name);
//            stmt.executeUpdate();
//            }
//    }
//
//    public void changeRole( String name, String role ) throws SQLException {
//        try (
//                Connection conn = DriverManager.getConnection( dbURL );
//                PreparedStatement stmt = conn.prepareStatement( updateRole );
//        ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, role);
//            stmt.setString(2, name);
//            stmt.executeUpdate();
//        }
//    }
//
//    public void changeGamesPlayed(String name, int gp) throws SQLException {
//        try (
//            Connection conn = DriverManager.getConnection(dbURL);
//            PreparedStatement stmt = conn.prepareStatement(updateGamesPlayed);
//        ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, new Integer(gp).toString());
//            stmt.setString(2, name);
//            stmt.executeUpdate();
//        }
//    }
//
//    public void changeWins(String name, int wins) throws SQLException {
//        try (
//            Connection conn = DriverManager.getConnection(dbURL);
//            PreparedStatement stmt = conn.prepareStatement(updateWins);
//        ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, new Integer(wins).toString());
//            stmt.setString(2, name);
//            stmt.executeUpdate();
//        }
//    }
//
//    public void changeLosses(String name, int losses) throws SQLException {
//        try (
//            Connection conn = DriverManager.getConnection(dbURL);
//            PreparedStatement stmt = conn.prepareStatement(updateLosses);
//        ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, new Integer(losses).toString());
//            stmt.setString(2, name);
//            stmt.executeUpdate();
//        }
//    }
//
//    public void deleteUser( String name ) throws SQLException {
//        try (
//                Connection conn = DriverManager.getConnection( dbURL );
//                PreparedStatement stmt = conn.prepareStatement( deleteUser );
//        ) {
//            stmt.setQueryTimeout(timeout);
//            stmt.setString(1, name);
//            stmt.executeUpdate();
//        }
//    }

}
