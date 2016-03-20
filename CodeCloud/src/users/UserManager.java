package users;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;

import users.User;
import json.UserListReturn;
import utility.DBController;

public class UserManager 
{
	public static boolean changeRole(User user, Role newRole)
	{
		//TODO check DB to see if the user is specified as the newRole in any course
		if(true) //change to check database
		{
			user.setActiveRole(newRole);
			return true;
		}
		return false;
	}
    
    
    public static UserListReturn getUserList() 
    {
		Map<String, User> map;
		try{
			map = DBController.getAllUsers();
		} catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		System.out.println("Total students: " + map.size());
		Collection<User> users = map.values();
		ArrayList<User> listUsers = new ArrayList<>(users);
		Collections.sort(listUsers, (User u1, User u2) -> //sort chronologically
		{	
			String lastname1 = u1.getLastname();
			String lastname2 = u2.getLastname();
			//just uses string compareTo
			return lastname1.compareTo(lastname2);  	
		});
		
        String[] usernames = new String[listUsers.size()];
		String[] fullNames = new String[listUsers.size()];
		for(int i = 0; i < listUsers.size(); i++){
			User u = listUsers.get(i);
			usernames[i] = u.getUsername();
            fullNames[i] = u.getLastname() +", "+ u.getFirstname();
		}
		
		UserListReturn listing = new UserListReturn();
		listing.usernames = usernames;
		listing.userFullNames = fullNames;
		return listing;
	}
    
}
