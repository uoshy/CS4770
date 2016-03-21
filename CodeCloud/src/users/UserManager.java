package users;

/**
 * A set of utility functions to help deal with Users.
 * 
 * @author Alex
 *
 */
public class UserManager 
{
	/**
	 * Change the active role of the speicifed User to the specified Role
	 * @param user the User which to change the active Role of.
	 * @param newRole the Role which the User should now take
	 * @return true iff the change was successful.
	 */
	public static boolean changeRole(User user, Role newRole)
	{
		//TODO check DB to see if the user is specified as the newRole in any course
		if(true) //change to check database
		{
			user.setActiveRole(newRole);
			return true;
		}
		return false;
	}
}
