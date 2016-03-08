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

	/**
	 * Get the User's username.
	 * @return the User's username
	 */
	public String getUsername(){
		return username;
	}

	/**
	 * Get the User's password.
	 * @return the User's password
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * Get the User's student number.
	 * @return the User's student number
	 */
	public long getStudentNumber(){
		return studentNumber;
	}

	/**
	 * Get the User's first name.
	 * @return the User's first name
	 */
	public String getFirstname(){
		return firstname;
	}

	/**
	 * Get the User's last name.
	 * @return the User's last name
	 */
	public String getLastname(){
		return lastname;
	}
	
	/**
     * Temporary toString method which formats the User object for output
     * to the text file which stores all users. 
     */
    public String toString() {
        return username + "|" + password + "|" + firstname + "|" + lastname + "|" + studentNumber;
    }
}
