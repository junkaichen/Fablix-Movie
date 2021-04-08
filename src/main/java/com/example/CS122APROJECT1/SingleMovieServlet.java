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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet{
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query = "SELECT  * from movies M, genres_in_movies GM, genres G, stars_in_movies SM, stars S, ratings R WHERE M.id = GM.movieId AND GM.genreId = G.id AND M.id = SM.movieId AND SM.starId = S.id AND M.id = R.movieId AND M.id = ?";

            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            JsonObject jsonObject = new JsonObject();
            rs.next();
            String movie_title = rs.getString("title");
            Integer movie_year = rs.getInt("year");
            String movie_director = rs.getString("director");
            Double movie_rating = rs.getDouble("rating");
            jsonObject.addProperty("movie_title", movie_title);
            jsonObject.addProperty("movie_year", movie_year);
            jsonObject.addProperty("movie_director", movie_director);
            jsonObject.addProperty("movie_rating", movie_rating);


            JsonArray genres_array = new JsonArray();
            JsonArray stars_array = new JsonArray();
            String movie_nameOfGenres = rs.getString("name");
            String movie_nameOfStars = rs.getString("starname");
            genres_array.add(movie_nameOfGenres);
            stars_array.add(movie_nameOfStars);
            while(rs.next())
            {
                movie_nameOfGenres = rs.getString("name");
                movie_nameOfStars = rs.getString("starname");
                boolean exists = false;
                boolean exists2 = false;
                for(int i = 0; i < genres_array.size(); i++)
                {
                    if(genres_array.toString().contains(movie_nameOfGenres))
                    {
                        exists = true;
                        break;
                    }
                }
                if(exists == false)
                {
                    genres_array.add(movie_nameOfGenres);
                }
                for(int i = 0; i < stars_array.size(); i++)
                {
                    if(stars_array.toString().contains(movie_nameOfStars))
                    {
                        exists2 = true;
                        break;
                    }
                }
                if(exists2 == false)
                {
                    stars_array.add(movie_nameOfStars);
                }

            }
            jsonObject.add("movie_nameOfGenres", genres_array);
            jsonObject.add("movie_nameOfStars", stars_array);


            jsonArray.add(jsonObject);
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
