package admin;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.before;
import static spark.Spark.externalStaticFileLocation;
import spark.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import java.io.IOException;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import spark.Request;
import spark.Response;

import users.UserAuthentication;
import users.User;

public class AdminDemo {

    private static final String ADMIN_TEMPLATE = "admin.mustache";
    private static final String ERROR_TEMPLATE = "error.mustache";

    private static void redirectError(
        Request req, Response res, String message)
    {
        Session sess = req.session(true);
        if ( sess == null ) {
            res.redirect("/error.html");
            return;
        }
        sess.attribute("message", message);
        res.redirect("/error");
    }

    public static void main(String[] args) throws java.io.IOException {
        port(8090); // change to port 8090
        UserAuthentication ua = new  UserAuthentication( "users.txt" );
        MustacheTemplateEngine mte = new MustacheTemplateEngine("admin-temp");

        externalStaticFileLocation("static/admin");

        before("/", (request, response) -> {
            response.type("text/html; charset=utf-8");
        } );

        before("/auth/*", (request, response) -> {
            User user = request.session().attribute("user");
            if ( user == null ) {
                response.redirect("/not-signed-in.html");
                return;
            }
            // accept all roles
        } );

        before("/admin/*", (request, response) -> {
            User user = request.session().attribute("user");
            if ( user == null ) {
                response.redirect("/not-signed-in.html");
                return;
            }
            if ( !user.role.equals( "admin" ) ) {
                response.redirect("/not-admin.html");
                return;
            }
        } );

        // redirect / to /login.html
        get("/", (request, response) -> {
            response.redirect("/login.html");
            return null;
        } );

        get("/error", (request, response) -> {
            String msg = request.session().attribute("message");
            request.session().attribute("message", null); // remove message
            if ( msg == null ) msg = "No Message";
            Map map = new HashMap();
            map.put("message", msg );
            return new ModelAndView(map, ERROR_TEMPLATE);
        }, mte );

        get("/admin/users", (request, response) -> {
            Map map = new HashMap();
            map.put("users", ua.getUsers() );
            return new ModelAndView(map, ADMIN_TEMPLATE);
        }, mte );

        get("/admin/delete/:user", (request, response) -> {
            String user = request.params(":user");
            if ( user == null || user.length() == 0 ) {
                response.redirect("/error.html");
                return null;
            }
            ua.removeUserByName( user );
            response.redirect("/admin/users");
            return null;
        } );

        post("/admin/newuser", (request, response) -> {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");
            String role = request.queryParams("role");
            if ( u == null || pw == null || role == null ) {
                response.redirect("/error.html");
                return null;
            }
            User user = ua.findByName( u );
            if ( user != null ) {
                redirectError(request, response, "Not a new user");
                return null;
            }
            User newUser = new User( u, pw, role );
            ua.addChangeUser( newUser );
            try {
                // XXX This is here only for a demo
                ua.writeUsersFile("users.txt");
            }
            catch( IOException ex ) {
                redirectError(request, response, ex.getMessage() );
                return null;
            }
            response.redirect("/admin/users");
            return null;
        } );

        post("/admin/password", (request, response) -> {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");
            if ( u == null || pw == null ) {
                response.redirect("/error.html");
                return null;
            }
            User user = ua.findByName( u );
            if ( user == null ) {
                response.redirect("/error.html");
                return null;
            }
            User newUser = new User( user.name, pw, user.role );
            ua.addChangeUser( newUser );
            response.redirect("/admin/users");
            return null;
        } );

        post("/login", (request, response) -> {
            String u = request.queryParams("user");
            String pw = request.queryParams("password");

            if ( u == null || pw == null ) {
                response.redirect("/error.html");
                return null;
            }
            User user = ua.findByName( u );
            if ( user == null ) {
                response.redirect("/login.html");
                return null;
            }
            Session sess = request.session(true);
            if ( sess == null ) {
                response.redirect("/error.html");
                return null;
            }
            sess.attribute("user", user);
            response.redirect("/auth/index.html");
            return null;
        } );
    }
}
