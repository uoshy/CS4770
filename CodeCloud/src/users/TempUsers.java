package users;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** 
 * A collection of all the users for the web application.
 * Temporarily uses a text file.
 *
 * @author Alex Brandt
 * MODIFIED
 *
 */
public class TempUsers
{
    /** The collection of registered users */
    private Collection<User> users;

    /** The static file location of the user information */
    private String fileLocation = "static/temp/users.txt";  

    /**
     * Construct a new Users object by filling the collection if User objects.
     * Get the users from file for the time being. 
     */
    public TempUsers()
    {
        try
        {
            users = new ArrayList<>();
            BufferedReader rd = new BufferedReader( new FileReader(fileLocation));
            String line = rd.readLine();
            while(line != null && line.length() != 0 )
            {
                String[] split = line.split("\\|", 5);
                users.add(new User(split[0], split[1], split[2], split[3], Long.parseLong(split[4])));
                line = rd.readLine();  
            } 
            rd.close();
        }
        catch(IOException ioe)
        {
            users = new ArrayList<>();
            return;   
        }
    }

    public boolean addUser(User u)
    {
        users.add(u);
        try
        {
            FileWriter wt = new FileWriter(fileLocation, true);
            String toWrite = u.toString() + "\n";
            wt.write(toWrite, 0, toWrite.length());
            wt.close();
            System.out.println("Wrote to file: " + toWrite);
            return true;
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            return false;
        }
    }

    /** 
     * Get the collection of users. 
     * @return a copy of the users collection
     */
    public Collection<User> getCollection()
    {
        Collection<User> temp = new ArrayList<>();
        temp.addAll(users);
        return temp;
    }
}
