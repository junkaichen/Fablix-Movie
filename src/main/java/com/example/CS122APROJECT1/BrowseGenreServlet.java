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


@WebServlet(name = "BrowseGenreServlet", urlPatterns = "/api/browseGenre")
public class BrowseGenreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public JsonArray genresfilterNames(String allgenres) {
        JsonArray outputGenres = new JsonArray();
        int total = 0;
        if(allgenres.contains(";"))
        {
            String[] genres = allgenres.split(";");
            if(genres.length == 2)
            {
                outputGenres.add(genres[0].split(",")[0]);
                outputGenres.add(genres[1].split(",")[0]);
            }
            else
            {
                while(total < 3 && total < genres.length)
                {
                    outputGenres.add(genres[total].split(",")[0]);
                    total++;
                }
            }
        }
        else
        {
            String[] genres = allgenres.split(",");
            outputGenres.add(genres[0]);
        }
        return outputGenres;
    }

    public JsonArray genresfilterIds(String allgenres) {
        JsonArray outputGenres = new JsonArray();
        int total = 0;
        if(allgenres.contains(";"))
        {
            String[] genres = allgenres.split(";");
            if(genres.length == 2)
            {
                outputGenres.add(genres[0].split(",")[1]);
                outputGenres.add(genres[1].split(",")[1]);
            }
            else
            {
                while(total < 3 && total < genres.length)
                {
                    outputGenres.add(genres[total].split(",")[1]);
                    total++;
                }
            }
        }
        else
        {
            String[] genres = allgenres.split(",");
            outputGenres.add(genres[1]);
        }
        return outputGenres;
    }

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String handleQuery(String genre) {
        String outputQuery = "SELECT S.movieId FROM movies M, " +
                "ratings R, genres_in_movies I, genres G, stars_in_movies S, " +
                "stars T WHERE M.id = R.movieId AND " +
                "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId " +
                "AND S.starId = T.id " +
                "and G.id = " + genre + "  " +
                "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            return outputQuery;
    }

    public String handleMovie(String movieID)
    {
        String outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, " +
                "R.rating, GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name ASC SEPARATOR ';') as allGenres " +
                "FROM  stars T, movies M,  ratings R, genres_in_movies I, genres G, stars_in_movies S " +
                "WHERE M.id = R.movieId AND R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId " +
                "AND S.starId = T.id AND S.movieId = ? GROUP BY S.movieId,R.rating ORDER BY rating DESC;";

        return outputQuery;
    }

    public String handleActors(String movieID)
    {
        String outputQuery = "SELECT T.starname, T.starId, starMovieCount FROM" +
                "    (SELECT m.starId, s.starname, count(*) as starMovieCount " +
                "     FROM stars_in_movies m, stars s  WHERE  m.starId = s.id GROUP by s.id) T,  " +
                "     stars_in_movies X" +
                "     WHERE X.starId = T.starId AND X.movieId = ?  " +
                "     ORDER BY T.starMovieCount DESC, T.starname ASC LIMIT 10;";

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
            System.out.println(genre);
            JsonArray jsonArray = new JsonArray();
            String query = handleQuery(genre);
            ResultSet rs = statement.executeQuery(query);
            JsonArray movies_in_genre = new JsonArray();
            while(rs.next())
            {
                String movie = rs.getString("movieId");
                movies_in_genre.add(movie);
            }
            rs.close();
            System.out.println("Movies for genre are found");
            for(int i = 0; i < movies_in_genre.size(); i++)
            {
                JsonObject jsonObject = new JsonObject();

                System.out.println(movies_in_genre.get(i).toString());
                String id = movies_in_genre.get(i).toString();
                String movieInfoQuery = handleMovie(id);
                System.out.println(id);
                String actorInfoQuery = handleActors(id);
                PreparedStatement statement2 = dbcon.prepareStatement(movieInfoQuery);
                statement2.setString(1, id.substring(1,id.length()-1));
                System.out.println("statement2 compiled");
                PreparedStatement statement3 = dbcon.prepareStatement(actorInfoQuery);
                statement3.setString(1, id.substring(1,id.length()-1));
                System.out.println("statement3 compiled");
                ResultSet rs2 =  statement2.executeQuery();
                System.out.println("movie info is retrieved");
                rs2.next();
                System.out.println("only row grabbed");
                System.out.println(rs2.getStatement().toString());
                String movie_id = rs2.getString("movieId");
                System.out.println("movie_id"+movie_id);
                String movie_title = rs2.getString("title");
                Integer movie_year = rs2.getInt("year");
                String movie_genres = rs2.getString("allGenres");
                JsonArray genre_names = genresfilterNames(movie_genres);
                JsonArray genre_ids = genresfilterIds(movie_genres);
                String movie_director = rs2.getString("director");
                Double movie_rating = rs2.getDouble("rating");
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.add("genre_names",genre_names);
                jsonObject.add("genre_ids",genre_ids);
                System.out.println("basic movie info is found");
                JsonArray starsId_array = new JsonArray();
                JsonArray stars_array = new JsonArray();
                ResultSet rs3 =  statement3.executeQuery();
                System.out.println("actor info is found");
                int counter = 0;
                while(rs3.next())
                {
                    String movie_star = rs3.getString("starname");
                    stars_array.add(movie_star);
                    String movie_starId = rs3.getString("starId");
                    starsId_array.add(movie_starId);
                    counter++;
                    if(counter == 3)
                    {
                        break;
                    }
                }
                System.out.println("actors in movie are found");
                jsonObject.add("movie_starid", starsId_array);
                jsonObject.add("movie_star", stars_array);
                jsonArray.add(jsonObject);
                System.out.println("movie is done");
                rs2.close();
                rs3.close();
                statement2.close();
                statement3.close();
            }



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
