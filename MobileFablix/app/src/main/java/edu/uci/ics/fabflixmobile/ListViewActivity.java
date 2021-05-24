package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;


public class ListViewActivity extends Activity {
    private Button homeButton;
    ListView listView;
    private Button nextButton;
    private ArrayList<Movie> movies;
    Button prevButton;
    private int pageNumber;
    private String searchQuery;
    private boolean preventNextPage;
    private final String host = "3.15.204.21";
    private final String port = "8443";
    //CS122APROJECT1-war
    private final String domain = "CS122APROJECT1-1.0-SNAPSHOT";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        homeButton = findViewById(R.id.home_button);
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        pageNumber = 1;
        preventNextPage = false;

        // TODO: this should be retrieved from the backend server
        Intent intent = getIntent();
        searchQuery = intent.getStringExtra("searchQuery");

        String jsonArray = intent.getStringExtra("jsonArray");
        movies = parseMovieInfo(jsonArray);
        if(movies.size() != 20) { preventNextPage = true; }
//        try {
//            JSONArray array = new JSONArray(jsonArray);
//            System.out.println(array.toString(2));
//            String movietitle = "";
//            short movieyear = 0;
//            String movieDirector = "";
//            String movieId = "";
//            for(int i = 0; i < array.length(); i++)
//            {
//                // build two arraylist to store the information
//                ArrayList<String> tempStars = new ArrayList<String>();
//                ArrayList<String> tempGenres = new ArrayList<String>();
//
//                JSONObject tempMovie = array.getJSONObject(i);
//                movietitle = tempMovie.getString("movie_title");
//                movieyear = (short) tempMovie.getInt("movie_year");
//                movieDirector = tempMovie.getString("movie_director");
//                movieId = tempMovie.getString("movie_id");
//
//                // create two JSON arrays to extract the information in JSONArray
//                JSONArray tempMovieStar = tempMovie.getJSONArray("movie_stars");
//                JSONArray tempMovieGenres = tempMovie.getJSONArray("genres");
//
//                for (int j = 0; j < 3; j++)
//                {
//                    String moviestars = "";
//                    moviestars = String.valueOf(tempMovieStar.get(j));
//                    moviestars = moviestars.substring( 0, moviestars.indexOf(","));
//                    tempStars.add(moviestars);
//                }
//
//                for (int x = 0; x < tempMovieGenres.length(); x++)
//                {
//                    String moviegenres = "";
//                    moviegenres = String.valueOf(tempMovieGenres.get(x));
//                    moviegenres = moviegenres.substring( 0, moviegenres.indexOf(","));
//                    tempGenres.add(moviegenres);
//                }
//
//                movies.add(new Movie(movietitle, movieyear, movieDirector, tempStars, tempGenres, movieId));
//
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        nextButton.setOnClickListener(view -> next());
        prevButton.setOnClickListener(view -> prev());
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


    private void goHome()
    {
        Intent intent = new Intent(ListViewActivity.this,MainPage.class);
        startActivity(intent);
    }

    private void next()
    {
        pageNumber++;
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/search" +"?title="+searchQuery
                        +"&RatingFirst=true&pageNumber="+ Integer.toString(pageNumber) + "&sortRating=DESC&sortTitle=ASC&pageSize=20",
                response -> {
                    System.out.println(response.toString());
                    Log.d("search.success", response);
                    // initialize the activity(page)/destination
                    ArrayList<Movie> newMoviesToSee = parseMovieInfo(response);
                    if(newMoviesToSee.size() != 0)
                    {
                        movies = newMoviesToSee;
                        Toast.makeText(getApplicationContext(),"Loading new set of movies",Toast.LENGTH_SHORT).show();
                        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
                        if(newMoviesToSee.size() != 20) { preventNextPage = true; }
                        else { preventNextPage = false; }
                        listView.setAdapter(adapter);

                    }
                    else
                    {
                        if(pageNumber != 1)
                        {
                            pageNumber--;
                        }
                        Toast.makeText(getApplicationContext(),"There is no more movies to load with this query",Toast.LENGTH_SHORT).show();
                        preventNextPage = true;
                    }
                },
                error -> {
                    // error
                    Log.d("SearchPage.error", error.toString());
                }) {
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);
    }

    private void prev()
    {
        if(pageNumber == 1)
        {
            Toast.makeText(getApplicationContext(),"Cannot load illegal page",Toast.LENGTH_SHORT).show();
        }
        else {
            pageNumber--;

            final RequestQueue queue = NetworkManager.sharedManager(this).queue;
            final StringRequest searchRequest = new StringRequest(
                    Request.Method.GET,
                    baseURL + "/api/search" + "?title=" + searchQuery
                            + "&RatingFirst=true&pageNumber=" + Integer.toString(pageNumber) + "&sortRating=DESC&sortTitle=ASC&pageSize=20",
                    response -> {
                        System.out.println(response.toString());
                        Log.d("search.success", response);
                        // initialize the activity(page)/destination
                        ArrayList<Movie> newMoviesToSee = parseMovieInfo(response);
                        if (newMoviesToSee.size() != 0) {
                            movies = newMoviesToSee;
                            Toast.makeText(getApplicationContext(), "Loading previous set of movies", Toast.LENGTH_SHORT).show();
                            MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
                            if (newMoviesToSee.size() != 20) {
                                preventNextPage = true;
                            } else {
                                preventNextPage = false;
                            }
                            listView.setAdapter(adapter);

                        } else {
                            if (pageNumber != 1) {
                                pageNumber--;
                            }

                        }
                    },
                    error -> {
                        // error
                        Log.d("SearchPage.error", error.toString());
                    }) {
            };

            // important: queue.add is where the login request is actually sent
            queue.add(searchRequest);
        }
    }

    private ArrayList<Movie> parseMovieInfo(String jsonResponse)
    {
        ArrayList<Movie> newMovies = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonResponse);
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

                newMovies.add(new Movie(movietitle, movieyear, movieDirector, tempStars, tempGenres, movieId));

            }


        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<Movie>();
        }



        return newMovies;
    }

}