package com.example.CS122APROJECT1;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

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
import java.util.ArrayList;

@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movielist")
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
            //String movie_nameOfStars = rs.getString("starname");
            // Create a Json Array for names and ids
            ArrayList<String> starIdList = new ArrayList<>();
            starIdList.add(rs.getString("starId"));
            ArrayList<String> movie_nameOfStars = new ArrayList<>();
            do {
                String movie_id2 = rs.getString("id");
                String movie_title2 = rs.getString("title");
                Integer movie_year2 = rs.getInt("year");
                String movie_director2 = rs.getString("director");
                Double movie_rating2 = rs.getDouble("rating");
                String movie_nameOfGenres2 = rs.getString("name");
                String movie_nameOfStars2 = rs.getString("starname");
                String star_id2 = rs.getString("starId");

                // if movie title isn't the same then add curJson obj and start the next one
                if(!movie_title.equals(movie_title2))
                {
                    JsonObject jsonObject = new JsonObject();
                    JsonArray star_ids = new Gson().toJsonTree(starIdList).getAsJsonArray();
                    JsonArray star_names = new Gson().toJsonTree(movie_nameOfStars).getAsJsonArray();
                    jsonObject.add("movie_nameOfStars",star_names);
                    jsonObject.add("star_ids",star_ids);
                    jsonObject.addProperty("movie_id", movie_id);
                    jsonObject.addProperty("movie_title", movie_title);
                    jsonObject.addProperty("movie_year", movie_year);
                    jsonObject.addProperty("movie_director", movie_director);
                    jsonObject.addProperty("movie_nameOfGenres", movie_nameOfGenres);
                    jsonObject.addProperty("movie_rating", movie_rating);
                    jsonArray.add(jsonObject);
                    movie_id = movie_id2;
                    movie_title = movie_title2;
                    movie_year = movie_year2;
                    movie_director = movie_director2;
                    movie_rating = movie_rating2;
                    movie_nameOfGenres = movie_nameOfGenres2;
                    countGenres = 0;
                    countStars = 0;
                    starIdList.clear();
                    starIdList.add(star_id2);
                    movie_nameOfStars.clear();
                    movie_nameOfStars.add(movie_nameOfStars2);

                }
                // Adding info about same movie
                else{
                    movie_title = movie_title2;
                    movie_year = movie_year2;
                    movie_director = movie_director2;
                    movie_rating = movie_rating2;


                    if (countStars < 2)
                    {
                        starIdList.add(star_id2);
                        movie_nameOfStars.add(movie_nameOfStars2);
                        countStars++;
                    }
                    if(countGenres < 2 && !movie_nameOfGenres.contains(movie_nameOfGenres2)){
                        movie_nameOfGenres = movie_nameOfGenres + ", " + movie_nameOfGenres2;
                        countGenres++;

                    }
                }
            }
            while (rs.next());


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
