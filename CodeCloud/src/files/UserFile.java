package files;

import java.io.File;

import users.User;

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
    
    public UserFile(User owner, String path)
    {
    	this.owner = owner;
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
}
