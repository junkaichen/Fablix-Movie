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

public class ListViewActivity extends Activity {
    Button homeButton;


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
            JSONObject movietitle = array.getJSONObject(0);
            String movietitle2 = movietitle.getString("movie_title");
            System.out.println(movietitle2);
            movies.add(new Movie(movietitle2, (short) 2004,"Neil"));

        } catch (JSONException e) {
            e.printStackTrace();
        }






        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        homeButton.setOnClickListener(view -> goHome());
        //Go to single movie page
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = movies.get(position);
            String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    public void goHome()
    {
        Intent intent = new Intent(ListViewActivity.this,MainPage.class);
        startActivity(intent);
    }
}