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


@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
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

    public void starfilter(String input, JsonArray staridArray, JsonArray starnameArray)
    {
        int counter = 0;
        int currentposition;
        while(counter < 3)
        {
            counter++;
            String starname = null;
            currentposition = input.indexOf(",");
            if (currentposition != 0)
            {
                starname = input.substring(0 , currentposition);
                starnameArray.add(starname);
            }
            if(currentposition+3 < input.length())
            {
                input = input.substring(currentposition + 1, input.length());
            }

            String starid = null;


            currentposition = input.indexOf(";");

            if(currentposition != -1)
            {
                if (currentposition != 0)
                {
                    starid= input.substring(0 , currentposition);
                    staridArray.add(starid);
                }
                if(currentposition+3 < input.length())
                {
                    input = input.substring(currentposition + 1, input.length());
                }
                else
                {
                    break;
                }
            }
            else
            {
                starid= input.substring(0 , input.length());
                staridArray.add(starid);
                break;
            }

        }
    }

    public String handleQuery(ArrayList<String> user_inputs, ArrayList<String> user_inputsTypes)
    {
        String outputQuery = "";
        if(user_inputs.size() == 1)
        {
            if (user_inputsTypes.get(0).equals("TITLE"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                        + "'%" + user_inputs.get(0) +"%'" + " GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("YEAR"))
            {

                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.year like "
                        + user_inputs.get(0) +" GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";

            }
            else if (user_inputsTypes.get(0).equals("DIRECTOR"))
            {

                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.director like "
                        + "'%" + user_inputs.get(0) +"%'" + " GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";

            }
            else if (user_inputsTypes.get(0).equals("STAR"))
            {

                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and T.starname like "
                        + "'%" + user_inputs.get(0) +"%'" + " GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";

            }
        }
        else if (user_inputs.size() == 2)
        {
            if (user_inputsTypes.get(0).equals("TITLE") && user_inputsTypes.get(1).equals("YEAR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                        + "'%" + user_inputs.get(0) +"%'" + " AND " + "M.year = " + user_inputs.get(1) + " " +
                        "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("TITLE") && user_inputsTypes.get(1).equals("DIRECTOR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                        + "'%" + user_inputs.get(0) +"%'" + " AND " + "M.director like  " + "'%" + user_inputs.get(1) +"%'" + " " +
                        "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("TITLE") && user_inputsTypes.get(1).equals("STAR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                        + "'%" + user_inputs.get(0) +"%'" + " AND " + "T.starname like  " + "'%" + user_inputs.get(1) +"%'" + " " +
                        "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("YEAR") && user_inputsTypes.get(1).equals("DIRECTOR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.year = "
                        +  user_inputs.get(0)  + " AND " + "M.director like  " + "'%" + user_inputs.get(1) +"%'" + " " +
                        "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("YEAR") && user_inputsTypes.get(1).equals("STAR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.year = "
                        +  user_inputs.get(0)  + " AND " + "T.starname like  " + "'%" + user_inputs.get(1) +"%'" + " " +
                        "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("DIRECTOR") && user_inputsTypes.get(1).equals("STAR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.director like "
                        +  "'%" + user_inputs.get(0) + "%'" + " AND " + "T.starname like  " + "'%" + user_inputs.get(1) +"%'" + " " +
                        "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
        }
        else if (user_inputs.size() == 3)
        {
            if (user_inputsTypes.get(0).equals("TITLE") && user_inputsTypes.get(1).equals("YEAR") && user_inputsTypes.get(2).equals("DIRECTOR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                        +  "'%" + user_inputs.get(0)  + "%'" + " AND " + "M.year =  "  + user_inputs.get(1)  + " AND "
                        + "M.director like  " + "'%" + user_inputs.get(2) +"%'" + "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if(user_inputsTypes.get(0).equals("TITLE") && user_inputsTypes.get(1).equals("YEAR") && user_inputsTypes.get(2).equals("STAR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                        +  "'%" + user_inputs.get(0)  + "%'" + " AND " + "M.year =  "  + user_inputs.get(1)  + " AND "
                        + "T.starname like  " + "'%" + user_inputs.get(2) +"%'" + "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("TITLE") && user_inputsTypes.get(1).equals("DIRECTOR") && user_inputsTypes.get(2).equals("STAR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                        +  "'%" + user_inputs.get(0)  + "%'" + " AND " + "M.director like  " + "'%" + user_inputs.get(1) + "%'"  + " AND "
                        + "T.starname like  " + "'%" + user_inputs.get(2) +"%'" + "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
            else if (user_inputsTypes.get(0).equals("YEAR") && user_inputsTypes.get(1).equals("DIRECTOR") && user_inputsTypes.get(2).equals("STAR"))
            {
                outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                        " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                        "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                        "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.year = "
                        +  user_inputs.get(0)  + " AND " + "M.director like  " + "'%" + user_inputs.get(1) + "%'"  + " AND "
                        + "T.starname like  " + "'%" + user_inputs.get(2) +"%'" + "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
            }
        }
        else
        {
            outputQuery = "SELECT S.movieId ,M.title, M.year, M.director, R.rating, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                    " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',S.starId) SEPARATOR ';') as starInfo FROM movies M," +
                    "  ratings R, genres_in_movies I, genres G, stars_in_movies S, stars T WHERE M.id = R.movieId AND " +
                    "R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId AND S.starId = T.id and M.title like "
                    +  "'%" + user_inputs.get(0)  + "%'" + " AND " + "M.year =  "  + user_inputs.get(1)  + " AND " + "M.director like  " + "'%" + user_inputs.get(2) + "%'"
                    + " AND " + "T.starname like  " + "'%" + user_inputs.get(3) +"%'" + "GROUP BY S.movieId,R.rating ORDER BY rating DESC limit 20;";
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
            Statement statement2 = dbcon.createStatement();
            ArrayList <String> user_inputs = new ArrayList<String>();
            ArrayList <String> user_inputsTypes = new ArrayList<String>();
            String input_title = request.getParameter("title");
            String input_year = request.getParameter("year");
            String input_director= request.getParameter("director");
            String input_star = request.getParameter("star");
            if(input_title != "" && input_title != null){user_inputs.add(input_title); user_inputsTypes.add("TITLE");}
            if(input_year != "" && input_year != null){user_inputs.add(input_year); user_inputsTypes.add("YEAR");}
            if(input_director != "" && input_director != null){user_inputs.add(input_director); user_inputsTypes.add("DIRECTOR");}
            if(input_star != "" && input_star != null){user_inputs.add(input_star); user_inputsTypes.add("STAR");}

            String query = handleQuery(user_inputs, user_inputsTypes);


            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();
            while(rs.next())
            {
                JsonArray stars_array = new JsonArray();
                JsonArray starsId_array = new JsonArray();
                JsonObject jsonObject = new JsonObject();
                String movie_genres = rs.getString("allGenres");
                JsonArray genre_names = genresfilterNames(movie_genres);
                JsonArray genre_ids = genresfilterIds(movie_genres);
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
                jsonObject.add("genre_names",genre_names);
                jsonObject.add("genre_ids",genre_ids);
//                    String movie_starInfo = rs.getString("starInfo");
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("movie_genres", movie_genres);
//                    starfilter(movie_starInfo, starsId_array, stars_array);
                jsonObject.add("movie_starid", starsId_array);
                jsonObject.add("movie_star", stars_array);
                jsonArray.add(jsonObject);
            }
            System.out.println("Done");
            System.out.println("Size = " + jsonArray.size());
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

