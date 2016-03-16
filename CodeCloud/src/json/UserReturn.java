package json;

/**
 * The return from a request wishing to get the current signed-in user's information.
 * 
 * @author Alex Brandt
 *
 */
public class UserReturn
{
	/**
	 * The user's username.
	 */
	public String username;
	
	/**
	 * The user's first name.
	 */
	public String firstname;
	
	/**
	 * The user's last name.
	 */
	public String lastname;
	
	/**
	 * The user's current active role.
	 */
	public int activeRole;
	
	/**
	 * The list of possible roles the user can take given all of their enrollments.
	 */
	public int[] possibleRoles;

}