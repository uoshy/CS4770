package json;

/**
 * A JSON object describing data for a User to be registered.
 * @author Alex
 *
 */
public class RegisterInput
{
	/**
	 * The intended username for the registration of the User.
	 */
	public String username;
	
	/**
	 * The user's password.
	 */
	public String password;
	
	/**
	 * The user's first name.
	 */
	public String firstName;
	
	/**
	 * The user's last name.
	 */
	public String lastName;
	
	/**
	 * The user's student/faculty number.
	 */
	public String studentNum;
}