package com.example.CS122APROJECT1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Arrays;
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
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Array;


@WebServlet(name = "BrowseGenreServlet", urlPatterns = "/api/browseGenre")
public class BrowseGenreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    // Create a dataSource which registered in web.
    private DataSource dataSource;

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

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String handleQuery() {
        String outputQuery = "SELECT S.movieId FROM movies M, " +
                "ratings R, genres_in_movies I, genres G, stars_in_movies S, " +
                "stars T WHERE M.id = R.movieId AND " +
                "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId " +
                "AND S.starId = T.id " +
                "AND G.id = ?"  +
                " GROUP BY S.movieId,R.rating ORDER BY rating DESC limit ?,?;";
            return outputQuery;
    }

    public String handleMovie(int numOfMovies)
    {
        String outputQuery = "SELECT S.movieId ,M.title, M.year, M.director," +
                " R.rating, GROUP_CONCAT(" +
                "DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                " GROUP_CONCAT(" +
                "DISTINCT CONCAT(" +
                "T.starname,',',T.starId) ORDER BY T.starMovieCount DESC, T.starname ASC SEPARATOR ';') as starInfo " +
                "FROM " +
                " (SELECT m.starId, s.starname, count(*) as starMovieCount " +
                " FROM stars_in_movies m, stars s  WHERE  m.starId = s.id GROUP by s.id) T, movies M,  ratings R," +
                " genres_in_movies I, genres G, stars_in_movies S" +
                " WHERE M.id = R.movieId AND" +
                " R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId" +
                " AND S.starId = T.starId AND S.movieId in (";
        for(int i=0; i < numOfMovies; i++)
        {
            if(i == numOfMovies-1)
            {
                outputQuery += "?";
            }
            else
            {
                outputQuery += "?,";
            }
        }
        outputQuery += ")  GROUP BY S.movieId,R.rating ORDER BY rating DESC limit ?,?;";

        return outputQuery;
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
            System.out.println(Collections.list(request.getParameterNames()));
            String genre = request.getParameter("genre");
            JsonArray jsonArray = new JsonArray();
            PreparedStatement preparedStatement = dbcon.prepareStatement(handleQuery());
            preparedStatement.setString(1,genre);
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,20);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<String> movies_in_genre = new ArrayList<String>();
            while(rs.next())
            {
               movies_in_genre.add(rs.getString("movieId"));
            }

            String[] movies = movies_in_genre.toArray(new String[movies_in_genre.size()]);

            PreparedStatement preparedStatement2 = dbcon.prepareStatement(handleMovie(movies.length));
            for(int i = 0; i < movies.length; i++)
            {
                preparedStatement2.setString(i+1,movies[i]);
            }

            preparedStatement2.setInt(movies.length+1,0);
            preparedStatement2.setInt(movies.length+2,20);
            ResultSet rs2 = preparedStatement2.executeQuery();
            while(rs2.next()) {
                JsonObject jsonObject = new JsonObject();
                String movie_id = rs2.getString("movieId");
                String movie_title = rs2.getString("title");
                Integer movie_year = rs2.getInt("year");
                JsonArray movie_stars = starsfilter(rs2.getString("starInfo").split(";"));
                JsonArray genres = genresfilter(rs2.getString("allGenres").split(";"));
                String movie_director = rs2.getString("director");
                Double movie_rating = rs2.getDouble("rating");
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.add("genres", genres);
                jsonObject.add("movie_stars", movie_stars);
                jsonArray.add(jsonObject);
            }
            rs2.close();
            preparedStatement2.close();




            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
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
