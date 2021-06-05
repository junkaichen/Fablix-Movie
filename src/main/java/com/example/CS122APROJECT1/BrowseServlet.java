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
import java.sql.PreparedStatement;
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
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbslave");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String handleQuery(String starts_with, String sortFirstBy,String sortRating, String sortTitle) {


        if(starts_with.equals("*"))
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
                    " AND S.starId = T.starId AND M.title REGEXP "
                    + "'^[^A-Za-z0-9]'" + " GROUP BY S.movieId,R.rating";
            if(sortFirstBy.equals("true"))
            {
                outputQuery += " ORDER BY R.rating " + sortRating + "  , M.title " + sortTitle;
            }
            else
            {
                outputQuery += " ORDER BY M.title " + sortTitle + " , R.rating " + sortRating;
            }

            outputQuery += " limit ? , ?;";
            return outputQuery;

        }
        else{
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
                    " AND S.starId = T.starId AND M.title LIKE ? GROUP BY S.movieId,R.rating";
            if(sortFirstBy.equals("true"))
            {
                outputQuery += " ORDER BY R.rating " + sortRating + "  , M.title " + sortTitle;
            }
            else
            {
                outputQuery += " ORDER BY M.title " + sortTitle + " , R.rating " + sortRating;
            }
            outputQuery += " limit ? , ?;";

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

            String input_starts_with = request.getParameter("starts_with");
            System.out.println(Collections.list(request.getParameterNames()));
            String sortFirstBy = request.getParameter("RatingFirst");
            String sortTitle = request.getParameter("sortTitle");
            String sortRating = request.getParameter("sortRating");
            int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            String query = handleQuery(input_starts_with,sortFirstBy,sortRating,sortTitle);
            PreparedStatement preparedStatement = dbcon.prepareStatement(query);
            if(input_starts_with.equals("*"))
            {

                preparedStatement.setInt(1,(pageNumber-1)*pageSize);
                preparedStatement.setInt(2,pageSize);
                //change this
            }
            else
            {
                preparedStatement.setString(1,input_starts_with+"%");
                preparedStatement.setInt(2,(pageNumber-1)*pageSize);
                preparedStatement.setInt(3,pageSize);
                //change this
            }
            System.out.println(preparedStatement.toString());
            ResultSet rs = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            while(rs.next())
            {
                JsonObject jsonObject = new JsonObject();
                JsonArray movie_genres = genresfilter(rs.getString("allGenres").split(";"));
                JsonArray movie_stars  = starsfilter(rs.getString("starInfo").split(";"));
                String movie_id = rs.getString("movieId");
                String movie_title = rs.getString("title");
                Integer movie_year = rs.getInt("year");
                String movie_director = rs.getString("director");
                Double movie_rating = rs.getDouble("rating");
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.add("genres",movie_genres);
                jsonObject.add("movie_stars", movie_stars);
                jsonArray.add(jsonObject);
            }
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            rs.close();
            preparedStatement.close();
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