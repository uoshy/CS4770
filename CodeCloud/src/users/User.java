package users;

/**
 * A Registered User of the system. A User has login credentials as a 
 * username and password.
 * @author Alex
 *
 */
public class User {
	
	/** The first and last name of the user	 */
	private String firstname, lastname;
	
	/** The user's username */
	private String username;
	
	/** The user's password */
	private String password;

	private long studentNumber;
	
	/** The user's active role on the system */
	private Role activeRole;
	
	/**
	 * Register a new User with the specified parameters.
	 * @param username the user's username
	 * @param password the user's password
	 * @param firstname the user's first name
	 * @param lastname the user's last name
	 */
	public User(String username, String password, String firstname, String lastname, long studentNumber){
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.studentNumber = studentNumber;
	}

	/**
	 * Set the User's active Role on the system.
	 * @param newRole the new Role for the User
	 * @see Role
	 */
	public void setActiveRole(Role newRole)
	{
		activeRole = newRole;
	}
	
	/**
	 * Get the User's active Role on the system.
	 * Often used to determine permissions of the user and how the system
	 * responds to requests.
	 * @return the User's current active Role
	 */
	public Role getActiveRole()
	{
		return activeRole;
	}

	public String getUsername(){
		return username;
	}

	public String getPassword(){
		return password;
	}

	public long getStudentNumber(){
		return studentNumber;
	}

	public String getFirstname(){
		return firstname;
	}

	public String getLastname(){
		return lastname;
	}
}
