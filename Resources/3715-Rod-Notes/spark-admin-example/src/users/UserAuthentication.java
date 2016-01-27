package users;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.PrintStream;

public class UserAuthentication {
    private ArrayList<User> users;

    public UserAuthentication( String path ) throws IOException {
        users = new ArrayList<>();
        parseUserFile( path );
    }

    void parseUserFile( String path ) throws IOException {
        Scanner sc = new Scanner( new File( path ) );
        while ( sc.hasNextLine() ) {
            String line = sc.nextLine();
            line = line.trim();
            if ( line.startsWith("#") ) continue;

            String[] words = line.split(":");
            if ( words.length != 3 ) {
                throw new IOException("malformed line");
            }
            users.add( new User( words[0], words[1], words[2] ) );
        }
        sc.close();
    }

    public User findByName( String name ) {
        for( User u : users ) {
            if ( u.name.equals( name ) ) {
                return u;
            }
        }
        return null;
    }
    
    public void addChangeUser( User nu ) {
        removeUserByName( nu.name );
        users.add( nu );
    }

    public void removeUserByName( String name ) {
        User ou = findByName( name );
        if ( ou != null ) {
            users.remove( ou );
        }
    }

    public ArrayList<User> getUsers() {
        // return a copy of the list
        return new ArrayList<User>(users);
    }

    public void writeUsersFile( String path ) throws IOException {
        PrintStream ps = new PrintStream( path );
        for( User u : users ) {
            ps.printf("%s:%s:%s%n", u.name, u.password, u.role );
        }
        if ( ps.checkError() ) {
            throw new IOException("write user file failed");
        }
        ps.close();
    }
}
