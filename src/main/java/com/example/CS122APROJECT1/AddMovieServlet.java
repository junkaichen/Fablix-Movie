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
            System.out.println("HI");
            String callFunc = "CALL add_movie(?,?,?,?,?,?,?,?);";
            PreparedStatement statement = dbCon.prepareStatement(callFunc);
            statement.setString(1,moiveId);
            statement.setString(2,movie_title);
            statement.setInt(3,Integer.parseInt(movie_year));
            statement.setString(4,movie_director);
            statement.setString(5,movie_genre);
            statement.setString(6,star_name);
            statement.setInt(7,Integer.parseInt(star_birthyear));
            statement.setString(8,starId);
            ResultSet rs = statement.executeQuery();
            System.out.println("HI2");
            if(rs.next())
            {
                if(rs.getString("answer").contains("exists"))
                {
                    responseJsonObject.addProperty("status","failure");
                    responseJsonObject.addProperty("message","movie already exists!");
                }
            }

            System.out.println("HI3");
            // set this user into the session
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            response.setStatus(200);
            rs.close();
            statement.close();
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
