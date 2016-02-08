package main;
import spark.Session;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.halt;
import static spark.Spark.externalStaticFileLocation;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.sql.SQLException;

import json.CompilerInput;

/**
 * The main class to run the java spark framework.
 */
public class CodeCloudMain 
{
    private static void setup()
    {

    }
    
///////////// A set of static helper methods used by the server. /////////////////////////

    /**
     * Print server logs.
     * @param src the String to log
     */
    public static void log(String src) {
        System.out.println(src);
    }
    
    //run the spark framework
    public static void main(String[] args) throws SQLException 
    {
        setup();
        externalStaticFileLocation("static"); 
        MustacheTemplateEngine mte = new MustacheTemplateEngine("templates");
        //redirect root to index.html
        get("/", (request, response) -> 
        {
            response.redirect("/CodeCloudTest.html");
            return null;   
        });
        
        get("/editor/compile", (request, response) ->
        {   
           response.type("application/json");
           try
           {
        	   Gson gson = new Gson();
        	   String body = request.body();
        	   CompilerInput input = gson.fromJson(body, CompilerInput.class);
        	   
           }
           catch(JsonParseException ex)
           {
        	   log("Malformed values in getting compiler input");
               halt(400, "malformed values");
               return null;
           }
           
           return null;
        }, new JsonTransformer() );

    }//main

}
