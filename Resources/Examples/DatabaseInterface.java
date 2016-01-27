package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import account.Account;
import account.AccountManager;
import account.StudentInformation;
import society.Election;
import society.MemberType;
import society.Society;
import society.SocietyInformation;

public class DatabaseInterface 
{
	// try loading sqlite
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch( Exception ex ) {
        	System.out.println("SQLite not found! Please install it.");
            System.err.println( ex.getMessage() );
            ex.printStackTrace();
            System.exit( 0 );
        }
    }
    
    // get a society by id
    private static String selectSocietyById =
    	"SELECT * FROM Societies WHERE ID=?";
    
    // get all societies
    private static String selectAllSocieties = 
    	"SELECT * FROM Societies";
    
    // add a society to the database 
    private static String addSociety = 
		"INSERT INTO Societies (ID, societyName, foundID, description, isSanctioned) VALUES(?, ?, ?, ?, ?)";
    
    // add member to society
    private static String addMemberToSociety =
    	"INSERT OR REPLACE INTO SocietyMembers (societyID, memberID, memberType) VALUES(?, ?, ?) ";
    
    // remove member form society
    private static String removeMember = 
    	"DELETE FROM SocietyMembers WHERE memberID=? AND societyID=?";
    
    // get all members for all societies
    private static String selectAllMembers = 
		"SELECT * FROM SocietyMembers";
    
    // get all members of the given society
    private static String selectAllMembersOfSociety = 
		"SELECT * FROM SocietyMembers WHERE societyID=?";
    
    // get all accounts 
    private static String selectAllAccounts = 
		"SELECT * FROM Accounts";
    
    // add new account to database
    private static String addAccount = 
		"INSERT INTO Accounts (ID, name, stuID, DOB, username, password, major) VALUES(?, ?, ?, ?, ?, ?, ?)";
    
    private static String selectAllElections = 
    	"SELECT * FROM ElectionC";
    private static String selectAllElectionsVoters = 
        	"SELECT * FROM ElectionV";
    
    private static String addElectionCandidate = 
    		"INSERT INTO ElectionC (socID, date, candidateID, votes) VALUES(?, ?, ?, ?)";
    private static String addElectionVoter = 
    		"INSERT INTO ElectionV (socID, date, voterID) VALUES(?, ?, ?)";
    private static String removeElectionVote = 
    		"DELETE FROM ElectionV WHERE socID = ? and voterID = ?";
    private static String addElectionVote = 
    		"UPDATE ElectionC SET votes=votes+1 WHERE socID = ? AND date = ? AND candidateID = ?";
    
    public static final String DB_FILENAME = "MUNBook.db";	
    public static String DB_URL;
    public static final int TIMEOUT = 20;

    public static void initialize() throws SQLException {
        // find path of database file on the users's system
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
    	// search for MUNBook.db in the current directory and the two parent directories
    	File currentDir = new File(System.getProperty("user.dir"));
    	int i = 0;
    	do {
	    	File[] filesInCurrentDir = currentDir.listFiles(dbFilter);
	    	if (filesInCurrentDir.length > 0) {
	    		return "jdbc:sqlite:" + filesInCurrentDir[0];
	    	}
	    	currentDir = currentDir.getParentFile();
    	} while (i < 2);
    	throw new FileNotFoundException("Database file MUNBook.db not found!");
    }
    
    public static void addSociety(Society society) throws SQLException
    {
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(addSociety);
  
			){
	    	long societyID = society.getSocietyID();
	    	SocietyInformation societyInfo = society.getSocietyInfo();
	    	String name = societyInfo.getSocietyName();
	    	long founderID = societyInfo.getFounderID();
	    	String description = societyInfo.getDescription();
	    	stmt.setLong(1,  societyID);
	    	stmt.setString(2, name);
	    	stmt.setLong(3, founderID);
	    	stmt.setString(4, description);
	    	stmt.setBoolean(5, false);
	    	stmt.execute();
	    	// add founder to the members of this society
	    	DatabaseInterface.addMemberToSociety(societyID, founderID, MemberType.eMemberType.MT_founder);
    	}
    }
    
    public static Map<Long, Society> getAllSocieties() throws SQLException 
    { 
    	try(
    			Connection conn = DriverManager.getConnection(DB_URL);
    			PreparedStatement stmt = conn.prepareStatement(selectAllSocieties);
    			PreparedStatement stmt2 = conn.prepareStatement(selectAllMembers);
			){
    		Map<Long, Society> map = new HashMap<>();
    		
    		stmt.setQueryTimeout(TIMEOUT);
    		ResultSet rs = stmt.executeQuery();
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
    		stmt2.setQueryTimeout(TIMEOUT);
    		rs = stmt2.executeQuery();
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
    }
    
    

    
    
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
