//package com.example.CS122APROJECT1;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.Collections;
//
//
//@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
//public class SearchServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    // Create a dataSource which registered in web.xml
//    // Create a dataSource which registered in web.
//    private DataSource dataSource;
//
//    public JsonArray genresfilter(String[] allgenres) {
//        JsonArray outputGenres = new JsonArray();
//        for(int i = 0; i < allgenres.length; i++)
//        {
//            outputGenres.add(allgenres[i]);
//        }
//        return outputGenres;
//    }
//
//    public JsonArray starsfilter(String[] allstars) {
//        JsonArray outputStars = new JsonArray();
//        for(int i = 0; i < allstars.length; i++)
//        {
//            outputStars.add(allstars[i]);
//        }
//        return outputStars;
//    }
//
//    /*
//        The function handleQuery() builds the query info together based on what the user inputs
//        for searching parameters. The info is organized in one format as :
//        Title, Year, Director, Star
//        The purpose for this is the organization of the SQL Query is when searching for a movie star, with the given,
//        group concatenation starInfo.
//     */
//    public String handleQuery(String[] searchInfo, String sortFirstBy,String sortRating, String sortTitle) {
//        String query = "SELECT S.movieId ,M.title, M.year, M.director," +
//                " R.rating, GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
//                " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',T.starId) ORDER BY T.starMovieCount DESC," +
//                " T.starname ASC SEPARATOR ';') as starInfo" +
//                " FROM (SELECT m.starId, s.starname, count(*) as starMovieCount FROM stars_in_movies m," +
//                " stars s  WHERE  m.starId = s.id GROUP by s.id) T, movies M,  ratings R," +
//                " genres_in_movies I, genres G, stars_in_movies S" +
//                " WHERE M.id = R.movieId AND" +
//                " R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId" +
//                " AND S.starId = T.starId ";
//        if(!searchInfo[0].equals(""))
//        {
//            query += " AND M.title like ? ";
//        }
//        if(!searchInfo[1].equals(""))
//        {
//            query += " AND M.year = ? ";
//        }
//        if(!searchInfo[2].equals(""))
//        {
//            query += " AND M.director like ?";
//        }
//        query += " GROUP BY S.movieId,R.rating ";
//        if(!searchInfo[3].equals(""))
//        {
//            query += " HAVING starInfo LIKE ? ";
//        }
//        if(sortFirstBy.equals("true"))
//        {
//            query += " ORDER BY R.rating " + sortRating + "  , M.title " + sortTitle;
//        }
//        else
//        {
//            query += " ORDER BY M.title " + sortTitle + " , R.rating " + sortRating;
//        }
//
//        query += " limit ? , ?;";
//
//        return query;
//    }
//
//    public void init(ServletConfig config) {
//        try {
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//     */
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        response.setContentType("application/json"); // Response mime type
//
//        // Output stream to STDOUT
//        PrintWriter out = response.getWriter();
//
//        try {
//            // Get a connection from dataSource
//            Connection dbcon = dataSource.getConnection();
//            int input_count = 0;
//            System.out.println(Collections.list(request.getParameterNames()));
//            // The order is title, year, director, star
//            String[] user_inputs = new String[] {"","","",""};
//            String input_title = request.getParameter("title");
//            String input_year = request.getParameter("year");
//            String input_director= request.getParameter("director");
//            String input_star = request.getParameter("star");
//            String sortFirstBy = request.getParameter("RatingFirst");
//            String sortTitle = request.getParameter("sortTitle");
//            String sortRating = request.getParameter("sortRating");
//            int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
//            System.out.println(pageNumber);
//            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
//            if(input_title != null && !input_title.contains("%"))
//            {
//                user_inputs[0] = input_title;
//                input_count++;
//            }
//            if(input_year != null && !input_year.contains("%"))
//            {
//                user_inputs[1] = input_year;
//                input_count++;
//            }
//            if(input_director != null && !input_director.contains("%"))
//            {
//                user_inputs[2] = input_director;
//                input_count++;
//            }
//            if(input_star != null && !input_star.contains("%"))
//            {
//                user_inputs[3] = input_star;
//                input_count++;
//            }
//
//            PreparedStatement preparedStatement = dbcon.prepareStatement(handleQuery(user_inputs,sortFirstBy,sortRating,sortTitle));
//            int prepPosition = 1;
//            // The order is title, year, director, star
//            if(!user_inputs[0].equals(""))
//            {
//                preparedStatement.setString(prepPosition,"%" + user_inputs[0] + "%");
//                prepPosition++;
//            }
//            if(!user_inputs[1].equals(""))
//            {
//                preparedStatement.setInt(prepPosition, Integer.parseInt(user_inputs[1]));
//                prepPosition++;
//            }
//            if(!user_inputs[2].equals(""))
//            {
//                preparedStatement.setString(prepPosition,"%" + user_inputs[2] + "%");
//                prepPosition++;
//            }
//            if(!user_inputs[3].equals(""))
//            {
//                preparedStatement.setString(prepPosition,"%" + user_inputs[3] + "%");
//                prepPosition++;
//            }
//            preparedStatement.setInt(prepPosition,(pageNumber-1)*pageSize);
//            preparedStatement.setInt(prepPosition+1,pageSize);
//            ResultSet rs = preparedStatement.executeQuery();
//            System.out.println(preparedStatement.toString());
//            JsonArray jsonArray = new JsonArray();
//            while(rs.next())
//            {
//                JsonObject jsonObject = new JsonObject();
//                JsonArray movie_stars = starsfilter(rs.getString("starInfo").split(";"));
//                JsonArray genres = genresfilter(rs.getString("allGenres").split(";"));
//                String movie_id = rs.getString("movieId");
//                String movie_title = rs.getString("title");
//                Integer movie_year = rs.getInt("year");
//                String movie_director = rs.getString("director");
//                Double movie_rating = rs.getDouble("rating");
//                jsonObject.add("genres",genres);
//                jsonObject.add("movie_stars", movie_stars);
//                jsonObject.addProperty("movie_id", movie_id);
//                jsonObject.addProperty("movie_title", movie_title);
//                jsonObject.addProperty("movie_year", movie_year);
//                jsonObject.addProperty("movie_director", movie_director);
//                jsonObject.addProperty("movie_rating", movie_rating);
//
//                jsonArray.add(jsonObject);
//            }
//
//            // write JSON string to output
//            out.write(jsonArray.toString());
//            // set response status to 200 (OK)
//            response.setStatus(200);
//            rs.close();
//            preparedStatement.close();
//            dbcon.close();
//
//        } catch (Exception e) {
//
//            // write error message JSON object to output
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("errorMessage", e.getMessage());
//            out.write(jsonObject.toString());
//
//            // set reponse status to 500 (Internal Server Error)
//            response.setStatus(500);
//
//        }
//        out.close();
//
//    }
//}


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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;


@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
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

    private static JsonObject generateJsonObject(String movieID, String heroName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", heroName);

        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("movieID", movieID);

        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }

    /*
        The function handleQuery() builds the query info together based on what the user inputs
        for searching parameters. The info is organized in one format as :
        Title, Year, Director, Star
        The purpose for this is the organization of the SQL Query is when searching for a movie star, with the given,
        group concatenation starInfo.
     */
    public String handleQuery(String[] searchInfo, String sortFirstBy,String sortRating, String sortTitle) {
        String query = "SELECT S.movieId ,M.title, M.year, M.director," +
                " R.rating, GROUP_CONCAT(DISTINCT CONCAT(G.name,',',I.genreId) ORDER BY G.name SEPARATOR ';') as allGenres," +
                " GROUP_CONCAT(DISTINCT CONCAT(T.starname,',',T.starId) ORDER BY T.starMovieCount DESC," +
                " T.starname ASC SEPARATOR ';') as starInfo" +
                " FROM (SELECT m.starId, s.starname, count(*) as starMovieCount FROM stars_in_movies m," +
                " stars s  WHERE  m.starId = s.id GROUP by s.id) T, movies M,  ratings R," +
                " genres_in_movies I, genres G, stars_in_movies S" +
                " WHERE M.id = R.movieId AND" +
                " R.movieId = I.movieId AND I.genreId = G.id AND R.movieId = S.movieId" +
                " AND S.starId = T.starId ";
        ArrayList<String> userinputtitle = new ArrayList<String>();
        try {

                // String finalQuery = "SELECT * FROM ft WHERE MATCH (title) AGAINST ('grad*' IN BOOLEAN MODE)";
                StringTokenizer st = new StringTokenizer(searchInfo[0]);
                String partOfQuery = "'";
                while(st.hasMoreTokens())
                {
                    partOfQuery += "+";
                    partOfQuery += st.nextToken();
                    partOfQuery += "* ";

                }
                partOfQuery += "'";
                String finalQuery = "SELECT * FROM ft WHERE MATCH (title) AGAINST (";
                finalQuery += partOfQuery;
                finalQuery += " IN BOOLEAN MODE)";



                Connection dbcon = dataSource.getConnection();

                // Declare our statement
                Statement statement = dbcon.createStatement();

                // Perform the query
                ResultSet rs = statement.executeQuery(finalQuery);

                while(rs.next())
                {
                    String movieTitle = rs.getString("title");
                    userinputtitle.add(movieTitle);
                }

                rs.close();
                statement.close();
                dbcon.close();
        } catch (Exception e) {
                System.out.println(e);
        }





        if(!searchInfo[0].equals(""))
        {
            for (int i = 0; i < userinputtitle.size(); i++)
            {
                if(i == 0)
                {
                    query += " AND (M.title = '";
                    query += userinputtitle.get(i);
                    query += "'";
                }
                else
                {
                    query += " OR M.title = '";
                    query += userinputtitle.get(i);
                    query += "'";
                }
            }
            query += ")";
//            query += " AND M.title like ? ";
//            query += " AND M.title = '";
//            query += searchInfo[0] + "'";
        }
        if(!searchInfo[1].equals(""))
        {
            query += " AND M.year = ? ";
        }
        if(!searchInfo[2].equals(""))
        {
            query += " AND M.director like ?";
        }
        query += " GROUP BY S.movieId,R.rating ";
        if(!searchInfo[3].equals(""))
        {
            query += " HAVING starInfo LIKE ? ";
        }
        if(sortFirstBy.equals("true"))
        {
            query += " ORDER BY R.rating " + sortRating + "  , M.title " + sortTitle;
        }
        else
        {
            query += " ORDER BY M.title " + sortTitle + " , R.rating " + sortRating;
        }

        query += " limit ? , ?;";

        return query;
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
            int input_count = 0;
            System.out.println(Collections.list(request.getParameterNames()));
            // The order is title, year, director, star
            String[] user_inputs = new String[] {"","","",""};
            String input_title = request.getParameter("title");
            String input_year = request.getParameter("year");
            String input_director= request.getParameter("director");
            String input_star = request.getParameter("star");
            String sortFirstBy = request.getParameter("RatingFirst");
            String sortTitle = request.getParameter("sortTitle");
            String sortRating = request.getParameter("sortRating");
            int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            System.out.println(pageNumber);
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            if(input_title != null && !input_title.contains("%"))
            {
                user_inputs[0] = input_title;
                input_count++;
            }
            if(input_year != null && !input_year.contains("%"))
            {
                user_inputs[1] = input_year;
                input_count++;
            }
            if(input_director != null && !input_director.contains("%"))
            {
                user_inputs[2] = input_director;
                input_count++;
            }
            if(input_star != null && !input_star.contains("%"))
            {
                user_inputs[3] = input_star;
                input_count++;
            }

            PreparedStatement preparedStatement = dbcon.prepareStatement(handleQuery(user_inputs,sortFirstBy,sortRating,sortTitle));
            int prepPosition = 1;
            // The order is title, year, director, star
//            if(!user_inputs[0].equals(""))
//            {
//                preparedStatement.setString(prepPosition,"%" + user_inputs[0] + "%");
//                prepPosition++;
//            }
            if(!user_inputs[1].equals(""))
            {
                preparedStatement.setInt(prepPosition, Integer.parseInt(user_inputs[1]));
                prepPosition++;
            }
            if(!user_inputs[2].equals(""))
            {
                preparedStatement.setString(prepPosition,"%" + user_inputs[2] + "%");
                prepPosition++;
            }
            if(!user_inputs[3].equals(""))
            {
                preparedStatement.setString(prepPosition,"%" + user_inputs[3] + "%");
                prepPosition++;
            }
            preparedStatement.setInt(prepPosition,(pageNumber-1)*pageSize);
            preparedStatement.setInt(prepPosition+1,pageSize);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(preparedStatement.toString());
            JsonArray jsonArray = new JsonArray();
            while(rs.next())
            {
                JsonObject jsonObject = new JsonObject();
                JsonArray movie_stars = starsfilter(rs.getString("starInfo").split(";"));
                JsonArray genres = genresfilter(rs.getString("allGenres").split(";"));
                String movie_id = rs.getString("movieId");
                String movie_title = rs.getString("title");
                Integer movie_year = rs.getInt("year");
                String movie_director = rs.getString("director");
                Double movie_rating = rs.getDouble("rating");
                jsonObject.add("genres",genres);
                jsonObject.add("movie_stars", movie_stars);
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);

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

