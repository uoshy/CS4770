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
    
    /**
     * Get the file represented by this UserFile object as a Java File object.
     * @return The Java File object pointing to this user file in the file system.
     */
    public File getFile(){
    	return null;
    }

    public User getOwner(){
	return owner;
    }

    public String getPath(){
	return path;
    }
}
