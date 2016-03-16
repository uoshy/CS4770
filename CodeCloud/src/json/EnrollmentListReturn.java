package json;

/**
 * The JSON object returned by a request to obtain all courses for a given user.
 * Note that matching indices in the arrays correspond to a single couse instance.
 * @author Alex Brandt
 *
 */
public class EnrollmentListReturn 
{
	/**
	 * The names all enrolled or previously enrolled courses. 
	 */
	public String[] courseNames;
	
	/**
	 * The terms of all the enrolled or previously enrolled courses.
	 */
	public String[] courseTerms;
	
	/**
	 * The course IDs of all the enrolled or previously enrolled courses.
	 */
	public String[] courseIDs;
	
	public boolean[] courseIsActive;
}
