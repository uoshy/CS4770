package files;

import files.UserFile;
import users.User;

import java.io.File;

public class FileManager
{
	/** Singleton reference */
	private static FileManager _manager;	
	
	/**
	 * Private constructor for singleton.
	 */
	private FileManager()
	{
		
	}
	
	/**
	 * Initialize the FileManager singleton.
	 */
	private static void initialize()
	{
		if(_manager == null)
		{
			_manager = new FileManager();
		}
	}
	
	/**
	 * Get a reference to a FileManager object for managing UserFiles and Files.
	 * @return a reference to a FileManager object
	 */
	public static FileManager getInstance()
	{
		if(_manager == null)
			initialize();
		
		return _manager;
	}
	
	/**
	 * Determine whether a particular User is authorized to view the file
	 * contained within the UserFile object.
	 * @param user the User requesting access to the UserFile
	 * @param file the UserFile being accessed
	 * @return true iff the User is authorized to view the UserFile
	 */
	public boolean authorize(User user, UserFile file)
	{
		//TODO authorization
		return true;
	}
}
