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
import java.util.Scanner;

import cloudCoding.CompilerReturn;
import cloudCoding.UserProcess;
import cloudCoding.Console;
import cloudCoding.JavaLanguage;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.SQLException;

import json.CompilerInput;
import json.CompilerReturnJson;
import json.ExecutionInput;
import json.ExecutionReturn;

import files.UserFile;
import main.JSONFileList;
import main.JSONFileList.JSONFileObject;

import javax.servlet.http.Part;
import javax.servlet.MultipartConfigElement;

/**
 * The main class to run the java spark framework.
 */
public class CodeCloudMain 
{
    private static void setup()
    {
        //TODO ensure docker daemon is running
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
            response.redirect("/home.html");
            return null;   
        });
        
        get("/editor", (request, response) -> {
        	response.redirect("/home.html");
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
                log(body);
        		ExecutionInput input = gson.fromJson(body, ExecutionInput.class);
        		log(input.fileName);
        		
        		UserFile uDir = new UserFile(null, "static/temp");
        		ExecutionReturn execRet = JavaLanguage.getInstance().execute(uDir, input.fileName);
        		
                System.out.println("about to return execution return");
        		return execRet;
        	}
        	catch(JsonParseException ex)
        	{
        		log("Malformed Values in getting exection input");
        		halt(400, "malformed values");
        		return null;
        	}
        }, new JsonTransformer());

        post("/editor/execute/active/writeInput/:activeProcessID", (request, response) -> 
        {
            response.type("text/plain");
            try {
                String body = request.body();
                log(body);
                String activeProcessID = request.params("activeProcessID");
                long procID = Long.parseLong(activeProcessID);
                UserProcess uProc = Console.getInstance().getProcess(procID);
                uProc.writeToProcess(body);
                return "";

            }
            catch(NumberFormatException e) //issue with activeProcessID request param
            {
                log("Error with activeProcessID!");
                halt(400, "malformed values");
                return null;
            }
        });

        post("/files/upload", (request, response) ->
        {
        	response.type("text/plain");
		//TODO: Get user/path parameters.
		if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null){
			MultipartConfigElement mce = new MultipartConfigElement(System.getProperty("static/temp"));
			request.raw().setAttribute("org.eclipse.jetty.multipartConfig", mce);
		}
		Part file = request.raw().getPart("file");
		String filename = file.getSubmittedFileName();
       		UserFile uDir = new UserFile(null, filename);
		try (final InputStream in = file.getInputStream()) {
			//TODO: Insert actual path to user's file directory
		 	Files.copy(in, Paths.get("static/temp/" + filename), StandardCopyOption.REPLACE_EXISTING);
		 	file.delete();
		}
		catch (Exception e){
			e.printStackTrace();
			return "0";
		}
                System.out.println("Saved file to " + uDir.getPath());
        	return "/temp/" + filename;
        });

        post("/files/view", (request, response) ->
        {
		System.out.println("files/view call");
        	response.type("application/json");
		String path = request.body();
		File file = new File(path);
		if (file.exists()){
			File[] files = file.listFiles();
			JSONFileList fl = new JSONFileList();
			fl.fileObjs = new JSONFileObject[files.length];
			for (int i = 0; i < files.length; i++){
				fl.fileObjs[i].fileName = files[i].getName();
				fl.fileObjs[i].isDirectory = files[i].isDirectory();
			}
			return fl;
		}
		return "";
	}, new JsonTransformer());


        get("/editor/execute/active/readOutput/:activeProcessID", (request, response) -> 
        {
            response.type("application/json");
            try {
                String activeProcessID = request.params("activeProcessID");
                long procID = Long.parseLong(activeProcessID);
                UserProcess uProc = Console.getInstance().getProcess(procID);
                String processOutput = uProc.readFromProcess();
                System.out.println("read from process!: " + processOutput);

                ExecutionReturn execRet = new ExecutionReturn();
                execRet.outputText = processOutput;
                execRet.exitStatus = uProc.getExitStatus();
                execRet.processID = uProc.getProcessID();
                return execRet;
            }
            catch(NumberFormatException e)
            {
                log("Error with activeProcessID in readOutput");
                halt(400, "malformed values");
                return null;
            }
        }, new JsonTransformer());

        post("/editor/execute/active/kill/:activeProcessID", (request, response) ->
        {
            response.type("application/json");
            try {
                String activeProcessID = request.params("activeProcessID");
                long procID = Long.parseLong(activeProcessID);
                UserProcess uProc = Console.getInstance().getProcess(procID);
                uProc.killProcess();
                ExecutionReturn execRet = new ExecutionReturn();
                execRet.exitStatus = uProc.getExitStatus();
                execRet.outputText = uProc.readFromProcess();
                execRet.processID = uProc.getProcessID();
                return execRet;
            }
            catch(NumberFormatException e)
            {
                log("Error with activeProcessID in readOutput");
                halt(400, "malformed values");
                return null;
            }
        }, new JsonTransformer());
        

    }//main

}
