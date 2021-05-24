package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.lang.*;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



public class ListViewActivity extends Activity {
    Button homeButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        homeButton = findViewById(R.id.home_button);
        // TODO: this should be retrieved from the backend server
        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonArray");
        final ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonArray);
            System.out.println(array.toString(2));
            String movietitle = "";
            short movieyear = 0;
            String movieDirector = "";
            String movieId = "";
            for(int i = 0; i < array.length(); i++)
            {
                // build two arraylist to store the information
                ArrayList<String> tempStars = new ArrayList<String>();
                ArrayList<String> tempGenres = new ArrayList<String>();

                JSONObject tempMovie = array.getJSONObject(i);
                movietitle = tempMovie.getString("movie_title");
                movieyear = (short) tempMovie.getInt("movie_year");
                movieDirector = tempMovie.getString("movie_director");
                movieId = tempMovie.getString("movie_id");

                // create two JSON arrays to extract the information in JSONArray
                JSONArray tempMovieStar = tempMovie.getJSONArray("movie_stars");
                JSONArray tempMovieGenres = tempMovie.getJSONArray("genres");

                for (int j = 0; j < 3; j++)
                {
                    String moviestars = "";
                    moviestars = String.valueOf(tempMovieStar.get(j));
                    moviestars = moviestars.substring( 0, moviestars.indexOf(","));
                    tempStars.add(moviestars);
                }

                for (int x = 0; x < tempMovieGenres.length(); x++)
                {
                    String moviegenres = "";
                    moviegenres = String.valueOf(tempMovieGenres.get(x));
                    moviegenres = moviegenres.substring( 0, moviegenres.indexOf(","));
                    tempGenres.add(moviegenres);
                }

                movies.add(new Movie(movietitle, movieyear, movieDirector, tempStars, tempGenres, movieId));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }






        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        homeButton.setOnClickListener(view -> goHome());
        //Go to single movie page
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            Movie movie = movies.get(position);
//            String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//            System.out.println(movie.getMovieid());
////            goSingleMovie(movie);
//
//        });
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Movie movie = movies.get(position);
                // Getting listview click value into String variable.
                String TempListViewClickedValue = movie.getMovieid();

                Intent intent2 = new Intent(ListViewActivity.this, SingleMovie.class);

                // Sending value to another activity using intent.
                intent2.putExtra("ListViewClickedValue", TempListViewClickedValue);

                startActivity(intent2);

            }
        });

    }


    public void goHome()
    {
        Intent intent = new Intent(ListViewActivity.this,MainPage.class);
        startActivity(intent);
    }
}