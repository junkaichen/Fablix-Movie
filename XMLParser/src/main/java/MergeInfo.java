import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;


public class MergeInfo{

    List<Movie> movies;
    List<Actor> actors;
    List<String> movieIds;
    int countRemoved;
    int countRemovedMovies;
    List<String> allGenres;
    HashMap<String,String> starsInMovie;



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
        allGenres = new ArrayList<String>();
        starsInMovie = new HashMap<>();
    }


    private void generateMovieIDs(){
        Iterator<Movie> it = movies.iterator();
        while(it.hasNext())
        {
            Movie id = it.next();
            if(!movieIds.contains(id.getId()))
            {
                movieIds.add(id.getId());
            }

        }
        if(movieIds.size() == movies.size())
        {
            System.out.println("all unique movie ids");
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
        //System.out.println("total removed ids from actors for movies that dont exist is :" + countRemoved);

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
        System.out.println("Movies removed:" + countRemovedMovies);
    }

    public void allGenresFromMovies()
    {
        Iterator<Movie> it = movies.iterator();
        while(it.hasNext())
        {
            ArrayList<String> g = it.next().getGenres();
            for(String gen : g)
            {
                if(!allGenres.contains(gen))
                {
                    allGenres.add(gen);
                }
            }
        }
    }

    public void run(){
        System.out.println("The size of Parse XML Movies size is :" + movies.size());
        System.out.println("The size of Parse XML Actors size is : " + actors.size());
        generateMovieIDs();
        compareMovieIDAgainstActors();
        findMoviesWithNoActors();
        System.out.println("Total number of movies remaining is: " + movies.size());
        allGenresFromMovies();
        movieAndStar();
        System.out.println("Total number of found Genres: " + allGenres.size());
    }

    public void movieAndStar()
    {

        Iterator<Movie> m = movies.iterator();
        while(m.hasNext())
        {
            Iterator<Actor> a = actors.iterator();
            Movie mov = m.next();
            while(a.hasNext())
            {
                Actor act = a.next();
                if(act.getMovies().contains(mov.getId()))
                {
                    starsInMovie.put(act.getId(),mov.getId());
                }
            }
        }
    }

    public List<String> getAllGenres()
    {
        return allGenres;
    }

    public List<Actor> getActors()
    {
        return actors;
    }

    public HashMap<String,String> getStarsInMovie()
    {
        return starsInMovie;
    }

    public List<Movie> getMovies()
    {
        return this.movies;
    }


    public void printNumberOfStarswithMovies()
    {
        int count = 0;
        Iterator<Actor> it = actors.iterator();
        while(it.hasNext())
        {
            if(!it.next().getMovies().isEmpty())
            {
                count++;
            }
        }
        System.out.println("Out of " + actors.size() + " actors, " + count + " have movies");
    }




    public static void main(String[] args)
    {
        MergeInfo m = new MergeInfo();
        m.run();
        InsertGenres ig = new InsertGenres();
        InsertStars is = new InsertStars();
        InsertMovies im = new InsertMovies();
        InsertRatings ir = new InsertRatings();
        InsertMovieGenreRelation mgr = new InsertMovieGenreRelation();
        InsertMovieStarRelation msr = new InsertMovieStarRelation();

        try {
            ig.run(m.getAllGenres());
            is.run(m.getActors());
            im.run(m.getMovies());
            ir.run(m.getMovies());
            mgr.run(m.getMovies());
            msr.run(m.getStarsInMovie());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        m.printNumberOfStarswithMovies();


    }


}