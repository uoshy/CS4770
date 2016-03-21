package assignments;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;

import users.User;
import json.EnrollmentListReturn;
import json.CourseListReturn;
import utility.DBController;
import assignments.Assignment;

/**
 * A set of utility functions to help deal with Courses.
 * 
 * @author Alex Brandt
 *
 */
public class CourseManager 
{
	/**
	 * The end date of the fall semester. December 31.
	 */
	public static final Calendar FALL_END_DATE = getFallEndDate();
	
	/**
	 * The end date of the winter semester. April 31.
	 */
	public static final Calendar WINTER_END_DATE = getWinterEndDate();
	
	/**
	 * The end date of the spring semester. August 31.
	 */
	public static final Calendar SPRING_END_DATE = getSpringEndDate();

	private static Calendar getFallEndDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(2000, 12, 31);
		return cal;
	}

	private static Calendar getWinterEndDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(2000, 4, 30);
		return cal;
	}

	private static Calendar getSpringEndDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(2000, 8, 31);
		return cal;
	}

	/**
	 * Determine if a course is active based on the course's semester and the current date.
	 * @param course the Course to determine if it is active
	 * @return true iff the course is offered in the current semester.
	 */
	public static boolean courseIsActive(Course course)
	{
		String term = course.getTerm();
		int index = -1;
		int yearIndex = -1;
		Calendar calToCompare = Calendar.getInstance();
		for(int i = 0; i < 3; i++)
		{
			if(i == 0)
			{	index = term.indexOf("FALL");
				if(index != -1)
				{	yearIndex = 4; calToCompare = FALL_END_DATE; }
			}
			else if(i == 1)
			{
				index = term.indexOf("WINTER");
				if(index != -1)
				{	yearIndex = 6; calToCompare = WINTER_END_DATE; }
			}
			else if(i == 2)
			{
				index = term.indexOf("SPRING");
				if(index != -1)
				{	yearIndex = 6; calToCompare = SPRING_END_DATE; }
			}
			
			if(yearIndex != -1)
				break;
		}

		try{
			String year = term.substring(yearIndex);
			int yearInt = Integer.parseInt(year);
			yearInt += 2000;
			
			Calendar current = Calendar.getInstance();
			
			if(yearInt == current.get(Calendar.YEAR)) {
				if(current.get(Calendar.MONTH) <= calToCompare.get(Calendar.MONTH)) {
					if(current.get(Calendar.DAY_OF_MONTH) <= calToCompare.get(Calendar.DAY_OF_MONTH))
						return true;
				}
			}
			return false;
			
		} catch (NumberFormatException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Get the enrollments for a given User and that User's active role. This includes student, marker, instructor.
	 * @param user the User to get enrollments for. 
	 * @return an EnrollmentListReturn encapsulating all necessary data
	 * @see json.EnrollmentListReturn
	 */
	public static EnrollmentListReturn getCoursesForUser(User user)
	{
		Map<String, Course> map;
		try{
			map = DBController.getAllEnrollmentsForUser(user);
		} catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		System.out.println("Total enrollments: " + map.size());
		Collection<Course> courses = map.values();
		ArrayList<Course> listCourses = new ArrayList<>(courses);
		Collections.sort(listCourses, (Course c1, Course c2) -> //sort chronologically
		{	
			String term1 = c1.getTerm();
			String term2 = c2.getTerm();
			
			int sem1 = -1 , sem2 = -1; //0 Winter, 1 Spring, 2 Fall
			int year1 = -1, year2 = -1;
			if(term1.indexOf("FALL") != -1)
			{
				sem1 = 2;
				year1 = Integer.parseInt(term1.substring(4));
			}
			else if(term1.indexOf("SPRING") != -1)
			{
				sem1 = 1;
				year1 = Integer.parseInt(term1.substring(6));
			}
			else if(term1.indexOf("WINTER") != -1)
			{
				sem1 = 0;
				year1 = Integer.parseInt(term1.substring(6));
			}
				
			if(term2.indexOf("FALL") != -1)
			{
				sem2 = 2;
				year2 = Integer.parseInt(term2.substring(4));
			}
			else if(term2.indexOf("SPRING") != -1)
			{
				sem2 = 1;
				year2 = Integer.parseInt(term2.substring(6));
			}
			else if(term2.indexOf("WINTER") != -1)
			{
				sem2 = 0;
				year2 = Integer.parseInt(term2.substring(6));
			}
			
			if(year1 < year2)
				return -1;
			else if(year2 < year1)
				return 1;
			else
			{
				return sem1 - sem2;
			}
			
		});
		
		String[] names = new String[listCourses.size()];
		String[] terms = new String[listCourses.size()];
		String[] IDs = new String[listCourses.size()];
		boolean[] active = new boolean[listCourses.size()];
		for(int i = 0; i < listCourses.size(); i++){
			Course c = listCourses.get(i);
			names[i] = c.getName();
			terms[i] = c.getTerm();
			IDs[i] = c.getCourseID();
			active[i] = courseIsActive(c);
		}
		
		EnrollmentListReturn listing = new EnrollmentListReturn();
		listing.courseNames = names;
		listing.courseIDs = IDs;
		listing.courseTerms = terms;
		listing.courseIsActive = active;
		return listing;
		
	}
	
	/**
	 * Determine if a User is authorized to view a course with the given role.
	 * @param user the User to authorize
	 * @param courseID the course ID of the Course instance to compare
	 * @param courseTerm the term of the Course instance to compare
	 * @param role the attempted role the User wishes to be authorized as
	 * @return true iff the user is authorized.
	 */
	public static boolean isUserAuthorizedWithRole(User user, String courseID, String courseTerm, String role)
	{
		try {
			String[] enroll = DBController.getEnrollment(user.getUsername(), courseID, courseTerm);
			if(enroll == null)
				return false;
			
			System.out.println(Arrays.toString(enroll));
			if(enroll[3].equalsIgnoreCase(role))
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Get a course given the courseID and term
	 * @param courseID the courseID of the course
	 * @param term the term of the course offering
	 * @return the Course object described by courseID and term, or null if not found.
	 */
	public static Course getCourse(String courseID, String term)
	{
		try
		{
			return DBController.getCourse(courseID, term);
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The path on the file system to the root directory contianing all course files.
	 */
	public static final String coursesPath = "static/courses/";
	
	/**
	 * Add an assignment to a Course specified by courseID, term. The new assignment should have a submisison limit as specified.
	 * @param courseID the courseID of the course 
	 * @param term the term of the course offeirng
	 * @param submissionLimit the limit on submission attemps to towards this assignment.
	 */
	public static void addAssignmentToCourse(String courseID, String term, int submissionLimit)
	{
		try {
			int maxAssignNumber = DBController.getMaxAssignmentNumberNumber(courseID, term);
			System.out.println("max assign number: " + maxAssignNumber);
			String path = coursesPath + term +  "/" + courseID;
			path += "/assignments/Assignment" + (maxAssignNumber+1);
			File file = new File(path);
			if (file.exists()) 
				return;
			try {
				file.mkdir();
				File tests = new File(path + "/tests");
				tests.mkdir();
				String assignName = "Assignment " + (maxAssignNumber + 1);
				System.out.println(path);
				DBController.addAssignment(courseID, term, maxAssignNumber+1, assignName, path, path + "/tests", submissionLimit);
			}
			catch (SecurityException ex){
				System.out.println("\n\nSecurity Exception!\n\n");
				ex.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    //For admin-page: COPIED in case of changes
    public static CourseListReturn getCourseList() 
    {
		Map<String, Course> map;
		try{
			map = DBController.getAllCourses();
		} catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		System.out.println("Total courses: " + map.size());
		Collection<Course> courses = map.values();
		ArrayList<Course> listCourses = new ArrayList<>(courses);
		Collections.sort(listCourses, (Course c1, Course c2) -> //sort chronologically
		{	
			String term1 = c1.getTerm();
			String term2 = c2.getTerm();
			
			int sem1 = -1 , sem2 = -1; //0 Winter, 1 Spring, 2 Fall
			int year1 = -1, year2 = -1;
			if(term1.indexOf("FALL") != -1)
			{
				sem1 = 2;
				year1 = Integer.parseInt(term1.substring(4));
			}
			else if(term1.indexOf("SPRING") != -1)
			{
				sem1 = 1;
				year1 = Integer.parseInt(term1.substring(6));
			}
			else if(term1.indexOf("WINTER") != -1)
			{
				sem1 = 0;
				year1 = Integer.parseInt(term1.substring(6));
			}
				
			if(term2.indexOf("FALL") != -1)
			{
				sem2 = 2;
				year2 = Integer.parseInt(term2.substring(4));
			}
			else if(term2.indexOf("SPRING") != -1)
			{
				sem2 = 1;
				year2 = Integer.parseInt(term2.substring(6));
			}
			else if(term2.indexOf("WINTER") != -1)
			{
				sem2 = 0;
				year2 = Integer.parseInt(term2.substring(6));
			}
			
			if(year1 < year2)
				return -1;
			else if(year2 < year1)
				return 1;
			else
			{
				return sem1 - sem2;
			}
			
		});
		
        String[] names = new String[listCourses.size()];
		String[] terms = new String[listCourses.size()];
		String[] IDs = new String[listCourses.size()];
		boolean[] active = new boolean[listCourses.size()];
		for(int i = 0; i < listCourses.size(); i++){
			Course c = listCourses.get(i);
			names[i] = c.getName();
			terms[i] = c.getTerm();
			IDs[i] = c.getCourseID();
			active[i] = courseIsActive(c);
		}
		
		CourseListReturn listing = new CourseListReturn();
		listing.courseNames = names;
		listing.courseIDs = IDs;
		listing.courseTerms = terms;
		listing.courseIsActive = active;
		return listing;
	}
    
}
