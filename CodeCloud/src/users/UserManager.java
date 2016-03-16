package users;

public class UserManager 
{
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
