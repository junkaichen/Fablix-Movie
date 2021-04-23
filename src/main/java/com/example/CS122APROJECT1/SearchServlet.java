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
import com.google.gson.Gson;
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
import java.util.ArrayList;


@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    // Create a dataSource which registered in web.
    private DataSource dataSource;


    public String handleQuery(ArrayList<String> user_inputs, ArrayList<String> user_inputsTypes)
    {
        String outputQuery = "";
        if(user_inputs.size() == 1)
        {
            if (user_inputsTypes.get(0).equals("TITLE"))
            {
                outputQuery = String.format("SELECT * from movies M, ratings R, genres_in_movies I, genres G, stars_in_movies S, " +
                        "stars T WHERE M.id = R.movieId AND R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId " +
                        "AND S.starId = T.id AND M.title like '%s'", user_inputs.get(0));
            }
            else if (user_inputsTypes.get(0).equals("YEAR"))
            {

                outputQuery = "SELECT * from movies M, ratings R, genres_in_movies I, genres G, stars_in_movies S, " +
                        "stars T WHERE M.id = R.movieId AND R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId " +
                        "AND S.starId = T.id AND M.year =" + "2003";
//                outputQuery += "ORDER BY R.rating  DESC";
            }
        }

        return outputQuery;
    }
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();
            ArrayList <String> user_inputs = new ArrayList<String>();
            ArrayList <String> user_inputsTypes = new ArrayList<String>();
            String input_title = request.getParameter("title");
            String input_year = request.getParameter("year");
            String input_director= request.getParameter("director");
            String input_star = request.getParameter("star");

            Integer querytype = -1;
            if(input_title != ""){user_inputs.add(input_title); user_inputsTypes.add("TITLE"); querytype = 0;}
            if(input_year != ""){user_inputs.add(input_year); user_inputsTypes.add("YEAR"); querytype = 1;}
            if(input_director != ""){user_inputs.add(input_director); user_inputsTypes.add("DIRECTOR");}
            if(input_star != ""){user_inputs.add(input_star); user_inputsTypes.add("STAR");}

            String query = handleQuery(user_inputs, user_inputsTypes);


            ResultSet rs = statement.executeQuery(query);

            if(querytype == 0)
            {
                JsonObject jsonObject = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                int countGenres = 0;
                int countStars = 0;
                // Perform the query
                if(rs.next()) {
                    String movie_title = rs.getString("title");
                    String movie_id = rs.getString("id");
                    String movie_year = rs.getString("year");
                    String movie_director = rs.getString("director");
                    String movie_rating = rs.getString("rating");
                    String movie_nameOfGenres = rs.getString("name");
                    String movie_star = rs.getString("starname");
                    String movie_starid = rs.getString("starId");
                    JsonArray stars_array = new JsonArray();
                    JsonArray starsId_array = new JsonArray();
                    stars_array.add(movie_star);
                    starsId_array.add(movie_starid);
                    jsonObject.addProperty("movie_title", movie_title);
                    jsonObject.addProperty("movie_id", movie_id);
                    jsonObject.addProperty("movie_director", movie_director);
                    jsonObject.addProperty("movie_year", movie_year);
                    jsonObject.addProperty("movie_rating", movie_rating);
                    while(rs.next())
                    {
                        movie_star = rs.getString("starname");
                        movie_starid = rs.getString("starId");
                        String movie_nameOfGenres2 = rs.getString("name");
                        if(countGenres < 2 && !movie_nameOfGenres.contains(movie_nameOfGenres2))
                        {
                            movie_nameOfGenres = movie_nameOfGenres + ", " + movie_nameOfGenres2;
                            countGenres++;
                        }
                        if(countStars < 3)
                        {
                            stars_array.add(movie_star);
                            starsId_array.add(movie_starid);
                            countStars++;
                        }
                    }
                    jsonObject.add("movie_starid", starsId_array);
                    jsonObject.addProperty("searchType", "TITLE");
                    jsonObject.add("movie_star", stars_array);
                    jsonObject.addProperty("movie_nameOfGenres", movie_nameOfGenres);
                    jsonArray.add(jsonObject);
                }
                // write JSON string to output
                out.write(jsonArray.toString());
                // set response status to 200 (OK)
                response.setStatus(200);
            }
            else if(querytype == 1)
            {

                JsonArray jsonArray = new JsonArray();
                int countGenres = 0;
                int countStars = 0;
                // Perform the query

                JsonArray stars_array = new JsonArray();
                JsonArray starsId_array = new JsonArray();
                rs.next();
                String movie_title = rs.getString("title");
                String movie_id = rs.getString("id");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_nameOfGenres = rs.getString("name");
                String movie_star = rs.getString("starname");
                String movie_rating = rs.getString("rating");
                String movie_starid = rs.getString("starId");
                stars_array.add(movie_star);
                starsId_array.add(movie_starid);
                rs.next();
                do {
                    String movie_title2 = rs.getString("title");
                    String movie_id2 = rs.getString("id");
                    String movie_year2 = rs.getString("year");
                    String movie_director2 = rs.getString("director");
                    String movie_nameOfGenres2 = rs.getString("name");
                    String movie_star2 = rs.getString("starname");
                    String movie_rating2 = rs.getString("rating");
                    String movie_starid2 = rs.getString("starId");
                    if(!movie_id.equals(movie_id2))
                    {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("searchType", "YEAR");
                        jsonObject.addProperty("movie_title", movie_title);
                        jsonObject.addProperty("movie_id", movie_id);
                        jsonObject.addProperty("movie_director", movie_director);
                        jsonObject.addProperty("movie_nameOfGenres", movie_nameOfGenres);
                        jsonObject.addProperty("movie_year", movie_year);
                        jsonObject.addProperty("movie_rating", movie_rating);
                        jsonObject.add("movie_starid", starsId_array);
                        jsonObject.add("movie_star", stars_array);
                        jsonArray.add(jsonObject);
                        movie_id = movie_id2;
                        movie_title = movie_title2;
                        movie_year = movie_year2;
                        movie_director = movie_director2;
                        movie_rating = movie_rating2;
                        movie_nameOfGenres = movie_nameOfGenres2;
                        stars_array = new JsonArray();
                        starsId_array = new JsonArray();
                        countStars = 0;
                        countGenres = 0;
                    }
                    else {
                        movie_id = movie_id2;
                        movie_title = movie_title2;
                        movie_year = movie_year2;
                        movie_director = movie_director2;
                        movie_rating = movie_rating2;


                        if(countGenres < 3 && !movie_nameOfGenres.contains(movie_nameOfGenres2)){
                            movie_nameOfGenres = movie_nameOfGenres + ", " + movie_nameOfGenres2;
                            countGenres++;
                        }
                        if(countStars < 3)
                        {
                            stars_array.add(movie_star2);
                            starsId_array.add(movie_starid2);
                            countStars++;
                        }
                    }
                } while(rs.next());



                // write JSON string to output
                out.write(jsonArray.toString());
                // set response status to 200 (OK)
                response.setStatus(200);
            }

            rs.close();
            statement.close();
            dbcon.close();

        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);

        }
        out.close();

    }
}

