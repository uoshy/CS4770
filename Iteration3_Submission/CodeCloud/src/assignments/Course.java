package assignments;


import users.User;

import java.util.ArrayList;
import java.util.Collection;


/**
 * A semester-specific offering of a course. There is a list of students, instructors, 
 * markers, and assignments associated with this course.
 * 
 * Note that the term of a course shall have the format "FALLXX", "WINTERXX", "SPRINGXX", where XX
 * is the year. (Obviously this can be improved...)
 * 
 * @author Alex
 *
 */
public class Course {

	/** The unique identifier for the course and section. Eg: COMP1710-002 */
	private String courseID;
	
	/** The semester specified for this instance of the course offering. Eg: FALL14 */
	private String term;
	
	/** The fully qualified name of the course. Eg: Object-Oriented Programming I*/
	private String name;
	
	/** The list of instructors for this course offering */
	private Collection<User> instructors;
	
	/** The list of markers for this course offering */
	private Collection<User> markers;
	
	/** The list of students registered for this course offering */
	private Collection<User> students;
	
	/** The list of assignments currently associated with this course offering */
	private Collection<Assignment> assignments;
	
	/**
	 * Create a new course offering with a unique course ID, a specific term
	 * it is being offered, and the full name of the course.
	 * @param courseID the unique ID of the course 
	 * @param term the term this course instance is being offered
	 * @param name the name of the course
	 */
	public Course(String courseID, String term, String name)
	{
		this.courseID = courseID;
		this.term = term;
		this.name = name;
		
		instructors = new ArrayList<User>();
		markers = new ArrayList<User>();
		students = new ArrayList<User>();
		assignments = new ArrayList<Assignment>();
	}
	
	/**
	 * Add an instructor to this Course offering.
	 * @param user the User to add as an Instructor
	 */
	public void addInstructor(User user)
	{
		instructors.add(user);
	}
	
	/**
	 * Add a Marker to this Course offering.
	 * @param user the User to add as a Marker.
	 */
	public void addMarker(User user)
	{
		markers.add(user);
	}
	
	/**
	 * Add a Student to this Course offering.
	 * @param user the User to add as a Student.
	 */
	public void addStudent(User user)
	{
		students.add(user);
	}
	
	/**
	 * Add an Assignment to this Course offering.
	 * @param assignment the Assignment to add.
	 */
	public void addAssignment(Assignment assignment)
	{
		assignments.add(assignment);
	}

	/**
	 * Get the course ID. Course ID has the format [SUBJ][NUMBER]-[SECTION]. Eg: COMP4770-001.
	 * @return the course's ID
	 */
	public String getCourseID(){
		return courseID;
	}

	/**
	 * Get the term of this course. The String's format is [semester][year]. Eg: WINTER16.
	 * @return the Cours's term.
	 */
	public String getTerm(){
		return term;
	}

	/**
	 * Get the descriptive name of this course.
	 * @return the name of the course.
	 */
	public String getName(){
		return name;
	}
}
