package com.example.CS122APROJECT1;

import com.google.gson.JsonObject;

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
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
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

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {
            out.println("<html>");
            out.println("<head><title>Error</title></head>");
            out.println("<body>");
            out.println("<p>recaptcha verification error</p>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</body>");
            out.println("</html>");

            out.close();
            return;
        }

        try {
            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();


            // Declare a new statement
            Statement statement = dbCon.createStatement();


            /* get the content in the login box from the http request(user's typed in username and the password)
                which refers to the value of <input name="name"> in index.html*/
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            JsonObject responseJsonObject = new JsonObject();

            // Generate a SQL query
            String query = String.format("SELECT * from customers where email like '%s'", username);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            // Check the Username exists in database or not
            if(rs.next() == false)
            {
                // the Username doesn't exists in database
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            }
            // the Username exists in database
            else
            {
                // Check the password correct or not
                String correctPassword = rs.getString("password");

                //if the password is not correct
                if(!correctPassword.equals( password))
                {
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "incorrect password");
                }

                // if the password is correct, So Login success:
                else
                {
                    // set this user into the session
                    request.getSession().setAttribute("user", new User(username));
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                }
                // set response status to 200 (OK)
                response.setStatus(200);

                rs.close();
                statement.close();
                dbCon.close();
            }

            response.getWriter().write(responseJsonObject.toString());
        } catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }
}

