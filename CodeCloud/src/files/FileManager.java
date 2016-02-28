package files;

import java.io.File;
import java.io.FilenameFilter;
import java.io.ImportStream;
import java.io.IOException;
import java.lang.String;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyAction;
import java.sql.SQLException;
import java.util.LinkedList;

import files.UserFile;
import users.Role;
import users.User;
import utility.DBController;

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
		boolean auth = false;
		String[] path = file.getPath().split("/");

		if (path.length >= 2){
			if (path[0].equals("users")){
				if (path[1].equals(user.getUsername())) auth = true;
			}

			else if (path[0].equals("courses")){
				if (path.length >= 3){
					String[] enrollment;
					try {
						enrollment = DBController.getEnrollment(user.getUsername(), path[2], path[1]);
					} catch (SQLException sqle){
						return false;
					}

					if (enrollment == null) return false;

					if (Role.valueOf(enrollment[3]).equals(Role.Student)){
						if (path.length >= 6){
							if (path[5].equals(user.getUsername()) || (path[3].equals("assignments") && !(path[5].equals("solutions")))) return true;
						}
						else if (path.length >= 4){
							if (path[3].equals("assignments")) return true;
						}
					}

					else {
						auth = true;
					}
				}
			}
		}

		return auth;
	}

		public void download(User user, UserFile uf, String urlString, String targetDir) throws IOException, MalformedURLException {
			if (uf.getFile().exists()){
				if (uf.getFile().isFile() && !(uf.getFile().isDirectory()) && this.authorize(user, uf)){
					URL url = new URL(urlString);
					Path path = Paths.get(targetDir);
					try(InputStream input = url.openStream();){
						Files.copy(input, path, StandardCopyAction.REPLACE_EXISTING);
					}
				}
			}
		}
}
