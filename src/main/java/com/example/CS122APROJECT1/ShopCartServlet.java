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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "ShopCartServlet", urlPatterns = "/api/shopcart")
public class ShopCartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        long lastAccessTime = session.getLastAccessedTime();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("sessionID", sessionId);
        responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());

        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        ArrayList<Integer> numOfItems = (ArrayList<Integer>) session.getAttribute("numOfItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            numOfItems = new ArrayList<>();
        }
        JsonArray previousItemsJsonArray = new JsonArray();
        JsonArray numOfItemsJsonArray = new JsonArray();
        previousItems.forEach(previousItemsJsonArray::add);
        numOfItems.forEach(numOfItemsJsonArray::add);
        responseJsonObject.add("previousItems", previousItemsJsonArray);
        responseJsonObject.add("numOfItems", previousItemsJsonArray);

        // write all the data into the jsonObject
        response.getWriter().write(responseJsonObject.toString());
    }

    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String item = request.getParameter("itemInfo");
        System.out.println(item);
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        ArrayList<Integer> numOfItems = (ArrayList<Integer>) session.getAttribute("numOfItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            previousItems.add(item);
            numOfItems.add(1);
            session.setAttribute("previousItems", previousItems);
            session.setAttribute("numOfItems", numOfItems);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousItems) {
                if(previousItems.contains(item))
                {
                    int itemindex = previousItems.indexOf(item);
                    int tempnumItem = numOfItems.get(itemindex) + 1;
                    numOfItems.set(itemindex, tempnumItem);
                }
                else
                {
                    previousItems.add(item);
                    numOfItems.add(1);
                }

            }
        }

        JsonObject responseJsonObject = new JsonObject();

        JsonArray previousItemsJsonArray = new JsonArray();
        JsonArray numOfItemsJsonArray = new JsonArray();
        previousItems.forEach(previousItemsJsonArray::add);
        numOfItems.forEach( numOfItemsJsonArray::add);
        responseJsonObject.add("previousItems", previousItemsJsonArray);
        responseJsonObject.add("numOfItems", numOfItemsJsonArray);

        response.getWriter().write(responseJsonObject.toString());
    }

}
