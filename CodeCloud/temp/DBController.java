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
import java.util.Arrays;
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
    private static String selectUser =
    	"SELECT * FROM User WHERE username=?";
    
    //get all users
    private static String selectAllUsers = 
    	"SELECT * FROM Users";
    
    //add a user
    private static String addUser = 
	"INSERT INTO Users (username, password) VALUES(?, ?)";    

    //remove user
    private static String removeUser = 
    	"DELETE FROM Users WHERE username=?";

    //courses
    //get a course by courseID and term
    private static String selectCourse =
    	"SELECT * FROM User WHERE username=? AND term=?";
    
    //get all courses
    private static String selectAllCourses = 
    	"SELECT * FROM Courses";
    
    //add a course 
    private static String addCourse = 
	"INSERT INTO Courses (courseID, term, name) VALUES(?, ?, ?)";

    //remove course
    private static String removeCourse = 
    	"DELETE FROM Courses WHERE courseID=? AND term=?";

    //enrollments
    //get an enrollment by username, courseID, and term
    private static String selectEnrollment =
    	"SELECT * FROM Courses WHERE username=? AND courseID=? AND term=?";
    
    //get all enrollments
    private static String selectAllEnrollments = 
    	"SELECT * FROM Enrollments";
    
    //add an enrollment
    private static String addEnrollment = 
	"INSERT INTO Enrollments (username, courseID, term, role) VALUES(?, ?, ?, ?)";

    //remove enrollment
    private static String removeEnrollment = 
    	"DELETE FROM Enrollments WHERE username=? AND courseID=? AND term=?";

    //assignments
    //get an assignment by courseID, term, and number
    private static String selectAssignment =
    	"SELECT * FROM Assignments WHERE courseID=? AND term=? AND number=?";

    //get all assignments
    private static String selectAllAssignments = 
    	"SELECT * FROM Assignments";
    
    //add an assignment 
    private static String addAssignment = 
	"INSERT INTO Assignments (courseID, term, number, name, path, testSuitePath, submissionLimit) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove assignment
    private static String removeMember = 
    	"DELETE FROM Assignments WHERE courseID=? AND term=? AND number=?";

    //assignment solutions
    //get an assignment solution by courseID, term, and number
    private static String selectASolution =
    	"SELECT * FROM User WHERE courseID=? AND term=? AND number=?";
    
    //get all assignment solutions
    private static String selectAllASolutions =
    	"SELECT * FROM AssignmentSolutions";
    
    //add an assignment solution 
    private static String addASolution =
	"INSERT INTO Users (courseID, term, number, path) VALUES(?, ?, ?, ?)";

    //remove assignment solution
    private static String removeMember = 
    	"DELETE FROM AssignmentSolutions WHERE courseID=? AND term=? AND number=?";

    //assignment feedback
    //get an assignment's feedback
    private static String selectAFeedback =
    	"SELECT * FROM AssignmentFeedback WHERE username=? AND courseID=? AND term=? AND number=?";
    
    //get all feedback
    private static String selectAllAFeedback =
    	"SELECT * FROM AssignmentFeedback";
    
    //add feedback
    private static String addAFeedback =
	"INSERT INTO AssignmentFeedback (username, courseID, term, number, grade, feedbackText, feedbackPath) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove feedback
    private static String removeMember = 
    	"DELETE FROM AssignmentFeedback WHERE username=? AND courseID=? AND term=? and number=?";

    //assignment submissions
    //get an assignmentFeedback by courseID, term, number, and username
    private static String selectAFeedback =
    	"SELECT * FROM AssignmentSubmissions WHERE username=? AND courseID=? AND term=? AND number=?";
    
    //get all feedback
    private static String selectAllAFeedback = 
    	"SELECT * FROM AssignmentFeedback";
    
    //add feedback
    private static String addAFeedback = 
	"INSERT INTO AssignmentFeedback (username, courseID, term, number, grade, feedbackText, feedbackPath) VALUES(?, ?, ?, ?, ?, ?, ?)";

    //remove submission
    private static String removeMember = 
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

	public static void addUser(User user){
		//ArrayList<String> params = new ArrayList<>(Arrays.asList(user.getUsername(), user.getPassword()));
		ArrayList params = new ArrayList<Object>();
		params.add(StatementClassType.DBSTRING);
		params.add(user.getUsername());
		params.add(StatementClassType.DBSTRING);
		params.add(user.getPassword());
		addStatement(params);
	}

	public static void addStatement(ArrayList list){
		try(
			Connection connection = DriverManager.getConnection(DB_URL);
			PreparedStatement preparedStatement = connection.prepareStatement(statementType);
		){
			for(int i = 1; i < list.size(); i += 2){
				list.get(i-1).addToStatement(preparedStatement, list.get(i), i);
			}
		}
	}
		

		
    
/*    public static void addSociety(Society society) throws SQLException
    {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement statement = conn.prepareStatement(addSociety);
  
			){
	    	long societyID = society.getSocietyID();
	    	SocietyInformation societyInfo = society.getSocietyInfo();
	    	String name = societyInfo.getSocietyName();
	    	long founderID = societyInfo.getFounderID();
	    	String description = societyInfo.getDescription();
		//Start index at 1
	    	statement.setLong(1,  societyID);
	    	statement.setString(2, name);
	    	statement.setLong(3, founderID);
	    	statement.setString(4, description);
	    	statement.setBoolean(5, false);
	    	statement.execute();
	    	// add founder to the members of this society
	    	DatabaseInterface.addMemberToSociety(societyID, founderID, MemberType.eMemberType.MT_founder);
    	}
    }
    
    public static Map<Long, Society> getAllSocieties() throws SQLException 
    { 
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement statement = conn.prepareStatement(selectAllSocieties);
    			PreparedStatement statement2 = conn.prepareStatement(selectAllMembers);
			){
    		Map<Long, Society> map = new HashMap<>();
    		
    		statement.setQueryTimeout(TIMEOUT);
    		ResultSet rs = statement.executeQuery();
    		while(rs.next())
    		{
    			long societyID = rs.getLong(1);
    			String societyName = rs.getString(2);
    			long founderID = rs.getLong(3);
    			String description = rs.getString(4);
    			boolean isSanctioned = rs.getBoolean(5);
    			
    			SocietyInformation societyInfo = new SocietyInformation(societyName, founderID, description);
    			Society society = new Society(societyInfo, societyID);
    			map.put(societyID, society);
    		}
    		
    		MemberType.eMemberType[] memTypes = MemberType.eMemberType.values();
    		statement2.setQueryTimeout(TIMEOUT);
    		rs = statement2.executeQuery();
    		while(rs.next())
    		{
    			long societyID = rs.getLong(1);
    			long accountID = rs.getLong(2);
    			MemberType.eMemberType memType = memTypes[rs.getInt(3)];
    			Society soc = map.get(societyID);
    			soc.addMember(accountID, memType);
    		}
    		
    		return map;
    	}    	
    }
    
    public static Map<Long, MemberType.eMemberType> getAllMembers(long societyID) throws SQLException {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(selectAllMembersOfSociety);
			){
    		Map<Long, MemberType.eMemberType> map = new HashMap<>();
    		MemberType.eMemberType[] memTypes = MemberType.eMemberType.values();
    		
    		stmt.setLong(1, societyID);
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next())
    		{
    			long accountID = rs.getLong(2);
    			MemberType.eMemberType memberType = memTypes[rs.getInt(3)];
    			map.put(accountID, memberType);
    		}
    		
    		return map;
    	}
    }
    
    public static void addAccount(Account acc) throws SQLException
    {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(addAccount);
  
			){
	    	long accountID = acc.getAccountID();
	    	StudentInformation stuInfo = acc.getStudentInfo();
	    	String name = stuInfo.getName();
	    	long studentID = stuInfo.getStudentID();
	    	Date DOB = stuInfo.getDateOfBirth();
	    	String major = stuInfo.getMajor();
	    	String user = acc.getUserName();
	    	String pass = acc.getPassword();
	    	stmt.setLong(1, accountID);
	    	stmt.setString(2,  name);
	    	stmt.setLong(3,  studentID);
	    	stmt.setDate(4,  new java.sql.Date(DOB.getTime()));
	    	stmt.setString(5,  user);
	    	stmt.setString(6,  pass);
	    	stmt.setString(7, major);
	    	stmt.execute();
    	
    	}
    }
    
    public static Map<Long, account.Account> getAllAccounts() throws SQLException
    {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(selectAllAccounts);
  
			){
    		
    		
    		//long ID, String name, long student id, Date DOB, String username, string password
    		Map<Long, account.Account> map = new HashMap<>();
    		
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next())
    		{
    			long accountID = rs.getLong(1);
    			String name = rs.getString(2);
    			long studentID = rs.getLong(3);
    			Date DOB = rs.getDate(4);
    			String user = rs.getString(5);
    			String password = rs.getString(6);
    			String major = rs.getString(7);
    			
    			StudentInformation stuInfo = new StudentInformation(name, studentID, DOB, major);
    			Account acc = new Account(accountID, stuInfo, user, password);
    			map.put(accountID, acc);
    		}
  
    		return map;
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
    
    public static Map<Long, Election> getCurrentElections() throws SQLException {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(selectAllElections);
    			PreparedStatement stmt2 = conn.prepareStatement(selectAllElectionsVoters);
  
			){
    		stmt.setQueryTimeout(TIMEOUT);
    		
    		Map<Long, Election> map = new HashMap<Long, Election>();
    		
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next())
    		{
    			long societyID = rs.getLong(1);
    			long time = rs.getLong(2);
    			long candidateID = rs.getLong(3);
    			//System.out.println(candidateID);
    			//System.out.println(AccountManager.getInstance().getAccount(candidateID).getStudentInfo().getName());
    			int votes = rs.getInt(4);
    			Date endDate = new Date(time);
    			Date now = new Date();
    			if(now.before(endDate))
    			{
    				if(map.containsKey(societyID))
    				{
    					Election e = map.get(societyID);
    					e.addCandidate(candidateID);
    					e.setVotes(candidateID, votes);
    					//for(Account a : e.getCandidates())
    						//System.out.println(a.getStudentInfo().getName());
    				}
    				else
    				{
    					Election e = new Election(endDate, societyID);
    					e.addCandidate(candidateID);
    					e.setVotes(candidateID, votes);
    					map.put(societyID, e);
    				}
    			}
    		}
    		ResultSet rs2 = stmt2.executeQuery();
    		while(rs2.next()){
    			long societyID = rs2.getLong(1);
    			long time = rs2.getLong(2);
    			long voterID = rs2.getLong(3);
    			
    			Date endDate = new Date(time);
    			Date now = new Date();
    			if(now.before(endDate)){
    				if(map.containsKey(societyID))
    				{
    					Election e = map.get(societyID);
    					e.addVoter(voterID);
    				}
    			}
    		}
    		
    		return map;
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
