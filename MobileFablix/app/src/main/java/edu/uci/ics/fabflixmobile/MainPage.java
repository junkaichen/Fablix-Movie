package edu.uci.ics.fabflixmobile;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class MainPage extends ActionBarActivity {

    private Button searchButton;
    private EditText searchBar;

    private final String host = "10.0.2.2";
    private final String port = "8080";
    //CS122APROJECT1-war
    private final String domain = "CS122APROJECT1-war/";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(view -> search());
    }



    public void search()
    {
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "api/search" +"?title="+searchBar.getText().toString()
                        +"&RatingFirst=true&pageNumber=1&sortRating=DESC&sortTitle=ASC&pageSize=20",
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.

                    System.out.println(response.toString());


                    Log.d("search.success", response);
                    // initialize the activity(page)/destination
                    Intent listPage = new Intent(MainPage.this, ListViewActivity.class);
                    listPage.putExtra("jsonArray", response.toString());


                    // activate the list page.
                    startActivity(listPage);
                },
                error -> {
                    // error
                    Log.d("MainPage.error", error.toString());
                }) {
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);
    }
}
