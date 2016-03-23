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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Scanner;

import cloudCoding.CompilerReturn;
import cloudCoding.UserProcess;
import cloudCoding.Console;
import cloudCoding.JavaLanguage;
import cloudCoding.CPPLanguage;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.SQLException;
import java.io.IOException;
import java.lang.SecurityException;

import json.CompilerInput;
import json.CompilerReturnJson;
import json.ExecutionInput;
import json.ExecutionReturn;
import json.RegisterInput;
import json.LoginInput;

import files.FileManager;
import files.UserFile;
import main.JSONFileList;
import main.JSONFileList.JSONFileObject;

import users.User;
import users.UserManager;
import users.Role;
import utility.DBController;
//import users.TempUsers;

import assignments.Assignment;
import assignments.CourseManager;
import assignments.Course;

import javax.servlet.http.Part;
import javax.servlet.MultipartConfigElement;

/**
 * The main class to run the java spark framework.
 */
public class CodeCloudMain
{
	//private static TempUsers users;
    private static DBController dbCon;

	private static void setup()
	{
		//TODO ensure docker daemon is running

		//Temporarily get users from file
		//users = new TempUsers();
        
        //create DB controller
        dbCon = new DBController();
        try {
            dbCon.initialize(); //connect
        }
        catch (SQLException e) {
            log("SQLite error: Initialize.");
            e.printStackTrace();
        }
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
		
		HTTPMethods.setupPaths();
		
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

        
        
		post("/login", (request, response) -> {
			//String usr = request.queryParams("user");
			//String pw = request.queryParams("password");
			//log("user: "+usr+" & pass: "+pw);
            
            log("Login request!");
			String body = request.body();
            log(body);
			LoginInput loginInput;
			try {
				Gson gson = new Gson();
				loginInput = gson.fromJson(body, LoginInput.class);
                User user = dbCon.getUser(loginInput.username);             

                //user with specified username not found
                if(user == null) {
                    log("User doesn't exist! Login failed");
                    //response.redirect("/login.html");
                    return "Username doesn't exist! Please register, or try again.";
                }
                if(user.getPassword().equals(loginInput.password)) {
                    Session session = request.session(true);
                    if ( session == null ) {
                        return "Error logging in. Browser does not support cookies.";
                    }
                    session.attribute("user", user);
                    log("Login successful. Username: " + user.getUsername());
                    //response.redirect("/home.html");
                    return "";
                } else {
                    log("Password doesn't match! Login failed");
                    return "Incorrect login info! Please register, or try again.";
                }
            }
			catch (JsonParseException | NumberFormatException ex)
			{
				log("Malformed values in login request.");
				return "Error in input values!";
			} catch (SQLException e) {
                log("SQLite error: Login.  " + e.getMessage());
                return "SQLite error: Login.";
            }
			//log("Login failed");
			//response.redirect("/registration.html");
			//return "";
		});

		post("/register", (request, response) -> {
			log("Registration request!");
			String body = request.body();
			RegisterInput regInput;
			try {
				Gson gson = new Gson();
				regInput = gson.fromJson(body, RegisterInput.class);
                
            		    // username already exists, registering failed
            		    if(dbCon.getUser(regInput.username) != null) {
            		        // (this should be prevented by javascript/AJAX on the
            		        // register page anyway, but just in case...)
            		        log("username exists, register failed");
            		        return "Username already exsists! Please choose another.";
            		    }

				/*Session session = request.session(true);
				if ( session == null ) {
					return "Error registering. Browser does not support cookies.";
				}*/

				long studentNumber = Long.parseLong(regInput.studentNum);
				User user = new User(regInput.username, regInput.password, regInput.firstName, regInput.lastName, studentNumber);
             			dbCon.addUser(user);
				File file = new File("static/users/" + regInput.username);
				file.mkdir();
				//session.attribute("user", user);
        		        log("User Registered! Username: "+user.getUsername());
				return "";
			}
			catch (JsonParseException | NumberFormatException ex)
			{
				log("Malformed values in register request.");
				return "Error in input values!";
			} catch (SQLException e) {
                log("SQLite error: Registration.  "  + e.getMessage());
                return "SQLite error: Registration.";
            }
		});

		// allow user to log out
		get("/logout", (request, response) -> {
			Session sess = request.session(true);
			if(sess.attribute("user") != null) {
				sess.attribute("user", null);
				response.redirect("/login.html");
			} else {
				// just redirect to the same page if user wasn't logged in -
				// this needs to be changed so that the "Log out" link isn't
				// even shown when the user is logged in
				response.redirect(request.url());
			}
			return null;

		});
        
        post("/admin/register", (request, response) -> {
			log("Admin: Registration request!");
			String body = request.body();
			RegisterInput regInput;
			try {
				Gson gson = new Gson();
				regInput = gson.fromJson(body, RegisterInput.class);
                
            		    // username already exists, registering failed
            		    if(dbCon.getUser(regInput.username) != null) {
            		        // (this should be prevented by javascript/AJAX on the
            		        // register page anyway, but just in case...)
            		        log("Admin: username exists, register failed");
            		        return "Username already exsists! Please choose another.";
            		    }

				long studentNumber = Long.parseLong(regInput.studentNum);
				User user = new User(regInput.username, regInput.password, regInput.firstName, regInput.lastName, studentNumber);
             			dbCon.addUser(user);
                        
                //Role not working currently
                //user.setActiveRole(regInput.userRole);
                //log("Admin: User Role Set! MainRole: "+user.getActiveRole());
                
				File file = new File("static/users/" + regInput.username);
				file.mkdir();
        		log("Admin: User Registered! Username: "+user.getUsername());
				return "";
			}
			catch (JsonParseException | NumberFormatException ex)
			{
				log("Malformed values in register request.");
				return "Error in input values!";
			} catch (SQLException e) {
                log("SQLite error: Registration.  "  + e.getMessage());
                return "SQLite error: Registration.";
            }
		});
        
        get("/admin/listUsers", (request, response) -> 
        {
            log("Admin: getting users");
            return UserManager.getUserList();
            
        }, new JsonTransformer());
        
        /*
        //TODO: get working
        delete("/admin/listUsers", (request, response) -> 
        {
            log("Admin: deleting users");
            return UserManager.getUserList();
            
        });*/
        
        //TODO: was trying to /admin/listUsers & /admin/listCourses on same page
        // need to implement way to view both lists at same time
        get("/admin/listCoursesAndUsers", (request, response) -> 
        {
            log("Admin: getting courses");
            return CourseManager.getCourseList();
            
        }, new JsonTransformer());

		/**
		 * Save the editor contents to a file on the server. Returns 0 iff successful.
		 */
		post("/editor/saveFile", (request, response) -> 
		{
			response.type("text/plain");
			try 
			{
				Gson gson = new Gson();
				String body = request.body();
				CompilerInput input = gson.fromJson(body, CompilerInput.class);
				File file = new File(input.fileName);
				if(!file.exists())
					return 1;
				
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write(input.fileContent, 0, input.fileContent.length());
				out.flush();
				out.close();
				return 0;
			}
			catch(JsonParseException jpe)
			{
				log("Malformed values in saving file content");
				halt(400, "malformed values");
				return 1;
			}
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
				CompilerReturn compRet = cloudCoding.JavaLanguage.getInstance().compile(new UserFile[]{uDir, uFile}, input.fileName);
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

		post("/editor/compile/cpp", (request, response) ->
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

				String fileName = input.fileName;
				int dotIndex = input.fileName.indexOf(".");
				if(dotIndex != -1)
					fileName = fileName.substring(0, dotIndex);

				File file = new File("static/temp/" + fileName + ".cpp");
				if(!file.exists())
					file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write(input.fileContent, 0, input.fileContent.length());
				out.flush();
				out.close();
				UserFile uFile = new UserFile(null, "static/temp/"+ fileName + ".cpp");

				UserFile uDir = new UserFile(null, "static/temp");
				CompilerReturn compRet = cloudCoding.CPPLanguage.getInstance().compile(new UserFile[]{uDir, uFile}, input.fileName);
				if(compRet == null)
					return null;

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

		post("/editor/execute/cpp", (request, response) ->
		{
			response.type("application/json");
			try {
				Gson gson = new Gson();
				String body = request.body();
				log(body);
				ExecutionInput input = gson.fromJson(body, ExecutionInput.class);
				log(input.fileName);

				UserFile uDir = new UserFile(null, "static/temp");
				ExecutionReturn execRet = CPPLanguage.getInstance().execute(uDir, input.fileName);

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

		post("/files/upload/:dir", (request, response) ->
		{
			response.type("text/plain");
			String dirPath = request.params(":dir");
			dirPath = dirPath.replaceAll("\\|", "/");
			log("query params: " + dirPath);
			//TODO: Get user/path parameters.
			if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null){
				MultipartConfigElement mce = new MultipartConfigElement(System.getProperty("static/temp"));
				request.raw().setAttribute("org.eclipse.jetty.multipartConfig", mce);
			}
			Part file = request.raw().getPart("file");
			String filename = file.getSubmittedFileName();
			User tempUser = request.session().attribute("user");
			UserFile uDir = new UserFile(tempUser, filename);
			try (final InputStream in = file.getInputStream()) {
				//TODO: Insert actual path to user's file directory
				Files.copy(in, Paths.get(dirPath + filename), StandardCopyOption.REPLACE_EXISTING);
				file.delete();
			}
			catch (FileAlreadyExistsException faes){
				return "0";
			}
			catch (Exception e){
				e.printStackTrace();
				return "0";
			}
			System.out.println("Saved file to " + dirPath + filename);
			dirPath = dirPath.substring(6);
			return dirPath + filename;
		});

		post("/files/view", (request, response) ->
		{
			log(((User) request.session().attribute("user")).getUsername());;
			String path = request.body();
			log("Path: " + path);
			if (!FileManager.authorize(((User) request.session().attribute("user")), new UserFile(path))) return "authFail";
			response.type("application/json");
			File file = new File(path);
			if (file.exists()){
				File[] files;
				files = file.listFiles();
				if(files.length > 1) 
					Arrays.sort(files);
				JSONFileList fl = new JSONFileList(files.length);
				for (int i = 0; i < files.length; i++){
					fl.fileObjs[i].fileName = files[i].getName();
					fl.fileObjs[i].isDirectory = files[i].isDirectory();
				}
				log("Returning...");
				return fl;
			}
			return null;
		}, new JsonTransformer());

		post("/files/add", (request, response) ->
		{
			String path = request.body();
			response.type("text/plain");
			if (!FileManager.authorize(request.session().attribute("user"), new UserFile(request.session().attribute("user"), path))) return "0";
			File file = new File(path);
			if (file.exists()) return "0";
			try {
				file.mkdir();
				return "1";
			}
			catch (SecurityException ex){
				ex.printStackTrace();
				return "2";
			}
		}, new JsonTransformer());

		post("/files/savetxt", (request, response) ->
		{
			log("so far...");
			boolean worked = false;
			String txt = request.body();
			log("Txt: " + txt);
			String path = txt.split("\\|")[0];
			String fileType = path.substring(0,1);
			log("Path: " + path);
			txt = txt.substring(path.length() + 1);
			log("Text: " + txt);
			log("Final path: " + path);
			response.type("text/plain");
			File file = new File(path);
			if (!file.exists()){
				log("File doesn't exist. Creating file.");
				file.createNewFile();
			}
			FileWriter fw = null;
			try {
				fw = new FileWriter(file);
				fw.write(txt);
				log("Wrote file, no problems.");
				worked = true;
			}
			catch (Exception e){
				e.printStackTrace();
				worked = false;
			}
			finally {
				if (fw != null){
					log("Closing filewriter...");
					fw.flush();
					fw.close();
				}
			}
			return ((worked) ? "1" : "0");
		}, new JsonTransformer());


		post("/files/delete", (request, response) ->
		{
			String path = request.body();
			response.type("text/plain");
			if (!FileManager.authorize(request.session().attribute("user"), new UserFile(request.session().attribute("user"), path))) return "0";
			File file = new File(path);
			try {
				delete(file);
				return "1";
			}
			catch (Exception e){
				e.printStackTrace();
				return "0";
			}
		}, new JsonTransformer());

		post("/files/deleteassignment", (request, response) ->
		{
			String[] aParams = request.body().split("\\|");
			response.type("text/plain");
			Course course = new Course(aParams[0], aParams[1], "");
			Assignment assignment = new Assignment(course, Integer.parseInt(aParams[2]));
			try {
				DBController.removeAssignment(assignment);
				return "1";
			}
			catch (SQLException sqle){
				return "0";
			}
		}, new JsonTransformer());

		post("/files/getcontents", (request, response) ->
		{
			response.type("application/json");
			String path = request.body();
			if (!FileManager.authorize(request.session().attribute("user"), new UserFile(request.session().attribute("user"), path))) return "";
			log("Received request for " + path);
			File file = new File(path);
			String[] pathParts = path.split("/");
			String[] returnArray = new String[2];
			if (file.exists() && ! file.isDirectory() && file.isFile()){
				log("Exists, not a dir, is a file.");
				if (pathParts[pathParts.length - 1].endsWith(".txt") || pathParts[pathParts.length - 1].endsWith(".java") || pathParts[pathParts.length - 1].endsWith(".cpp")){
					log("Text/Java/C++ file");
					returnArray[0] = "false";
					Scanner scanner = new Scanner(file);
					log("Scanner established.");
					String tempString = "";
					try {
						tempString = scanner.useDelimiter("//A").next();
						log("Read file into string");
					}
					finally {
						scanner.close();
						log("Close scanner");
					}
					returnArray[1] = tempString;
					log("Assign string to array");
					return returnArray;
				}
				else {
					log("Some other kind of file");
					returnArray[0] = "true";
					if (!(path.charAt(0) ==('/'))){
						path = "/" + path;
					}
					returnArray[1] = path;
					return returnArray;
				}
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

	private static void delete(File file){
		if (file.exists()){
			if (file.isDirectory()){
				for (File subFile : file.listFiles()){
					log(file.getPath());
					delete(subFile);
				}
			}
			file.delete();
		}
	}
}
