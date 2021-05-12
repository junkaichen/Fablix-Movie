package com.example.CS122APROJECT1;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
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


@WebServlet(name = "AddMovieServlet", urlPatterns = "/api/addmovie")
public class AddMovieServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        try {
            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();
            /* get the content in the login box from the http request(user's typed in username and the password)
                which refers to the value of <input name="name"> in index.html*/
            String movie_title = request.getParameter("movie_title");
            String movie_director = request.getParameter("movie_director");
            String movie_year = request.getParameter("movie_year");
            String star_name = request.getParameter("movie_star");
            String movie_genre = request.getParameter("movie_genre");
            String star_birthyear = request.getParameter("star_birthyear");
            JsonObject responseJsonObject = new JsonObject();

            String moiveId = movie_title.substring(0,1) +  movie_title.hashCode();
            moiveId = "m" + moiveId;
            if(moiveId.length() > 10)
            {
                moiveId = moiveId.substring(0,9);
            }

            String starId = star_name.substring(0,1) +  star_name.hashCode();
            starId = "a" + starId;
            if(starId.length() > 10)
            {
                starId = starId.substring(0,9);
            }


            String query7 = "CALL add_genre(?);" ;
            PreparedStatement statement7 = dbCon.prepareStatement(query7);
            statement7.setString(1, movie_genre);
            statement7.executeQuery();

                        String query6 = "SELECT * FROM genres WHERE name like ?" ;
                        PreparedStatement statement6 = dbCon.prepareStatement(query6);
                        statement6.setString(1, movie_genre);
                        ResultSet rs6 = statement6.executeQuery();
                        rs6.next();
                        int genresid2 = rs6.getInt("id");


                        String query11 = "CALL add_star(?, ?, ?);" ;
                        PreparedStatement statement11 = dbCon.prepareStatement(query11);
                        statement11.setString(1, starId);
                        statement11.setString(2, star_name);
                        statement11.setInt(3, Integer.parseInt(star_birthyear));
                        statement11.executeQuery();


                        String query4 = "CALL add_movie(?, ?, ?, ?, ?, ?);" ;
                        PreparedStatement statement4 = dbCon.prepareStatement(query4);
                        statement4.setString(1, moiveId);
                        statement4.setString(2, movie_title);
                        statement4.setInt(3, Integer.parseInt(movie_year));
                        statement4.setString(4, movie_director);
                        statement4.setInt(5, genresid2);
                        statement4.setString(6, star_name);
                        statement4.executeQuery();






//            String query = "SELECT * FROM movies WHERE title like ? AND director like ? and year = ?;";
//            PreparedStatement statement = dbCon.prepareStatement(query);
//            statement.setString(1, movie_title);
//            statement.setString(2, movie_director);
//            statement.setInt(3, Integer.parseInt(movie_year));
//            // Perform the query
//            ResultSet rs = statement.executeQuery();
//            if(rs.next())
//            {
//                responseJsonObject.addProperty("status", "fail");
//                responseJsonObject.addProperty("message", "Movie " + movie_title + " exist");
//            }
//            else
//            {
//                String query2 = "SELECT * FROM stars WHERE starname like ? AND birthYear = ?;";
//                PreparedStatement statement2 = dbCon.prepareStatement(query2);
//                statement2.setString(1, star_name);
//                statement2.setInt(2, Integer.parseInt(star_birthyear));
//                // Perform the query
//                ResultSet rs2 = statement2.executeQuery();
//                if(rs2.next())
//                {
//                    starId = rs2.getString("id");
//                    String query13 = "SELECT * FROM genres WHERE name like ? ;";
//                    PreparedStatement statement13 = dbCon.prepareStatement(query13);
//                    statement13.setString(1, movie_genre);
//                    // Perform the query
//                    ResultSet rs13 = statement13.executeQuery();
//                    if(rs13.next())
//                    {
//                        int genresid = rs13.getInt("id");
//
//                        String query14 = "CALL add_movie(?, ?, ?, ?, ?, ?);" ;
//                        PreparedStatement statement14 = dbCon.prepareStatement(query14);
//                        statement14.setString(1, moiveId);
//                        statement14.setString(2, movie_title);
//                        statement14.setInt(3, Integer.parseInt(movie_year));
//                        statement14.setString(4, movie_director);
//                        statement14.setInt(5, genresid);
//                        statement14.setString(6, star_name);
//                        statement14.executeQuery();
//                    }
//                    responseJsonObject.addProperty("message", "Star " + star_name + " exist");
//                }
//                else
//                {
//                    String query21 = "CALL add_star(?, ?, ?);" ;
//                    PreparedStatement statement21 = dbCon.prepareStatement(query21);
//                    statement21.setString(1, starId);
//                    statement21.setString(2, star_name);
//                    statement21.setInt(3, Integer.parseInt(star_birthyear));
//
//                    String query3 = "SELECT * FROM genres WHERE name like ? ;";
//                    PreparedStatement statement3 = dbCon.prepareStatement(query3);
//                    statement3.setString(1, movie_genre);
//                    // Perform the query
//                    ResultSet rs3 = statement3.executeQuery();
//                    if(rs3.next())
//                    {
//                        int genresid = rs3.getInt("id");
//                        String query20 = "CALL add_movie(?, ?, ?, ?, ?, ?);" ;
//                        PreparedStatement statement20 = dbCon.prepareStatement(query20);
//                        statement20.setString(1, moiveId);
//                        statement20.setString(2, movie_title);
//                        statement20.setInt(3, Integer.parseInt(movie_year));
//                        statement20.setString(4, movie_director);
//                        statement20.setInt(5, genresid);
//                        statement20.setString(6, star_name);
//                        statement20.executeQuery();
//                        responseJsonObject.addProperty("message", "Genres " + movie_genre + " exist");
//                    }
//                    else
//                    {
//
//                        String query7 = "CALL add_genre(?);" ;
//                        PreparedStatement statement7 = dbCon.prepareStatement(query7);
//                        statement7.setString(1, movie_genre);
//
//                        String query6 = "SELECT * FROM genres WHERE name like ?" ;
//                        PreparedStatement statement6 = dbCon.prepareStatement(query6);
//                        statement6.setString(1, movie_genre);
//                        ResultSet rs6 = statement6.executeQuery();
//                        rs6.next();
//                        int genresid2 = rs6.getInt("id");
//
//
//                        String query11 = "CALL add_star(?, ?, ?);" ;
//                        PreparedStatement statement11 = dbCon.prepareStatement(query11);
//                        statement11.setString(1, starId);
//                        statement11.setString(2, star_name);
//                        statement11.setInt(3, Integer.parseInt(star_birthyear));
//
//
//                        String query4 = "CALL add_movie(?, ?, ?, ?, ?, ?);" ;
//                        PreparedStatement statement4 = dbCon.prepareStatement(query4);
//                        statement4.setString(1, moiveId);
//                        statement4.setString(2, movie_title);
//                        statement4.setInt(3, Integer.parseInt(movie_year));
//                        statement4.setString(4, movie_director);
//                        statement4.setInt(5, genresid2);
//                        statement4.setString(6, star_name);
//                        statement4.executeQuery();
//
//                    }
//                }
//
//                // set this user into the session
//                responseJsonObject.addProperty("status", "success");
//                responseJsonObject.addProperty("message", "success");
//                // set response status to 200 (OK)
//                response.setStatus(200);
//            }
            dbCon.close();
            response.getWriter().write(responseJsonObject.toString());
        } catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }
}
