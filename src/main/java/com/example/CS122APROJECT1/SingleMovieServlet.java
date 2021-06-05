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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet{
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbslave");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public JsonArray genresfilter(String[] allgenres) {
        JsonArray outputGenres = new JsonArray();
        for(int i = 0; i < allgenres.length; i++)
        {
            outputGenres.add(allgenres[i]);
        }
        return outputGenres;
    }

    public JsonArray starsfilter(String[] allstars) {
        JsonArray outputStars = new JsonArray();
        for(int i = 0; i < allstars.length; i++)
        {
            outputStars.add(allstars[i]);
        }
        return outputStars;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");
        System.out.println(Collections.list(request.getParameterNames()));
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query = "SELECT S.movieId ,M.title, M.year, M.director," +
                    " R.rating, GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId)" +
                    " ORDER BY G.name SEPARATOR ';') as allGenres," +
                    " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',T.starId)" +
                    " ORDER BY T.starMovieCount DESC, T.starname ASC SEPARATOR ';') as starInfo" +
                    " FROM (SELECT m.starId, s.starname, count(*) as starMovieCount" +
                    " FROM stars_in_movies m, stars s  WHERE  m.starId = s.id GROUP by s.id) T, movies M,  ratings R," +
                    " genres_in_movies I, genres G, stars_in_movies S" +
                    " WHERE M.id = R.movieId AND" +
                    "     R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId" +
                    "     AND S.starId = T.starId AND M.id = ?" +
                    " GROUP BY S.movieId,R.rating ;";


            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            JsonObject jsonObject = new JsonObject();
            rs.next();
            String movie_title = rs.getString("title");
            Integer movie_year = rs.getInt("year");
            String movie_director = rs.getString("director");
            Double movie_rating = rs.getDouble("rating");
            JsonArray genres = genresfilter(rs.getString("allGenres").split(";"));
            JsonArray movie_stars = starsfilter(rs.getString("starInfo").split(";"));
            jsonObject.add("genres",genres);
            jsonObject.addProperty("movie_title", movie_title);
            jsonObject.addProperty("movie_year", movie_year);
            jsonObject.addProperty("movie_director", movie_director);
            jsonObject.addProperty("movie_rating", movie_rating);
            jsonObject.add("movie_stars", movie_stars);
            jsonArray.add(jsonObject);
            System.out.println("data found");
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
