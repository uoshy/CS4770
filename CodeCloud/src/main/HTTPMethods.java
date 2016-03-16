package main;

import spark.Session;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.halt;

import main.JsonTransformer;
import main.CodeCloudMain;

import users.User;
import users.Role;
import users.UserManager;

import assignments.CourseManager;

import json.UserReturn;

public class HTTPMethods {

	public static void setupPaths()
	{
        before("/home.html", (request, response) ->
        {
        	Session session = request.session();
        	if(session == null)
        		System.out.println("SESSION WAS NULL");
        	else
        	{	User user = request.session().attribute("user");
            if(user == null)
            {
                response.redirect("/login.html");
                return;
            }
        	}
        });
        
        before("/editor.html", (request, response) ->
        {
            User user = request.session().attribute("user");
            if(user == null)
            {
                response.redirect("/login.html");
                return;
            }
        });
        
        before("/files.html", (request, response) ->
        {
            User user = request.session().attribute("user");
            if(user == null)
            {
                response.redirect("/login.html");
                return;
            }
        });
        
        before("/courses.html", (request, response) ->
        {
            User user = request.session().attribute("user");
            if(user == null)
            {
                response.redirect("/login.html");
                return;
            }
        });

        get("/users/activeUser", (request, response) -> 
        {
            User user = request.session().attribute("user");
            UserReturn userRet = new UserReturn();
            if(user == null)
            {
                userRet.username = userRet.firstname = userRet.lastname = "N/A";
                userRet.activeRole = -1;
                userRet.possibleRoles = null;
            }
            else
            {
                userRet.username = user.getUsername();
                userRet.firstname = user.getFirstname();
                userRet.lastname = user.getLastname();
                userRet.activeRole = user.getActiveRole().ordinal();
                userRet.possibleRoles = new int[]{0,1,2};
                //TODO get possible roles

            }
            return userRet;
        }, new JsonTransformer());

        post("/users/changeRole", (request, response) -> 
        {
            User user = request.session().attribute("user");
            UserReturn userRet = new UserReturn();
            if(user == null)
            {
                userRet.username = userRet.firstname = userRet.lastname = "N/A";
                userRet.activeRole = -1;
                userRet.possibleRoles = null;
            }
            else
            {
                String body = request.body();
                try{ 
                    int roleInt = Integer.parseInt(body);
                    if(roleInt >= 0 && roleInt < Role.values().length)
                    {
                        Role newRole = Role.values()[roleInt];
                        if(!UserManager.changeRole(user, newRole))
                        {
                            CodeCloudMain.log("Changing role of " + user.getUsername() + " failed!");
                            return userRet;
                        }
                        userRet.username = user.getUsername();
                        userRet.firstname = user.getFirstname();
                        userRet.lastname = user.getLastname();
                        userRet.activeRole = user.getActiveRole().ordinal();
                        userRet.possibleRoles = new int[]{0,1,2};
                        CodeCloudMain.log("Changed user " + user.getUsername() + " to role: " + newRole);
                        //TODO get possible roles
                    }
                } catch(NumberFormatException nfe)
                {

                }
            }
            return userRet;
        }, new JsonTransformer());


        get("/courses/listCourses", (request, response) -> 
        {
            User user = request.session().attribute("user");
            if(user == null)
            {
                return null;
            }

            return CourseManager.getCoursesForUser(user);
        });

	}
}
