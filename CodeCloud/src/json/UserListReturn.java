package json;

/**
 * The JSON object returned by a request to obtain all users.
 */
public class UserListReturn 
{
	/**
	 * The usernames of all users in database
	 */
	public String[] usernames;

	/**
	 * The first & last names of all users in database.
	 */
	public String[] userFullNames;
    
}
