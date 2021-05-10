import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class MergeInfo{

    List<Movie> movies;
    List<Actor> actors;
    List<String> movieIds;
    int countRemoved;
    int countRemovedMovies;
    List<String> allGenres;


    public MergeInfo()
    {
        countRemoved = 0;
        countRemovedMovies = 0;
        actorParseDOBXML a = new actorParseDOBXML();
        movieParseXML m = new movieParseXML();
        a.run();
        m.run();
        movies = m.getMovies();
        actors = a.getActors();
        movieIds = new ArrayList<String>();
    }


    private void generateMovieIDs(){
        Iterator<Movie> it = movies.iterator();
        while(it.hasNext())
        {
            Movie id = it.next();
            movieIds.add(id.getId());
        }
    }

    //check to see if there is a movie within the actors that isnt in a movie title and remove it
    private void compareMovieIDAgainstActors() {

        int i = 0;
        while(i < actors.size())
        {
            List<String> ids = actors.get(i).getMovies();
            Iterator<String> it = ids.iterator();
            List<String> idsToRemove = new ArrayList<String>();
            while(it.hasNext())
            {
                String movieID = it.next();
                if(!movieIds.contains(movieID))
                {
                    idsToRemove.add(movieID);
                    countRemoved++;
                }
            }
            if(!idsToRemove.isEmpty())
            {
                for(String id : idsToRemove)
                {
                    actors.get(i).removeMovieID(id);
                }
            }
            i++;
        }
        System.out.println("total removed ids from actors for movies that dont exist is :" + Integer.toString(countRemoved));

    }

    private void findMoviesWithNoActors()
    {
        int i = 0;

        while(i < movies.size())
        {
            Movie tempMovie = movies.get(i);
            Iterator<Actor> it = actors.iterator();
            boolean hasActors = false;
            while(it.hasNext() && !hasActors)
            {
                if(it.next().playedInMovie(tempMovie.getId()))
                {
                    hasActors = true;
                }
            }
            if(hasActors)
            {
                i++;
            }
            else
            {
                countRemovedMovies++;
                movies.remove(i);
                if(i-2 < 0)
                {
                    i = 0;
                }
                else{
                    i = i -2;
                }

            }
        }
        System.out.println("Movies removed:" + Integer.toString(countRemovedMovies));
    }

    public void run(){
        generateMovieIDs();
        compareMovieIDAgainstActors();
        findMoviesWithNoActors();
        System.out.println("This is working");
    }








    public static void main(String[] args)
    {
        MergeInfo info = new MergeInfo();
        info.run();
    }


}