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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;


// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "ShopCartServlet", urlPatterns = "/api/shopcart")
public class ShopCartServlet extends HttpServlet {

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();

        try {
            Connection dbcon = dataSource.getConnection();
            Statement statement = dbcon.createStatement();

            HttpSession session = request.getSession();
            String sessionId = session.getId();
            long lastAccessTime = session.getLastAccessedTime();

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("sessionID", sessionId);
            responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());

            ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
            ArrayList<Integer> NumOfItems = (ArrayList<Integer>) session.getAttribute("numItems");
            if (previousItems == null) {
                previousItems = new ArrayList<>();
                NumOfItems = new ArrayList<>();
            }
            JsonArray previousItemsJsonArray = new JsonArray();
            JsonArray moviesTitleJsonArray = new JsonArray();
            JsonArray NumOfItemsJsonArray = new JsonArray();

            NumOfItems.forEach(NumOfItemsJsonArray::add);
            previousItems.forEach(previousItemsJsonArray::add);
            for(int i = 0; i < previousItems.size(); i++)
            {
                String movieID = previousItems.get(i);
                String query = "SELECT M.title FROM movies M WHERE M.id = '" + movieID + "'";
                ResultSet rs = statement.executeQuery(query);
                rs.next();
                String movie_title = rs.getString("title");
                rs.close();
                moviesTitleJsonArray.add(movie_title);

            }
            responseJsonObject.add("previousItems", previousItemsJsonArray);
            responseJsonObject.add("moviesTitle", moviesTitleJsonArray);
            responseJsonObject.add("numItems",  NumOfItemsJsonArray);

            // write all the data into the jsonObject
            out.write(responseJsonObject.toString());
            // set response status to 200 (OK)
            response.setStatus(200);


            statement.close();
            dbcon.close();
        } catch (SQLException e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }

    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        String item = request.getParameter("itemInfo");
        String option = request.getParameter("option");
        System.out.println(item);
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        ArrayList<Integer> NumOfItems = (ArrayList<Integer>) session.getAttribute("numItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            NumOfItems = new ArrayList<>();
            previousItems.add(item);
            NumOfItems.add(1);
            session.setAttribute("previousItems", previousItems);
            session.setAttribute("numItems", NumOfItems);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            JsonArray moviesTitleJsonArray = new JsonArray();
            try {
                Connection dbcon = dataSource.getConnection();
                Statement statement = dbcon.createStatement();
                synchronized (previousItems) {

                    if (previousItems.contains(item)) {
                        int repeatIndex = previousItems.indexOf(item);
                        NumOfItems.set(repeatIndex, NumOfItems.get(repeatIndex) + 1);

                    } else {
                        NumOfItems.add(1);
                        previousItems.add(item);
                    }
                    if(option.equals("REMOVE"))
                    {
                        int repeatIndex = previousItems.indexOf(item);
                        NumOfItems.set(repeatIndex, NumOfItems.get(repeatIndex) - 2);
                    }
                    if (option.equals("DELETE"))
                    {
                        int repeatIndex = previousItems.indexOf(item);
                        previousItems.remove(item);
                        NumOfItems.remove(repeatIndex);
                    }
                }
                if (!option.equals("NONE")){
                for (int i = 0; i < previousItems.size(); i++) {
                    String movieID = previousItems.get(i);
                    String query = "SELECT M.title FROM movies M WHERE M.id = '" + movieID + "'";
                    ResultSet rs = statement.executeQuery(query);
                    rs.next();
                    String movie_title = rs.getString("title");
                    rs.close();
                    moviesTitleJsonArray.add(movie_title);
                }
                    response.setStatus(200);
                    statement.close();
                    dbcon.close();
                }
            } catch (SQLException e) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("errorMessage", e.getMessage());
                out.write(jsonObject.toString());

                // set reponse status to 500 (Internal Server Error)
                response.setStatus(500);
            }

            JsonObject responseJsonObject = new JsonObject();

            JsonArray previousItemsJsonArray = new JsonArray();
            JsonArray NumOfItemsJsonArray = new JsonArray();
            previousItems.forEach(previousItemsJsonArray::add);
            NumOfItems.forEach(NumOfItemsJsonArray::add);
            responseJsonObject.add("moviesTitle", moviesTitleJsonArray);
            responseJsonObject.add("numItems", NumOfItemsJsonArray);
            responseJsonObject.add("previousItems", previousItemsJsonArray);

            out.write(responseJsonObject.toString());
        }
    }
}
