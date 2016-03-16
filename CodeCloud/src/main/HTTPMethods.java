package main;

import spark.Session;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.halt;

import users.User;

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
        


	}
}
