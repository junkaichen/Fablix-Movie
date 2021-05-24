package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private final ArrayList<Movie> movies;

    public MovieListViewAdapter(ArrayList<Movie> movies, Context context) {
        super(context, R.layout.row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Movie movie = movies.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView subtitleView = view.findViewById(R.id.subtitle);



        titleView.setText(movie.getName());

        String movieInfo = "Year: ";
        movieInfo += String.valueOf(movie.getYear());
        movieInfo += "\n";
        movieInfo += "Director: ";
        movieInfo += movie.getDirector();
        movieInfo += "\n";
        movieInfo += "Stars: ";
        for (int i = 0; i < 3; i++)
        {
            if(i < 2)
            {
                movieInfo += movie.getMoviestars().get(i);
                movieInfo += ", ";
            }
            else
            {
                movieInfo += movie.getMoviestars().get(i);
                movieInfo += ".";
            }
        }
        movieInfo += "\n";
        movieInfo += "Genres: ";
        for (int i = 0; i < movie.getGenres().size(); i++)
        {
            if(i < movie.getGenres().size() - 1)
            {
                movieInfo += movie.getGenres().get(i);
                movieInfo += ", ";
            }
            else
            {
                movieInfo += movie.getGenres().get(i);
                movieInfo += ".";
            }
        }

        subtitleView.setText(movieInfo);
//        subtitleView.setText(movie.getDirector());
//        subtitleView2.setText(String.valueOf(movie.getYear()));


        return view;
    }
}