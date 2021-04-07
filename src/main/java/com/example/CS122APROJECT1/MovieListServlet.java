package com.example.CS122APROJECT1;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "StarsServlet", urlPatterns = "/api/movielist")
public class MovieListServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

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

//            String query = "SELECT * from movies M, ratings R WHERE M.id = R.movieId ORDER BY " +
//                    "rating DESC limit 20";

            String query = "SELECT * from movies M,  ratings R, " +
                    "genres_in_movies I, genres G, stars_in_movies S, stars T " +
                    "WHERE M.id = R.movieId AND " +
                    "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId" +
                    " AND S.starId = T.id ORDER BY rating DESC limit 185";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            rs.next();
            int countStars = 0;
            int countGenres = 0;
            String movie_id = rs.getString("id");
            String movie_title = rs.getString("title");
            Integer movie_year = rs.getInt("year");
            String movie_director = rs.getString("director");
            Double movie_rating = rs.getDouble("rating");
            String movie_nameOfGenres = rs.getString("name");
            String movie_nameOfStars = rs.getString("starname");
            rs.next();
            do {
                String movie_id2 = rs.getString("id");
                String movie_title2 = rs.getString("title");
                Integer movie_year2 = rs.getInt("year");
                String movie_director2 = rs.getString("director");
                Double movie_rating2 = rs.getDouble("rating");
                String movie_nameOfGenres2 = rs.getString("name");
                String movie_nameOfStars2 = rs.getString("starname");
                if(!movie_title.equals(movie_title2))
                {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("movie_id", movie_id);
                    jsonObject.addProperty("movie_title", movie_title);
                    jsonObject.addProperty("movie_year", movie_year);
                    jsonObject.addProperty("movie_director", movie_director);
                    jsonObject.addProperty("movie_nameOfGenres", movie_nameOfGenres);
                    jsonObject.addProperty("movie_nameOfStars", movie_nameOfStars);
                    jsonObject.addProperty("movie_rating", movie_rating);
                    jsonArray.add(jsonObject);
                    movie_title = movie_title2;
                    movie_year = movie_year2;
                    movie_director = movie_director2;
                    movie_rating = movie_rating2;
                    movie_nameOfGenres = movie_nameOfGenres2;
                    movie_nameOfStars = movie_nameOfStars2;
                    countGenres = 0;
                    countStars = 0;
                }
                else{
                    movie_title = movie_title2;
                    movie_year = movie_year2;
                    movie_director = movie_director2;
                    movie_rating = movie_rating2;
                    if (countStars < 2)
                    {
                        movie_nameOfStars = movie_nameOfStars + ", " + movie_nameOfStars2;
                        countStars++;
                    }
                    if(countGenres < 2 && !movie_nameOfGenres.contains(movie_nameOfGenres2)){
                        movie_nameOfGenres = movie_nameOfGenres + ", " + movie_nameOfGenres2;
                        countGenres++;
                    }
                }
            }
            while (rs.next());
            // Iterate through each row of rs
//            while (rs.next()) {
//                String movie_id = rs.getString("id");
//                String movie_title = rs.getString("title");
//                Integer movie_year = rs.getInt("year");
//                String movie_director = rs.getString("director");
//                Double movie_rating = rs.getDouble("rating");
//
//                // Create a JsonObject based on the data we retrieve from rs
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("movie_id", movie_id);
//                jsonObject.addProperty("movie_title", movie_title);
//                jsonObject.addProperty("movie_year", movie_year);
//                jsonObject.addProperty("movie_director", movie_director);
//                jsonObject.addProperty("movie_rating", movie_rating);
//
//                jsonArray.add(jsonObject);
//            }

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

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
