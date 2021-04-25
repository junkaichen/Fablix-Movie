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
import java.util.Collections;

@WebServlet(name = "BrowseServlet", urlPatterns = "/api/browse")
public class BrowseServlet extends  HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public String genresfilter(String allgenres) {
        String outputGenres = "";
        int currentposition;
        while (allgenres.length() != 2) {
            String subString = "";
            currentposition = allgenres.indexOf(","); //this finds the first occurrence of "."
            if (currentposition != 0) {
                subString = allgenres.substring(0, currentposition); //this will give abc
                outputGenres += subString;// returns the movie Avatar 3
                outputGenres += ", ";
            }
            if (currentposition + 3 < allgenres.length()) {
                int nextChar = allgenres.indexOf(";");
                allgenres = allgenres.substring(currentposition + 3, allgenres.length());
            } else {
                break;
            }

        }
        outputGenres = outputGenres.replace(";", "");
        return outputGenres;
    }

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String handleQuery(String starts_with) {
        if(starts_with.equals("*"))
        {
            String outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                    " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                    "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                    "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title REGEXP "
                    + "'^[^A-Za-z0-9]'" + " GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            return outputQuery;

        }
        else{
            String outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                    " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                    "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                    "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                    + "'" + starts_with +"%'" + " GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";

            return outputQuery;
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
            Statement statement2 = dbcon.createStatement();
            String input_starts_with = request.getParameter("starts_with");
            System.out.println(Collections.list(request.getParameterNames()));
            System.out.println(input_starts_with);

            String query = handleQuery(input_starts_with);


            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();
            while(rs.next())
            {
                JsonArray stars_array = new JsonArray();
                JsonArray starsId_array = new JsonArray();
                JsonObject jsonObject = new JsonObject();
                String movie_id = rs.getString("movieId");
                String query2 = "SELECT T.starname, T.starId, starMovieCount FROM(SELECT m.starId, s.starname, " +
                        "count(*) as starMovieCount FROM stars_in_movies m, stars s  WHERE  m.starId = s.id GROUP by s.id) " +
                        "T,stars_in_movies X WHERE X.starId = T.starId AND X.movieId =  '" +   movie_id + "'" +
                        " ORDER BY T.starMovieCount DESC LIMIT 3;";
                ResultSet rs2 = statement2.executeQuery(query2);
                int counter = 0;
                while(rs2.next())
                {
                    String movie_star = rs2.getString("starname");
                    stars_array.add(movie_star);
                    String movie_starId = rs2.getString("starId");
                    starsId_array.add(movie_starId);
                    counter++;
                    if(counter == 3)
                    {
                        break;
                    }
                }
                String movie_title = rs.getString("title");
                Integer movie_year = rs.getInt("year");
                String movie_director = rs.getString("director");
                Double movie_rating = rs.getDouble("rating");
                String movie_genres = genresfilter(rs.getString("allGenres"));

                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("movie_genres", movie_genres);

                jsonObject.add("movie_starid", starsId_array);
                jsonObject.add("movie_star", stars_array);
                jsonArray.add(jsonObject);
            }
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            rs.close();
            statement.close();
            dbcon.close();
            System.out.println("Results are done");

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