package files;

import java.io.File;
import java.sql.SQLException;

import assignments.Course;
import users.User;
import utility.DBController;

/**
 * A representation of a user's file as stored in the server's
 * Unix-based file system.
 * @author Alex Brandt
 *
 */
public class UserFile {
	
    /** The path to the file in the file system */
    //Does not start with 'static/'
    private String path;
    
    /** The owner of the file */
    private User owner;

    //The path parameter should not start with "static/"
    public UserFile(User owner, String path)
    {
    	this.owner = owner;
    	this.path = path;
    }

    public UserFile(String path){
	//String[] pathParts = path.split("/");
	this.path = path;
    }
    
    /**
     * Get the file represented by this UserFile object as a Java File object.
     * @return The Java File object pointing to this user file in the file system.
     */
    public File getFile(){
	//Should this method take a User object as a parameter?
	//FileManager manager = FileManager.getInstance();
	//if manager.authorize(user, this){...} else{...}
	File file = new File(path);
    	return file;
    }

    public User getOwner(){
	return owner;
    }

    public String getPath(){
	return path;
    }

    public User interpretOwner(String path){
	if (path.charAt(0) == '/') path = path.substring(1);
	String[] pathParts = path.split("/");
	if (pathParts.length < 2) return null;
	try {
		if (pathParts[0].equals("users")){
			return DBController.getUser(pathParts[1]);
		}
		else if (pathParts[0].equals("courses") && pathParts.length > 2){
			return DBController.getInstructor(new Course(pathParts[2], pathParts[1], ""));
		}
	}
	catch (SQLException sqle){
		return null;
	}
	return null;
    }
}
