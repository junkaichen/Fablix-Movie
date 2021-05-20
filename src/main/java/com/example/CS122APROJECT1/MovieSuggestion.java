package com.example.CS122APROJECT1;

import java.util.StringTokenizer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.sql.DataSource;

// server endpoint URL
@WebServlet("/movie-suggestion")
public class MovieSuggestion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public MovieSuggestion() {
        super();
    }

    /*
     *
     * Match the query against superheroes and return a JSON response.
     *
     *
     * The format is like this because it can be directly used by the
     *   JSON auto complete library this example is using. So that you don't have to convert the format.
     *
     * The response contains a list of suggestions.
     * In each suggestion object, the "value" is the item string shown in the dropdown list,
     *   the "data" object can contain any additional information.
     *
     *
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // setup the response json arrray
            JsonArray jsonArray = new JsonArray();

            // get the query string from parameter
            String query = request.getParameter("query");
            if (query == null || query.trim().isEmpty()) {
                response.getWriter().write(jsonArray.toString());
                return;
            }

            // String finalQuery = "SELECT * FROM ft WHERE MATCH (title) AGAINST ('+grad*, +...' IN BOOLEAN MODE)";
            StringTokenizer st = new StringTokenizer(query);
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
            // search on superheroes and add the results to JSON Array
            // this example only does a substring match
            // TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars
            int counter = 0;
            // No more than 10 results
            while(rs.next() && counter < 10)
            {
                counter++;
                String id = rs.getString("movieID");
                String movieTitle = rs.getString("title");
                jsonArray.add(generateJsonObject(id, movieTitle));
            }

            response.getWriter().write(jsonArray.toString());
            response.setStatus(200);
            rs.close();
            statement.close();
            dbcon.close();
            return;
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(500, e.getMessage());
        }
    }


    private static JsonObject generateJsonObject(String movieID, String heroName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", heroName);

        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("movieID", movieID);

        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }


}
