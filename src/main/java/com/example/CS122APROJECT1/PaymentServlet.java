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
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;


import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;



@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
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

        try {
            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();


            // Declare a new statement
            Statement statement = dbCon.createStatement();


            /* get the content in the login box from the http request(user's typed in username and the password)
                which refers to the value of <input name="name"> in index.html*/
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String creditcard = request.getParameter("creditcard");
            String expirationdate = request.getParameter("expirationdate");

            JsonObject responseJsonObject = new JsonObject();

            // Generate a SQL query
            String query = String.format("SELECT * from creditcards WHERE id  like '%s'", creditcard);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            // Check the Username exists in database or not
            if(rs.next() == false)
            {
                // the Username doesn't exists in database
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Payment Information incorrect");
            }
            // the Username exists in database
            else
            {
                // Check the password correct or not
                String correctFirstname = rs.getString("firstName");
                String correctLastname = rs.getString("lastName");
                Date correctExpiration = rs.getDate("expiration");
                String date = correctExpiration.toString();

                //if the password is not correct
                if(correctFirstname.equals(firstname) && correctLastname.equals(lastname) && date.equals(expirationdate))
                {
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                }

                // if the password is correct, So Login success:
                else
                {
                    // set this user into the session
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "Payment Information incorrect");
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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type
        HttpSession session = request.getSession();
        JsonObject responseJsonObject = new JsonObject();
        // get the previous items in a ArrayList
        ArrayList<Integer> NumOfItems = (ArrayList<Integer>) session.getAttribute("numItems");
        int totalPrice = 0;
        for (int i = 0; i < NumOfItems.size(); i++)
        {
            totalPrice += NumOfItems.get(i) * 10;
        }
        responseJsonObject.addProperty("totalPrice", totalPrice );
        response.getWriter().write(responseJsonObject.toString());
    }
}