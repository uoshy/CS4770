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





import cloudCoding.CompilerReturn;


import cloudCoding.Console;
import cloudCoding.JavaLanguage;


import com.google.gson.Gson;
import com.google.gson.JsonParseException;





import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;





import json.CompilerInput;
import json.CompilerReturnJson;
import json.ExecutionInput;
import json.ExecutionReturn;
import files.UserFile;

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
        
        get("/editor", (request, response) -> {
        	response.redirect("/CodeCloudTest.html");
        	return null;
        });
        
        post("/editor/compile/java", (request, response) ->
        {   
           response.type("application/json");
           try
           {
        	   Gson gson = new Gson();
        	   String body = request.body();
        	   CompilerInput input = gson.fromJson(body, CompilerInput.class);
        	   log(request.body());
        	   log(input.fileContent);
        	   log(input.fileName);
        	   File dir = new File("static/temp");
        	   if(!dir.exists())
        		   dir.mkdir();
        	   File file = new File("static/temp/" + input.fileName + ".java");
        	   if(!file.exists())
        		   file.createNewFile();
        	   BufferedWriter out = new BufferedWriter(new FileWriter(file));
        	   out.write(input.fileContent, 0, input.fileContent.length());
        	   out.flush();
        	   out.close();
        	   UserFile uFile = new UserFile(null, "static/temp/"+input.fileName + ".java");
        	   
        	   UserFile uDir = new UserFile(null, "static/temp");
        	   CompilerReturn compRet = cloudCoding.JavaLanguage.getInstance().compile(new UserFile[]{uDir, uFile});
        	   log("CompilerMessage " + compRet.compilerMessage);
        	   CompilerReturnJson jsonObj = new CompilerReturnJson();
        	   jsonObj.compilerExitStatus = compRet.compilerExitStatus;
        	   jsonObj.compilerMessageToDisplay = compRet.displayAsHTML();
        	   return jsonObj;
           }
           catch(JsonParseException ex)
           {
        	   log("Malformed values in getting compiler input");
               halt(400, "malformed values");
               return null;
           }
        }, new JsonTransformer());
        
        post("/editor/execute/java", (request, response) ->
        {
        	response.type("application/json");
        	try {
        		Gson gson = new Gson();
        		String body = request.body();
        		ExecutionInput input = gson.fromJson(body, ExecutionInput.class);
        		log(input.fileName);
        		
        		UserFile uDir = new UserFile(null, "static/temp");
        		ExecutionReturn execRet = JavaLanguage.getInstance().execute(uDir, input.fileName);
        		
        		
        	}
        	catch(JsonParseException ex)
        	{
        		log("Malformed Values in getting exection input");
        		halt(400, "malformed values");
        		return null;
        	}
        	return null;
        });

    }//main

}
