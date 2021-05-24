package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.lang.*;

public class SingleMovie extends Activity {
    Button homeButton;


    private final String host = "10.0.2.2";
    private final String port = "8080";
    //CS122APROJECT1-war
    private final String domain = "CS122APROJECT1-war";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlemovie);
        String MovieID = getIntent().getStringExtra("ListViewClickedValue");
        System.out.println(MovieID);
        display(MovieID);

        homeButton = findViewById(R.id.home);
        homeButton.setOnClickListener(view -> goHome());
    }



    public void display(String MovieID)
    {
        System.out.println(MovieID);
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest singlemovieRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/single-movie" +"?id=" + MovieID,
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.

                    System.out.println(response.toString());
                    try {
                        ArrayList<String> tempStars = new ArrayList<String>();
                        ArrayList<String> tempGenres = new ArrayList<String>();

                        JSONArray array = new JSONArray(response);
                        JSONObject tempMovie = array.getJSONObject(0);
                        JSONArray tempMovieStar = tempMovie.getJSONArray("movie_stars");
                        JSONArray tempMovieGenres = tempMovie.getJSONArray("genres");
                        String movietitle = tempMovie.getString("movie_title");
                        final TextView titleTextView = (TextView) findViewById(R.id.singlemoviename);
                        titleTextView.setText(movietitle);

                        String allInfo = "Year: ";
                        String movieyear = String.valueOf(tempMovie.getInt("movie_year"));
                        allInfo += movieyear;
                        allInfo += "\n";

                        allInfo += "Director: ";
                        String movieDirector = tempMovie.getString("movie_director");
                        allInfo += movieDirector;
                        allInfo += "\n";

                        allInfo += "Stars: ";
                        for (int i = 0; i < tempMovieStar.length(); i++)
                        {
                            if(i < tempMovieStar.length() - 1)
                            {
                                String moviestars = "";
                                moviestars = String.valueOf(tempMovieStar.get(i));
                                moviestars = moviestars.substring( 0, moviestars.indexOf(","));
                                allInfo += moviestars;
                                allInfo += ", ";
                            }
                            else
                            {
                                String moviestars = "";
                                moviestars = String.valueOf(tempMovieStar.get(i));
                                moviestars = moviestars.substring( 0, moviestars.indexOf(","));
                                allInfo += moviestars;
                                allInfo += "\n";
                            }
                        }

                        allInfo += "Genres: ";
                        for (int i = 0; i < tempMovieGenres.length(); i++)
                        {
                            if(i < tempMovieGenres.length() - 1)
                            {
                                String moviegenres = "";
                                moviegenres = String.valueOf(tempMovieGenres.get(i));
                                moviegenres = moviegenres.substring( 0, moviegenres.indexOf(","));
                                allInfo += moviegenres;
                                allInfo += ", ";
                            }
                            else
                            {
                                String moviegenres = "";
                                moviegenres = String.valueOf(tempMovieGenres.get(i));
                                moviegenres = moviegenres.substring( 0, moviegenres.indexOf(","));
                                allInfo += moviegenres;
                                allInfo += "\n";
                            }
                        }

                        final TextView allinfoTextView = (TextView) findViewById(R.id.allinfo);
                        allinfoTextView.setText(allInfo);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    helloTextView.setText(R.string.user_greeting);

                    Log.d("getsinglemovie.success", response);
                    // initialize the activity(page)/destination

                    // activate the list page.
                },
                error -> {
                    // error
                    Log.d("SingleMovie.error", error.toString());
                }) {
        };

        // important: queue.add is where the login request is actually sent
        queue.add(singlemovieRequest);
    }


    public void goHome()
    {
        Intent intent = new Intent(SingleMovie.this,MainPage.class);
        startActivity(intent);
    }
}
