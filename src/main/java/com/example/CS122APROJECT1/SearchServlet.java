//package com.example.CS122APROJECT1;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.servlet.ServletConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
//
//@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
//public class SearchServlet extends HttpServlet {
//    /**
//     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//     */
//
//    private static final long serialVersionUID = 1L;
//    // Create a dataSource which registered in web.xml
//    private DataSource dataSource;
//
//    public void init(ServletConfig config) {
//        try {
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        try {
//            // Create a new connection to database
//            Connection dbCon = dataSource.getConnection();
//            // Output stream to STDOUT
//            PrintWriter out = response.getWriter();
//
//            // Declare a new statement
//            Statement statement = dbCon.createStatement();
//
//
//            /* get the content in the login box from the http request(user's typed in username and the password)
//                which refers to the value of <input name="name"> in index.html*/
//            String title = request.getParameter("title");
//            JsonArray jsonArray = new JsonArray();
//            JsonObject responseJsonObject = new JsonObject();
//
//            // Generate a SQL query
//            String query = String.format("SELECT * from movies where title like '%s'", title);
//            // Perform the query
//            ResultSet rs = statement.executeQuery(query);
//            String movie_title = rs.getString("title");
//
//
//            responseJsonObject.addProperty("title", movie_title);
//            jsonArray.add(responseJsonObject);
//            // the Username exists in database
//            response.setStatus(200);
//            out.write(jsonArray.toString());
//
//            rs.close();
//            statement.close();
//            dbCon.close();
//
//        } catch (Exception e){
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("errorMessage", e.getMessage());
//            // set reponse status to 500 (Internal Server Error)
//            response.setStatus(500);
//        }
//    }
//
//}


package com.example.CS122APROJECT1;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;


@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 2L;
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();


            // Declare a new statement
            Statement statement = dbCon.createStatement();


            /* get the content in the login box from the http request(user's typed in username and the password)
                which refers to the value of <input name="name"> in index.html*/
            String movietitle = request.getParameter("movietitle");

            JsonObject responseJsonObject = new JsonObject();

            // Generate a SQL query
            String query = String.format("SELECT * from movies where title like '%s'", movietitle);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

//            System.out.println(movie_title);
            // Check the Username exists in database or not
            if(rs.next() == false)
            {
                // the Username doesn't exists in database
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "user " + movietitle+ " doesn't exist");
            }
            // the Username exists in database
            else
            {
                    String movie_title = rs.getString("title");
                    request.getSession().setAttribute("user", new User(movietitle));
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");

                // set response status to 200 (OK)
                response.setStatus(200);

                rs.close();
                statement.close();
                dbCon.close();
            }

            response.getWriter().write(responseJsonObject.toString());
        } catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }
}